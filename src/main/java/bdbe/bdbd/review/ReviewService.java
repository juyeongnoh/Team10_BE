package bdbe.bdbd.review;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.reservation.ReservationJPARepository;
import bdbe.bdbd.rkeyword.RkeywordJPARepository;
import bdbe.bdbd.rkeyword.reviewKeyword.ReviewKeywordJPARepository;
import bdbe.bdbd.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final CarwashJPARepository carwashJPARepository;
    private final ReservationJPARepository reservationJPARepository;
    private final RkeywordJPARepository rkeywordJPARepository;
    private final ReviewKeywordJPARepository reviewKeywordJPARepository;

    @Transactional
    public void createReview(ReviewRequest.SaveDTO dto, User user) {
        Carwash carwash = carwashJPARepository.findById(dto.getCarwashId())
                .orElseThrow(() -> new IllegalArgumentException("Carwash not found"));
        Reservation reservation = reservationJPARepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Review review = dto.toReviewEntity(user, carwash, reservation);
        System.out.println(review);
        reviewJPARepository.save(review);

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

    public Review getReviewById(Long id) {
        return (Review) reviewJPARepository.findById(id).orElse(null);
    }

}