package bdbe.bdbd.carwash;

import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.location.LocationJPARepository;
import bdbe.bdbd.user.User;
import bdbe.bdbd.user.UserJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test") //test profile 사용
@Transactional
@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//통합테스트(SF-F-DS(Handler, ExHandler)-C-S-R-PC-DB) 다 뜬다.
public class CarwashRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    KeywordJPARepository keywordJPARepository;

    @Autowired
    private ObjectMapper om;

    private User user;


    @Test
    @DisplayName("전체 세차장 목록 조회")
    public void findAll_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/carwashes")
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("세차장 등록 기능")
    public void save_test() throws Exception {
        // given
        // dto 생성
        CarwashRequest.SaveDTO dto = new CarwashRequest.SaveDTO();
        dto.setName("test 세차장");

        dto.setTel("01012345678");
        dto.setDescription("테스트 설명");
        dto.setPrice("100");

        CarwashRequest.LocationDTO locationDTO = new CarwashRequest.LocationDTO();
        locationDTO.setPlaceName("test 장소");
        locationDTO.setAddress("test 주소");
        locationDTO.setLatitude(1.234);
        locationDTO.setLongitude(5.678);
        dto.setLocation(locationDTO);

        CarwashRequest.OperatingTimeDTO optimeDTO = new CarwashRequest.OperatingTimeDTO();
        CarwashRequest.OperatingTimeDTO.TimeSlot weekdaySlot = new CarwashRequest.OperatingTimeDTO.TimeSlot();
        weekdaySlot.setStart(LocalTime.of(9, 0));
        weekdaySlot.setEnd(LocalTime.of(17, 0));
        optimeDTO.setWeekday(weekdaySlot);

        CarwashRequest.OperatingTimeDTO.TimeSlot weekendSlot = new CarwashRequest.OperatingTimeDTO.TimeSlot();
        weekendSlot.setStart(LocalTime.of(10, 0));
        weekendSlot.setEnd(LocalTime.of(16, 0));
        optimeDTO.setWeekend(weekendSlot);
        dto.setOptime(optimeDTO);

//        dto.setImage(Arrays.asList("image1.jpg", "image2.jpg"));
        Keyword keyword = Keyword.builder()
                .name("하부세차")
                .build();
        Keyword savedKeyword = keywordJPARepository.save(keyword);
        dto.setKeywordId(Arrays.asList(savedKeyword.getId()));


        String requestBody = om.writeValueAsString(dto);
        System.out.println("요청 데이터 : " + requestBody);
        // when
        ResultActions resultActions = mvc.perform(
                post("/owner/carwashes/register")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
//        System.out.println("응답 Body : " + responseBody);
//
//        // verify
//        resultActions.andExpect(jsonPath("$.success").value("true"));

    }


    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("주변 세차장 검색")
    public void findNearbyCarwashes_test() throws Exception {
        // given
        double testLatitude = 1.23;
        double testLongitude = 2.34;

        // when
        ResultActions resultActions = mvc.perform(
                get("/carwashes/nearby")
                        .param("latitude", String.valueOf(testLatitude))
                        .param("longitude", String.valueOf(testLongitude))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$").isArray());
    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("가장 가까운 세차장 검색(추천세차장)")
    public void findNearestCarwash_test() throws Exception {
        // given
        double testLatitude = 1.23;
        double testLongitude = 2.34;

        // when
        ResultActions resultActions = mvc.perform(
                get("/carwashes/recommended")
                        .param("latitude", String.valueOf(testLatitude))
                        .param("longitude", String.valueOf(testLongitude))
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$").isMap());
    }

    @WithUserDetails(value = "user@nate.com")
    @Test
    @DisplayName("키워드로 세차장 검색")
    public void findCarwashesByKeywords_test() throws Exception {
        // given
        CarwashRequest.SearchRequestDTO searchRequest = new CarwashRequest.SearchRequestDTO();
        searchRequest.setKeywords(Arrays.asList("하부세차"));
        searchRequest.setLatitude(1.23);
        searchRequest.setLongitude(2.34);

        String requestBody = om.writeValueAsString(searchRequest);

        // when
        ResultActions resultActions = mvc.perform(
                post("/carwashes/search")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("세차장 상세 정보 조회 기능")
    public void findByIdTest() throws Exception {

        Long carwashId = carwashJPARepository.findFirstBy().getId();
        System.out.println("carwashId:" + carwashId);

        ResultActions resultActions = mvc.perform(
                get(String.format("/carwashes/%d/introduction", carwashId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body:" + responseBody);

        resultActions.andExpect(jsonPath("$.success").value("true"));
    }



}


