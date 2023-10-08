package bdbe.bdbd.optime;

import bdbe.bdbd.carwash.Carwash;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="optime")
public class Optime{ // 영업시간
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Enumerated(EnumType.STRING) // DB에 문자열 저장
    @Column(name="day_name", length = 50, nullable = true)
    private DayType dayName; // enum 요일명

    @Column(name="start_time", nullable = true)
    private LocalTime startTime; // ex)10:00

    @Column(name="end_time", nullable = true)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="c_id",  nullable = false)
    private Carwash carwash;

    @Builder
    public Optime(Long id, DayType dayName, LocalTime startTime, LocalTime endTime, Carwash carwash) {
        this.id = id;
        this.dayName = dayName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carwash = carwash;
    }
}