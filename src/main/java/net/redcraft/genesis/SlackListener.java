package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import net.redcraft.genesis.domain.Reference;
import net.redcraft.genesis.domain.SlackURL;
import net.redcraft.genesis.utils.AirTable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RED on 04/10/2015.
 */
@Component
public class SlackListener {

    private static final Logger log = LoggerFactory.getLogger(SlackListener.class);
    private static final Pattern PATTERN = Pattern.compile("<(http.+?)[>|]");

    @Autowired
    private SlackURLRepository urlRepository;

    @Autowired
    private AirTable airTable;

    @Value("${slack.apikey}")
    private String slackAPIKey;

    @PostConstruct
    public void setupListeners() throws GenesisException {

        SlackSession session;
        try {
            session = SlackSessionFactory.createWebSocketSlackSession(slackAPIKey);
            session.connect();
        } catch (IOException e) {
            throw new GenesisException("Can't connect to Slack service", e);
        }

        new Thread(() -> {
            session.getChannels().stream().forEach(slackChannel -> session.joinChannel(slackChannel.getName()));
        }).start();

        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                Matcher matcher = PATTERN.matcher(event.getMessageContent().toLowerCase());
                while (matcher.find()) {
                    String url = matcher.group(1).replaceAll("/$", "");
                    SlackURL slackURL = urlRepository.findOne(url);
                    if (slackURL == null) {
                        slackURL = new SlackURL(url, new ArrayList<Reference>());
                        try {
                            Document doc = Jsoup.connect(url).get();
                            Element titleElement = doc.select("title").first();
                            if (titleElement != null) {
                                slackURL.setTitle(titleElement.text());
                            }
                            Element descriptionElement = doc.select("meta[property=og:description]").first();
                            if (descriptionElement != null) {
                                slackURL.setDescription(descriptionElement.attr("content"));
                            }
                            Element imageElement = doc.select("meta[property=og:image]").first();
                            if (imageElement != null) {
                                slackURL.setImageURL(imageElement.attr("content"));
                            }
                        } catch (Exception e) {
                            log.debug("Can't parse URL", e);
                        }
                        urlRepository.save(slackURL);
                    }
                    slackURL.getReferences().add(new Reference(event.getChannel().getName(), new Date()));
                    urlRepository.save(slackURL);
                    if (slackURL.getReferences().size() == 1) {
                        airTable.addRecord(slackURL);
                    }
                }
            }
        });

        session.addchannelCreatedListener(new SlackChannelCreatedListener() {
            @Override
            public void onEvent(SlackChannelCreated event, SlackSession session) {
                session.joinChannel(event.getSlackChannel().getName());
            }
        });
    }

}
