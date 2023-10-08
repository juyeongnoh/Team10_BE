package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
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

    @Column(length = 100, nullable = true)
    private String name;

    @Column(length = 50, nullable = true)
    private String ext;

    @Column(length = 255, nullable = true)
    private String path;

    @Column(nullable = true)
    private int size;

    @Builder
    public File(Long id, Carwash carwash, String name, String ext, String path, int size) {
        this.id = id;
        this.carwash = carwash;
        this.name = name;
        this.ext = ext;
        this.path = path;
        this.size = size;
    }
}
