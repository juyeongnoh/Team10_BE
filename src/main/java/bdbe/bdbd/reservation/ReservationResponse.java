package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.file.File;
import bdbe.bdbd.region.Region;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationResponse {
    @Getter
    @Setter
    @ToString
    public static class findAllResponseDTO {
        private List<BayResponseDTO> bayList;

        public findAllResponseDTO(List<Bay> bayList, List<Reservation> reservationList) {
            this.bayList = bayList.stream()
                    .map(bay -> {
                        BayResponseDTO bayResponseDTO = new BayResponseDTO();
                        bayResponseDTO.setBayId(bay.getId());
                        bayResponseDTO.setBayNo(bay.getBayNum());
                // 해당 베이의 예약 모두 담기
                List<BookedTimeDTO> bookedTimes = reservationList.stream()
                        .filter(reservation -> reservation.getBay().getId().equals(bay.getId()))
                        .map(reservation -> {
                            BookedTimeDTO bookedTimeDTO = new BookedTimeDTO();
                            bookedTimeDTO.setStartTime(reservation.getStartTime());
                            bookedTimeDTO.setEndTime(reservation.getEndTime());
                            return bookedTimeDTO;
                        }).collect(Collectors.toList());
                bayResponseDTO.setBayBookedTime(bookedTimes);
                return bayResponseDTO;
            }).collect(Collectors.toList());
        }

        @Getter
        @Setter
        @ToString
        public static class BayResponseDTO {
            private Long bayId;
            private int bayNo;
            private List<BookedTimeDTO> bayBookedTime;
        }
        @Getter
        @Setter
        @ToString
        public static class BookedTimeDTO{
            private LocalTime startTime;
            private LocalTime endTime;
        }

    }
    @Getter
    @Setter
    @ToString
    public static class findLatestOneResponseDTO {
        private ReservationDTO reservation;
        private CarwashDTO carwash;

        public findLatestOneResponseDTO(Reservation reservation, Bay bay, Carwash carwash, Region region) {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.date = reservation.getDate();
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            reservationDTO.time = timeDTO;
            reservationDTO.price = reservation.getPrice();
            reservationDTO.bayNo = bay.getBayNum();
            this.reservation = reservationDTO;

            CarwashDTO carwashDTO = new CarwashDTO();
            carwashDTO.name = carwash.getName();
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.latitude = region.getLatitude();
            regionDTO.longitude = region.getLongitude();
            carwashDTO.region = regionDTO;
//            carwashDTO.imagePath = image.getPath();
            this.carwash = carwashDTO;
        }
    }
    @Getter
    @Setter
    @ToString
    public static class ReservationDTO {
        private LocalDate date;
        private TimeDTO time;
        private int price;
        private int bayNo; // 예약된 베이 번호
    }
    @Getter
    @Setter
    @ToString
    public static class CarwashDTO {
        private String name;
        private RegionDTO region;
//        private String imagePath;
    }
    @Getter
    @Setter
    @ToString
    public static class TimeDTO{
        private LocalTime start;
        private LocalTime end;
    }
    @Getter
    @Setter
    @ToString
    public static class RegionDTO{
        private double latitude;
        private double longitude;
    }

    @Getter
    @Setter
    @ToString
    public static class fetchCurrentStatusReservationDTO {
        private List<ReservationInfoDTO> current;
        private List<ReservationInfoDTO> upcoming;
        private List<ReservationInfoDTO> completed;

        public fetchCurrentStatusReservationDTO(List<ReservationInfoDTO> current, List<ReservationInfoDTO> upcoming, List<ReservationInfoDTO> completed) {
            this.current = current;
            this.upcoming = upcoming;
            this.completed = completed;
        }
    }

    @Getter
    @Setter
    public static class ReservationInfoDTO{
        private Long id; // 예약 id
        private LocalDate date;
        private TimeDTO time;
        private String carwashName;
        private int bayNum;
        private int price;
//        private String image;
        public ReservationInfoDTO(Reservation reservation, Bay bay, Carwash carwash) {
            this.id = reservation.getId();
            this.date = reservation.getDate();
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.start = reservation.getStartTime();
            timeDTO.end = reservation.getEndTime();
            this.time = timeDTO;
            this.carwashName = carwash.getName();
            this.bayNum = bay.getBayNum();
            this.price = reservation.getPrice();
        }
    }

}
