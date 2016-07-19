package net.redcraft.genesis.repositories;

import net.redcraft.genesis.domain.ActiveUserSet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by maxim on 13/7/16.
 */

public interface DayActiveUsersRepository extends MongoRepository<ActiveUserSet, Date> {
	@Override
	ActiveUserSet findOne(Date date);

	@Override
	List<ActiveUserSet> findAll();
}
