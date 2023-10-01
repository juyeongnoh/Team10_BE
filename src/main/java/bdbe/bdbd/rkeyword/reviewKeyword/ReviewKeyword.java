package bdbe.bdbd.rkeyword.reviewKeyword;

import bdbe.bdbd.review.Review;
import bdbe.bdbd.rkeyword.Rkeyword;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="review_keyword")
public class ReviewKeyword { //키워드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="r_id",  nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="k_id",  nullable = false)
    private Rkeyword keyword;

    @Builder
    public ReviewKeyword(Long id, Review review, Rkeyword keyword) {
        this.id = id;
        this.review = review;
        this.keyword = keyword;
    }
}