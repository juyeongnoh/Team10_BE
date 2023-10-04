package bdbe.bdbd.reservation;

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
public class ReservationRestController {

    private final ReservationService reservationService;

    // 예약하기
    @GetMapping("/carwashes/{carwash_id}/bays/{bay_id}/reservations")
    public ResponseEntity<?> save(
            @PathVariable("carwash_id") Long carwashId,
            @PathVariable("bay_id") Long bayId,
            @RequestBody ReservationRequest.SaveDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
            )
    {
        reservationService.save(dto, carwashId, bayId, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));

    }

}