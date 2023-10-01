package bdbe.bdbd.user;

import bdbe.bdbd.region.Region;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="r_id",  nullable = false)
    private Region region;

    @Column(length = 100, nullable = false, unique = true)
    private String email; // 인증시 필요한 필드
    @Column(length = 256, nullable = false)
    private String password;
    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 30, nullable = false)
    private String role;
    @Column(nullable = false)
    private int credit;
    @Column(length = 20, nullable = false)
    private String tel;

    @Builder
    public User(long id, Region region, String email, String password, String username, String role, int credit, String tel) {
        this.id = id;
        this.region = region;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.credit = credit;
        this.tel = tel;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
