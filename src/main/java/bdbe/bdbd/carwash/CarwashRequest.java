package bdbe.bdbd.carwash;

import bdbe.bdbd.file.File;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.region.Region;
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
        private RegionDTO region;

        private String price;

        private OperatingTimeDTO optime;
        private List<String> images;
        private List<Long> keywordId;
        private String description;
        private String tel;


        public Carwash toCarwashEntity(Region region, User user) {
            return Carwash.builder()
                    .name(name)
                    .tel(tel)
                    .des(description)
                    .price(Integer.parseInt(price))  // 문자열 price를 int로 변환
                    .region(region)
                    .user(user)
                    .build();
        }

        public Region toRegionEntity() {
            return Region.builder()
                    .placeName(region.getPlaceName())
                    .address(region.getAddress())
                    .latitude(region.getLatitude())
                    .longitude(region.getLongitude())
                    .build();
        }

        public List<Optime> toOptimeEntities(Carwash carwash) {
            List<Optime> optimeList = new ArrayList<>();

            optimeList.add(Optime.builder()
                    .dayName(DayType.WEEKDAY)
                    .startTime(optime.getWeekday().getStart())
                    .endTime(optime.getWeekday().getEnd())
                    .carwash(carwash)
                    .build());

            optimeList.add(Optime.builder()
                    .dayName(DayType.WEEKEND)
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
                        .ext(ext)
                        .path("https://cdn.example.com/images/image1.jpg")
                        .size(1500000)
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
    public static class RegionDTO {
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
}