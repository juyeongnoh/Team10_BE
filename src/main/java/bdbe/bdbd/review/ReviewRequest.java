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

        @NotEmpty
        private int id;

        @NotEmpty
        private int u_id;

        @NotEmpty
        private int w_id;

        @NotEmpty
        private int id2; //예약 아이디

        @NotEmpty
        @Size(min = 0, max = 30, message = " 30글자 제한")
        private String singlecomment;

        @NotEmpty
        private Integer rate;

        @NotEmpty
        private Integer keyword;

    }
}
