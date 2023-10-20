package bdbe.bdbd.carwash;


import bdbe.bdbd._core.errors.utils.Haversine;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.review.ReviewJPARepository;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

//@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CarwashService {
    private final CarwashJPARepository carwashJPARepository;
    private final KeywordJPARepository keywordJPARepository;
    private final LocationJPARepository locationJPARepository;
    private final OptimeJPARepository optimeJPARepository;
    private final CarwashKeywordJPARepository carwashKeywordJPARepository;
    private final ReviewJPARepository reviewJPARepository;
    private final BayJPARepository bayJPARepository;

    public List<CarwashResponse.FindAllDTO> findAll(int page) {
        // Pageable 검증
        if (page < 0) {
            throw new IllegalArgumentException("Invalid page number.");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Carwash> carwashEntities = carwashJPARepository.findAll(pageable);

        // Page 객체 검증
        if (carwashEntities == null || !carwashEntities.hasContent()) {
            throw new NoSuchElementException("No carwash entities found.");
        }

        List<CarwashResponse.FindAllDTO> carwashResponses = carwashEntities.getContent().stream()
                .map(CarwashResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        // List 객체 검증
        if (carwashResponses == null || carwashResponses.isEmpty()) {
            throw new NoSuchElementException("No carwash entities transformed.");
        }

        return carwashResponses;
    }

    @Transactional
    public void save(CarwashRequest.SaveDTO saveDTO, User sessionUser) {
        // 별점은 리뷰에서 계산해서 넣어주기
        // 지역
        Location location = saveDTO.toLocationEntity();
        locationJPARepository.save(location);
        // 세차장
        Carwash carwash = saveDTO.toCarwashEntity(location, sessionUser);
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


    public List<CarwashRequest.CarwashDistanceDTO> findNearbyCarwashesByUserLocation(CarwashRequest.UserLocationDTO userLocation) {
        List<Carwash> carwashes = carwashJPARepository.findCarwashesWithin10Kilometers(userLocation.getLatitude(), userLocation.getLongitude());

        return carwashes.stream()
                .map(carwash -> {
                    double distance = Haversine.distance(userLocation.getLatitude(), userLocation.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude());
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    CarwashRequest.CarwashDistanceDTO dto = new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price);
                    return dto;
                })
                .sorted(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .collect(Collectors.toList());
    }

    public CarwashRequest.CarwashDistanceDTO findNearestCarwashByUserLocation(CarwashRequest.UserLocationDTO userLocation) {
        List<Carwash> carwashes = carwashJPARepository.findCarwashesWithin10Kilometers(userLocation.getLatitude(), userLocation.getLongitude());

        return carwashes.stream()
                .map(carwash -> {
                    double distance = Haversine.distance(userLocation.getLatitude(), userLocation.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude());
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    CarwashRequest.CarwashDistanceDTO dto = new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price);
                    return dto;
                })
                .min(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .orElse(null);
    }

    public List<CarwashRequest.CarwashDistanceDTO> findCarwashesByKeywords(CarwashRequest.SearchRequestDTO searchRequest) {
        List<Carwash> carwashesWithin10Km = carwashJPARepository.findCarwashesWithin10Kilometers(searchRequest.getLatitude(), searchRequest.getLongitude());

        List<Keyword> selectedKeywords = keywordJPARepository.findAllById(searchRequest.getKeywordIds());

        List<CarwashKeyword> carwashKeywords = carwashKeywordJPARepository.findByKeywordIn(selectedKeywords);

        Set<Long> carwashIdsWithSelectedKeywords = carwashKeywords.stream()
                .map(carwashKeyword -> carwashKeyword.getCarwash().getId())
                .collect(Collectors.toSet());

        List<CarwashRequest.CarwashDistanceDTO> result = carwashesWithin10Km.stream()
                .filter(carwash -> carwashIdsWithSelectedKeywords.contains(carwash.getId()))
                .map(carwash -> {
                    double distance = Haversine.distance(
                            searchRequest.getLatitude(), searchRequest.getLongitude(),
                            carwash.getLocation().getLatitude(), carwash.getLocation().getLongitude()
                    );
                    double rate = carwash.getRate();
                    int price = carwash.getPrice();

                    return new CarwashRequest.CarwashDistanceDTO(carwash.getId(), carwash.getName(), carwash.getLocation(), distance, rate, price);
                })
                .sorted(Comparator.comparingDouble(CarwashRequest.CarwashDistanceDTO::getDistance))
                .collect(Collectors.toList());

        return result;
    }

    public CarwashResponse.findByIdDTO getfindById(Long carwashId) {

        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("not found carwash"));
        int reviewCnt = reviewJPARepository.findByCarwash_Id(carwashId).size();
        int bayCnt = bayJPARepository.findByCarwashId(carwashId).size();
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("location not found"));
        List<Long> keywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);

        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwashId);
        Map<DayType, Optime> optimeByDayType = new EnumMap<>(DayType.class);
        optimeList.forEach(ol -> optimeByDayType.put(ol.getDayType(), ol));

        Optime weekOptime = optimeByDayType.get(DayType.WEEKDAY);
        Optime endOptime = optimeByDayType.get(DayType.WEEKEND);

        return new CarwashResponse.findByIdDTO(carwash, reviewCnt, bayCnt, location, keywordIds, weekOptime, endOptime);
    }

    public CarwashResponse.carwashDetailsDTO findCarwashByDetails(Long carwashId) {

        Carwash carwash = carwashJPARepository.findById(carwashId)
                .orElseThrow(() -> new IllegalArgumentException("not found carwash"));
        Location location = locationJPARepository.findById(carwash.getLocation().getId())
                .orElseThrow(() -> new NoSuchElementException("location not found"));
        int bayCnt = bayJPARepository.findByCarwashId(carwashId).size();
        List<Long> keywordIds = carwashKeywordJPARepository.findKeywordIdsByCarwashId(carwashId);

        List<Optime> optimeList = optimeJPARepository.findByCarwash_Id(carwashId);
        Map<DayType, Optime> optimeByDayType = new EnumMap<>(DayType.class);
        optimeList.forEach(ol -> optimeByDayType.put(ol.getDayType(), ol));

        Optime weekOptime = optimeByDayType.get(DayType.WEEKDAY);
        Optime endOptime = optimeByDayType.get(DayType.WEEKEND);

        return new CarwashResponse.carwashDetailsDTO(carwash, location, bayCnt, keywordIds, weekOptime, endOptime);


    }

    private static final Logger logger = LoggerFactory.getLogger(CarwashService.class);
    public CarwashRequest.updateCarwashDetailsDTO updateCarwashDetails(Long carwashId, CarwashRequest.updateCarwashDetailsDTO updatedto) {

        try {
            CarwashRequest.updateLocationDTO updateLocationDTO = updatedto.getLocationDTO();

            Carwash carwash = carwashJPARepository.findById(carwashId)
                    .orElseThrow(() -> new IllegalArgumentException("not found carwash"));

            carwash.setName(updatedto.getName());
            carwash.setTel(updatedto.getTel());
            carwash.setDes(updatedto.getDescription());
            carwash.setPrice(updatedto.getPrice());

            Location location = locationJPARepository.findById(carwash.getLocation().getId())
                    .orElseThrow(() -> new NoSuchElementException("location not found"));


            location.setAddress(updateLocationDTO.getAddress());
            location.setPlace(updateLocationDTO.getPlaceName());


            CarwashRequest.updateOperatingTimeDTO updateOperatingTimeDTO = updatedto.getOptime();

//            updateOperatingTimeDTO.setWeekday(updateOperatingTimeDTO.getWeekday());
//            updateOperatingTimeDTO.setWeekend(updateOperatingTimeDTO.getWeekend());

            carwashJPARepository.save(carwash);
            locationJPARepository.save(location);


            updatedto.setOptime(updateOperatingTimeDTO);
            updatedto.setupdateLocationDTO(updatedto.toLocationDTO(location));

            return updatedto;

        } catch (Exception e) {
            logger.error("Error in updateCarwashDetails", e);
            throw e;
        }

    }

}
