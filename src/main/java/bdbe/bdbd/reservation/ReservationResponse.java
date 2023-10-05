package bdbe.bdbd.reservation;

import bdbe.bdbd.bay.Bay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
            this.bayList = bayList.stream().map(bay -> {
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

}
