package net.redcraft.genesis.domain;

/**
 * Created by maxim on 21/7/16.
 */
public class DigestReference {
	private String url;
	private String digestURL;

	public DigestReference() {
	}

	public DigestReference(String url, String digestURL) {
		this.url = url;
		this.digestURL = digestURL;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDigestURL() {
		return digestURL;
	}

	public void setDigestURL(String digestURL) {
		this.digestURL = digestURL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DigestReference digestReference = (DigestReference) o;

		if (!url.equals(digestReference.url)) return false;
		return digestURL.equals(digestReference.digestURL);

	}

	@Override
	public int hashCode() {
		int result = url.hashCode();
		result = 31 * result + digestURL.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "DigestReference{" +
				"url='" + url + '\'' +
				", digestURL='" + digestURL + '\'' +
				'}';
	}
}
