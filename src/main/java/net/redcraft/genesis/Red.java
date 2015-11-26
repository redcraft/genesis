package net.redcraft.genesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by RED on 06/11/2015.
 */
public class Red {

    private static Logger log = LoggerFactory.getLogger(Red.class);

    public static void main(String[] args) {
        try {
            throw new Exception("red");
        } catch (Exception e) {
            log.error("red {}", "max", "data", e);
        }


    }
}
