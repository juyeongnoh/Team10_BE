package bdbe.bdbd.bay;


import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BayService {
    private final BayJPARepository bayJPARepository;
    private final CarwashJPARepository carwashJPARepository;


    public void createBay(BayRequest.SaveDTO dto, Long carwashId) {
        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Bay bay = dto.toBayEntity(carwash);
        System.out.println(bay);
        bayJPARepository.save(bay);
    }

    public void deleteBay(Long bayId) {
        if (bayJPARepository.existsById(bayId)) { // 존재한다면 삭제
            bayJPARepository.deleteById(bayId);
        } else throw new EntityNotFoundException("bayId : "+ bayId + " not found");
    }

    public void changeStatus(Long bayId, int status) {
        Bay bay = bayJPARepository.findById(bayId)
                .orElseThrow(() -> new IllegalArgumentException("Bay not found"));
        bay.changeStatus(status);
    }
}
