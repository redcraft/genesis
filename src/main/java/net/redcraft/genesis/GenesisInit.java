package net.redcraft.genesis;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.listeners.PresenceChangeListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import net.redcraft.genesis.repositories.DigestReferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by RED on 04/10/2015.
 */
@Service
public class GenesisInit {

	private static final Logger log = LoggerFactory.getLogger(GenesisInit.class);

    @Autowired
    private SlackSession session;

    @Autowired
    private Set<SlackChannelCreatedListener> channelCreatedListeners;

    @Autowired
    private Set<SlackMessagePostedListener> messagePostedListeners;

	@Autowired
	private Set<PresenceChangeListener> presenceChangeListeners;

    @Autowired
    private ScheduledExecutorService executorService;

	@Autowired
	DigestReferenceRepository digestReferenceRepository;


    @PostConstruct
    public void setupListeners() throws GenesisException {

        channelCreatedListeners.stream().forEach(session::addchannelCreatedListener);

        messagePostedListeners.stream().forEach(session::addMessagePostedListener);

	    presenceChangeListeners.stream().forEach(session::addPresenceChangeListener);

        new Thread(() -> {
            session.getChannels().stream().forEach(slackChannel -> session.joinChannel(slackChannel.getName()));
        }).start();

	    executorService.scheduleWithFixedDelay(new ReferenceTracker(digestReferenceRepository), 0, 1, TimeUnit.DAYS);

	    log.info("Genesis successfully started");
    }

}
