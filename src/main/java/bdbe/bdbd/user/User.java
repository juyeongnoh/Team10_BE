package bdbe.bdbd.user;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY) //외래키
//    @JoinColumn(name="r_id", nullable = false)
//    private Region region;

    @Column(length = 100, nullable = false, unique = true)
    private String email; // 인증시 필요한 필드
    @Column(length = 256, nullable = false)
    private String password;
    @Column(length = 45, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private UserRole role;

    @Column(length = 20, nullable = false)
    private String tel;

    @Column(length = 10, nullable = false)
    private int credit;

    @Builder
    public User(Long id, String email, String password, String username, String role, int credit, String tel) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = UserRole.valueOf(role);
        this.credit = credit;
        this.tel = tel;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
