package net.redcraft.genesis.workers;

import net.redcraft.genesis.SlackURLRepository;
import net.redcraft.genesis.domain.SlackURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by RED on 12/11/2015.
 */
@Component
public class MongoPersistenceURLWorker implements SlackURLWorker {

    private final SlackURLRepository urlRepository;

    @Autowired
    public MongoPersistenceURLWorker(SlackURLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public void processURL(SlackURL slackURL) {
        SlackURL storedURL = urlRepository.findOne(slackURL.getUrl());
        if(storedURL != null) {
            storedURL.getReferences().addAll(slackURL.getReferences());
            storedURL.setLastPosted(slackURL.getLastPosted());
        }
        else {
            storedURL = slackURL;
        }
        urlRepository.save(storedURL);
    }
}
