package net.redcraft.genesis.workers;

import net.redcraft.genesis.domain.SlackURL;

/**
 * Created by RED on 12/11/2015.
 */
public interface SlackURLWorker {
    void processURL(SlackURL slackURL);
}
