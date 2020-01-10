package hello;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class ShortLink {

    @Id
    private String url;
    private String hash;
    @ElementCollection
    private List<String> custom;

    protected ShortLink() {
    }

    public ShortLink(final String url, final String hash) {
        this.url = url;
        this.hash = hash;
    }

    public ShortLink(final String url, final String hash, final List<String> custom) {
        this.url = url;
        this.hash = hash;
        this.custom = custom;
    }

    public ShortLink(final String url, final List<String> custom) {
        this.url = url;
        this.custom = custom;
    }

    @Override
    public String toString() {
        return String.format(
                "ShortLink[url='%s', hash='%s']",
                url, hash);
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }
    public List<String> getCustom() {
        return custom;
    }
}
