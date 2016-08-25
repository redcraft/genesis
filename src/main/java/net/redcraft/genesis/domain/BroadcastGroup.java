package net.redcraft.genesis.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by maxim on 25/8/16.
 */
public class BroadcastGroup {

	@Id
	private String name;
	private List<BroadcastSet> broadcastSets;

	public BroadcastGroup() {
	}

	public BroadcastGroup(String name, List<BroadcastSet> broadcastSets) {
		this.name = name;
		this.broadcastSets = broadcastSets;
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
}
