package bdbe.bdbd.member;

import bdbe.bdbd._core.errors.security.JWTProvider;
import bdbe.bdbd.carwash.CarwashJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OwnerRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MemberJPARepository memberJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    private ObjectMapper om;


    @BeforeEach
    public void setup() {
        MemberRequest.JoinDTO mockOwnerDTO = new MemberRequest.JoinDTO();
        mockOwnerDTO.setUsername("aaamockowner");
        mockOwnerDTO.setEmail("aaamockowner@naver.com");
        mockOwnerDTO.setPassword("asdf1234!");
        mockOwnerDTO.setRole(MemberRole.ROLE_OWNER);
        mockOwnerDTO.setTel("010-1234-5678");

        Member mockOwner = mockOwnerDTO.toEntity(passwordEncoder.encode(mockOwnerDTO.getPassword()));

        memberJPARepository.save(mockOwner);

        MemberRequest.JoinDTO mockUserDTO = new MemberRequest.JoinDTO();
        mockUserDTO.setUsername("aaauserRoleUser");
        mockUserDTO.setEmail("aaauserRoleUser@naver.com");
        mockUserDTO.setPassword("aaaa1111!");
        mockUserDTO.setRole(MemberRole.ROLE_USER);
        mockUserDTO.setTel("010-1234-5678");

        Member mockUserWithMemberRole = mockUserDTO.toEntity(passwordEncoder.encode(mockUserDTO.getPassword()));

        memberJPARepository.save(mockUserWithMemberRole);
    }



    @Test
    public void checkTest() throws Exception {
        //given
        MemberRequest.EmailCheckDTO requestDTO = new MemberRequest.EmailCheckDTO();
        requestDTO.setEmail("bdbd@naver.com");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/api/owner/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void joinTest() throws Exception {
        MemberRequest.JoinDTO requestDTO = new MemberRequest.JoinDTO();
        requestDTO.setUsername("aaamockowner");
        requestDTO.setEmail("aaamockowner@nate.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole(MemberRole.ROLE_OWNER);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/owner/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail("aaamockowner@naver.com");
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                    post("/api/owner/login")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists(JWTProvider.HEADER))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.response.redirectUrl").value("/owner/home"))
                .andDo(print());
    }
    //jwt.io 에서 ROLE_OWNER정상반환 확인함 및 리다이렉트 확인


    @Test
    public void loginAsUserOnOwnerPageTest() throws Exception {
        MemberRequest.LoginDTO requestDTO = new MemberRequest.LoginDTO();
        requestDTO.setEmail("aaauserRoleUser@naver.com");
        requestDTO.setPassword("aaaa1111!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/api/owner/login")  // owner 페이지에서의 로그인 URL을 사용
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.error.message").value("can't access this page"))
                .andDo(print());
    }

    @WithUserDetails(value = "owner@nate.com")
    @Test
    @DisplayName("매출관리")
    public void findAllReservationByOwner_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/owner/sales")
                        .param("carwash-id",   "2")
                        .param("selected-date", "2023-10-01")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails(value = "owner@nate.com")
    @Test
    @DisplayName("세차장들의 한달 매출 조회 기능")
    public void findRevenueByOwner_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/owner/revenue")
                        .param("carwash-id",  "2")
                        .param("selected-date", "2023-10-01")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails(value = "owner@nate.com")
    @Test
    @DisplayName("매장 관리 - owner별")
    public void fetchOwnerReservationOverview_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/owner/carwashes"));
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails(value = "owner@nate.com")
    @Test
    @DisplayName("매장 관리 - 세차장별")
    public void fetchCarwashReservationOverview_test() throws Exception {
        //given
        Long carwashId = carwashJPARepository.findFirstBy().getId();
        System.out.println("carwash id :" + carwashId);
        //when
        ResultActions resultActions = mvc.perform(
                get(String.format("/api/owner/carwashes/%d", carwashId)));
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @WithUserDetails(value = "owner@nate.com")
    @Test
    @DisplayName("세차장 홈")
    public void OwnerHome_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(
                get("/api/owner/home"));
        //then
        // eye
        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 Body : " + responseBody);
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }
}


