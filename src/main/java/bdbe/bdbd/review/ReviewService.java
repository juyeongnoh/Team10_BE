package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.rkeyword.Rkeyword;
import bdbe.bdbd.rkeyword.RkeywordJPARepository;
import bdbe.bdbd.rkeyword.reviewKeyword.ReviewKeyword;
import bdbe.bdbd.rkeyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final RkeywordJPARepository rkeywordJPARepository;
    private final ReviewKeywordJPARepository reviewKeywordJPARepository;



    public void createReview(ReviewRequest.SaveDTO dto, User user) {
        Carwash carwash = carwashJPARepository.findById(dto.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Review review = dto.toReviewEntity(user, carwash);
        Review savedReview = reviewJPARepository.save(review);
        //키워드-리뷰 다대다 매핑 (없는 키워드일 경우 무시)
        List<Rkeyword> rkeywordList = rkeywordJPARepository.findAllByIdIn(dto.getRKeywordIdList());
        List<ReviewKeyword> reviewKeywords = rkeywordList.stream()
                .map(keyword -> ReviewKeyword.builder().review(savedReview).keyword(keyword).build())
                .collect(Collectors.toList());
        reviewKeywordJPARepository.saveAll(reviewKeywords);
    }


    public Review getReviewById(Long id) {
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