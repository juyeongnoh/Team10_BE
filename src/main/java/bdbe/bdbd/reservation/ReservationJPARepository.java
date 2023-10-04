package bdbe.bdbd.reservation;

import bdbe.bdbd.carwash.Carwash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByBayId(Long bayId); // 베이의 예약 목록 찾기

    List<Reservation> findReservationByUserId(Long userId); // userId의 예약 목록 찾기


    List<Reservation> findReservationByBayIdAndUserId(Long bayId, Long userId);
}
