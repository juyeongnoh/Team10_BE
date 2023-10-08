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
    private final KeywordJPARepository keywordJPARepository;
    private final RegionJPARepository regionJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final CarwashKeywordJPARepository carwashKeywordJPARepository;

    public List<CarwashResponse.FindAllDTO> findAll(int page) {
        // 1. 페이지 객체 만들기
        Pageable pageable = PageRequest.of(page, 10);
        // 2. DB 조회하기
        Page<Carwash> carwashes = carwashJPARepository.findAll(pageable);
        // 3. DTO 만들기
        List<CarwashResponse.FindAllDTO> dtos = carwashes.getContent().stream()
                .map(CarwashResponse.FindAllDTO::new)
                .collect(Collectors.toList());
        return dtos;
    }


    @Transactional // 트랜잭션 시작
    public void save(CarwashRequest.SaveDTO saveDTO, User sessionUser) {
        // 별점은 리뷰에서 계산해서 넣어주기
        // 지역
        Region region = saveDTO.toRegionEntity();
        regionJPARepository.save(region);
        // 세차장
        Carwash carwash = saveDTO.toCarwashEntity(region, sessionUser);
        carwashJPARepository.save(carwash);
        // 운영시간
        List<Optime> optimes = saveDTO.toOptimeEntities(carwash);
        optimeJPARepository.saveAll(optimes);
        // 키워드
        List<Long> keywordIdList = saveDTO.getKeywordId();
        List<CarwashKeyword> carwashKeywordList = new ArrayList<>();
        for (Long keywordId : keywordIdList) {
            Keyword keyword = keywordJPARepository.findById(keywordId)
                    .orElseThrow(() -> new IllegalArgumentException("Keyword not found"));
            //carwash-keyword 다대다 매핑
            CarwashKeyword carwashKeyword = CarwashKeyword.builder().carwash(carwash).keyword(keyword).build();
            carwashKeywordList.add(carwashKeyword);
        }
        carwashKeywordJPARepository.saveAll(carwashKeywordList);
    } //변경감지, 더티체킹, flush, 트랜잭션 종료

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
