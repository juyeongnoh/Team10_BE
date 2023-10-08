package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.review.Review;
import bdbe.bdbd.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private int id;

    @Column(nullable = true)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="b_id",  nullable = false)
    private Bay bay;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="u_id",  nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "r_id", nullable = false) //외래키 가짐
    private Review review;

    @Builder
    public Reservation(int id, int price, Bay bay, User user, Review review) {
        this.id = id;
        this.price = price;
        this.bay = bay;
        this.user = user;
        this.review = review;
    }
}