package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.optime.OptimeJPARepository;
import bdbe.bdbd.reservation.ReservationRequest.SaveDTO;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import bdbe.bdbd.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static bdbe.bdbd.optime.DayType.HOLIDAY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@Transactional
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//통합테스트(SF-F-DS(Handler, ExHandler)-C-S-R-PC-DB) 다 뜬다.
public class ReservationRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    KeywordJPARepository keywordJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    OptimeJPARepository optimeJPARepository;

    @Autowired
    private ObjectMapper om;

    private User user;

    @BeforeEach()
    public void setup() {
//        Location location = Location.builder()
//                .place("place")
//                .address("address")
//                .latitude(1.234)
//                .longitude(2.345)
//                .build();
//        Location savedLocation = locationJPARepository.save(location);

//        User user = User.builder()
//                .role(UserRole.ROLE_USER)
//                .email("user@nate.com")
//                .password("user1234!")
//                .username("useruser")
//                .build();
//        User savedUser = userJPARepository.findFirstBy();


//        Carwash savedCarwash = carwashJPARepository.findFirstBy();


        //Long id, int bayNum, int bayType, Carwash carwash, int status
//        Bay bay = Bay.builder()
//                .bayNum(1)
//                .carwash(savedCarwash)
//                .status(1)
//                .build();
//        bayJPARepository.save(bay);

//        Carwash carwash = carwashJPARepository.findFirstBy();

//        Optime optime = Optime.builder()
//                .dayType(HOLIDAY)
//                .startTime(LocalTime.of(6, 0)) // 오전 6시
//                .endTime(LocalTime.of(22, 0)) // 오후 10시
//                .build();
//        Optime savedOptime = optimeJPARepository.save(optime);
//        Carwash carwash = Carwash.builder()
//                .price(100)
//                .name("세차장")
//                .des("좋은 세차장입니다.")
//                .tel("010-2222-3333")
//                .optime(savedOptime)
//                .location(savedLocation)
//                .user(savedUser)
//                .build();
//        carwashJPARepository.save(carwash);
    }


    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("예약 기능")
    public void save_test() throws Exception {
        // given
        Carwash carwash = carwashJPARepository.findFirstBy();
        Long carwashId = carwash.getId();
        System.out.println("carwashId : "+ carwashId);
        Long bayId = bayJPARepository.findFirstBy().getId();
        System.out.println("bayId : " + bayId);

        Bay bay = bayJPARepository.findFirstBy();
        if(bay != null) {
            bayId = bay.getId();
            System.out.println("bayId : " + bayId);
        } else {
            System.out.println("No Bay entity found");
             throw new EntityNotFoundException("No Bay entity found");
        }
//        // dto 생성
        SaveDTO saveDTO = new SaveDTO();
        // SaveDTO 객체 생성 및 값 설정
        saveDTO.setBayId(bayId);
        LocalDate date = LocalDate.now();
        saveDTO.setStartTime(LocalDateTime.of(date, LocalTime.of(6, 0))); // 오전 6시
        saveDTO.setEndTime(LocalDateTime.of(date, LocalTime.of(22, 00))); // 오후 10시
//
        String requestBody = om.writeValueAsString(saveDTO);
        System.out.println("요청 데이터 : " + requestBody);
//         when
//        /carwashes/{carwash_id}/bays/{bay_id}/reservations
        ResultActions resultActions = mvc.perform(
                post(String.format("/carwashes/%d/bays/%d/reservations", carwashId, bayId))
                        .content(om.writeValueAsString(saveDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
//
//        // eye(1)
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));


    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("세차장별 예약 조회 내역 기능")
    public void findAllByCarwash_test() throws Exception {
        //given
        Carwash carwash = carwashJPARepository.findFirstBy();
        System.out.println("carwashId : " + carwash.getId());
        Bay bay = bayJPARepository.findFirstBy();
        System.out.println("bayId : " + bay.getId());

        User user = userJPARepository.findByEmail("user@nate.com").orElseThrow(()->new IllegalArgumentException("user not found"));
        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30)) //30분 뒤로 설정
                .bay(bay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);
        // 예약 2
        Reservation reservation2 = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(60)) //60분 뒤로 설정
                .bay(bay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation2);

        //when
        ResultActions resultActions = mvc.perform(
                get(String.format("/carwashes/%d/bays", carwash.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);


    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("결제 후 예약 내역 조회")
    public void fetchLatestReservation_test() throws Exception {
        //given
        User user = userJPARepository.findFirstBy();
        Bay savedBay = bayJPARepository.findFirstBy();

        // 예약 1
        Reservation reservation = Reservation.builder()
                .price(5000)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30)) //60분 뒤로 설정
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);

        //when
        ResultActions resultActions = mvc.perform(
                get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }

//    /reservations/current-status
    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("현재 시간 기준 예약 내역 조회")
    public void fetchCurrentStatusReservation_test() throws Exception {
        //given
        Location location = Location.builder().build();
        Location savedLocation = locationJPARepository.save(location);

        User user = userJPARepository.findByEmail("user@nate.com")
                .orElseThrow(() -> new IllegalArgumentException("user not found"));


        Carwash carwash = Carwash.builder()
                .price(100)
                .name("세차장")
                .des("좋은 세차장입니다.")
                .tel("010-2222-3333")
                .location(savedLocation)
                .user(user)
                .build();
        Carwash savedCarwash = carwashJPARepository.save(carwash);

        Bay bay = Bay.builder()
                .bayNum(10)
                .carwash(savedCarwash)
                .status(1)
                .build();
        Bay savedBay = bayJPARepository.save(bay);
        // 보여주기
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        System.out.println("today = " + today);
        System.out.println("now : " + now.toString());

//         예약 1
        Reservation reservation = Reservation.builder()
                .id(20L)
                .price(5000)
                .startTime(LocalDateTime.from(LocalTime.of(10, 0))) // 10:00 AM
                .endTime(LocalDateTime.from(LocalTime.of(11, 0)))  // 11:00 AM
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation);

//        // 예약 2
        Reservation reservation2 = Reservation.builder()
                .price(4500)
                .startTime(LocalDateTime.from(LocalTime.of(14, 0))) // 10:00 AM
                .endTime(LocalDateTime.from(LocalTime.of(16, 0)))  // 11:00 AM
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation2);
                // 예약 3
        Reservation reservation3 = Reservation.builder()
                .price(4500)
                .startTime(LocalDateTime.from(LocalTime.of(20, 0))) // 10:00 AM
                .endTime(LocalDateTime.from(LocalTime.of(22, 0)))  // 11:00 AM
                .bay(savedBay)
                .user(user)
                .build();
        reservationJPARepository.save(reservation3);
//
        //when
        ResultActions resultActions = mvc.perform(
                get("/reservations/current-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

    }



}
