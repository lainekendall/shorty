package shorty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
public class ShortyController {

    @Autowired
    private ShortLinkRepository repository;

    @Value("${server.port}")
    private String serverPort;
    @Value("${server.host}")
    private String serverHost;

    @RequestMapping("/")
    public Iterable<ShortLink> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{hash}")
    public RedirectView hashRedirect(@PathVariable final String hash) {
        List<ShortLink> shorty = repository.findByHash(hash);
        RedirectView redirectView = new RedirectView();
        if (!shorty.isEmpty()) {
            ShortLink shortLink = shorty.get(0);
            shortLink.setVisited(shortLink.getVisited() + 1);
            repository.save(shortLink);
            System.out.println(shortLink);
            redirectView.setUrl(shortLink.getUrl());
        }
        return redirectView;
    }

    @RequestMapping("/create")
    public String create(@RequestParam(value = "url") String url) {
        final List<ShortLink> shortLinks = repository.findByUrl(url);
        ShortLink shortLink = new ShortLink(url, sanitizeHash(url), null);
        if (!shortLinks.isEmpty()) {
            shortLink = shortLinks.get(0);
        }
        repository.save(shortLink);
        final String shortLinkUrl = createUrl(shortLink.getUrl());
        return "<a href=" + shortLinkUrl + " >" + shortLinkUrl + "</a>";
    }

    @RequestMapping("/custom")
    public String customLink(@RequestParam(value = "url") String url, @RequestParam(value = "custom") String customShorty) {
        List<ShortLink> shortLinks = repository.findByUrl(url);
        if (shortLinks.isEmpty()) {
            repository.save(new ShortLink(url, sanitizeHash(url), Collections.singleton(customShorty)));
            return "Your new custom link is: " + customShorty;
        }
        final ShortLink shortLink = shortLinks.get(0);
        final Set<String> custom = shortLink.getCustom();
        custom.add(customShorty);
        shortLink.setCustom(custom);
        repository.save(shortLink);
        return "Your new custom link is: " + customShorty;
    }

    @RequestMapping("/stats")
    public String stats(@RequestParam(value = "url") String url) {
        final List<ShortLink> shortLinks = repository.findByUrl(url);
        if (shortLinks.isEmpty()) {
            return "This short link hasn't been created yet";
        }
        ShortLink shortLink = shortLinks.get(0);
        return shortLink + "\n and its been visited " + visitsPerDay(shortLink) + " times a day";
    }

    static long visitsPerDay(final ShortLink shortLink) {
        long now = DAYS.between(shortLink.getCreatedAt(), LocalDateTime.now());
        if (now == 0) {
            return 0;
        }
        return shortLink.getVisited() / now;
    }

    String createUrl(final String url) {
        final String hash = String.valueOf(url.hashCode()).replace("-", "");
        return "http://" + serverHost + ":" + serverPort + "/" + hash;
    }

    private String sanitizeHash(final String url) {
        return String.valueOf(url.hashCode()).replace("-", "");
    }
}
