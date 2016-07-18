package net.redcraft.genesis;

import ch.qos.logback.classic.net.SMTPAppender;

import javax.mail.internet.InternetAddress;

/**
 * Created by maxim on 18/7/16.
 */
public class HerokuErrorAppender extends SMTPAppender {
	@Override
	public void start() {
		String userName = System.getenv("LOG_EMAIL_USER_NAME");
		String password = System.getenv("LOG_EMAIL_PASSWORD");
		String to = System.getenv("LOG_EMAIL_TO");
		if(userName != null && password != null && to != null) {
			setUsername(userName);
			setPassword(password);
			setFrom(userName);
			addTo(to);
			super.start();
		}
		else {
			System.err.println("Can't initialize email error logging");
		}
	}
}
