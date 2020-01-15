/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shorty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public class ShortyControllerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShortyController controller;

    @Autowired
    private ShortLinkRepository repository;
    private static ShortLink SHORT_LINK = new ShortLink("url", "1234", Collections.emptySet());

    @Test
    public void testAll() {
        repository.save(SHORT_LINK);
        assertThat(controller.all()).extracting(ShortLink::getUrl).containsOnly("url");
    }

    @Test
    public void testRedirect() {
        repository.save(SHORT_LINK);
        assertThat(controller.hashRedirect("1234")).extracting(RedirectView::getUrl).isEqualTo("url");
    }

    @Test
    public void testRedirectNull() {
        assertThat(controller.hashRedirect("notASavedHash")).extracting(RedirectView::getUrl).isEqualTo("");
    }

    @Test
    public void testUpsert() {
        repository.deleteAll();
        final String shortLinkUrl = controller.createUrl("url");
        assertThat(controller.create("url")).isEqualTo("<a href=" + shortLinkUrl + " >" + shortLinkUrl + "</a>");
        assertThat(controller.create("url")).isEqualTo("<a href=" + shortLinkUrl + " >" + shortLinkUrl + "</a>");
        assertThat(controller.all()).extracting(ShortLink::getUrl).containsOnly("url");
    }

    @Test
    public void testCreate() {
        final String shortLinkUrl = controller.createUrl("newUrl");
        assertThat(controller.create("newUrl")).isEqualTo("<a href=" + shortLinkUrl + " >" + shortLinkUrl + "</a>");
    }

    @Test
    public void testCustom() {
        assertThat(controller.customLink("url3", "my-custom-link")).isEqualTo("Your new custom link is: my-custom-link");
        assertThat(repository.findByUrl("url3")).extracting(ShortLink::getCustom).containsOnly(Collections.singleton("my-custom-link"));
    }

    @Test
    public void testStats() {
        assertThat(controller.stats("url")).contains("ShortLink[url='url', hash='1234', custom='[]', createdAt=");
    }
}
