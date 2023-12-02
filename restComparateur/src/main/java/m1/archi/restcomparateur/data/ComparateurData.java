package m1.archi.restcomparateur.data;

import m1.archi.restcomparateur.exceptions.ComparateurException;
import m1.archi.restcomparateur.models.Comparateur;
import m1.archi.restcomparateur.repositories.ComparateurRepository;
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

@Configuration
public class ComparateurData {

    private final Logger logger = LoggerFactory.getLogger(ComparateurData.class);
    private final String baseUri = "http://localhost:8090/agenceService/api";

    @Autowired
    RestTemplate proxyAgence;

    @Bean
    public CommandLineRunner initDatabase(ComparateurRepository comparateurRepository) {
        return args -> {
            // Génération d'un Comparateur aléatoire

            System.out.println("\nGénération d'un Comparateur aléatoires...");

            // Génération du Comparateur
            Comparateur comparateur = new Comparateur(RandomDonneesComparateur.randomNomComparateur());

            // Sauvegarde de l'agence
            comparateur = comparateurRepository.save(comparateur);
            ParameterizedTypeReference<List<Long>> typeReference = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<List<Long>> responseEntity = proxyAgence.exchange(baseUri + "/idAgences", HttpMethod.GET, null, typeReference);
            List<Long> idAgences = responseEntity.getBody();

            if (idAgences == null || idAgences.isEmpty()) {
                throw new ComparateurException("Aucune agence n'est disponible");
            }

            for (Long idAgence : idAgences) {
                comparateur.addIdAgence(idAgence);
            }

            comparateurRepository.save(comparateur);

            logger.info("Preloading database with " + comparateur);
            logger.info("Server comparateur ready!");
        };
    }
}
