package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackConnected;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackConnectedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by RED on 04/10/2015.
 */
@Service
public class SlackService {

    @Autowired
    private SlackSession session;

    @Autowired
    private Set<SlackChannelCreatedListener> channelCreatedListeners;

    @Autowired
    private Set<SlackMessagePostedListener> messagePostedListeners;


    @PostConstruct
    public void setupListeners() throws GenesisException {

	    session.addSlackConnectedListener(new SlackConnectedListener() {
		    @Override
		    public void onEvent(SlackConnected slackConnected, SlackSession slackSession) {
			    System.out.println("User attached to slack: " + slackConnected.getConnectedPersona().getUserName());
		    }
	    });

        channelCreatedListeners.stream().forEach(session::addchannelCreatedListener);

        messagePostedListeners.stream().forEach(session::addMessagePostedListener);

        new Thread(() -> {
            session.getChannels().stream().forEach(slackChannel -> session.joinChannel(slackChannel.getName()));
        }).start();

    }

}
