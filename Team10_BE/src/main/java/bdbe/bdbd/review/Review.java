package bdbe.bdbd.review;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @Builder
    public Review(int id,String singlecomment, Integer rate, Integer keyword) {
        this.id = id;
        this.singlecomment = singlecomment;
        this.rate = rate;
        this.keyword = keyword;
    }

}


