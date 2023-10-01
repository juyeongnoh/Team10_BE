package bdbe.bdbd.review;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int u_id;
    private int w_id;
    private int id2;
    private String singlecomment;
    private Integer rate;
    private Integer keyword;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @Builder
    public Review(int id,String singlecomment, Integer rate, Integer keyword) {
        this.id = id;
        this.singlecomment = singlecomment;
        this.rate = rate;
        this.keyword = keyword;
    }



    //@OneToOne
    //@JoinColumn(name = "reservation_id")
    //private Reservation reservation;

    //@ManyToOne
    //@JoinColumn(name = "carwash_id")
    //private CarWash carWash;

}


