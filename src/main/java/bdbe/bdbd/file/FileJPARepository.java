package bdbe.bdbd.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileJPARepository extends JpaRepository<File, Long> {
}
