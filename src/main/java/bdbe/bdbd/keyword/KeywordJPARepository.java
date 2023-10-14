package bdbe.bdbd.keyword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordJPARepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByType(int type); // type이 1인 것만 가져오기
}
