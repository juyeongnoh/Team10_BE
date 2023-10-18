package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="c_id",  nullable = false)
    private Carwash carwash;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String url;

    @Column(length = 255, nullable = false)
    private String path;

//    private User uploadedBy; // 업로드한 사용자의 엔티티. 외래 키로 사용됩니다.
    @Builder
    public File(Long id, Carwash carwash, String name, String url, String path) {
        this.id = id;
        this.carwash = carwash;
        this.name = name;
        this.url = url;
        this.path = path;
    }
}
