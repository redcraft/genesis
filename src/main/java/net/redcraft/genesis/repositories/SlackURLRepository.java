package net.redcraft.genesis.repositories;

import net.redcraft.genesis.domain.SlackURL;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */
public interface SlackURLRepository extends MongoRepository<SlackURL, String> {

    @Override
    SlackURL findOne(String s);

    @Override
    List<SlackURL> findAll();

    List<SlackURL> findByDigestExists(boolean exists);
}
