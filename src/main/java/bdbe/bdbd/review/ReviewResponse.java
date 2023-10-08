package bdbe.bdbd.review;

import bdbe.bdbd.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;

public class ReviewResponse {

    @Getter
    @Setter
    public static class getReviewById{
        private Long id;
        private Long uId;
        private Long cId;
        private Reservation reservation; //1:1, 참조용(read-only)
        private String comment;
        private double rate;

        public getReviewById(Review review) {
            this.id = review.getId();
            this.uId = review.getUser().getId();
            this.cId = review.getCarwash().getId();
            this.reservation = review.getReservation();
            this.comment = review.getComment();
            this.rate = review.getRate();
        }

    }
}


