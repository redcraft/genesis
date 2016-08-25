package net.redcraft.genesis.domain;

import java.util.Date;

/**
 * Created by maxim on 25/8/16.
 */
public class BroadcastMessage {

	private String message;
	private Date date;

	public BroadcastMessage() {
	}

	public BroadcastMessage(String message, Date date) {
		this.message = message;
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
