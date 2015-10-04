package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RED on 04/10/2015.
 */
@Component
public class SlackListener {

    private static Pattern PATTERN = Pattern.compile("<(.+?)>");

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

        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                Matcher matcher = PATTERN.matcher(event.getMessageContent());
                while(matcher.find()) {
                    String url = matcher.group(1);
                    SlackURL red = urlRepository.findOne(url);
                    if(red == null) {
                        urlRepository.save(new SlackURL(url, new Reference(event.getChannel().getName(), new Date())));
                    } else {
                        red.getReferences().add(new Reference(event.getChannel().getName(), new Date()));
                        urlRepository.save(red);
                    }
                }
            }
        });
    }
}
