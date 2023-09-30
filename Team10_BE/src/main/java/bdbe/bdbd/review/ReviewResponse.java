package bdbe.bdbd.review;

import lombok.Getter;
import lombok.Setter;

public class ReviewResponse {

    @Getter
    @Setter
    public static class getReviewById{
        private int id;
        private int u_id;
        private int w_id;
        private int id2;

        private String singlecomment;
        private Integer rate;
        private Integer keyword;

        public getReviewById(Review review) {
            this.id = review.getId();
            this.u_id = review.getId();
            this.w_id = review.getId();
            this.id2 = review.getId();
            this.singlecomment = review.getSinglecomment();
            this.rate = review.getRate();
            this.keyword = review.getKeyword();

        }
    }
}


