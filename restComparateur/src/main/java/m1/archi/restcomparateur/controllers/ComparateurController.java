package m1.archi.restcomparateur.controllers;

import m1.archi.restcomparateur.exceptions.*;
import m1.archi.restcomparateur.models.Comparateur;
import m1.archi.restcomparateur.models.modelsAgence.Agence;
import m1.archi.restcomparateur.models.modelsHotel.Offre;
import m1.archi.restcomparateur.models.modelsHotel.Reservation;
import m1.archi.restcomparateur.repositories.ComparateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${base-uri}")
public class ComparateurController {
    @Autowired
    RestTemplate proxyAgence;
    @Autowired
    private ComparateurRepository comparateurRepository;
    private final String baseUri = "http://localhost:8090/agenceService/api";

    public ComparateurController(RestTemplate proxyAgence) {
        this.proxyAgence = proxyAgence;
    }


    /* Méthodes */
    @GetMapping("/comparateurs")
    public List<Comparateur> getComparateur() {
        return comparateurRepository.findAll();
    }

    @GetMapping("/idComparateurs")
    public List<Long> getAllIdComparateur() {
        return comparateurRepository.findAll().stream().map(Comparateur::getIdComparateur).collect(Collectors.toList());
    }

    @GetMapping("/comparateur/count")
    public String count() {
        return String.format("{\"%s\": %d}", "count", comparateurRepository.count());
    }

    @GetMapping("/comparateur")
    public Comparateur getFirstComparateur() throws ComparateurNotFoundException {
        return comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found"));
    }

    @GetMapping("/comparateur/idAgences")
    public List<Long> getAllIdAgences() throws ComparateurNotFoundException {
        return comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found")).getIdAgences();
    }

    @GetMapping("/comparateur/idAgences/count")
    public int countAgence() throws ComparateurNotFoundException {
        return comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found")).getIdAgences().size();
    }

    @GetMapping("/idComparateur")
    public Long getFirstIdComparateur() throws ComparateurNotFoundException {
        return comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found")).getIdComparateur();
    }

    @GetMapping("/comparateur/recherche")
    public Map<Agence, List<List<Offre>>> rechercheChambreById(@RequestParam String ville, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateArrivee,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateDepart, @RequestParam int prixMin,
                                                               @RequestParam int prixMax, @RequestParam int nombreEtoiles, @RequestParam int nombrePersonne) throws AgenceException, ComparateurNotFoundException {

        Comparateur comparateur = comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found"));

        try {
            Map<Agence, List<List<Offre>>> mapOffresParAgences = new HashMap<>();

            List<Long> idAgences = comparateur.getIdAgences();

            for (long idAgence : idAgences) {
                String agenceUri = baseUri + "/agences/{id}/recherche";

                // Construire l'URI de la requête avec les paramètres
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(agenceUri)
                        .queryParam("ville", ville)
                        .queryParam("dateArrivee", dateArrivee)
                        .queryParam("dateDepart", dateDepart)
                        .queryParam("prixMin", prixMin)
                        .queryParam("prixMax", prixMax)
                        .queryParam("nombreEtoiles", nombreEtoiles)
                        .queryParam("nombrePersonne", nombrePersonne);

                // Définir le type de retour
                ParameterizedTypeReference<List<List<Offre>>> typeReference = new ParameterizedTypeReference<>() {
                };

                // Appeler le service
                ResponseEntity<List<List<Offre>>> responseEntity = proxyAgence.exchange(builder.buildAndExpand(idAgence).toUri(), HttpMethod.GET, null, typeReference);
                List<List<Offre>> offresParAgences = responseEntity.getBody();


                if (offresParAgences != null && !offresParAgences.isEmpty()) {
                    // Trier par hotel (nombre étoiles)
                    offresParAgences.sort(Comparator.comparing(o -> o.get(0).getHotel().getNombreEtoiles()));

                    // Trier les offres par prix
                    for (List<Offre> offres : offresParAgences) {
                        offres.sort(Comparator.comparing(Offre::getPrixAvecReduction));
                    }

                    ResponseEntity<Agence> agenceResponseEntity = proxyAgence.getForEntity(baseUri + "/agences/{id}", Agence.class, idAgence);
                    Agence agence = agenceResponseEntity.getBody();
                    System.out.println("Response from Agence service: " + agenceResponseEntity.getBody());

                    if (agence != null) {
                        System.out.println(agence);
                        mapOffresParAgences.put(agence, offresParAgences);
                    }
                }
            }

            if (mapOffresParAgences.isEmpty()) {
                throw new NoRoomAvailableException("No offers found");
            }
            return mapOffresParAgences;
        } catch (HttpClientErrorException ex) {
            throw new AgenceException("problem Agence : " + ex.getMessage());
        }
    }

