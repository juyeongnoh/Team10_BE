package bdbe.bdbd.optime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptimeJPARepository extends JpaRepository<Optime, Long> {
    Optime findFirstBy();

    List<Optime> findByCarwash_Id(Long carwashId);

}
