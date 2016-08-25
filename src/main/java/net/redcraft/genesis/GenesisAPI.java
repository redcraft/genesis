package net.redcraft.genesis;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import net.redcraft.genesis.domain.*;
import net.redcraft.genesis.repositories.BroadcastRepository;
import net.redcraft.genesis.repositories.DigestReferenceRepository;
import net.redcraft.genesis.repositories.SlackURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by RED on 04/10/2015.
 */

@RestController
public class GenesisAPI {

    private static final Logger log = LoggerFactory.getLogger(GenesisAPI.class);

    @Autowired
    private SlackURLRepository urlRepository;

    @Autowired
    private DigestReferenceRepository referenceRepository;

    @Autowired
    private BroadcastRepository broadcastRepository;

    @Autowired
    private SlackSession session;

    @RequestMapping("/api/link")
    public List<SlackURL> getAllLinks() {
        return urlRepository.findAll();
    }

    @RequestMapping("/api/reference")
    public List<DigestReference> getReferences(@RequestParam("searchString") String searchTerm) {
	    String searchString = searchTerm.toLowerCase();
        return referenceRepository.findAll().stream()
		        .filter(ref -> ref.getUrl().contains(searchString)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/group", method = RequestMethod.POST, produces = "application/json")
    public List<BroadcastGroup> createGroup(@RequestParam("name") String name, @RequestParam("setCount") Integer setCount) {
        if(broadcastRepository.findOne(name) == null) {
            List<String> userIds = session.getUsers().stream()
                    .filter(slackUser -> !slackUser.isBot())
                    .map(SlackPersona::getId)
                    .collect(Collectors.toList());
            List<BroadcastSet> broadcastSets = Lists.partition(userIds, setCount).stream()
                    .map(set -> new BroadcastSet(set, new ArrayList<BroadcastMessage>()))
                    .collect(Collectors.toList());
            broadcastRepository.save(new BroadcastGroup(name, broadcastSets));
        }
        else {
            log.error("Group with name {} already exist", name);
        }
        return broadcastRepository.findAll();
    }

    @RequestMapping(value = "/api/group", method = RequestMethod.GET, produces = "application/json")
    public List<BroadcastGroup> getGroups() {
        return broadcastRepository.findAll();
    }

    @RequestMapping(value = "/api/community", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Integer> getCommunitySize() {
        return ImmutableMap.of("size", session.getUsers().size());
    }
}
