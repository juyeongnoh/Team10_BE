package bdbe.bdbd.user;

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
    @Column(length = 100, nullable = false, unique = true)
    private String email; // 인증시 필요한 필드
    @Column(length = 256, nullable = false)
    private String password;
    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 30, nullable = false)
    private String roles;
    @Column
    private int creadit;
    @Column(length = 20, nullable = false)
    private String phoneNumber;



    @Builder
    public User(long id, String email, String password, String username, String roles, int creadit, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
        this.creadit = creadit;
        this.phoneNumber = phoneNumber;
    }
    public void updatePassword(String password) {
        this.password = password;
    }

}
