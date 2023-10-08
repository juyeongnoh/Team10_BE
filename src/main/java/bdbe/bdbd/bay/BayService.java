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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BayService {
    private final BayJPARepository bayJPARepository;
    private final CarwashJPARepository carwashJPARepository;


    public void createBay(BayRequest.SaveDTO dto) {
        Carwash carwash = carwashJPARepository.findById(dto.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Bay bay = dto.toBayEntity(carwash);
        System.out.println(bay);
        bayJPARepository.save(bay);
    }

    public void deleteBay(BayRequest.SaveDTO saveDTO) {
        Carwash carwash = carwashJPARepository.findById(saveDTO.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));

        Object bay = bayJPARepository.findAllById(saveDTO.getBayId())
                .orElseThrow(() -> new IllegalArgumentException("Bay not found"));

        bayJPARepository.delete((Bay) bay);
    }

    public void statusBay(BayRequest.SaveDTO saveDTO) {

    }
}
