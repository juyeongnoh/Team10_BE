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

    @Column(length = 50, nullable = false)
    private String keywordName;

    @Builder
    public Keyword(Long id, String keywordName) {
        this.id = id;
        this.keywordName = keywordName;
    }
}