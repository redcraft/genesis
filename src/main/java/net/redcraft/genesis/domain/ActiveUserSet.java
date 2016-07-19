package net.redcraft.genesis.domain;

import org.springframework.data.annotation.Id;

import java.util.Set;

/**
 * Created by maxim on 13/7/16.
 */
public class ActiveUserSet {

	@Id
	private String date;
	private Set<String> userIDs;

	public ActiveUserSet() {
	}

	public ActiveUserSet(String date, Set<String> userIDs) {
		this.date = date;
		this.userIDs = userIDs;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<String> getUserIDs() {
		return userIDs;
	}

	public void setUserIDs(Set<String> userIDs) {
		this.userIDs = userIDs;
	}

	public void addUser(String id) {
		userIDs.add(id);
	}

	@Override
	public String toString() {
		return "ActiveUserSet{" +
				"date='" + date + '\'' +
				", userIDs=" + userIDs +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ActiveUserSet that = (ActiveUserSet) o;

		return date.equals(that.date);

	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}
}
