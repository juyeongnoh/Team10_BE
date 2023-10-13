package bdbe.bdbd.reservation;


import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.reservation.ReservationResponse.ReservationInfoDTO;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationJPARepository reservationJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final BayJPARepository bayJPARepository;
    private final LocationJPARepository locationJPARepository;
//    private final Fil


    @Transactional
    public void save(ReservationRequest.SaveDTO dto, Long carwashId, Long bayId, User sessionUser) {
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("carwash not found"));
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new IllegalArgumentException("bay not found"));
        //예약 생성
        Reservation reservation = dto.toReservationEntity(carwash, bay, sessionUser);
        reservationJPARepository.save(reservation);
    } //변경감지, 더티체킹, flush, 트랜잭션 종료

    public ReservationResponse.findAllResponseDTO findAllByCarwash(Long carwashId, User sessionUser) {
        //베이에서 해당 세차장 id와 관련된 베이 객체 모두 찾기
        List<Bay> bayList = bayJPARepository.findByCarwashId(carwashId);
        // id만 추출하기
        List<Long> bayIdList = bayJPARepository.findIdsByCarwashId(carwashId);
        //예약에서 베이 id 리스트로 모두 찾기
        List<Reservation> reservationList = reservationJPARepository.findByBayIdIn(bayIdList);
        return new ReservationResponse.findAllResponseDTO(bayList, reservationList);
    }

    public ReservationResponse.findLatestOneResponseDTO fetchLatestReservation(User sessionUser) {
        // 가장 최근의 예약 찾기
        Reservation reservation = reservationJPARepository.findTopByUserIdOrderByIdDesc(sessionUser.getId())
                .orElseThrow(() -> new NoSuchElementException("no reservation found"));
        // 예약과 관련된 베이 찾기
        Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                .orElseThrow(() -> new NoSuchElementException("no bay found"));
        // 베이가 속해있는 세차장 찾기
        Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                .orElseThrow(() -> new NoSuchElementException("no carwash found"));
        // 세차장이 위치한 위치 찾기
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("no location found"));
        return new ReservationResponse.findLatestOneResponseDTO(reservation, bay, carwash, location);
    }

    public ReservationResponse.fetchCurrentStatusReservationDTO fetchCurrentStatusReservation(User sessionUser) {
        // 유저의 예약내역 모두 조회
        List<Reservation> reservationList = reservationJPARepository.findByUserId(sessionUser.getId());
        // 현재, 다가오는, 완료된 예약 찾기
        List<ReservationInfoDTO> current = new ArrayList<>();
        List<ReservationInfoDTO> upcoming = new ArrayList<>();
        List<ReservationInfoDTO> completed = new ArrayList<>();
        // 현재 날짜, 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        // 예약 분류하기
        for (Reservation reservation : reservationList) {
            Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Bay not found"));
            Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Carwash not found"));

            LocalDateTime startDateTime = reservation.getStartTime();
            LocalDate reservationDate = startDateTime.toLocalDate();
            LocalDateTime endDateTime = reservation.getEndTime();

            if (reservationDate.equals(today)) {
                if (now.isAfter(startDateTime) && now.isBefore(endDateTime)) {
                    current.add(new ReservationInfoDTO(reservation, bay, carwash));
                } else if (now.isBefore(startDateTime)) {
                    upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
                } else if (now.isAfter(endDateTime)) {
                    completed.add(new ReservationInfoDTO(reservation, bay, carwash));
                }
            } else if (reservationDate.isBefore(today)) {
                completed.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else if (reservationDate.isAfter(today)) {
                upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else {
                throw new IllegalStateException("reservation id: " + reservation.getId() + " 예약 상태가 올바르지 않습니다");
            }
        }
        return new ReservationResponse.fetchCurrentStatusReservationDTO(current, upcoming, completed);
    }
}
