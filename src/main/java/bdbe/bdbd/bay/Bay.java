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

    @Column(name="bay_type", nullable = true)
    private int bayType;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="w_id",  nullable = false)
    private Carwash carwash;

    @Column(name="status", nullable = true)
    private int status; //상태

    @OneToMany(mappedBy = "bay") //읽기 전용, 양방향
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder
    public Bay(Long id, int bayNum, int bayType, Carwash carwash, int status, List<Reservation> reservationList) {
        this.id = id;
        this.bayNum = bayNum;
        this.bayType = bayType;
        this.carwash = carwash;
        this.status = status;
        this.reservationList = reservationList;
    }
}