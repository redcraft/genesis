package net.redcraft.genesis;

import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by maxim on 13/7/16.
 */
public class SchedulerConfig {

	@Bean
	public ScheduledExecutorService createScheduledExecutorService() throws GenesisException {
		return Executors.newScheduledThreadPool(2);
	}
}
