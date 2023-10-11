package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="bay")
public class Bay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="bay_num", nullable = false)
    private int bayNum;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="w_id",  nullable = false)
    private Carwash carwash;

    @Column(name="status", nullable = false)
    private int status; //상태

    @OneToMany(mappedBy = "bay") //읽기 전용, 양방향
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder
    public Bay(Long id, int bayNum, Carwash carwash, int status) {
        this.id = id;
        this.bayNum = bayNum;
        this.carwash = carwash;
        this.status = status;
    }

    public void changeStatus(int newStatus) {
        if (newStatus != 0 && newStatus != 1) {
            throw new IllegalArgumentException("Invalid status value: " + newStatus);
        }
        this.status = newStatus;
    }
}