package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.review.Review;
import bdbe.bdbd.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@NoArgsConstructor
@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(nullable = true)
    private int price;

    @Column(name="date", length = 50, nullable = true)
    private LocalDate date; // 예약 날짜

    @Column(name="start_time", nullable = true)
    private LocalTime startTime; // ex)10:00

    @Column(name="end_time", nullable = true)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="b_id",  nullable = false)
    private Bay bay;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="u_id",  nullable = false)
    private User user;


    @Builder
    public Reservation(Long id, int price, LocalDate date, LocalTime startTime, LocalTime endTime, Bay bay, User user) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bay = bay;
        this.user = user;
    }
}