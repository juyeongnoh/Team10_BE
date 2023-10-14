package bdbe.bdbd.location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJPARepository extends JpaRepository<Location, Long> {
    Location findFirstBy();
}
