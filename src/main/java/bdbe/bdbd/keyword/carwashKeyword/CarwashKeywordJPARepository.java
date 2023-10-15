package bdbe.bdbd.keyword.carwashKeyword;

import bdbe.bdbd.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarwashKeywordJPARepository extends JpaRepository<CarwashKeyword, Long> {
    List<CarwashKeyword> findByKeywordIn(List<Keyword> keywords);

}
