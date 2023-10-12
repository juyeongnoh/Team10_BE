package bdbe.bdbd.review;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.bay.BayJPARepository;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import bdbe.bdbd.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ReviewJPARepository reviewJPARepository;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    KeywordJPARepository keywordJPARepository;

    @Autowired
    ReviewKeywordJPARepository reviewKeywordJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    ReservationJPARepository reservationJPARepository;

    @Autowired
    private ObjectMapper om;

    Long carwashId;

    @Test
    public void makeMember() throws Exception{
        User user = User.builder()
                .role(UserRole.ROLE_USER)
                .email("user@nate.com")
                .password("user1234!")
                .username("useruser")
                .tel("010-2222-3333")
                .build();
        User savedUser = userJPARepository.save(user);
    }


    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("리뷰 등록 기능")
    public void createReviewTest() throws Exception {
        // given
        Location location = Location.builder().address("address").latitude(10).longitude(20).placeName("예쁨").build();
        Location savedLocation = locationJPARepository.save(location);

        User user = User.builder()
                .role(UserRole.ROLE_USER)
                .email("hi89@nate.com")
                .password("user1234!")
                .username("useruser")
                .tel("010-2222-3333")
                .build();
        User savedUser = userJPARepository.save(user);

        Carwash carwash = Carwash.builder()
                .name("세차장")
                .des("좋은 세차장입니다.")
                .tel("010-2222-3333")
                .location(savedLocation)
                .user(savedUser)
                .build();
        Carwash savedCarwash = carwashJPARepository.save(carwash);
        carwashId = savedCarwash.getId();
        // 키워드
        List<Keyword> keywordList = new ArrayList<>();
        Keyword keyword = Keyword.builder().keywordName("하부세차").build();
        keywordList.add(keyword);
        Keyword keyword2 = Keyword.builder().keywordName("야간 조명").build();
        keywordList.add(keyword2);
        List<Keyword> savedKeywordList = keywordJPARepository.saveAll(keywordList);
        List<Long> keywordIds = savedKeywordList.stream()
                .map(Keyword::getId)
                .collect(Collectors.toList());
        System.out.println("idList:");
        for (Long keywordId : keywordIds) {
            System.out.println("keywordId = " + keywordId);
        }

        Bay bay = Bay.builder()
                .bayNum(10)
                .carwash(savedCarwash)
                .status(1)
                .build();
        Bay savedBay = bayJPARepository.save(bay);

        Reservation reservation = Reservation.builder()
                .id(20L)
                .price(5000)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0)) // 10:00 AM
                .endTime(LocalTime.of(11, 0))  // 11:00 AM
                .bay(savedBay)
                .user(user)
                .build();
        Reservation savedReservation = reservationJPARepository.save(reservation);


        ReviewRequest.SaveDTO dto = new ReviewRequest.SaveDTO();
        dto.setCarwashId(savedCarwash.getId());
        dto.setKeywordList(keywordIds);
        dto.setReservationId(savedReservation.getId());
        dto.setRate(5);
        dto.setComment("좋네요");


        String requestBody = om.writeValueAsString(dto);
        System.out.println("요청 데이터 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(
                post("/reviews")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
//        resultActions.andExpect(jsonPath("$.success").value("true"));
//        //DB 저장 확인
//        List<Review> reviewList = reviewJPARepository.findAll();
//        for (Review review : reviewList) {
//            System.out.println(review.getComment());
//            System.out.println(review.getCreatedAt());
//        }
//        //키워드 매핑 확인
//        System.out.println("keyword 매핑 확인");
//        List<ReviewKeyword> reviewKeywordList = reviewKeywordJPARepository.findAll();
//        for (ReviewKeyword reviewKeyword : reviewKeywordList) {
//            System.out.println(reviewKeyword.getId());
//            System.out.print(" " + reviewKeyword.getReview().getId());
//            System.out.println("-" + reviewKeyword.getKeyword().getId());
//        }
//        System.out.println("keyword ID");
//        for (Long keywordId : keywordIds) {
//            System.out.println(keywordId);
//        }
//        //세차장 0점 -> 5점
//        Optional<Carwash> byId = carwashJPARepository.findById(savedCarwash.getId());
//        if (byId.isPresent()) {
//            Carwash carwash1 = byId.get();
//            assertThat(carwash1.getRate()).isEqualTo(5);
//        }

    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("리뷰 별점 확인 코드")
    public void checkRateTest() throws Exception {
        // given
        Location location = Location.builder().address("address").latitude(10).longitude(20).placeName("예쁨").build();
        Location savedLocation = locationJPARepository.save(location);

        User user = User.builder()
                .role(UserRole.ROLE_USER)
                .email("hi5@nate.com")
                .password("user1234!")
                .username("useruser")
                .build();
        User savedUser = userJPARepository.save(user);

        Carwash carwash = Carwash.builder()
                .name("세차장")
                .des("좋은 세차장입니다.")
                .tel("010-2222-3333")
                .location(savedLocation)
                .user(savedUser)
                .build();
        Carwash savedCarwash = carwashJPARepository.save(carwash);
        // 키워드
        List<Keyword> keywordList = new ArrayList<>();
        Keyword keyword = Keyword.builder().keywordName("하부세차").build();
        keywordList.add(keyword);
        Keyword keyword2 = Keyword.builder().keywordName("야간 조명").build();
        keywordList.add(keyword2);
        List<Keyword> savedKeywordList = keywordJPARepository.saveAll(keywordList);
        List<Long> keywordIds = savedKeywordList.stream()
                .map(Keyword::getId)
                .collect(Collectors.toList());

        Bay bay = Bay.builder()
                .bayNum(10)
                .carwash(savedCarwash)
                .status(1)
                .build();
        Bay savedBay = bayJPARepository.save(bay);

        Reservation reservation = Reservation.builder()
                .id(20L)
                .price(5000)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0)) // 10:00 AM
                .endTime(LocalTime.of(11, 0))  // 11:00 AM
                .bay(savedBay)
                .user(user)
                .build();
        Reservation savedReservation = reservationJPARepository.save(reservation);

        //dto 보냄
        ReviewRequest.SaveDTO dto = new ReviewRequest.SaveDTO();
        dto.setCarwashId(13L);
        dto.setKeywordList(keywordIds);
        dto.setReservationId(savedReservation.getId());
        dto.setRate(1);
        dto.setComment("좋네요");

        String requestBody = om.writeValueAsString(dto);
        System.out.println("요청 데이터 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(
                post("/reviews")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify

        Optional<Carwash> byId1 = carwashJPARepository.findById(13L);
        if (byId1.isPresent()) {
            Carwash carwash1 = byId1.get();
//            assertThat(carwash1.getRate()).isEqualTo(4);
        }
    }
//    /carwashes/{carwash_id}/reviews
    @WithUserDetails("user@nate.com")
    @Test
    @DisplayName("리뷰 조회 기능")
    public void find_review_test() throws Exception {
        // given
        this.createReviewTest();

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.get(String.format("/carwashes/%d/reviews", carwashId))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
    }
}
