package net.redcraft.genesis.domain;

/**
 * Created by maxim on 29/3/17.
 */
public enum CustomDimension {
	USER(1), CHANNEL(2);

	private final int index;

	CustomDimension(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
