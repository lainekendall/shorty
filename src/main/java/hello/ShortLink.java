package hello;

import javax.persistence.*;

@Entity
public class ShortLink {

    @Id
    private String url;
    private String hash;

    protected ShortLink() {
    }

    public ShortLink(String url, String hash) {
        this.url = url;
        this.hash = hash;
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
}
