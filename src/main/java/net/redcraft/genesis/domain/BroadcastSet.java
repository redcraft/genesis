package net.redcraft.genesis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by maxim on 25/8/16.
 */
public class BroadcastSet {
	private int id;
	private List<String> users;
	private List<BroadcastMessage> messages;

	public BroadcastSet() {
	}

	public BroadcastSet(int id, List<String> users, List<BroadcastMessage> messages) {
		this.id = id;
		this.users = users;
		this.messages = messages;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public List<BroadcastMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<BroadcastMessage> messages) {
		this.messages = messages;
	}
}
