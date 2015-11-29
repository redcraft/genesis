package net.redcraft.genesis.domain;

import java.util.Date;

/**
 * Created by RED on 04/10/2015.
 */
public class Reference {

    private String channel;
    private Date date;

    public Reference() {
    }

    public Reference(String channel, Date date) {
        this.channel = channel;
        this.date = date;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reference reference = (Reference) o;

        if (!channel.equals(reference.channel)) return false;
        return date.equals(reference.date);

    }

    @Override
    public int hashCode() {
        int result = channel.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "channel='" + channel + '\'' +
                ", date=" + date +
                '}';
    }
}
