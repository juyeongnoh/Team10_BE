package bdbe.bdbd.reservation;


import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.region.RegionJPARepository;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional // 트랜잭션 시작
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

    public ReservationResponse.findLatestOneResponseDTO fetchLatestReservation(User sessionUser){
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
}
