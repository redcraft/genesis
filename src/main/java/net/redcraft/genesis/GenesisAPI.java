package net.redcraft.genesis;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ullink.slack.simpleslackapi.SlackPersona;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import net.redcraft.genesis.domain.*;
import net.redcraft.genesis.repositories.BroadcastRepository;
import net.redcraft.genesis.repositories.DayActiveUsersRepository;
import net.redcraft.genesis.repositories.DigestReferenceRepository;
import net.redcraft.genesis.repositories.SlackURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
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
    private DayActiveUsersRepository activeUsersRepository;

    @Autowired
    private DigestReferenceRepository referenceRepository;

    @Autowired
    private BroadcastRepository broadcastRepository;

	@Autowired
	@Qualifier("DM")
	private SlackSession session;

	@Autowired
	private ScheduledExecutorService executorService;

    @RequestMapping("/api/link")
    public List<SlackURL> getAllLinks() {
        return urlRepository.findByDigestExists(false);
    }

    @RequestMapping("/api/reference")
    public List<DigestReference> getReferences(@RequestParam("searchString") String searchTerm) {
	    String searchString = searchTerm.toLowerCase();
        return referenceRepository.findAll().stream()
		        .filter(ref -> ref.getUrl().contains(searchString)).collect(Collectors.toList());
    }

    @RequestMapping("/api/stats")
    public String getStats() {
        return activeUsersRepository.findAll().stream()
                .map(record -> record.getDate() + "," + record.getUserIDs().size())
                .collect(Collectors.joining("\n"));
    }

    @RequestMapping(value = "/api/group", method = RequestMethod.POST, produces = "application/json")
    public List<BroadcastGroup> createGroup(@RequestParam("name") String name, @RequestParam("setSize") Integer setSize) {
        if(broadcastRepository.findOne(name) == null) {
            AtomicInteger counter = new AtomicInteger(0);
            List<String> userIds = session.getUsers().stream()
                    .filter(slackUser -> !slackUser.isBot())
                    .map(SlackPersona::getId)
                    .collect(Collectors.toList());
            List<BroadcastSet> broadcastSets = Lists.partition(userIds, setSize).stream()
                    .map(set -> new BroadcastSet(counter.incrementAndGet(), set, new ArrayList<BroadcastMessage>()))
                    .collect(Collectors.toList());
            broadcastRepository.save(new BroadcastGroup(name, broadcastSets, new Date()));
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

    @RequestMapping(value = "/api/group", method = RequestMethod.DELETE, produces = "application/json")
    public List<BroadcastGroup> deleteGroups(@RequestParam("name") String name) {
        broadcastRepository.delete(name);
        return broadcastRepository.findAll();
    }

    @RequestMapping(value = "/api/community", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Integer> getCommunitySize() {
        return ImmutableMap.of("size", session.getUsers().size());
    }

    @RequestMapping(value = "/api/broadcast", method = RequestMethod.POST, produces = "application/json")
    public List<BroadcastGroup> broadcastMessage(@RequestParam("name") String name, @RequestParam("set") Integer setId,  @RequestParam("message") String message) {
        BroadcastGroup group = broadcastRepository.findOne(name);
        if(group != null) {
            Optional<BroadcastSet> setOptional = group.getBroadcastSets().stream()
                    .filter(set -> set.getId() == setId)
                    .findFirst();
            if(setOptional.isPresent()) {
                BroadcastSet set = setOptional.get();
                set.getMessages().add(new BroadcastMessage(message, new Date()));
                broadcastRepository.save(group);
	            executorService.execute(() -> {
		            set.getUsers().forEach(userId -> {
			            SlackUser user = session.findUserById(userId);
			            session.sendMessageToUser(user, message, null);
		            });
	            });
            } else {
                log.error("Can't find set with id {}", setId);
            }

        }
        else {
            log.error("Can't find group with name {}", name);
        }
	    return broadcastRepository.findAll();
    }
}
