package net.redcraft.genesis.listeners;

import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.PresenceChange;
import com.ullink.slack.simpleslackapi.listeners.PresenceChangeListener;
import net.redcraft.genesis.domain.ActiveUserSet;
import net.redcraft.genesis.repositories.DayActiveUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by maxim on 13/7/16.
 */
@Component
public class PresenceListener implements PresenceChangeListener {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private final Map<String , ActiveUserSet> samples = new HashMap<>();

	@Autowired
	public PresenceListener(ScheduledExecutorService scheduledExecutorService, DayActiveUsersRepository dayActiveUsersRepository) {
		dayActiveUsersRepository.findAll().forEach(usersSample -> samples.put(usersSample.getDate(), usersSample));
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				dayActiveUsersRepository.save(samples.values());
			}
		}, 1, 1, TimeUnit.HOURS);
	}

	@Override
	public void onEvent(PresenceChange event, SlackSession session) {
		String today = dateFormat.format(new Date());
		synchronized (samples) {
			samples.putIfAbsent(today, new ActiveUserSet(today, getActiveUsers(session))).addUser(event.getUserId());
		}
	}

	private Set<String> getActiveUsers(SlackSession session) {
		return session.getUsers().stream()
				.filter(slackUser -> slackUser.getPresence() == SlackPersona.SlackPresence.ACTIVE)
				.map(SlackPersona::getId)
				.collect(Collectors.toSet());
	}
}
