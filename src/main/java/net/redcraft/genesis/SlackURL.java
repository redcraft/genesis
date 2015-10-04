package net.redcraft.genesis;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */
public class SlackURL {

    @Id
    private String url;

    private List<Reference> references;

    public SlackURL() {
    }

    public SlackURL(String url, Reference reference) {
        this.url = url;
        this.references = Arrays.asList(reference);
    }

    public SlackURL(String url, List<Reference> references) {
        this.url = url;
        this.references = references;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }
}
