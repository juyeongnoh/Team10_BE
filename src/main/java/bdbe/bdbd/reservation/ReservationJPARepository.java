package bdbe.bdbd.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBayIdIn(List<Long> bayIds); // bay id 리스트로 관련된 모든 reservation 찾기

    Optional<Reservation> findTopByUserIdOrderByIdDesc(Long userId); // 해당 유저의 가장 최근 예약 id(가장 큰 예약 id) 하나 찾기

    List<Reservation> findReservationsByBayId(Long bayId); // 베이의 예약 목록 찾기

    List<Reservation> findByUserId(Long userId); // user의 예약 목록 찾기


    List<Reservation> findReservationByBayIdAndUserId(Long bayId, Long userId);
}
