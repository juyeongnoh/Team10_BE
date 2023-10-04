package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.region.Region;
import bdbe.bdbd.review.Review;
import bdbe.bdbd.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReservationRequest {

    @Getter
    @Setter
    @ToString
    public static class SaveDTO {

        private Long bay_id;

        private LocalDate selected_date; //날짜만 포함함

        private TimeDTO time; //시간만 포함

        public Reservation toReservationEntity(Carwash carwash, Bay bay, User user){
            int perPrice = carwash.getPrice();
            LocalTime startTime = LocalTime.parse(time.getStart().toString());
            LocalTime endTime = LocalTime.parse(time.getEnd().toString());

            int minutesDifference = (int)ChronoUnit.MINUTES.between(startTime, endTime); //시간 차 계산
            int blocksOf30Minutes = minutesDifference / 30; //30분 단위로 계산
            int price = perPrice * blocksOf30Minutes;

            return Reservation.builder()
                    .price(price)
                    .bay(bay)
                    .user(user)
                    .build();
        }
        @Getter
        @Setter
        @ToString
        public static class TimeDTO {
            private LocalTime start; //시작 시간
            private LocalTime end; // 끝 시간
        }
    }
}