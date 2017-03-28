package net.redcraft.genesis.listeners;

import com.brsanthu.googleanalytics.EventHit;
import com.brsanthu.googleanalytics.GoogleAnalytics;
import com.brsanthu.googleanalytics.GoogleAnalyticsRequest;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import net.redcraft.genesis.domain.CustomDimension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by maxim on 28/3/17.
 */

@Component
public class Analytics implements SlackMessagePostedListener {

	private static final String CATEGORY = "Slack";
	private static final String ACTION = "Message Posted";

	@Value("${ga.id}")
	private String gaId;

	@Override
	public void onEvent(SlackMessagePosted event, SlackSession session) {
		SlackUser sender = event.getSender();
		String message = event.getMessageContent();

		GoogleAnalytics ga = new GoogleAnalytics(gaId);
		GoogleAnalyticsRequest gaRequest = new EventHit(
				CATEGORY,
				ACTION,
				message,
				message.length()
		);

		gaRequest.customDimension(CustomDimension.USER.getIndex(), sender.getUserName());
		gaRequest.customDimension(CustomDimension.CHANNEL.getIndex(), event.getChannel().getName());

		gaRequest.userId(sender.getId());
		ga.post(gaRequest);
	}

}
