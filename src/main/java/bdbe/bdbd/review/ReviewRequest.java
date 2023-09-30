package bdbe.bdbd.review;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.websocket.OnMessage;

public class ReviewRequest {

    @Getter
    @Setter
    public static class CreateReviewDTO {


        private int id;

        private int u_id;


        private int w_id;


        private int id2; //예약 아이디

        @NotEmpty
        @Size(min = 0, max = 30, message = " 30글자 제한")
        private String singlecomment;


        private Integer rate;


        private Integer keyword;

    }
}
