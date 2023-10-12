package bdbe.bdbd.user;

import bdbe.bdbd._core.errors.security.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
    UserJPARepository userJPARepository;

    private User MockUser;

    private User MockUserWithUserRole;


    @BeforeEach
    public void setup() {
        MockUser = new User();
        MockUser.setUsername("mockowner");
        MockUser.setEmail("mockowner@naver.com");
        MockUser.setPassword(passwordEncoder.encode("asdf1234!"));
        MockUser.setRole(UserRole.ROLE_OWNER);
        MockUser.setTel("010-1234-5678");

        userJPARepository.save(MockUser);

        MockUserWithUserRole = new User();
        MockUserWithUserRole.setUsername("userRoleUser");
        MockUserWithUserRole.setEmail("userRoleUser@naver.com");
        MockUserWithUserRole.setPassword(passwordEncoder.encode("aaaa1111!"));
        MockUserWithUserRole.setRole(UserRole.ROLE_USER);
        MockUserWithUserRole.setTel("010-1234-5678");

        userJPARepository.save(MockUserWithUserRole);
        //권한 검사를 위한 userrole객체
    }

    @AfterEach
    public void cleanup() {
        userJPARepository.delete(MockUser);
        userJPARepository.delete(MockUserWithUserRole);
    }



    @Autowired
    private ObjectMapper om;

    @Test
    public void checkTest() throws Exception {
        //given
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("newowner@naver.com");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/owner/check")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void joinTest() throws Exception {
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewowner");
        requestDTO.setEmail("newowner@naver.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole(UserRole.ROLE_OWNER);
//        requestDTO.setCredit(0);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/owner/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("mockowner@naver.com");
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/owner/login")
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
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("userRoleUser@naver.com");
        requestDTO.setPassword("aaaa1111!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/owner/login")  // owner 페이지에서의 로그인 URL을 사용
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.error.message").value("can't access this page"))
                .andDo(print());
    }
}


