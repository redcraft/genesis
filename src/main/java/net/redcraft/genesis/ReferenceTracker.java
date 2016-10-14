package net.redcraft.genesis;

import com.google.common.collect.ImmutableSet;
import net.redcraft.genesis.domain.DigestReference;
import net.redcraft.genesis.repositories.DigestReferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by maxim on 21/7/16.
 */
public class ReferenceTracker implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(ReferenceTracker.class);
	private final static Client CLIENT = ClientBuilder.newClient();

	private final static Pattern DIGEST_URL_PATTERN = Pattern.compile(
			"(https://medium.com/creativerussia-digest/.*?).source=latest");
	private final static Pattern REFERENCE_PATTERN = Pattern.compile("(https?://.*?)\"");

	private final static String DIGEST_LIST_URL = "https://medium.com/creativerussia-digest/latest";

	private final static Set<String> EXCLUDE_EXTENSIONS = ImmutableSet.of(
			"^.*\\.png", "^.*\\.gif", "^.*\\.jpeg", "^.*\\.ico", "^.*\\.jpg", "^.*\\.svg", "^.*\\.js");

	private final DigestReferenceRepository referenceRepository;

	public ReferenceTracker(DigestReferenceRepository referenceRepository) {
		this.referenceRepository = referenceRepository;
	}

	@Override
	public void run() {
		referenceRepository.save(getUrls(DIGEST_LIST_URL, DIGEST_URL_PATTERN)
				.stream()
				.filter(url -> url.length() < 300)
				.map(url -> getUrls(url.toLowerCase(), REFERENCE_PATTERN)
						.stream()
						.map(refUrl -> {
							String refUrlDecoded = "";
							String urlDecoded = "";
							try {
								refUrlDecoded = URLDecoder.decode(refUrl, "UTF-8");
								urlDecoded = URLDecoder.decode(url, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								log.debug("Can't decode URL", e);
							}
							return new DigestReference(refUrlDecoded, urlDecoded);
						})
						.collect(Collectors.toSet()))
				.flatMap(Collection::stream)
				.filter(digestReference -> EXCLUDE_EXTENSIONS.stream().noneMatch(digestReference.getUrl()::matches))
				.collect(Collectors.toSet())
		);
	}

	private Set<String> getUrls(String sourceUrl, Pattern pattern) {
		Set<String> urls = new HashSet<>();

		WebTarget webTarget = CLIENT.target(sourceUrl);
		Response response = webTarget.request(MediaType.TEXT_HTML_TYPE).get();

		if(response.getStatus() == 200) {
			Matcher matcher = pattern.matcher(response.readEntity(String.class));
			while(matcher.find()) {
				urls.add(matcher.group(1));
			}
		}
		else {
			log.error("Bad response on digest reference extraction from url {}", sourceUrl);
		}

		return urls;
	}

}
