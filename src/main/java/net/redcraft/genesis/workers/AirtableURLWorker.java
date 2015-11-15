package net.redcraft.genesis.workers;

import net.redcraft.genesis.domain.SlackURL;
import net.redcraft.genesis.utils.AirTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by RED on 12/11/2015.
 */
@Component
public class AirtableURLWorker implements SlackURLWorker {

    @Autowired
    private AirTable airTable;

    @Override
    public void processURL(SlackURL slackURL) {
        if (slackURL.getReferences().size() == 1) {
            airTable.addRecord(slackURL);
        }
    }
}
