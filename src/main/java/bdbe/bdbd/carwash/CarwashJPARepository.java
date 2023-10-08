package bdbe.bdbd.carwash;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarwashJPARepository extends JpaRepository<Carwash, Long> {
    Carwash findFirstBy(); // 맨 처음 하나 찾기


}
