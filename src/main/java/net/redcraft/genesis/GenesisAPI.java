package net.redcraft.genesis;

import net.redcraft.genesis.domain.DigestReference;
import net.redcraft.genesis.domain.SlackURL;
import net.redcraft.genesis.repositories.DigestReferenceRepository;
import net.redcraft.genesis.repositories.SlackURLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by RED on 04/10/2015.
 */

@RestController
public class GenesisAPI {

    @Autowired
    private SlackURLRepository urlRepository;

    @Autowired
    private DigestReferenceRepository referenceRepository;

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
}
