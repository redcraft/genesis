package net.redcraft.genesis.workers;

import net.redcraft.genesis.SlackURLRepository;
import net.redcraft.genesis.domain.Reference;
import net.redcraft.genesis.domain.SlackURL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by RED on 30/11/2015.
 */
public class MongoPersistenceURLWorkerTest {

    private static final Date OLD_DATE = new Date(1220227200L * 1000);
    private static final Date NEW_DATE = new Date(1220327200L * 1000);

    private static final String CHANNEL_NAME_1 = "channel_1";
    private static final String CHANNEL_NAME_2 = "channel_2";

    private static final Date DATE_1 = new Date(1221327200L * 1000);;
    private static final Date DATE_2 = new Date(1220328200L * 1000);;

    private static final String PERSIST_URL = "http://test.com";
    private static final SlackURL PERSIST_SLACK_URL = new SlackURL("//test.com", PERSIST_URL, new ArrayList<>(Arrays.asList(new Reference(CHANNEL_NAME_1, DATE_1))), "title", "description", "http://test.com/image.png", OLD_DATE);
    private static final SlackURL TEST_SLACK_URL_EXISTED = new SlackURL("//test.com", PERSIST_URL, Arrays.asList(new Reference(CHANNEL_NAME_2, DATE_2)), "title", "description", "http://test.com/image.png", NEW_DATE);
    private static final SlackURL TEST_SLACK_URL_RESULT = new SlackURL("//test.com", PERSIST_URL, Arrays.asList(new Reference(CHANNEL_NAME_1, DATE_1), new Reference(CHANNEL_NAME_2, DATE_2)), "title", "description", "http://test.com/image.png", NEW_DATE);

    private static final SlackURL TEST_SLACK_URL_NEW = new SlackURL("//red.com", "http://red.com", Arrays.asList(new Reference("channel", new Date())), "title", "description", "http://red.com/image.png", new Date());

    private SlackURLRepository urlRepository;
    private MongoPersistenceURLWorker urlWorker;

    @Before
    public void init() {
        urlRepository = mock(SlackURLRepository.class);
        when(urlRepository.findOne(PERSIST_URL)).thenReturn(PERSIST_SLACK_URL);
        urlWorker = new MongoPersistenceURLWorker(urlRepository);
    }

    @Test
    public void testProcessURLNewURL() throws Exception {
        when(urlRepository.save(any(SlackURL.class))).then(new Answer<SlackURL>() {
            @Override
            public SlackURL answer(InvocationOnMock invocationOnMock) throws Throwable {
                SlackURL slackURL = (SlackURL) invocationOnMock.getArguments()[0];
                assertEquals(TEST_SLACK_URL_NEW, slackURL);
                return null;
            }
        });

        urlWorker.processURL(TEST_SLACK_URL_NEW);
    }

    @Test
    public void testProcessURLExistedURL() throws Exception {
        when(urlRepository.save(any(SlackURL.class))).then(new Answer<SlackURL>() {
            @Override
            public SlackURL answer(InvocationOnMock invocationOnMock) throws Throwable {
                SlackURL slackURL = (SlackURL) invocationOnMock.getArguments()[0];
                assertEquals(TEST_SLACK_URL_RESULT, slackURL);
                return null;
            }
        });

        urlWorker.processURL(TEST_SLACK_URL_EXISTED);
    }
}