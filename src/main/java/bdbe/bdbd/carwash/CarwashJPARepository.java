package bdbe.bdbd.carwash;

import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarwashJPARepository extends JpaRepository<Carwash, Long> {
    Carwash findFirstBy(); // 맨 처음 하나 찾기
    @Query(value = "SELECT cw.* FROM carwash cw JOIN location l ON cw.l_id = l.id WHERE ST_Distance_Sphere(point(l.longitude, l.latitude), point(:longitude, :latitude)) <= 10000", nativeQuery = true)
    List<Carwash> findCarwashesWithin10Kilometers(@Param("latitude") double latitude, @Param("longitude") double longitude);

    Optional<Carwash> findById(Long carwashId);

}
