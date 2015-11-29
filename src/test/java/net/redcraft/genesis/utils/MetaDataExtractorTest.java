package net.redcraft.genesis.utils;

import net.redcraft.genesis.domain.SlackURL;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by RED on 30/11/2015.
 */
public class MetaDataExtractorTest {

    private static final String URL_1 = "https://google.com";
    private static final String URL_2 = "http://test.com";

    private static final String PR_URL_1 = "//google.com";
    private static final String PR_URL_2 = "//test.com";

    private static final MetaDataExtractor EXTRACTOR = new MetaDataExtractor();

    @Test
    public void testExtract() throws Exception {
        SlackURL slackURL1 = EXTRACTOR.extract(URL_1);
        assertEquals(URL_1, slackURL1.getUrl());
        assertEquals(PR_URL_1, slackURL1.getPrURL());
        assertNotNull(slackURL1.getLastPosted());

        SlackURL slackURL2 = EXTRACTOR.extract(URL_2);
        assertEquals(URL_2, slackURL2.getUrl());
        assertEquals(PR_URL_2, slackURL2.getPrURL());
        assertNotNull(slackURL2.getLastPosted());
    }
}