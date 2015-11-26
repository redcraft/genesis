package net.redcraft.genesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RED on 25/11/2015.
 */

@Service
public class BlackList {

    private static final Logger log = LoggerFactory.getLogger(BlackList.class);

    private final List<String> blackList = new ArrayList<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    void init() throws GenesisException {
        try {
            Resource resource = resourceLoader.getResource("classpath:blacklist.txt");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("Blacklist record registered: {}", line);
                blackList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new GenesisException("Can't read blacklist", e);
        }
    }

    public boolean isValid(String url) {
        return !blackList.stream().anyMatch(url::contains);
    }
}
