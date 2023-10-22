package bdbe.bdbd.file;

import bdbe.bdbd.carwash.Carwash;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class FileResponse {

    @Getter
    @Setter
    public static class FileResponseDTO {
        private Long id;
        private String name;
        private String url;
        private String path;
        private LocalDateTime uploadedAt;
        private Carwash carwash;

        public FileResponseDTO(Long id, String name, String url, String path, LocalDateTime uploadedAt, Carwash carwash) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.path = path;
            this.uploadedAt = uploadedAt;
            this.carwash = carwash;
        }
    }

    @Getter
    @Setter
    public static class SimpleFileResponseDTO {
        private Long id;
        private String name;
        private String url;
        private String path;
        private LocalDateTime uploadedAt;
        private SimpleCarwashDTO carwash;

        public SimpleFileResponseDTO(Long id, String name, String url, String path, LocalDateTime uploadedAt, SimpleCarwashDTO carwash) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.path = path;
            this.uploadedAt = uploadedAt;
            this.carwash = carwash;
        }
    }

    @Getter
    @Setter
    public static class SimpleCarwashDTO {
        private Long id;

        public SimpleCarwashDTO(Long id) {
            this.id = id;
        }
    }
}
