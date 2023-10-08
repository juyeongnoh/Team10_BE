package bdbe.bdbd.reservation;


import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.region.RegionJPARepository;
import bdbe.bdbd.reservation.ReservationResponse.ReservationInfoDTO;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationJPARepository reservationJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final BayJPARepository bayJPARepository;
    private final RegionJPARepository regionJPARepository;
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
        Region region = regionJPARepository.findById(carwash.getRegion().getId())
                .orElseThrow(() -> new NoSuchElementException("no region found"));
        return new ReservationResponse.findLatestOneResponseDTO(reservation, bay, carwash, region);
    }

    public ReservationResponse.fetchCurrentStatusReservationDTO fetchCurrentStatusReservation(User sessionUser) {
        // 유저의 예약내역 모두 조회
        List<Reservation> reservationList = reservationJPARepository.findByUserId(sessionUser.getId());
        // 현재, 다가오는, 완료된 예약 찾기
        List<ReservationInfoDTO> current = new ArrayList<>();
        List<ReservationInfoDTO> upcoming = new ArrayList<>();
        List<ReservationInfoDTO> completed = new ArrayList<>();
        // 현재 날짜, 시간 가져오기
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        // 예약 분류하기
        for (Reservation reservation : reservationList) {
            Bay bay = bayJPARepository.findById(reservation.getBay().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Bay not found"));
            Carwash carwash = carwashJPARepository.findById(bay.getCarwash().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Carwash not found"));
            // 예약 분류하기
            if (reservation.getDate().equals(today)) { // 오늘일 경우
                // 진행중인 예약일 경우 (현재시간이 예약의 시작시간 이후, 종료시간 전)
                if (now.isAfter(reservation.getStartTime()) && now.isBefore(reservation.getEndTime())) {
                    current.add(new ReservationInfoDTO(reservation, bay, carwash));
                }
                // 예정된 예약일 경우 (현재시간이 예약의 시작시간 전)
                else if (now.isBefore(reservation.getStartTime())) {
                    upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
                }
                // 완료된 예약일 경우 (현재시간이 예약의 종료시간 이후)
                else if (now.isAfter(reservation.getEndTime())) {
                    completed.add(new ReservationInfoDTO(reservation, bay, carwash));
                }
            } else if (reservation.getDate().isBefore(today)) {
                // 예약날짜가 현재날짜 이전인 경우
                completed.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else if (reservation.getDate().isAfter(today)) {
                // 예약날짜가 현재날짜 이후인 경우
                upcoming.add(new ReservationInfoDTO(reservation, bay, carwash));
            } else {
                throw new IllegalStateException("reservation id: " + reservation.getId() + " 예약 상태가 올바르지 않습니다");
            }
        }
        return new ReservationResponse.fetchCurrentStatusReservationDTO(current, upcoming, completed);
    }
}
