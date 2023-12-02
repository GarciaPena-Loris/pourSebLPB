package m1.archi.restagence.data;

import m1.archi.restagence.exceptions.AgenceException;
import m1.archi.restagence.models.Agence;
import m1.archi.restagence.models.ReductionHotel;
import m1.archi.restagence.repositories.AgenceRepository;
import m1.archi.restagence.repositories.ReductionHotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class AgenceData {
    private final Logger logger = LoggerFactory.getLogger(AgenceData.class);
    private final String baseUri = "http://localhost:8080/hotelService/api";

    @Autowired
    RestTemplate proxyHotel;

    @Bean
    public CommandLineRunner initDatabase(AgenceRepository agenceRepository, ReductionHotelRepository reductionHotelRepository) {
        return args -> {
            // Génération de plusieurs agences aléatoire
            int nombreAgences = (int) (Math.random() * 10) + 5;

            System.out.println("\nGénération de " + nombreAgences + " agences aléatoires...");

            for (int i = 0; i < nombreAgences; i++) {

                // Génération de l'agence
                Agence agence = new Agence(RandomDonneesAgence.randomNomAgence());

                // Sauvegarde de l'agence
                agence = agenceRepository.save(agence);

                // Association des hotels partenaire
                ParameterizedTypeReference<List<Long>> typeReference = new ParameterizedTypeReference<>() {
                };

                // Appel à la méthode de recherche d'offres de l'hôtel via le proxyHotel
                ResponseEntity<List<Long>> responseEntity = proxyHotel.exchange(baseUri + "/idHotels", HttpMethod.GET, null, typeReference);
                List<Long> idHotels = responseEntity.getBody();

                if (idHotels == null || idHotels.isEmpty()) {
                    throw new AgenceException("Aucun hotel n'est disponible");
                }

                Map<Long, Integer> hotelsPartenairesReduction = RandomDonneesAgence.randomReductionHotelPartenaire(idHotels, idHotels.size());

                for (Map.Entry<Long, Integer> entry : hotelsPartenairesReduction.entrySet()) {
                    ReductionHotel reductionHotel = new ReductionHotel(entry.getKey(), entry.getValue(), agence);
                    reductionHotelRepository.save(reductionHotel);
                    agence.addReductionHotel(reductionHotel);
                }

                // Mise à jour de l'hôtel avec la liste complète de chambres
                agenceRepository.save(agence);

                // Sauvegarde de l'hôtel
                logger.info("\n[" + (i + 1) + "] Preloading database with " + agence);
            }
            logger.info("Server agence ready!");
        };
    }
}