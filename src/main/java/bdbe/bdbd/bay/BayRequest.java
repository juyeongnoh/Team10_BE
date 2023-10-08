package bdbe.bdbd.bay;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.reservation.ReservationResponse;
import bdbe.bdbd.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BayRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        @NotNull
        private int bayNum;

        public Bay toBayEntity(Carwash carwash) {
            return Bay.builder()
                    .carwash(carwash)
                    .bayNum(bayNum)
                    .status(1) // 활성화 상태로 생성
                    .build();
        }

    }
    @Getter
    @Setter
    @ToString
    public static class DeleteDTO {
        @NotNull
        private Long bayId;

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