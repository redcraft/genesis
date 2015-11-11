package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * Created by RED on 05/11/2015.
 */
public class SlackConfig {

    @Value("${slack.apikey}")
    private String slackAPIKey;

    @Bean
    public SlackSession createSlackSession() throws GenesisException {
        SlackSession session;
        try {
            session = SlackSessionFactory.createWebSocketSlackSession(slackAPIKey);
            session.connect();
        } catch (IOException e) {
            throw new GenesisException("Can't connect to Slack service", e);
        }
        return session;
    }
}
