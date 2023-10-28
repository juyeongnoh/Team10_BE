package bdbe.bdbd.user;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole role;

    @Column(length = 100, nullable = false, unique = true)
    private String email; // 인증시 필요한 필드

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String tel;


    @Builder
    public User(Long id, String email, String password, String username, UserRole role, String tel) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.tel = tel;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
