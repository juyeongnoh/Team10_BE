package bdbe.bdbd.region;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="region")
public class Region { //지역
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="place_name", length = 255, nullable = true)
    private String placeName; //장소명

    @Column(name="address", length = 255, nullable = true)
    private String address; //도로명 주소

    @Column(nullable = true)
    private double latitude; //위도

    @Column(nullable = true)
    private double longitude; //경도


    @Builder
    public Region(Long id, String placeName, String address, double latitude, double longitude) {
        this.id = id;
        this.placeName = placeName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}