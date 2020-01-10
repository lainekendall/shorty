package shorty;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ShortLink {

    @Id
    private String url;
    private String hash;
    @ElementCollection
    private List<String> custom;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Integer visited;

    protected ShortLink() {
    }

    public ShortLink(final String url, final String hash, final List<String> custom) {
        this.url = url;
        this.hash = hash;
        this.custom = custom;
        this.visited = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "ShortLink[url='%s', hash='%s', custom='%s', createdAt='%s', visited='%s']",
                url, hash, custom, createdAt, visited);
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getVisited() {
        return visited;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setCustom(List<String> custom) {
        this.custom = custom;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setVisited(Integer visited) {
        this.visited = visited;
    }


}
