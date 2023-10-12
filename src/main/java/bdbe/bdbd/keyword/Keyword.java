package bdbe.bdbd.keyword;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="keyword")
public class Keyword { //키워드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="name", length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int type;

    @Builder
    public Keyword(Long id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}