package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.keyword.Keyword;
import bdbe.bdbd.keyword.KeywordJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.review.ReviewResponse.ReviewByCarwashIdDTO;
import bdbe.bdbd.review.ReviewResponse.ReviewKeywordResponseDTO;
import bdbe.bdbd.review.ReviewResponse.ReviewResponseDTO;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeyword;
import bdbe.bdbd.keyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;
    private final ReviewKeywordJPARepository reviewKeywordJPARepository;
    private final KeywordJPARepository keywordJPARepository;

    @Transactional
    public void createReview(ReviewRequest.SaveDTO dto, User user) {
        Carwash carwash = carwashJPARepository.findById(dto.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Reservation reservation = reservationJPARepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Review review = dto.toReviewEntity(user, carwash, reservation);
        log.info("review: {}", review);

        Review savedReview = reviewJPARepository.save(review);
        // 리뷰 키워드 저장
        List<Long> keywordIdList = dto.getKeywordList();
        System.out.println("keywordIdList:");
        for (Long aLong : keywordIdList) {
            System.out.println(aLong);
        }
        keywordIdList.stream()
                .map(id -> {
                    Keyword keyword = keywordJPARepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("keyword not found"));
                    ReviewKeyword reviewKeyword = ReviewKeyword.builder().keyword(keyword).review(savedReview).build();
                    ReviewKeyword savedReviewKeyword = reviewKeywordJPARepository.save(reviewKeyword);
                    System.out.println("reviewKeyword:" + savedReviewKeyword.toString());
                    return savedReviewKeyword;
                })
                .collect(Collectors.toList());

        updateAverageRate(dto, carwash);

    }

    private void updateAverageRate(ReviewRequest.SaveDTO dto, Carwash carwash) {
        // 세차장에 평점 저장
        // 고객이 보낸 평점
        double clientRate = dto.getRate();
        // 세차장 평점
        double carwashRate = carwash.getRate();
        // 세차장 리뷰 몇 개 있는지
        long num = reviewJPARepository.countByCarwash_Id(carwash.getId());
        //평균 구하기
        double totalScore = 0;
        totalScore += clientRate;
        totalScore += (carwashRate * (num - 1));
        double rate = totalScore / num;
        carwash.updateRate(rate);
    }

    public ReviewResponseDTO getReviewsByCarwashId(Long carwashId) {
        List<Review> reviewList = reviewJPARepository.findByCarwash_Id(carwashId);

        List<ReviewByCarwashIdDTO> dto = reviewList.stream()
                .map(r -> {
                    List<ReviewKeyword> reviewKeywordList = reviewKeywordJPARepository.findByReview_Id(r.getId());


                    return new ReviewByCarwashIdDTO(r, r.getUser(), reviewKeywordList);
                })
                .collect(Collectors.toList());

        return new ReviewResponseDTO(dto);

    }


    public ReviewKeywordResponseDTO getReviewKeyword() {
        List<Keyword> keywordList = keywordJPARepository.findByType(2);

        return new ReviewKeywordResponseDTO(keywordList);
    }

}