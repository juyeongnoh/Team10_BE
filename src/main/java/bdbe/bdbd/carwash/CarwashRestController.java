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
    @PostMapping("/carwashes/register")
    public ResponseEntity<?> save(@RequestBody @Valid CarwashRequest.SaveDTO saveDTOs, Errors errors,  @AuthenticationPrincipal CustomUserDetails userDetails) {
        carwashService.save(saveDTOs, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}