package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
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
        private Long reservationId;
        private List<Long> keywordList;
        private double rate;
        private String comment;


        public Review toReviewEntity(User user, Carwash carwash, Reservation reservation) {
            return Review.builder()
                    .user(user)
                    .carwash(carwash)
                    .reservation(reservation)
                    .comment(comment)
                    .rate(rate)
                    .build();
        }
    }
}
