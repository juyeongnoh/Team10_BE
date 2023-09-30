package bdbe.bdbd.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;

    @Autowired
    public ReviewService(ReviewJPARepository reviewJPARepository) {
        this.reviewJPARepository = reviewJPARepository;
    }


    public Review createReview(Review review) {
        return reviewJPARepository.save(review);
    }


    public Review getReviewById(int id) {
        return (Review) reviewJPARepository.findById(id).orElse(null);
    }
}


