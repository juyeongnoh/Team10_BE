package bdbe.bdbd.carwash;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
    public class SearchRequestDTO {
        private List<String> keywords;
        private double latitude;
        private double longitude;
}