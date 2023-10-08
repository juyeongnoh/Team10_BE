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
    private double rate;

    @OneToOne
    @JoinColumn(name = "r_id", nullable = false) //외래키 가짐
    private Reservation reservation;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }


    @Builder
    public Review(Long id, User user, Carwash carwash, String comment, double rate, Reservation reservation, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.carwash = carwash;
        this.comment = comment;
        this.rate = rate;
        this.reservation = reservation;
        this.createdAt = createdAt;
    }
}


