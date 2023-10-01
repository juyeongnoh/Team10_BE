package bdbe.bdbd.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

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

    /*@Autowired
    CarwashJPARepository carwashJPARepository;*/

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setup() {
        // 적절한 테스트 데이터를 준비하세요.
        // 예를 들어, Carwash 엔티티를 생성하고 저장할 수 있습니다.
    }

    @Test
    @DisplayName("리뷰 추가 및 평균 점수 업데이트")
    public void addReviewAndUpdateAverageScore_test() throws Exception {
        // given
        Long carwashId = 1L; // Carwash ID
        Review review = new Review();
        review.setRate((int) 5.0);
        review.setSinglecomment("좋은세차!");

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
