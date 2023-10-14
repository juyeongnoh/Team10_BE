package bdbe.bdbd.bay;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class BayRestController {

    private final BayService bayService;

    @PostMapping("/owner/carwashes/{carwash_id}/bays")
    public ResponseEntity<?> createBay(
            @PathVariable("carwash_id") Long carwashId,
            @RequestBody BayRequest.SaveDTO saveDTO, Errors errors)
    {
        bayService.createBay(saveDTO, carwashId);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

//    @DeleteMapping("/owner/bays/{bay_id}") //베이 삭제
//    public ResponseEntity<?> deleteBay(@PathVariable("bay_id") Long bayId){
//        bayService.deleteBay(bayId);
//        return ResponseEntity.ok(ApiUtils.success(null));
//    }

    @PutMapping("/owner/bays/{bay_id}/status") //베이 활성화/비활성화
    public ResponseEntity<?> updateStatus(
            @PathVariable("bay_id") Long bayId,
            @RequestParam int status) {
        bayService.changeStatus(bayId, status);
        return ResponseEntity.ok(ApiUtils.success(null));
    }


}