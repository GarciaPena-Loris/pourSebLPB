package m1.archi.resthotel.repositories;

import m1.archi.resthotel.models.Carte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteRepository extends JpaRepository<Carte, Long> {
    Optional<Carte> findByNumero(String numero);
}
