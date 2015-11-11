package net.redcraft.genesis.listeners;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import org.springframework.stereotype.Component;

/**
 * Created by RED on 05/11/2015.
 */

@Component
public class ChannelAutoJoin implements SlackChannelCreatedListener {

    @Override
    public void onEvent(SlackChannelCreated event, SlackSession session) {
        session.joinChannel(event.getSlackChannel().getName());
    }

}
