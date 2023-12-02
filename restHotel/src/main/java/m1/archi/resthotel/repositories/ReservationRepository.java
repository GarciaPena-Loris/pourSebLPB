package m1.archi.resthotel.repositories;

import m1.archi.resthotel.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
