package bdbe.bdbd.review;


import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import bdbe.bdbd.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="u_id",  nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="c_id",  nullable = false)
    private Carwash carwash;

    @Column(length = 100, nullable = true)
    private String comment;

    @Column(nullable = true)
    private int rate;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @OneToOne(mappedBy = "review") //참조를 위한 값(DB 반영 X)
    private Reservation reservation;

    @Builder
    public Review(Long id, User user, Carwash carwash, Reservation reservation, String comment, int rate) {
        this.id = id;
        this.user = user;
        this.carwash = carwash;
        this.reservation = reservation;
        this.comment = comment;
        this.rate = rate;
    }

}


