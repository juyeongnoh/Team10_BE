package bdbe.bdbd.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewJPARepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long id);

    long countByCarwash_Id(Long carwashId);


}