    @PostMapping("${base-uri}/comparateur/reservation")
    public Reservation reserverChambresHotel(@RequestParam String email, @RequestParam String motDePasse, @RequestParam Offre offre,
                                             @RequestParam boolean petitDejeuner, @RequestParam String nomClient, @RequestParam String prenomClient,
                                             @RequestParam String telephone, @RequestParam String nomCarte, @RequestParam String numeroCarte,
                                             @RequestParam String expirationCarte, @RequestParam String CCVCarte) throws ReservationProblemeException, AgenceException {
        try {
            // Construire l'URI de l'agence
            String agenceUri = baseUri + "/agences/{id}/reservation";

            // Construire les paramètres de la requête
            Map<String, Object> params = new HashMap<>();
            params.put("id", offre.getHotel().getIdHotel());
            params.put("email", email);
            params.put("motDePasse", motDePasse);
            params.put("offre", offre);
            params.put("petitDejeuner", petitDejeuner);
            params.put("nomClient", nomClient);
            params.put("prenomClient", prenomClient);
            params.put("telephone", telephone);
            params.put("nomCarte", nomCarte);
            params.put("numeroCarte", numeroCarte);
            params.put("expirationCarte", expirationCarte);
            params.put("CCVCarte", CCVCarte);

            ParameterizedTypeReference<Reservation> typeReference = new ParameterizedTypeReference<>() {
            };

            // Appel à la méthode de reservation d'offres de l'hôtel via le proxyAgence
            ResponseEntity<Reservation> responseEntity = proxyAgence.exchange(agenceUri, HttpMethod.POST, null, typeReference, params);

            Reservation reservation = responseEntity.getBody();
            if (reservation == null) {
                throw new ReservationProblemeException("Reservation problem");
            }

            return reservation;
        } catch (HttpClientErrorException ex) {
            throw new AgenceException("problem Agence : " + ex.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comparateur")
    public Comparateur createComparateur(@RequestBody Comparateur comparateur) {
        return comparateurRepository.save(comparateur);
    }

    @PutMapping("/comparateur/agence")
    public Comparateur addAgenceComparateur(@RequestBody Long idAgence) throws ComparateurNotFoundException {
        return comparateurRepository.findFirst().map(comparateur -> {
            comparateur.addIdAgence(idAgence);

            return comparateurRepository.save(comparateur);
        }).orElseThrow(() -> new ComparateurNotFoundException("Comparator not found"));
    }

    @PutMapping("/comparateur")
    public Comparateur updateComparateur(@RequestBody Comparateur newComparateur) {
        return comparateurRepository.findFirst().map(comparateur -> {
            // Mettez à jour les champs nécessaires avec les valeurs de newComparateur
            comparateur.setNom(newComparateur.getNom());
            comparateur.setIdAgences(newComparateur.getIdAgences());

            return comparateurRepository.save(comparateur);
        }).orElseGet(() -> comparateurRepository.save(newComparateur));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comparateur")
    public void deleteComparateur() throws ComparateurNotFoundException {
        Comparateur comparateur = comparateurRepository.findFirst().orElseThrow(() -> new ComparateurNotFoundException("Comparator not found"));
        comparateurRepository.delete(comparateur);
    }

    // Gestion d'erreur
    @RequestMapping("/error")
    public void handleError() throws ComparateurException {
        throw new ComparateurException("An error occurred while processing your request :");
    }
}