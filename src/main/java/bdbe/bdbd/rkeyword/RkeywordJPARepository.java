package bdbe.bdbd.rkeyword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RkeywordJPARepository extends JpaRepository<Rkeyword, Long> {
    List<Rkeyword> findAllByIdIn(List<Long> ids); //리스트에서 RKeyword 찾기

}
