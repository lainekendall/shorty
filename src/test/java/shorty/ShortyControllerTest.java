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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShortyControllerTest {
	@Autowired
	private TestEntityManager entityManager;

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

	@Test
	public void testVisitsPerDay() {
		final ShortLink shortLink = new ShortLink("https://example.com", "123456789", Collections.emptyList());
		shortLink.setCreatedAt(LocalDateTime.of(2020, 1, 5, 3, 37));
		final long zeroVisits = ShortyController.visitsPerDay(shortLink);
		assertThat(zeroVisits).isGreaterThanOrEqualTo(0L);

		shortLink.setVisited(19);
		final long someVisits = ShortyController.visitsPerDay(shortLink);
		assertThat(someVisits).isGreaterThanOrEqualTo(3L);

	}
}