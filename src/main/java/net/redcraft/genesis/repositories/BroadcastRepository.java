package net.redcraft.genesis.repositories;

import net.redcraft.genesis.domain.BroadcastGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by maxim on 25/8/16.
 */
public interface BroadcastRepository extends MongoRepository<BroadcastGroup, String> {

	@Override
	BroadcastGroup findOne(String s);

	@Override
	List<BroadcastGroup> findAll();
}
