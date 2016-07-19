package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.listeners.PresenceChangeListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@Autowired
	private Set<PresenceChangeListener> presenceChangeListeners;


    @PostConstruct
    public void setupListeners() throws GenesisException {

        channelCreatedListeners.stream().forEach(session::addchannelCreatedListener);

        messagePostedListeners.stream().forEach(session::addMessagePostedListener);

	    presenceChangeListeners.stream().forEach(session::addPresenceChangeListener);

        new Thread(() -> {
            session.getChannels().stream().forEach(slackChannel -> session.joinChannel(slackChannel.getName()));
        }).start();

    }

}
