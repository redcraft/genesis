package net.redcraft.genesis.utils;

import net.redcraft.genesis.domain.SlackURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by RED on 15/11/2015.
 */
@Service
public class MetaDataExtractor {

    private static final Logger log = LoggerFactory.getLogger(MetaDataExtractor.class);

    public SlackURL extract(String url) {
        SlackURL slackURL = new SlackURL();
        slackURL.setUrl(url);
        slackURL.setPrURL(url.replaceAll("https?:",""));
        slackURL.setLastPosted(new Date());
        try {
            Document doc = Jsoup.connect(url).get();
            Element titleElement = doc.select("title").first();
            if (titleElement != null) {
                slackURL.setTitle(titleElement.text());
            }
            Element descriptionElement = doc.select("meta[property=og:description]").first();
            if (descriptionElement != null) {
                slackURL.setDescription(descriptionElement.attr("content"));
            }
            Element imageElement = doc.select("meta[property=og:image]").first();
            if (imageElement != null) {
                slackURL.setImageURL(imageElement.attr("content"));
            }
        } catch (Exception e) {
            log.debug("Can't parse URL {}", url, e);
        }
        return slackURL;
    }
}
