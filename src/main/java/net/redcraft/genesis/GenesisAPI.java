package net.redcraft.genesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */

@Controller
public class GenesisAPI {

    @Autowired
    SlackURLRepository urlRepository;

    @RequestMapping("/api/link")
    public List<SlackURL> getAllLinks() {
        return urlRepository.findAll();
    }
}
