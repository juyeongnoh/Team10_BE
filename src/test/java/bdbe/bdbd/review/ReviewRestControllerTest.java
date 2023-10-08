package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.region.RegionJPARepository;
import bdbe.bdbd.rkeyword.Rkeyword;
import bdbe.bdbd.rkeyword.RkeywordJPARepository;
import bdbe.bdbd.rkeyword.reviewKeyword.ReviewKeyword;
import bdbe.bdbd.rkeyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ReviewJPARepository reviewJPARepository;

    @Autowired
    RegionJPARepository regionJPARepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    RkeywordJPARepository rkeywordJPARepository;

    @Autowired
    ReviewKeywordJPARepository reviewKeywordJPARepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setup() {

    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("리뷰 등록 기능")
    public void createReviewTest() throws Exception {
        // given
        Region region = Region.builder().build();
        Region savedRegion = regionJPARepository.save(region);

        User user = User.builder()
                //.region(savedRegion)
                .role("USER")
                .email("hi@nate.com")
                .password("user1234!")
                .username("useruser")
                .build();
        User savedUser = userJPARepository.save(user);

        Carwash carwash = Carwash.builder()
                .name("세차장")
                .des("좋은 세차장입니다.")
                .tel("010-2222-3333")
                .region(savedRegion)
                .user(savedUser)
                .build();
        Carwash savedCarwash = carwashJPARepository.save(carwash);
        // 키워드
        List<Rkeyword> keywordList = new ArrayList<>();
        Rkeyword keyword = Rkeyword.builder().keywordName("하부세차").build();
        keywordList.add(keyword);
        Rkeyword keyword2 = Rkeyword.builder().keywordName("야간 조명").build();
        keywordList.add(keyword2);
        List<Rkeyword> savedKeywordList = rkeywordJPARepository.saveAll(keywordList);
        List<Long> keywordIds = savedKeywordList.stream()
                .map(Rkeyword::getId)
                .collect(Collectors.toList());


        // dto 생성
        ReviewRequest.SaveDTO dto = new ReviewRequest.SaveDTO();
        dto.setCarwashId(savedCarwash.getId());
        dto.setRKeywordIdList(keywordIds);
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
        System.out.println("응답 Body : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
        //DB 저장 확인
        List<Review> reviewList = reviewJPARepository.findAll();
        for (Review review : reviewList) {
            System.out.println(review.getComment());
            System.out.println(review.getCreatedAt());
        }
        //키워드 매핑 확인
        System.out.println("keyword 매핑 확인");
        List<ReviewKeyword> reviewKeywordList = reviewKeywordJPARepository.findAll();
        for (ReviewKeyword reviewKeyword : reviewKeywordList) {
            System.out.println(reviewKeyword.getId());
            System.out.print(" " + reviewKeyword.getReview().getId());
            System.out.println("-" +reviewKeyword.getKeyword().getId());
        }
        System.out.println("keyword ID");
        for (Long keywordId : keywordIds) {
            System.out.println(keywordId);
        }

    }

    @Test
    @DisplayName("리뷰 추가 및 평균 점수 업데이트")
    public void addReviewAndUpdateAverageScore_test() throws Exception {
        // given
        Long carwashId = 1L; // Carwash ID
        Review review = new Review();
        review.setRate((int) 5.0);
        review.setComment("좋은세차!");

        // when
        ResultActions resultActions = mvc.perform(
                post("/carwashes/" + carwashId + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(review))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.data.rate").value(5.0));
        resultActions.andExpect(jsonPath("$.data.comment").value("좋은세차!"));



       /* Carwash updatedCarwash = carwashJPARepository.findById(carwashId).orElseThrow();
        assertEquals(5.0, updatedCarwash.getAverageScore());*/
    }
}
