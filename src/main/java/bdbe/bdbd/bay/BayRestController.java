package bdbe.bdbd.bay;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import bdbe.bdbd.carwash.CarwashRequest;
import bdbe.bdbd.carwash.CarwashResponse;
import bdbe.bdbd.carwash.CarwashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class BayRestController {

    private final BayService bayService;

    @PostMapping("/carwashes/{carwash_id}/bays")  //베이 추가
    public ResponseEntity<?> createBay(@RequestBody @Valid BayRequest.SaveDTO saveDTO, Errors errors) {
        bayService.createBay(saveDTO);
        return ResponseEntity.ok(ApiUtils.success(null));

    }

    @DeleteMapping("/carwashes/{carwash_id}/bays/{bay_id}") //베이 삭제
    public ResponseEntity<?> deleteBay(@RequestBody @Valid BayRequest.SaveDTO saveDTO, Errors errors){
        bayService.deleteBay(saveDTO);
        return ResponseEntity.ok(ApiUtils.success(null));

    }

    @PostMapping("/carwashes/{carsh_id}/bays/{bays_id}/status") //베이 활성화
    public ResponseEntity<?> statusBay(@RequestBody @Valid BayRequest.SaveDTO saveDTO, Errors errors ) {
        bayService.statusBay(saveDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }
    // 전체 세차장 목록 조회, 10개씩 페이징
//    @GetMapping("/carwashes")
//    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page) {
//        List<CarwashResponse.FindAllDTO> dtos = carwashService.findAll(page);
//        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(dtos);
//        return ResponseEntity.ok(apiResult);
//    }

    //세차장 등록
//    @PostMapping("/carwashes/register")
//    public ResponseEntity<?> save(@RequestBody @Valid CarwashRequest.SaveDTO saveDTOs, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
//        carwashService.save(saveDTOs, userDetails.getUser());
//        return ResponseEntity.ok(ApiUtils.success(null));
//    }

}