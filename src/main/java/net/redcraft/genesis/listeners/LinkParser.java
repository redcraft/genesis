package net.redcraft.genesis.listeners;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import net.redcraft.genesis.BlackList;
import net.redcraft.genesis.domain.Reference;
import net.redcraft.genesis.utils.MetaDataExtractor;
import net.redcraft.genesis.workers.SlackURLWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RED on 05/11/2015.
 */
@Component
public class LinkParser implements SlackMessagePostedListener {

    private static final Logger log = LoggerFactory.getLogger(LinkParser.class);
    private static final Pattern PATTERN = Pattern.compile("<(http.+?)[>|]");

    private final MetaDataExtractor extractor;
    private final BlackList blackList;
    private final Set<SlackURLWorker> workers;

    @Autowired
    public LinkParser(MetaDataExtractor extractor, Set<SlackURLWorker> workers, BlackList blackList) {
        this.extractor = extractor;
        this.workers = workers;
        this.blackList = blackList;
    }

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        String channelName = event.getChannel().getName();
        extractURLs(event.getMessageContent()).stream()
                .filter(blackList::isValid)
                .map(extractor::extract)
                .forEach(slackURL -> {
                    slackURL.setReferences(Arrays.asList(new Reference(channelName, new Date())));
                    workers.forEach(worker -> worker.processURL(slackURL));
                });
    }

    Set<String> extractURLs(String message) {
        Matcher matcher = PATTERN.matcher(message.toLowerCase());
        Set<String> urls = new HashSet<>();
        while (matcher.find()) {
            urls.add(matcher.group(1).replaceAll("/$", ""));
        }
        return urls;
    }
}
