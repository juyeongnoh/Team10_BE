package bdbe.bdbd.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;

    @Autowired
    public ReviewService(ReviewJPARepository reviewJPARepository) {
        this.reviewJPARepository = reviewJPARepository;
    }

    //@Autowired
    //private CarWashJPARepository CarWashJPARepository;

    public Review createReview(Review review) {
        return reviewJPARepository.save(review);
    }


    public Review getReviewById(int id) {
        return (Review) reviewJPARepository.findById(id).orElse(null);
    }

   /* public List<Review> getReviewsByCarWashId(Long carWashId) {
        List<Review> reviews = ReviewJPARepository.findByCarWashId(carWashId);
        return reviews.stream()
                .map(review -> new Review())
                .collect(Collectors.toList());
    }
}
*/ //개별 리뷰 저장



   /* public Review addReview(Review review) {
        // Review 저장
        Review savedReview = reviewJPARepository.save(review);

        // CarWash의 평균 점수 업데이트
        updateAverageScore(savedReview.getCarWash());

        return savedReview;
    }

    private void updateAverageScore(CarWash carWash) {
        List<Review> reviews = carWash.getReview();

        double totalScore = 0;
        for (Review review : reviews) {
            totalScore += review.getRate();
        }

        double averageScore = totalScore / reviews.size();

        carWash.setAverageScore(averageScore);
        carWashRepository.save(carWash);
    }


*/
}