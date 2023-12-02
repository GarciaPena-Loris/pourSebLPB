package m1.archi.restagence.controllers;

import m1.archi.restagence.exceptions.*;
import m1.archi.restagence.models.Agence;
import m1.archi.restagence.models.ReductionHotel;
import m1.archi.restagence.models.Utilisateur;
import m1.archi.restagence.models.modelsHotel.Offre;
import m1.archi.restagence.models.modelsHotel.Reservation;
import m1.archi.restagence.repositories.AgenceRepository;
import m1.archi.restagence.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AgenceController {
    @Autowired
    RestTemplate proxyHotel;
    @Autowired
    private AgenceRepository agenceRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    private final String baseUri = "http://localhost:8080/hotelService/api";


    /* Méthodes */
    @GetMapping("${base-uri}/agences")
    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    @GetMapping("${base-uri}/idAgences")
    public List<Long> getAllIdAgences() {
        return agenceRepository.findAll().stream().map(Agence::getIdAgence).collect(Collectors.toList());
    }

    @GetMapping("${base-uri}/agences/count")
    public String count() {
        return String.format("{\"%s\": %d}", "count", agenceRepository.count());
    }

    @GetMapping("${base-uri}/agences/{id}")
    public Agence getAgenceById(@PathVariable long id) throws AgenceNotFoundException {
        return agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));
    }

    @GetMapping("${base-uri}/agences/{id}/recherche")
    public List<List<Offre>> rechercheChambreById(@PathVariable long id, @RequestParam String ville, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateArrivee,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateDepart, @RequestParam int prixMin,
                                                  @RequestParam int prixMax, @RequestParam int nombreEtoiles, @RequestParam int nombrePersonne) throws AgenceNotFoundException, HotelException {

        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));

        try {
            // On récupère les hotels de l'agence
            List<List<Offre>> listeOffresParHotels = new ArrayList<>();

            List<ReductionHotel> reductionHotels = agence.getReductionHotels();

            for (ReductionHotel reductionHotel : reductionHotels) {
                // Construire l'URI de l'hôtel
                String hotelUri = baseUri + "/hotels/{id}/recherche";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(hotelUri)
                        .queryParam("ville", ville)
                        .queryParam("dateArrivee", dateArrivee)
                        .queryParam("dateDepart", dateDepart)
                        .queryParam("prixMin", prixMin)
                        .queryParam("prixMax", prixMax)
                        .queryParam("nombreEtoiles", nombreEtoiles)
                        .queryParam("nombrePersonne", nombrePersonne);

                // Appel à la méthode de recherche d'offres de l'hôtel via le proxyHotel en utilisant ParameterizedTypeReference pour spécifier le type générique de la liste
                ParameterizedTypeReference<List<Offre>> typeReference = new ParameterizedTypeReference<>() {
                };

                // Appel à la méthode de recherche d'offres de l'hôtel via le proxyHotel
                ResponseEntity<List<Offre>> responseEntity = proxyHotel.exchange(builder.buildAndExpand(reductionHotel.getIdHotel()).toUri(), HttpMethod.GET, null, typeReference);
                List<Offre> offresHotel = responseEntity.getBody();

                // Appliquer la reduction
                if (offresHotel != null && !offresHotel.isEmpty()) {
                    for (Offre offre : offresHotel) {
                        offre.setPrixAvecReduction(Math.round(offre.getPrix() * (1 - reductionHotel.getReduction() / 100.0) * 100.0) / 100.0);
                    }
                    listeOffresParHotels.add(offresHotel);
                }
            }

            return listeOffresParHotels;
        } catch (HttpClientErrorException ex) {
            throw new HotelException("problem Hotel : " + ex.getMessage());
        }
    }


    @PostMapping("${base-uri}/agences/{id}/reservation")
    public Reservation reserverChambresHotel(@PathVariable long id, @RequestParam String email, @RequestParam String motDePasse, @RequestParam Offre offre,
                                             @RequestParam boolean petitDejeuner, @RequestParam String nomClient, @RequestParam String prenomClient,
                                             @RequestParam String telephone, @RequestParam String nomCarte, @RequestParam String numeroCarte,
                                             @RequestParam String expirationCarte, @RequestParam String CCVCarte) throws AgenceNotFoundException, UtilisateurWrongPasswordException, UtilisateurNotRegisteredException, ReservationProblemeException, HotelException {
        // On recupere l'agence
        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));

        try {
            // Essaye de recuperer l'utilisateur avec le login et le mot de passe
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(() -> new AgenceNotFoundException("Utilisateur not found with email " + email));
            if (!utilisateur.getMotDePasse().equals(motDePasse)) {
                throw new UtilisateurWrongPasswordException("Wrong password");
            }
            if (utilisateur.getAgence().getIdAgence() == agence.getIdAgence()) {
                throw new UtilisateurNotRegisteredException("Utilisateur not registered in this agency");
            }

            // Construire l'URI de l'hôtel
            String hotelUri = baseUri + "/hotels/{id}/reservation";

            // Construire les paramètres de la requête
            Map<String, Object> params = new HashMap<>();
            params.put("id", offre.getHotel().getIdHotel());
            params.put("offre", offre);
            params.put("petitDejeuner", petitDejeuner);
            params.put("nomClient", nomClient);
            params.put("prenomClient", prenomClient);
            params.put("email", email);
            params.put("telephone", telephone);
            params.put("nomCarte", nomCarte);
            params.put("numeroCarte", numeroCarte);
            params.put("expirationCarte", expirationCarte);
            params.put("CCVCarte", CCVCarte);

            ParameterizedTypeReference<Reservation> typeReference = new ParameterizedTypeReference<>() {
            };

            // Appel à la méthode de reservation d'offres de l'hôtel via le proxyHotel
            ResponseEntity<Reservation> responseEntity = proxyHotel.exchange(hotelUri, HttpMethod.POST, null, typeReference, params);

            Reservation reservation = responseEntity.getBody();
            if (reservation == null) {
                throw new ReservationProblemeException("Reservation problem");
            }
            utilisateur.addReservation(reservation.getIdReservation());
            utilisateurRepository.save(utilisateur);
            return reservation;

        } catch (HttpClientErrorException ex) {
            throw new HotelException("problem Agence : " + ex.getMessage());
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${base-uri}/agences")
    public Agence createAgence(@RequestBody Agence agence) {
        return agenceRepository.save(agence);
    }

    @PutMapping("${base-uri}/agences/{id}")
    public Agence updateAgence(@RequestBody Agence newAgence, @PathVariable long id) {
        return agenceRepository.findById(id).map(agence -> {
            agence.setNom(newAgence.getNom());
            agence.setReductionHotels(newAgence.getReductionHotels());
            agence.setListeUtilisateurs(newAgence.getListeUtilisateurs());

            return agenceRepository.save(agence);
        }).orElseGet(() -> agenceRepository.save(newAgence));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${base-uri}/agences/{id}")
    public void deteleAgence(@PathVariable long id) throws AgenceNotFoundException {
        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));
        agenceRepository.delete(agence);
    }

    // PARTIE USER
    @GetMapping("${base-uri}/agences/{id}/utilisateurs")
    public List<Utilisateur> getAllUtilisateurs(@PathVariable long id) throws AgenceNotFoundException {
        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));
        return agence.getListeUtilisateurs();
    }

    @GetMapping("${base-uri}/agences/utilisateurs")
    public Utilisateur getUtilisateurByEmailMotDePasse(@RequestParam String email, @RequestParam String motDePasse) throws UtilisateurWrongPasswordException, UtilisateurNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur not found with email " + email));
        if (!utilisateur.getMotDePasse().equals(motDePasse)) {
            throw new UtilisateurWrongPasswordException("Wrong password");
        }
        return utilisateur;
    }

    @GetMapping("${base-uri}/agences/{id}/utilisateurs/{idUtilisateur}")
    public Utilisateur getUtilisateurById(@PathVariable long id, @PathVariable long idUtilisateur) throws AgenceNotFoundException, UtilisateurNotRegisteredException {
        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agency not found with id " + id));
        return agence.getListeUtilisateurs().stream().filter(utilisateur -> utilisateur.getIdUtilisateur() == idUtilisateur).findFirst().orElseThrow(() -> new UtilisateurNotRegisteredException("Utilisateur with id " + idUtilisateur + " not registered in this agency"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${base-uri}/agences/{id}/utilisateurs")
    public Utilisateur addUtilisateur(@PathVariable long id, @RequestBody Utilisateur utilisateur) throws AgenceNotFoundException, UtilisateurAlreadyRegisteredException {
        Agence agence = agenceRepository.findById(id).orElseThrow(() -> new AgenceNotFoundException("Agence not found with id " + id));

        if (agence.getListeUtilisateurs().stream().anyMatch(utilisateurAgence -> utilisateurAgence.getEmail().equals(utilisateur.getEmail())))
            throw new UtilisateurAlreadyRegisteredException("Utilisateur " + utilisateur.getEmail() + " already registered in this agency");

        utilisateur.setAgence(agence);
        utilisateurRepository.save(utilisateur);
        agence.addUtilisateur(utilisateur);
        agenceRepository.save(agence);
        return utilisateur;
    }
}
