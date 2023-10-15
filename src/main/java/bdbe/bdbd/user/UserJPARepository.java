package bdbe.bdbd.user;

import bdbe.bdbd.bay.Bay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Long> {
    User findFirstBy();
    Optional<User> findByEmail(String email);
}
