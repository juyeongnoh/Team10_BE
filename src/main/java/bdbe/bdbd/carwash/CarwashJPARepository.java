package bdbe.bdbd.carwash;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarwashJPARepository extends JpaRepository<Carwash, Long> {
    Carwash findFirstBy(); // 테스트시에 사용
    @Query(value = "SELECT cw.* FROM carwash cw JOIN location l ON cw.l_id = l.id WHERE ST_Distance_Sphere(point(l.longitude, l.latitude), point(:longitude, :latitude)) <= 10000", nativeQuery = true)
    List<Carwash> findCarwashesWithin10Kilometers(@Param("latitude") double latitude, @Param("longitude") double longitude);

    Optional<Carwash> findById(Long carwashId);
    // user의 id로 세차장 id 리스트 찾기
    @Query("SELECT c.id FROM Carwash c WHERE c.user.id = :userId")
    List<Long> findCarwashIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Carwash c WHERE c.user.id = :userId")
    List<Carwash> findCarwashesByUserId(@Param("userId") Long userId);

    Optional<Carwash> findByIdAndUser_Id(Long carwashId, Long userId);

    List<Carwash> findAllByIdInAndUser_Id(List<Long> carwashIds, Long userId);

    List<Carwash> findByUser_Id(Long userId);
}
