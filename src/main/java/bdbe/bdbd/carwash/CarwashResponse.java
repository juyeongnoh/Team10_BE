package bdbe.bdbd.carwash;

import lombok.Getter;
import lombok.Setter;
public class CarwashResponse {
    @Getter
    @Setter
    public static class FindAllDTO {
        private Long id;
        private String name;
        private double rate;
        private String tel;
        private String des;
        private int price;
        private Long lId;
        private Long userId;

        public FindAllDTO(Carwash carwash) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.tel = carwash.getTel();
            this.des = carwash.getDes();
            this.price = carwash.getPrice();
            this.lId = carwash.getLocation().getId();
            this.userId = carwash.getUser().getId();
        }
    }
    @Getter
    @Setter
    public class KeywordResponseDTO {
        private String keywordName;

        public KeywordResponseDTO(String keywordName) {
            this.keywordName = keywordName;
        }

        // getters, setters
    }


}
