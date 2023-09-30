package bdbe.bdbd.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewJPARepository extends JpaRepository<Review, Integer> {

    Optional<Object> findById(int id);
}
