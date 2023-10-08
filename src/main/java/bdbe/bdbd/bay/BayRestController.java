package bdbe.bdbd.bay;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class BayRestController {

    private final BayService bayService;

    @PostMapping("/carwashes/{carwash_id}/bays")  //베이 추가
    public ResponseEntity<?> createBay(
            @RequestBody @Valid BayRequest.SaveDTO saveDTO,
            Errors errors,
            @PathVariable("carwash_id") Long carwashId) {
        bayService.createBay(saveDTO, carwashId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @DeleteMapping("/bays/{bay_id}") //베이 삭제
    public ResponseEntity<?> deleteBay(@PathVariable("bay_id") Long bayId){
        bayService.deleteBay(bayId);
        return ResponseEntity.ok(ApiUtils.success(null));

    }

    @PostMapping("/bays/{bays_id}/status") //베이 활성화/비활성화
    public ResponseEntity<?> statusBay(
            @PathVariable("bay_id") Long bayId,
            @RequestParam int status) {
        bayService.changeStatus(bayId, status);
        return ResponseEntity.ok(ApiUtils.success(null));
    }


}