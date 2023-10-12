package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.location.LocationJPARepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.xml.transform.Result;
import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BayRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    LocationJPARepository locationJPARepository;

    @Autowired
    BayJPARepository bayJPARepository;

    @Autowired
    CarwashJPARepository carwashJPARepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("베이 추가")
    public void createBayTest() throws Exception{

        Long carwashId = carwashJPARepository.findFirstBy().getId();
        System.out.println("carwashId:" + carwashId);
        if(carwashId==null) throw new IllegalArgumentException("not found carwash");

        BayRequest.SaveDTO saveDTO = new BayRequest.SaveDTO();
        saveDTO.setBayNum(saveDTO.getBayNum());

        String requestBody = om.writeValueAsString(carwashId);
        System.out.println("요청 데이터:" + requestBody);

        ResultActions resultActions = mvc.perform(
                post("/owner/carwashes/%d/bays", carwashId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_ATOM_XML_VALUE)
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("응답 body:" + responseBody);

        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    @DisplayName("베이 삭제")
    public void deleteBayTest() throws Exception {

        Long carwashId = 1L;
        Long bayId = 1L;

        mockMvc.perform(delete("owner/carwashes/%d/bays/%d", carwashId, bayId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"));
    }

}
