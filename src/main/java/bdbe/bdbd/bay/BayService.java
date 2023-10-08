package bdbe.bdbd.bay;


import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.carwash.CarwashRequest;
import bdbe.bdbd.carwash.CarwashResponse;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeywordJPARepository;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.region.RegionJPARepository;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void statusBay(BayRequest.SaveDTO saveDTO) {

    }
}
