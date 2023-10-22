package bdbe.bdbd.carwash;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class CarwashRestController {

    private final CarwashService carwashService;

    // 전체 세차장 목록 조회, 10개씩 페이징
    @GetMapping("/carwashes")
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<CarwashResponse.FindAllDTO> dtos = carwashService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(dtos);
        return ResponseEntity.ok(apiResult);
    }

    //세차장 등록
    @PostMapping("/owner/carwashes/register")
    public ResponseEntity<?> save(@RequestBody @Valid CarwashRequest.SaveDTO saveDTOs, Errors errors,  @AuthenticationPrincipal CustomUserDetails userDetails) {
        carwashService.save(saveDTOs, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/carwashes/search")
    public ResponseEntity<?> findCarwashesByKeywords(@RequestParam List<Long> keywordIds,
                                                     @RequestParam double latitude,
                                                     @RequestParam double longitude) {
        CarwashRequest.SearchRequestDTO searchRequest = new CarwashRequest.SearchRequestDTO();
        searchRequest.setKeywordIds(keywordIds);
        searchRequest.setLatitude(latitude);
        searchRequest.setLongitude(longitude);

        List<CarwashRequest.CarwashDistanceDTO> carwashes = carwashService.findCarwashesByKeywords(searchRequest);
        return ResponseEntity.ok(ApiUtils.success(carwashes));
    }

    @GetMapping("/carwashes/nearby")
    public ResponseEntity<?> findNearestCarwashesByUserLocation(@RequestParam double latitude, @RequestParam double longitude) {
        CarwashRequest.UserLocationDTO userLocation = new CarwashRequest.UserLocationDTO();
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        List<CarwashRequest.CarwashDistanceDTO> carwashes = carwashService.findNearbyCarwashesByUserLocation(userLocation);
        return ResponseEntity.ok(ApiUtils.success(carwashes));
    }

    @GetMapping("/carwashes/recommended")
    public ResponseEntity<?> findNearestCarwash(@RequestParam double latitude, @RequestParam double longitude) {
        CarwashRequest.UserLocationDTO userLocation = new CarwashRequest.UserLocationDTO();
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        CarwashRequest.CarwashDistanceDTO carwash = carwashService.findNearestCarwashByUserLocation(userLocation);
        if (carwash != null) {
            return ResponseEntity.ok(ApiUtils.success(carwash));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/carwashes/{carwash_id}/info")
    public ResponseEntity<?> findById(@PathVariable("carwash_id") Long carwashId) {
        CarwashResponse.findByIdDTO findByIdDTO = carwashService.getfindById(carwashId);
        return ResponseEntity.ok(ApiUtils.success(findByIdDTO));
    }

    @GetMapping("/owner/carwashes/{carwash_id}/details") //세차장 정보 수정_세차장 기존 정보 불러오기
    public ResponseEntity<?> findCarwashByDetails(@PathVariable("carwash_id") Long carwashId) {
        CarwashResponse.carwashDetailsDTO carwashDetailsDTO = carwashService.findCarwashByDetails(carwashId);
        return ResponseEntity.ok(ApiUtils.success(carwashDetailsDTO));
    }

    @PutMapping("/owner/carwashes/{carwash_id}/details") //세차장 정보 수정_세차장 정보 수정 적용
    public ResponseEntity<?> updateCarwashDetails(@PathVariable("carwash_id") Long carwashId, @RequestBody CarwashRequest.updateCarwashDetailsDTO updatedto ) {
        CarwashResponse.updateCarwashDetailsResponseDTO updateCarwashDetailsDTO = carwashService.updateCarwashDetails(carwashId, updatedto);
        return ResponseEntity.ok(ApiUtils.success(updateCarwashDetailsDTO));

    }

}



