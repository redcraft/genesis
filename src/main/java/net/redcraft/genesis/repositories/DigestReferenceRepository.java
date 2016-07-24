package net.redcraft.genesis.repositories;

import net.redcraft.genesis.domain.DigestReference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by maxim on 21/7/16.
 */
public interface DigestReferenceRepository extends MongoRepository<DigestReference, String> {

	@Override
	DigestReference findOne(String s);

	@Override
	List<DigestReference> findAll();
}
