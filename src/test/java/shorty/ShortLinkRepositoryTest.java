package shorty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShortLinkRepositoryTest {


    @Autowired
    private ShortLinkRepository repository;

    @Test
    public void testFindByHash() {
        final ShortLink shortLink = new ShortLink("https://example.com", "123456789", Collections.emptyList());
        repository.save(shortLink);
        List<ShortLink> shortLinks = repository.findByHash("123456789");

        assertThat(shortLinks).extracting(ShortLink::getHash).containsOnly("123456789");
        assertThat(shortLinks).extracting(ShortLink::getUrl).containsOnly("https://example.com");
        assertThat(shortLinks).extracting(ShortLink::getVisited).containsOnly(0);
        assertThat(shortLinks).extracting(ShortLink::getCreatedAt).isNotEmpty();

    }
}
