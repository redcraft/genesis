package net.redcraft.genesis.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */
public class SlackURL {

    @Id
    private String url;

    private List<Reference> references;

    private String title;

    private String description;

    private String imageURL;

    public SlackURL() {
    }

    public SlackURL(String url, List<Reference> references, String title, String description, String imageURL) {
        this.url = url;
        this.references = references;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "SlackURL{" +
                "url='" + url + '\'' +
                ", references=" + references +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
