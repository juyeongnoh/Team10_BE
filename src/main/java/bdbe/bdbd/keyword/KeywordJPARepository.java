package bdbe.bdbd.keyword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordJPARepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByType(KeywordType type);
}
