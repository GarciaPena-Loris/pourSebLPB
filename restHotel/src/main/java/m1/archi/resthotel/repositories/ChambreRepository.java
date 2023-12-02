package m1.archi.resthotel.repositories;

import m1.archi.resthotel.models.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
}
