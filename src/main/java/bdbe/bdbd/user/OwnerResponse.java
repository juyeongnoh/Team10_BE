package bdbe.bdbd.user;

import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
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

}
