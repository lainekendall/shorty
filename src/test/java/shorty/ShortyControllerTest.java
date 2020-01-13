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
    private static ShortLink SHORT_LINK = new ShortLink("url", "1234", Collections.emptyList());

    @Before
    public void setUp() {
        repository.save(SHORT_LINK);
    }

    @Test
    public void testAll() {
        assertThat(controller.all()).extracting(ShortLink::getUrl).containsOnly("url");
    }

    @Test
    public void testRedirect() {
        assertThat(controller.hashRedirect("1234")).extracting(RedirectView::getUrl).isEqualTo("url");
    }

    @Test
    public void testRedirectNull() {
        assertThat(controller.hashRedirect("notASavedHash")).extracting(RedirectView::getUrl).isNull();
    }
}
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(ShortyController.class)
//public class ShortyControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//
//
////    @Autowired
////    private TestEntityManager entityManager;
//
////    private ShortyController controller = new ShortyController();
//
//    @Autowired
//    private ShortLinkRepository repository;
//
//    @Before
//    public void setUp() {
//    }
//
//    @Test
//    public void testVisitsPerDay() {
//        final ShortLink shortLink = new ShortLink("https://example.com", "123456789", Collections.emptyList());
//        shortLink.setCreatedAt(LocalDateTime.of(2020, 1, 1, 3, 37));
//        final long zeroVisits = ShortyController.visitsPerDay(shortLink);
//        assertThat(zeroVisits).isGreaterThanOrEqualTo(0L);
//
//        shortLink.setVisited(19);
//        final long someVisits = ShortyController.visitsPerDay(shortLink);
//        assertThat(someVisits).isLessThanOrEqualTo(3L);
//    }
//
//    @Test
//    public void testAll() throws Exception {
//        mockMvc.perform(get("/")).andReturn();
//    }
//}