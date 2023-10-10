package bdbe.bdbd.review;

import bdbe.bdbd._core.errors.security.CustomUserDetails;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReviewRestController {

    private final ReviewService reviewService;

    @PostMapping("/reviews") //리뷰 등록
    public ResponseEntity<?> createReview (@RequestBody @Valid ReviewRequest.SaveDTO saveDTO, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.createReview(saveDTO, userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @GetMapping("/carwashes/{carwash_id}/reviews")
    public ResponseEntity<Review> getReviewsByCarwashId(@PathVariable("carwash_id") Long carwashId) {
        Review review = reviewService.getReviewsByCarwashId(carwashId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
/*

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long carWashId) {
        List<Review> reviews = reviewService.getReviewsByCarWashId(carWashId);
        return ResponseEntity.ok(reviews);
    }
*/

}
