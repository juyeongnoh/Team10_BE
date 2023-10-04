package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.Carwash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BayJPARepository extends JpaRepository<Bay, Long> {
    Bay findFirstBy(); // 하나 찾기
}
