package bdbe.bdbd.user;

import bdbe.bdbd._core.errors.security.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserJPARepository userJPARepository;

    @BeforeEach
    public void setup() {
        UserRequest.JoinDTO mockUserDTO = new UserRequest.JoinDTO();
        mockUserDTO.setUsername("mockuser");
        mockUserDTO.setEmail("mock@naver.com");
        mockUserDTO.setPassword("asdf1234!");
        mockUserDTO.setRole(UserRole.ROLE_USER);
        mockUserDTO.setTel("010-1234-5678");

        User mockUser = mockUserDTO.toEntity(passwordEncoder.encode(mockUserDTO.getPassword()));

        userJPARepository.save(mockUser);
    }


    @Autowired
    private ObjectMapper om;


    @Test
    public void checkTest() throws Exception {
        //given
        UserRequest.EmailCheckDTO requestDTO = new UserRequest.EmailCheckDTO();
        requestDTO.setEmail("bdbd@naver.com");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/user/check")
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
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail("newuser@naver.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole(UserRole.ROLE_USER);
//        requestDTO.setCredit(0);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("mock@naver.com");
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists(JWTProvider.HEADER))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.response.redirectUrl").value("/user/home"))
                .andDo(print());
    }
    //jwt.io 에서 ROLE_USER정상반환 확인함 및 user/home으로 리다이렉트


    @Test
    public void sameEmailTest() throws Exception {

        String email = "mock@naver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole(UserRole.ROLE_USER);
//        requestDTO.setCredit(0);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("duplicate email exist : " + email))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinEmailExceptionTest() throws Exception {

        String email = "mocknaver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole(UserRole.ROLE_USER);
//        requestDTO.setCredit(0);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("이메일 형식으로 작성해주세요:email"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void joinPasswordExceptionTest() throws Exception {

        String email = "mock@naver.com";
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("imnewuser");
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234");
        requestDTO.setRole(UserRole.ROLE_USER);
//        requestDTO.setCredit(0);
        requestDTO.setTel("010-1234-5678");


        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/join")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    @Test
    public void loginWrongEmailTest() throws Exception {
        String email = "aaaa@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("asdf1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("email not found : "+requestDTO.getEmail()))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    //규칙에 맞지 않는 패스워드
    @Test
    public void loginWrongPasswordTest() throws Exception {
        String email = "mock@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaaaaaa!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.:password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }

    //이메일은 맞는데 잘못 입력된 패스워드
    @Test
    public void loginNotMatchPasswordTest() throws Exception {
        String email = "mock@naver.com";
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail(email);
        requestDTO.setPassword("aaaa1234!");

        String requestBody = om.writeValueAsString(requestDTO);

        mvc.perform(
                        post("/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error.message").value("wrong password"))
                .andExpect(jsonPath("$.error.status").value(400))
                .andDo(print());
    }


}

