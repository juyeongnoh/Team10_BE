package bdbe.bdbd.user;

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
        private List<BayReservationDTO> bays;

        public CarwashManageDTO(List<Optime> optimeList, List<Reservation> reservationList) {
            if (!reservationList.isEmpty()) {
                Carwash carwash = reservationList.get(0).getBay().getCarwash();
                this.id = carwash.getId();
                this.name = carwash.getName();
            }
            this.optime = new OperationTimeDTO(optimeList);
            // Bay의 id별로 Reservation 리스트를 그룹화
            Map<Long, List<Reservation>> bayReservationsMap = reservationList.stream()
                    .collect(Collectors.groupingBy(reservation -> reservation.getBay().getId()));

            // 각 Bay에 대해 BayReservationDTO 객체를 생성하고 bays 리스트에 추가
            this.bays = new ArrayList<>();
            for (Map.Entry<Long, List<Reservation>> entry : bayReservationsMap.entrySet()) {
                BayReservationDTO bayReservationDTO = new BayReservationDTO(entry.getValue());
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

        public BayReservationDTO(List<Reservation> reservationList) {
            this.bayNo = reservationList.get(0).getBay().getBayNum();
            this.bayBookedTime = reservationList.stream().map(BookedTimeDTO::new).collect(Collectors.toList());
        }
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
