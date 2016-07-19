package net.redcraft.genesis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by RED on 27/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@Import(BlackList.class)
@SpringApplicationConfiguration(classes = BlackListTest.class)
public class BlackListTest {

    private static final String VALID_1 = "http://youtube.com/something";
    private static final String VALID_2 = "http://green.com";
    private static final String INVALID_1 = "https://creativerussia.slack.com/archives/tools/p1446365909000859";
    private static final String INVALID_2 = "https://green.com/file/data";

    @Autowired BlackList blackList;

    @Test
    public void testIsValid() throws Exception {
        assertTrue(blackList.isValid(VALID_1));
        assertTrue(blackList.isValid(VALID_2));
        assertFalse(blackList.isValid(INVALID_1));
        assertFalse(blackList.isValid(INVALID_2));
    }
}