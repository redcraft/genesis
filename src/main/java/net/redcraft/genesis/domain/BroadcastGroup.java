package net.redcraft.genesis.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by maxim on 25/8/16.
 */
public class BroadcastGroup {

	@Id
	private String name;
	private List<BroadcastSet> broadcastSets;
	private Date date;

	public BroadcastGroup() {
	}

	public BroadcastGroup(String name, List<BroadcastSet> broadcastSets, Date date) {
		this.name = name;
		this.broadcastSets = broadcastSets;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BroadcastSet> getBroadcastSets() {
		return broadcastSets;
	}

	public void setBroadcastSets(List<BroadcastSet> broadcastSets) {
		this.broadcastSets = broadcastSets;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
