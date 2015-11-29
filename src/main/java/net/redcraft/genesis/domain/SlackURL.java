package net.redcraft.genesis.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by RED on 04/10/2015.
 */
public class SlackURL {

    private String prURL;

    @Id
    private String url;

    private List<Reference> references;

    private String title;

    private String description;

    private String imageURL;

    private Date lastPosted;

    public SlackURL() {
    }

    public SlackURL(String prURL, String url, List<Reference> references, String title, String description, String imageURL, Date lastPosted) {
        this.prURL = prURL;
        this.url = url;
        this.references = references;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.lastPosted = lastPosted;
    }

    public String getPrURL() {
        return prURL;
    }

    public void setPrURL(String prURL) {
        this.prURL = prURL;
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

    public Date getLastPosted() {
        return lastPosted;
    }

    public void setLastPosted(Date lastPosted) {
        this.lastPosted = lastPosted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlackURL slackURL = (SlackURL) o;

        if (!prURL.equals(slackURL.prURL)) return false;
        if (!url.equals(slackURL.url)) return false;
        if (!references.equals(slackURL.references)) return false;
        if (title != null ? !title.equals(slackURL.title) : slackURL.title != null) return false;
        if (description != null ? !description.equals(slackURL.description) : slackURL.description != null)
            return false;
        if (imageURL != null ? !imageURL.equals(slackURL.imageURL) : slackURL.imageURL != null) return false;
        return lastPosted.equals(slackURL.lastPosted);

    }

    @Override
    public int hashCode() {
        int result = prURL.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + references.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageURL != null ? imageURL.hashCode() : 0);
        result = 31 * result + lastPosted.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SlackURL{" +
                "prURL='" + prURL + '\'' +
                ", url='" + url + '\'' +
                ", references=" + references +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", lastPosted=" + lastPosted +
                '}';
    }
}
