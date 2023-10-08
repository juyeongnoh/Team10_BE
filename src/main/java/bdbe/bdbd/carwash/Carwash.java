package bdbe.bdbd.carwash;

import bdbe.bdbd.file.File;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.user.User;
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
@Table(name="carwash")
public class Carwash{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name; //세차장 이름

    @Column(nullable = true)
    private double rate; //별점

    @Column(length = 50, nullable = true)
    private String tel; //전화번호

    @Column(length = 255, nullable = true)
    private String des; //세차장 설갸명

    @Column(name="price", nullable = true)
    private int price; //30분당 가격

    @OneToOne //일대일-소유측
    @JoinColumn(name="r_id", nullable = false) //외래키
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="u_id",  nullable = false)
    private User user;

    @OneToMany(mappedBy = "carwash") //양방향 비소유측, 1:1 참조 - readOnly
    private List<CarwashKeyword> carwashKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "carwash") //양방향 비소유측, 1:1 참조  - readOnly
    private List<File> fileList = new ArrayList<>();

    @Builder
    public Carwash(Long id, String name, double rate, String tel, String des, int price, Region region, User user, List<CarwashKeyword> carwashKeywords, List<File> fileList) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.tel = tel;
        this.des = des;
        this.price = price;
        this.region = region;
        this.user = user;
        this.carwashKeywords = carwashKeywords;
        this.fileList = fileList;
    }

    public void updateRate(double rate) {
        this.rate = rate;
    }
}
