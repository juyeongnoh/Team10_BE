package bdbe.bdbd.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserJPARepository userJPARepository;

    @Autowired
    private ObjectMapper om;

    @Test
    public void check_test() throws Exception {
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
    public void login_test() throws Exception {
        //given
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("bdbd@naver.com");
        requestDTO.setPassword("asdf1234!");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/user/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andDo(print()); //
    }

    @Test
    public void join_test() throws Exception {
        //given
        UserRequest.JoinDTO requestDTO = new UserRequest.JoinDTO();
        requestDTO.setUsername("tempuser");
        requestDTO.setEmail("bdbd@naver.com");
        requestDTO.setPassword("asdf1234!");
        requestDTO.setRole("USER");
        String requestBody = om.writeValueAsString(requestDTO);
        //when
        ResultActions resultActions = mvc.perform(
                post("/user/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.role").value("USER")) // role
                .andDo(print());
    }
}
