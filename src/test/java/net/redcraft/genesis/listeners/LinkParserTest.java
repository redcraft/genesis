package net.redcraft.genesis.listeners;

import com.google.common.collect.ImmutableSet;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import net.redcraft.genesis.BlackList;
import net.redcraft.genesis.domain.Reference;
import net.redcraft.genesis.domain.SlackURL;
import net.redcraft.genesis.utils.MetaDataExtractor;
import net.redcraft.genesis.workers.SlackURLWorker;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by RED on 14/11/2015.
 */
public class LinkParserTest {

    private static final List<String> messages = Arrays.asList(
            "nothing",
            "<http://red.com>",
            "<https://red.com>",
            "<http://red.com|red.com>",
            "<https://red.com|red.com>",
            "<http://red.com/|red.com/>",
            "<http://Red.com/|Red.com/>",
            "I’m writing something and adding a link <http://red.com|red.com>",
            "red <http://red.com|red.com> <http://max.com|max.com>"
    );

    private static final List<Set<String>> urlSets = Arrays.asList(
            ImmutableSet.of(),
            ImmutableSet.of("http://red.com"),
            ImmutableSet.of("https://red.com"),
            ImmutableSet.of("http://red.com"),
            ImmutableSet.of("https://red.com"),
            ImmutableSet.of("http://red.com"),
            ImmutableSet.of("http://red.com"),
            ImmutableSet.of("http://red.com"),
            ImmutableSet.of("http://red.com", "http://max.com")
    );

    private static final String CHANNEL = "channel";
    private static final Date DATE = new Date();

    private static final String MESSAGE = "<http://Red.com|red.com> some more text <http://max.com|max.com>";
    private static final String URL = "http://red.com";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_URL = "imageURL";
    private static final String BLACKED_URL = "http://max.com";


    @org.junit.Test
    public void testExtractURLs() throws Exception {
        LinkParser linkParser = new LinkParser(null, null, null);
        for(int i = 0; i < messages.size(); ++i) {
            Set<String> extractedLinks = linkParser.extractURLs(messages.get(i));
            assertEquals(urlSets.get(i).size(), extractedLinks.size());
            urlSets.get(i).stream().forEach(url -> assertTrue(extractedLinks.contains(url)));
        }
    }

    @org.junit.Test
    public void testOnEvent() throws Exception {
        Reference reference = new Reference(CHANNEL, DATE);
        SlackURL slackURL = new SlackURL(URL, null, TITLE, DESCRIPTION, IMAGE_URL);

        SlackURLWorker worker = url -> {
            assertEquals(url, slackURL);
        };

        MetaDataExtractor extractor = mock(MetaDataExtractor.class);
        when(extractor.extract(URL)).thenReturn(slackURL);

        BlackList blackList = mock(BlackList.class);
        when(blackList.isValid(anyString())).thenReturn(true);
        when(blackList.isValid(BLACKED_URL)).thenReturn(false);

        LinkParser linkParser = new LinkParser(extractor, ImmutableSet.of(worker), blackList);

        SlackChannel channel = mock(SlackChannel.class);
        when(channel.getName()).thenReturn(CHANNEL);

        SlackMessagePosted event = mock(SlackMessagePosted.class);
        when(event.getMessageContent()).thenReturn(MESSAGE);
        when(event.getChannel()).thenReturn(channel);

        linkParser.onEvent(event, null);
    }
}