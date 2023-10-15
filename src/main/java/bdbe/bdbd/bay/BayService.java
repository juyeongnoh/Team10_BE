package bdbe.bdbd.bay;


import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.reservation.ReservationJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@RequiredArgsConstructor
@Service
public class BayService {
    private final BayJPARepository bayJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;

    public void createBay(BayRequest.SaveDTO dto, Long carwashId) {
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Bay bay = dto.toBayEntity(carwash);

        bayJPARepository.save(bay);
    }

//    public void deleteBay(Long bayId) {
//        // 존재하는지 확인
//        Bay bay = bayJPARepository.findById(bayId)
//                .orElseThrow(() -> new EntityNotFoundException("bayId : " + bayId + " not found"));
//
//        // 연관된 Reservation 레코드 삭제
//        reservationJPARepository.deleteAllByBayId(bayId);
//
//        // Bay 레코드 삭제
////        bayJPARepository.delete(bay);
//    }

    public void changeStatus(Long bayId, int status) {
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new IllegalArgumentException("Bay not found"));
        bay.changeStatus(status);
    }
}
