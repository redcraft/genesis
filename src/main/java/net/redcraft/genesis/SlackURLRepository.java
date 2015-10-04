package net.redcraft.genesis;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by RED on 04/10/2015.
 */
public interface SlackURLRepository extends MongoRepository<SlackURL, String> {

    @Override
    SlackURL findOne(String s);

}
