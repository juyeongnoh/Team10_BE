package bdbe.bdbd.location;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="location")
public class Location { //지역
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="place", length = 255, nullable = false)
    private String place; //장소명

    @Column(name="address", length = 255, nullable = false)
    private String address; //도로명 주소

    @Column(nullable = false)
    private double latitude; //위도

    @Column(nullable = false)
    private double longitude; //경도


    @Builder
    public Location(Long id, String place, String address, double latitude, double longitude) {
        this.id = id;
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateAddress(String address, String place, double latitude, double longitude) {
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
