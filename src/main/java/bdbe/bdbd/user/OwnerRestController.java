package bdbe.bdbd.user;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import bdbe.bdbd.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/owner")
public class OwnerRestController {
    private final OwnerService ownerService;
    // (기능3) 이메일 중복체크
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody @Valid UserRequest.EmailCheckDTO emailCheckDTO, Errors errors) {
        ownerService.sameCheckEmail(emailCheckDTO.getEmail());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

   //  (기능4) 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> joinOwner(@RequestBody @Valid UserRequest.JoinDTO requestDTO, Errors errors) {
        requestDTO.setRole(UserRole.ROLE_OWNER);
        ownerService.join(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // (기능5) 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            throw new BadRequestError(errorMessage);
        }
        UserResponse.LoginResponse response = ownerService.login(requestDTO);
        return ResponseEntity.ok().header(JWTProvider.HEADER, response.getJwtToken()).body(ApiUtils.success(response));
    }
    // 로그아웃 사용안함 - 프론트에서 JWT 토큰을 브라우저의 localstorage에서 삭제하면 됨.

    @GetMapping("/sales")
    public ResponseEntity<?> findAllOwnerReservation(
            @RequestParam("carwash-id") List<Long> carwashIds,
            @RequestParam("selected-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ownerService.findSales(carwashIds, selectedDate, userDetails.getUser());
        OwnerResponse.SaleResponseDTO saleResponseDTO = ownerService.findSales(carwashIds, selectedDate, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(saleResponseDTO));
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> findMonthRevenueByCarwash(
            @RequestParam("carwash-id") List<Long> carwashIds,
            @RequestParam("selected-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        Map<String, Long> map = ownerService.findMonthRevenue(carwashIds, selectedDate, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(map));
    }

    @GetMapping("/carwashes")
    public ResponseEntity<?> fetchOwnerReservationOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.ReservationOverviewResponseDTO dto = ownerService.fetchOwnerReservationOverview(userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/carwashes/{carwash_id}")
    public ResponseEntity<?> fetchCarwashReservationOverview(
            @PathVariable("carwash_id") Long carwashId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.CarwashManageDTO dto = ownerService.fetchCarwashReservationOverview(carwashId, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }

    @GetMapping("/home")
    public ResponseEntity<?> fetchOwnerHomepage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        OwnerResponse.OwnerDashboardDTO dto = ownerService.fetchOwnerHomepage(userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(dto));
    }
}

