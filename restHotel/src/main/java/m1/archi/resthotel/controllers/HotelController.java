package m1.archi.resthotel.controllers;

import m1.archi.resthotel.exceptions.DateNonValideException;
import m1.archi.resthotel.exceptions.HotelNotFoundException;
import m1.archi.resthotel.exceptions.OffreNotFoundException;
import m1.archi.resthotel.models.*;
import m1.archi.resthotel.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HotelController {
    /* Attributs */
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    /* Méthodes */
    @GetMapping("${base-uri}/hotels")
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @GetMapping("${base-uri}/idHotels")
    public List<Long> getAllIdHotels() {
        return hotelRepository.findAll().stream().map(Hotel::getIdHotel).collect(Collectors.toList());
    }

    @GetMapping("${base-uri}/hotels/{id}/image")
    public String getHotelImage(@PathVariable long id) throws HotelNotFoundException {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id)).getImageHotel();
    }

    @GetMapping("${base-uri}/hotels/{id}/chambres")
    public List<Chambre> getHotelChambres(@PathVariable long id) throws HotelNotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        return new ArrayList<>(hotel.getChambres());
    }

    @GetMapping("${base-uri}/hotels/{id}/offres")
    public List<Offre> getHotelOffres(@PathVariable long id) throws HotelNotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        return new ArrayList<>(hotel.getOffres());
    }

    @GetMapping("${base-uri}/hotels/{id}/reservations")
    public List<Reservation> getHotelReservations(@PathVariable long id) throws HotelNotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        return new ArrayList<>(hotel.getReservations());
    }

    @GetMapping("${base-uri}/hotels/count")
    public String count() {
        return String.format("{\"%s\": %d}", "count", hotelRepository.count());
    }

    @GetMapping("${base-uri}/hotels/{id}")
    public Hotel getHotelById(@PathVariable long id) throws HotelNotFoundException {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
    }

    @GetMapping("${base-uri}/hotels/{id}/recherche")
    public List<Offre> rechercheChambreById(@PathVariable long id, @RequestParam String ville, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateArrivee,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateDepart, @RequestParam int prixMin,
                                            @RequestParam int prixMax, @RequestParam int nombreEtoiles, @RequestParam int nombrePersonne) throws HotelNotFoundException, DateNonValideException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        String villeDecode = URLDecoder.decode(ville, StandardCharsets.UTF_8);
        List<Offre> offers = hotel.rechercheChambres(villeDecode, dateArrivee, dateDepart, prixMin, prixMax, nombreEtoiles, nombrePersonne);
        if (offers.isEmpty()) {
            return offers;
        }
        for (Offre offre : offers) {
            offreRepository.save(offre);
            hotel.addOffre(offre);
        }
        hotelRepository.save(hotel);
        return offers;
    }

    @PostMapping("${base-uri}/hotels/{id}/reservation")
    public Reservation reserverChambreById(@PathVariable long id, @RequestBody Offre offre, @RequestBody boolean petitDejeuner, @RequestBody String nomClient,
                                           @RequestBody String prenomClient, @RequestBody String email, @RequestBody String telephone, @RequestBody String nomCarte,
                                           @RequestBody String numeroCarte, @RequestBody String expirationCarte, @RequestBody String CCVCarte) throws HotelNotFoundException, DateNonValideException, OffreNotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        Offre storedOffre = offreRepository.findById(offre.getIdOffre()).orElseThrow(() -> new OffreNotFoundException("Offre not found"));
        if (storedOffre.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new DateNonValideException("Offre expired");
        }

        // Créer une carte si elle n'existe pas déjà
        Carte carte = carteRepository.findByNumero(numeroCarte).orElseGet(() -> carteRepository.save(new Carte(nomCarte, numeroCarte, expirationCarte, CCVCarte)));

        // Créer un client si il n'existe pas déjà
        Client clientPrincipal = clientRepository.findByEmail(email).orElseGet(() -> clientRepository.save(new Client(nomClient, prenomClient, email, telephone, carte)));

        // Créer la réservation
        Reservation reservation = new Reservation(hotel, storedOffre.getChambres(), clientPrincipal, storedOffre.getDateArrivee(), storedOffre.getDateDepart(), storedOffre.getNombreLitsTotal(), petitDejeuner);
        reservationRepository.save(reservation);

        // Ajouter la réservation à l'hotel et a l'historique du client
        hotel.addReservation(reservation);
        hotelRepository.save(hotel);

        clientPrincipal.addReservationToHistorique(reservation);
        clientRepository.save(clientPrincipal);

        return reservation;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${base-uri}/hotels")
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @PutMapping("${base-uri}/hotels/{id}")
    public Hotel updateHotel(@RequestBody Hotel newHotel, @PathVariable long id) {
        return hotelRepository.findById(id).map(hotel -> {
            hotel.setNom(newHotel.getNom());
            hotel.setAdresse(newHotel.getAdresse());
            hotel.setNombreEtoiles(newHotel.getNombreEtoiles());
            hotel.setImageHotel(newHotel.getImageHotel());
            hotel.setChambres(newHotel.getChambres());
            hotel.setReservations(newHotel.getReservations());
            return hotelRepository.save(hotel);
        }).orElseGet(() -> hotelRepository.save(newHotel));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${base-uri}/hotels/{id}")
    public void deteleHotel(@PathVariable long id) throws HotelNotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException("Hotel not found with id " + id));
        hotelRepository.delete(hotel);
    }

}
