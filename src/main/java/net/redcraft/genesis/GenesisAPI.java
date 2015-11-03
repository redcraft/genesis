package net.redcraft.genesis;

import net.redcraft.genesis.domain.SlackURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */

@RestController
public class GenesisAPI {

    @Autowired
    private SlackURLRepository urlRepository;

    @RequestMapping("/api/link")
    public List<SlackURL> getAllLinks() {
        return urlRepository.findAll();
    }
}
