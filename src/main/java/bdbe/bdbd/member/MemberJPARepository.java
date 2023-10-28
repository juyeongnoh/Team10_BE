package bdbe.bdbd.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJPARepository extends JpaRepository<Member, Long> {
    Member findFirstBy();
    Optional<Member> findByEmail(String email);
}
