package bdbe.bdbd.keyword.carwashKeyword;

import bdbe.bdbd.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarwashKeywordJPARepository extends JpaRepository<CarwashKeyword, Long> {
    List<CarwashKeyword> findByKeywordIn(List<Keyword> keywords);

    @Query("SELECT ck.keyword.id FROM CarwashKeyword ck WHERE ck.carwash.id = :carwashId")
    List<Long> findKeywordIdsByCarwashId(@Param("carwashId") Long carwashId);
}
