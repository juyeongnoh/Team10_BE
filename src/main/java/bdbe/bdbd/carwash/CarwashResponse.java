package bdbe.bdbd.carwash;

import bdbe.bdbd.location.Location;
import bdbe.bdbd.optime.Optime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

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



    @Getter
    @Setter
    public static class findByIdDTO {
        private Long id;
        private String name;
        private double rate;
        private int reviewCnt; // 리뷰 갯수
        private int bayCnt; // 예약 가능한 갯수
        private OperatingTimeDTOResponse optime; //
        private CarwashResponse.locationDTO locationDTO;
        private List<Long> keywordId;
        private String description;
        private String tel;
//        private List<String> image;


        public findByIdDTO(Carwash carwash, int reviewCnt, int bayCnt, Location location, List<Long> keywordId, Optime weekOptime, Optime endOptime) {
//            this.image = image;
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.rate = carwash.getRate();
            this.reviewCnt = reviewCnt;
            this.bayCnt = bayCnt;
            this.optime = toOptimeListDTO(weekOptime, endOptime);
            this.locationDTO = toLocationDTO(location);
            this.keywordId = keywordId;
            this.description = carwash.getDes();
            this.tel = carwash.getTel();
        }

        public OperatingTimeDTOResponse toOptimeListDTO(Optime weekOptime, Optime endOptime) {
            OperatingTimeDTOResponse dto = new OperatingTimeDTOResponse();
            OperatingTimeDTOResponse.TimeSlotResponse weekSlot= new OperatingTimeDTOResponse.TimeSlotResponse();
            weekSlot.setStart(weekOptime.getStartTime());
            weekSlot.setEnd(weekOptime.getEndTime());
            dto.setWeekday(weekSlot);

            OperatingTimeDTOResponse.TimeSlotResponse endSlot= new OperatingTimeDTOResponse.TimeSlotResponse();
            weekSlot.setStart(endOptime.getStartTime());
            weekSlot.setEnd(endOptime.getEndTime());
            dto.setWeekday(weekSlot);

            return dto;

        }

        public locationDTO toLocationDTO(Location location) {
            locationDTO locationDTO = new locationDTO();
            locationDTO.setAddress(location.getAddress());
            locationDTO.setPlaceName(location.getPlace());
            return locationDTO;
        }
    }
    @Getter
    @Setter
    public static class OperatingTimeDTOResponse {
        private TimeSlotResponse weekday;
        private TimeSlotResponse weekend;

        @Getter
        @Setter
        public static class TimeSlotResponse {
            private LocalTime start;
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class locationDTO {
        private String placeName;
        private String address;

    }
}
