package bdbe.bdbd.reservation;

import bdbe.bdbd.carwash.Carwash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {

}
