package bdbe.bdbd.user;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import bdbe.bdbd._core.errors.exception.InternalServerError;
import bdbe.bdbd._core.errors.exception.UnAuthorizedError;
import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service

public class OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;

    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        try {
            userJPARepository.save(requestDTO.toEntity(encodedPassword));
        } catch (Exception e) {
            throw new InternalServerError("unknown server error");
        }
    }


    public UserResponse.LoginResponse login(UserRequest.LoginDTO requestDTO) {
        User userPS = userJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                () -> new BadRequestError("email not found : " + requestDTO.getEmail())
        );

        if (!passwordEncoder.matches(requestDTO.getPassword(), userPS.getPassword())) {
            throw new BadRequestError("wrong password");
        }

        // 여기서 사용자의 권한을 확인합니다.
        String userRole = String.valueOf(userPS.getRole());
        if (!"ROLE_OWNER".equals(userRole) && !"ROLE_ADMIN".equals(userRole)) {
            throw new UnAuthorizedError("can't access this page");
        }

        String jwt = JWTProvider.create(userPS);
        String redirectUrl = "/owner/home";

        return new UserResponse.LoginResponse(jwt, redirectUrl);
    }


    public void sameCheckEmail(String email) {
        Optional<User> userOP = userJPARepository.findByEmail(email);
        if (userOP.isPresent()) {
            throw new BadRequestError("duplicate email exist : " + email);
        }
    }
    public OwnerResponse.SaleResponseDTO findSales(List<Long> carwashIds, LocalDate selectedDate, User sessionUser) {
        // 해당 유저가 운영하는 세차장의 id인지 확인
        List<Carwash> carwashList = carwashJPARepository.findAllByIdInAndUser_Id(carwashIds, sessionUser.getId());
        if (carwashIds.size() != carwashList.size())
            throw new IllegalArgumentException("User is not the owner of the carwash. ");
        // 예약을 시간순으로(가장 최근 예약부터) 정렬
        // 세차장 id로 제시된 것들만 가져오기
        List<Reservation> reservationList = reservationJPARepository.findAllByCarwash_IdInOrderByStartTimeDesc(carwashIds, selectedDate)
                .stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());
        if (reservationList.isEmpty()) return new OwnerResponse.SaleResponseDTO(new ArrayList<>());
        // 예약과 예약에 대한 세차장 정보 보여주기
        return new OwnerResponse.SaleResponseDTO(reservationList);

    }

    public Map<String, Long> findMonthRevenue(List<Long> carwashIds, LocalDate selectedDate, User sessionUser) {
        // 해당 유저가 운영하는 세차장의 id인지 확인
        List<Carwash> carwashList = carwashJPARepository.findAllByIdInAndUser_Id(carwashIds, sessionUser.getId());
        if (carwashIds.size() != carwashList.size())
            throw new IllegalArgumentException("User is not the owner of the carwash.");
        // 매출 구하기
        Map<String, Long> response = new HashMap<>();
        Long revenue = reservationJPARepository.findTotalRevenueByCarwashIdsAndDate(carwashIds, selectedDate);

        response.put("revenue", revenue);

        return response;
    }


}
