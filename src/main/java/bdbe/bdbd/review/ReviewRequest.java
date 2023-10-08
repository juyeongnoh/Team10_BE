package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class ReviewRequest {
    @Getter
    @Setter
    @ToString
    public static class SaveDTO {
        private Long carwashId;
        private List<Long> rKeywordIdList;
        private int rate;
        private String comment;

        public Review toReviewEntity(User user, Carwash carwash) {
            return Review.builder()
                    .user(user)
                    .carwash(carwash)
                    .comment(comment)
                    .rate(rate)
                    .build();
        }

    }
}
