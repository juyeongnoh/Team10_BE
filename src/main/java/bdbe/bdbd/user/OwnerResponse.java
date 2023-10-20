package bdbe.bdbd.user;

import bdbe.bdbd.bay.Bay;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.optime.DayType;
import bdbe.bdbd.optime.Optime;
import bdbe.bdbd.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OwnerResponse {

    @Getter
    @Setter
    @ToString
    public static class SaleResponseDTO {
        private List<ReservationCarwashDTO> response;

        public SaleResponseDTO(List<Reservation> reservationList) {
            this.response = reservationList.stream()
                    .map(ReservationCarwashDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationCarwashDTO {
        private ReservationDTO reservation;
        private CarwashDTO carwash;

        public ReservationCarwashDTO(Reservation reservation) {
            this.reservation = new ReservationDTO(reservation);
            this.carwash = new CarwashDTO(reservation.getBay().getCarwash());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationDTO {
        private Long reservationId;
        private int bayNo;
        private String nickname;
        private int totalPrice;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public ReservationDTO(Reservation reservation) {
            this.reservationId = reservation.getId();
            this.bayNo = reservation.getBay().getBayNum();
            this.nickname = reservation.getUser().getUsername();
            this.totalPrice = reservation.getPrice();
            this.startTime = reservation.getStartTime();
            this.endTime = reservation.getEndTime();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashDTO {
        private Long carwashId;
        private String name;

        public CarwashDTO(Carwash carwash) {
            this.carwashId = carwash.getId();
            this.name = carwash.getName();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ReservationOverviewResponseDTO {
        private List<CarwashManageDTO> carwash = new ArrayList<>();

        public void addCarwashManageDTO(CarwashManageDTO carwashManageDTO) {
            this.carwash.add(carwashManageDTO);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class CarwashManageDTO {
        private Long id;
//        private String image;
        private String name;
        private OperationTimeDTO optime;
        private List<BayReservationDTO> bays = new ArrayList<>();

        public CarwashManageDTO(Carwash carwash, List<Bay> bayList, List<Optime> optimeList, List<Reservation> reservationList) {
            this.id = carwash.getId();
            this.name = carwash.getName();
            this.optime = new OperationTimeDTO(optimeList);
            for (Bay bay : bayList) {
                BayReservationDTO bayReservationDTO = new BayReservationDTO(bay, reservationList);
                this.bays.add(bayReservationDTO);
            }
        }
    }

    @Getter
    @Setter
    @ToString
    public static class OperationTimeDTO {
        private TimeFrameDTO weekday;
        private TimeFrameDTO weekend;

        public OperationTimeDTO(List<Optime> optimeList) {
            optimeList.forEach(optime -> {
                if (optime.getDayType() == DayType.WEEKDAY) {
                    this.weekday = new TimeFrameDTO(optime);
                } else if (optime.getDayType() == DayType.WEEKEND) {
                    this.weekend = new TimeFrameDTO(optime);
                }
            });
        }
    }

    @Getter
    @Setter
    @ToString
    public static class TimeFrameDTO {
        private LocalTime start;
        private LocalTime end;

        public TimeFrameDTO(Optime optime) {
            this.start = optime.getStartTime();
            this.end = optime.getEndTime();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class BayReservationDTO {
        private int bayNo;
        private List<BookedTimeDTO> bayBookedTime;

        public BayReservationDTO(Bay bay, List<Reservation> reservationList) {
            this.bayNo = bay.getBayNum();
            this.bayBookedTime = reservationList.stream()
                    .filter(reservation -> reservation.getBay() != null && reservation.getBay().getId().equals(bay.getId()))
                    .map(BookedTimeDTO::new)
                    .collect(Collectors.toList());

        }


        @Getter
        @Setter
        @ToString
        public static class BookedTimeDTO {
            private LocalDateTime start;
            private LocalDateTime end;

            public BookedTimeDTO(Reservation reservation) {
                this.start = reservation.getStartTime();
                this.end = reservation.getEndTime();
            }
        }

    }
}
