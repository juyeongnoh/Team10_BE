package bdbe.bdbd.carwash;

import bdbe.bdbd.file.File;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CarwashRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotEmpty
        private String name;
        @NotNull
        private LocationDTO location;

        private String price;

        private OperatingTimeDTO optime;
        private List<String> images;
        private List<Long> keywordId;
        private String description;
        private String tel;


        public Carwash toCarwashEntity(Location location, User user) {
            return Carwash.builder()
                    .name(name)
                    .rate(0)
                    .tel(tel)
                    .des(description)
                    .price(Integer.parseInt(price))  // 문자열 price를 int로 변환
                    .location(location)
                    .user(user)
                    .build();
        }

        public Location toLocationEntity() {
            return Location.builder()
                    .place(location.placeName)
                    .address(location.address)
                    .latitude(location.latitude)
                    .longitude(location.longitude)
                    .build();
        }

        public List<Optime> toOptimeEntities(Carwash carwash) {
            List<Optime> optimeList = new ArrayList<>();

            optimeList.add(Optime.builder()
                    .dayType(DayType.WEEKDAY)
                    .startTime(optime.getWeekday().getStart())
                    .endTime(optime.getWeekday().getEnd())
                    .carwash(carwash)
                    .build());

            optimeList.add(Optime.builder()
                    .dayType(DayType.WEEKEND)
                    .startTime(optime.getWeekend().getStart())
                    .endTime(optime.getWeekend().getEnd())
                    .carwash(carwash)
                    .build());

            return optimeList;
        }

        public List<File> toFileEntities(Carwash carwash) {
            List<File> fileList = new ArrayList<>();
            for (String image : images) {
                String ext = getFileExtension(image);
                File file = File.builder()
                        .name(image)
                        .path("https://cdn.example.com/images/image1.jpg")
                        .build();
                fileList.add(file);
            }
            return fileList;
        }

        public String getFileExtension(String filename) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex == -1) {
                return null;
            }
            return filename.substring(dotIndex + 1);
        }


    }

    @Getter
    @Setter
    @ToString
    public static class LocationDTO {
        private String placeName;
        private String address;
        private double latitude;
        private double longitude;

    }

    @Getter
    @Setter
    public static class OperatingTimeDTO {
        private TimeSlot weekday;
        private TimeSlot weekend;

        @Getter
        @Setter
        public static class TimeSlot {
            private LocalTime start;
            private LocalTime end;

        }
    }

    @Getter
    @Setter
    public static class CarwashDistanceDTO {

        private Long id;
        private String name;
        private Location location;
        private double distance;
        private double rate;
        private int price;

        public CarwashDistanceDTO(Long id, String name, Location location, double distance, double rate, int price) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.distance = distance;
            this.rate = rate;
            this.price = price;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UserLocationDTO {
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    public static class SearchRequestDTO {
        private List<Long> keywordIds;
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    @ToString
    public static class updateCarwashDetailsDTO {


        private Long id;
        private String name;
        private int price;
        private String tel;
        private updateLocationDTO locationDTO;
        private int bayCnt;
        private updateOperatingTimeDTO optime;
        private List<Long> keywordId;
        private String description;

//        private List<String> images;

        public updateCarwashDetailsDTO(Carwash carwash, Location location, int bayCnt, List<Long> keywordId, Optime weekOptime, Optime endOptime) {
            //this.image = image;
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.price = carwash.getPrice();
            this.tel = carwash.getTel();
            this.locationDTO = toLocationDTO(location);
            this.bayCnt = bayCnt;
            this.optime = toOptimeListDTO(weekOptime,endOptime);
            this.keywordId = keywordId;
            this.description = carwash.getDes();
        }

        public updateCarwashDetailsDTO() {

        }

//        public updateOperatingTimeDTO toOptimeListDTO(updateOperatingTimeDTO optime) {
//            updateOperatingTimeDTO dto = new updateOperatingTimeDTO();
//            updateOperatingTimeDTO.updateTimeSlot weekSlot= new updateOperatingTimeDTO.updateTimeSlot();
//            weekSlot.setStart(optime.getWeekday().getStart());
//            weekSlot.setEnd(optime.getWeekday().getEnd());
//            dto.setWeekday(weekSlot);
//
//            updateOperatingTimeDTO.updateTimeSlot endSlot= new updateOperatingTimeDTO.updateTimeSlot();
//            endSlot.setStart(optime.getWeekend().getStart());
//            endSlot.setEnd(optime.getWeekend().getEnd());
//            dto.setWeekend(endSlot);
//
//            return dto;

//        }

        public updateOperatingTimeDTO toOptimeListDTO(Optime weekdayOptime, Optime weekendOptime) {
            updateOperatingTimeDTO dto = new updateOperatingTimeDTO();

            updateOperatingTimeDTO.updateTimeSlot weekSlot = new updateOperatingTimeDTO.updateTimeSlot();
            weekSlot.setStart(weekdayOptime.getStartTime().toString());
            weekSlot.setEnd(weekdayOptime.getEndTime().toString());
            dto.setWeekday(weekSlot);

            updateOperatingTimeDTO.updateTimeSlot endSlot = new updateOperatingTimeDTO.updateTimeSlot();
            endSlot.setStart(weekendOptime.getStartTime().toString());
            endSlot.setEnd(weekendOptime.getEndTime().toString());
            dto.setWeekend(endSlot);

            return dto;
        }

        public updateLocationDTO toLocationDTO(Location location) {
            updateLocationDTO updateLocationDTO = new updateLocationDTO();
            updateLocationDTO.setAddress(location.getAddress());
            updateLocationDTO.setPlaceName(location.getPlace());
            return updateLocationDTO;
        }

        public updateLocationDTO setupdateLocationDTO(updateLocationDTO updateLocationDto) {
            updateLocationDTO updateLocationDTO = new updateLocationDTO();
            updateLocationDTO.setAddress(updateLocationDto.getAddress());
            updateLocationDTO.setPlaceName(updateLocationDto.getPlaceName());
            return updateLocationDTO;

        }
    }
    @Getter
    @Setter
    public static class updateOperatingTimeDTO {
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekday;
        private CarwashRequest.updateOperatingTimeDTO.updateTimeSlot weekend;

        @Getter
        @Setter
        public static class updateTimeSlot {
            private String start;
            private String end;

        }
    }

    @Getter
    @Setter
    public static class updateLocationDTO {
        private String placeName;
        private String address;

    }



}