package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

/**
 * Created by RED on 05/11/2015.
 */
public class SlackConfig {

    @Value("${slack.apikey}")
    private String slackAPIKey;

    @Value("${slack.apikey.dm}")
    private String slackAPIKeyDM;

    @Bean
    @Primary
    public SlackSession createSlackSession() throws GenesisException {
	    return createSession(slackAPIKey, "Can't connect to Slack service");
    }

    @Bean(name="DM")
    public SlackSession createDMSlackSession() throws GenesisException {
        return createSession(slackAPIKeyDM, "Can't connect to Slack service with DM key");
    }

    private SlackSession createSession(String apiKey, String errorMessage) throws GenesisException {
	    SlackSession session;
	    try {
		    session = SlackSessionFactory.createWebSocketSlackSession(apiKey);
		    session.connect();
	    } catch (IOException e) {
		    throw new GenesisException(errorMessage, e);
	    }
	    return session;
    }
}
