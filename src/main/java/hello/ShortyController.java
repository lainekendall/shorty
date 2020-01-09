package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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
          redirectView.setUrl(shorty.get(0).getUrl());
        }
        return redirectView;
    }

    @RequestMapping("/create")
    public String create(@RequestParam(value = "url") String url) {
        final ShortLink shortLink = new ShortLink(url, sanitizeHash(url));
        repository.save(shortLink);
        return createUrl(shortLink.getUrl());
    }

    private String createUrl(final String url) {
        final String hash = String.valueOf(url.hashCode()).replace("-", "");
        return "http://" + serverHost + ":" + serverPort + "/" + hash;
    }

    private String sanitizeHash(final String url) {
        return String.valueOf(url.hashCode()).replace("-", "");
    }
}
