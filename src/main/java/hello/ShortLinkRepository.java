package hello;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ShortLinkRepository extends CrudRepository<ShortLink, Long> {

	List<ShortLink> findByUrl(String url);
	List<ShortLink> findByHash(String hash);
}
