package net.redcraft.genesis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                session.getChannels().stream().forEach(slackChannel -> session.joinChannel(slackChannel.getName()));
            }
        }).start();

        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                Matcher matcher = PATTERN.matcher(event.getMessageContent());
                while (matcher.find()) {
                    String url = matcher.group(1);
                    SlackURL red = urlRepository.findOne(url);
                    if (red == null) {
                        SlackURL slackURL = new SlackURL(url, new Reference(event.getChannel().getName(), new Date()));
                        try {
                            Document doc = Jsoup.connect(url).get();
                            Element titleElement = doc.select("title").first();
                            if(titleElement != null) {
                                slackURL.setTitle(titleElement.text());
                            }
                            Element descriptionElement = doc.select("meta[property=og:description]").first();
                            if(descriptionElement != null) {
                                slackURL.setDescription(descriptionElement.attr("content"));
                            }
                            Element imageElement = doc.select("meta[property=og:image]").first();
                            if(imageElement != null) {
                                slackURL.setImageURL(imageElement.attr("content"));
                            }
                        } catch (Exception e) {
                            log.debug("Can't parse URL", e);
                        }
                        urlRepository.save(slackURL);
                        addToAirTable(slackURL);
                    } else {
                        red.getReferences().add(new Reference(event.getChannel().getName(), new Date()));
                        urlRepository.save(red);
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

    private void addToAirTable(SlackURL slackURL) {

        String url = "https://api.airtable.com/v0/app6e52Oq9b9YqhOQ/everything";
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer key87AvIr7tSWtQjs");
            con.setRequestProperty("Content-type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
            ObjectNode request = jsonNodeFactory.objectNode();
            request.put("Title", Optional.ofNullable(slackURL.getTitle()).orElse(""))
                   .put("Link", slackURL.getUrl())
                   .put("Description", Optional.ofNullable(slackURL.getDescription()).orElse(""))
                   .put("Date", "2015-09-14T15:53:31.000Z")
                   .put("channel", slackURL.getReferences().get(0).getChannel());
            String jsonRequest = mapper.writeValueAsString(jsonNodeFactory.objectNode().set("fields", request));
            log.debug("Airtable payload: {}", jsonRequest);

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(jsonRequest);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            log.debug("Airtable response code: {}", responseCode);

        } catch (Exception e) {
            log.error("Error posting to Airtable", e);
        }
    }
}
