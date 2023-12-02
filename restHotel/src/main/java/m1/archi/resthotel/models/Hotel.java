package m1.archi.resthotel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import m1.archi.resthotel.exceptions.DateNonValideException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Hotel {
    @Id
    @GeneratedValue
    private long idHotel;
    private String nom;
    @OneToOne
    private Adresse adresse;
    private int nombreEtoiles;
    @JsonIgnore
    @Column(length = 10000000)
    private String imageHotel;
    @OneToMany
    @JsonIgnore
    private List<Chambre> chambres;
    @OneToMany
    @JsonIgnore
    private List<Reservation> reservations;
    @OneToMany
    @JsonIgnore
    private List<Offre> offres;

    public Hotel() {
    }

    public Hotel(String nom, Adresse adresse, int nombreEtoiles, String imageHotel) {
        this.nom = nom;
        this.adresse = adresse;
        this.nombreEtoiles = nombreEtoiles;
        this.imageHotel = imageHotel;
        this.chambres = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.offres = new ArrayList<>();
    }

    public long getIdHotel() {
        return idHotel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public int getNombreEtoiles() {
        return nombreEtoiles;
    }

    public void setNombreEtoiles(int nombreEtoiles) {
        this.nombreEtoiles = nombreEtoiles;
    }

    public String getImageHotel() {
        return imageHotel;
    }

    public void setImageHotel(String imageHotel) {
        this.imageHotel = imageHotel;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }

    public Chambre getChambre(int numero) {
        for (Chambre chambre : this.chambres) {
            if (chambre.getNumero() == numero) {
                return chambre;
            }
        }
        return null;
    }

    public void addChambre(Chambre chambre) {
        this.chambres.add(chambre);
    }

    public void removeChambre(Chambre chambre) {
        this.chambres.remove(chambre);
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    public void addOffre(Offre offre) {
        this.offres.add(offre);
    }

    public void removeOffre(Offre offre) {
        this.offres.remove(offre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return getNombreEtoiles() == hotel.getNombreEtoiles() && Objects.equals(getIdHotel(), hotel.getIdHotel()) && Objects.equals(getNom(), hotel.getNom()) && Objects.equals(getAdresse(), hotel.getAdresse()) && Objects.equals(getImageHotel(), hotel.getImageHotel()) && Objects.equals(getChambres(), hotel.getChambres()) && Objects.equals(getReservations(), hotel.getReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdHotel(), getNom(), getAdresse(), getNombreEtoiles(), getImageHotel(), getChambres(), getReservations());
    }

    // Fonction récursive pour trouver des combinaisons de chambres
    private void chercherCombinaison(ArrayList<Chambre> chambresDisponibles, int personnesRestantes,
                                     ArrayList<Chambre> combinaisonActuelle,
                                     Set<List<Integer>> combinaisonsDeLits, List<ArrayList<Chambre>> listeCombinaisonsChambres) {
        if (personnesRestantes == 0) {
            if (!combinaisonActuelle.isEmpty()) {
                combinaisonActuelle.sort(Comparator.comparing(Chambre::getNombreLits));
                // Générer une liste du nombre de lits dans la combinaison actuelle
                List<Integer> lits = combinaisonActuelle.stream()
                        .map(Chambre::getNombreLits)
                        .collect(Collectors.toList());

                if (!combinaisonsDeLits.contains(lits)) {
                    combinaisonsDeLits.add(lits);
                    listeCombinaisonsChambres.add(new ArrayList<>(combinaisonActuelle));
                }
            }
        } else if (personnesRestantes > 0) {
            for (Chambre chambre : chambresDisponibles) {
                if (chambre.getNombreLits() <= personnesRestantes) {
                    if (!combinaisonActuelle.contains(chambre)) {
                        combinaisonActuelle.add(chambre);
                        chercherCombinaison(chambresDisponibles, personnesRestantes - chambre.getNombreLits(),
                                combinaisonActuelle, combinaisonsDeLits, listeCombinaisonsChambres);
                        combinaisonActuelle.remove(chambre);
                    }
                }
            }
        }
    }

    // functions
    public ArrayList<Offre> rechercheChambres(String ville, LocalDateTime dateArrivee,
                                              LocalDateTime dateDepart,
                                              int prixMin,
                                              int prixMax, int nombreEtoiles, int nombrePersonne) throws DateNonValideException {

        if (dateArrivee.isAfter(dateDepart)) {
            throw new DateNonValideException("La date d'arrivée doit être avant la date de départ");
        }

        ArrayList<Offre> offres = new ArrayList<>();
        if (this.adresse.getVille().equals(ville) && this.nombreEtoiles >= nombreEtoiles) {

            ArrayList<Chambre> chambresDisponibles = new ArrayList<>();

            // On ajoute toute les chambre qui correspondent aux critères
            for (Chambre chambre : this.chambres) {
                if (chambre.getPrix() >= prixMin && chambre.getPrix() <= prixMax
                        && chambre.getNombreLits() <= nombrePersonne) {
                    chambresDisponibles.add(chambre);
                }
            }

            // On supprime les chambre déja reservées
            List<Chambre> chambresARetirer = new ArrayList<>();

            for (Reservation reservation : this.reservations) {
                if (reservation.getDateArrivee().isBefore(dateDepart)
                        && reservation.getDateDepart().isAfter(dateArrivee)) {
                    chambresARetirer.addAll(reservation.getChambresReservees());
                }
            }
            chambresDisponibles.removeIf(chambre -> chambresARetirer.stream().anyMatch(ch -> ch.getNumero() == chambre.getNumero()));

            if (!chambresDisponibles.isEmpty()) {
                // Vérifier si il y a une chambre avec le nombre de lits correspondant
                for (Chambre chambre : chambresDisponibles) {
                    if (chambre.getNombreLits() == nombrePersonne) {
                        Offre offre = new Offre(chambre.getNombreLits(), chambre.getPrix(), dateArrivee, dateDepart, new ArrayList<>(Collections.singletonList(chambre)), this);
                        offres.add(offre);
                        return offres;
                    }
                }

                // Vérifier s'il existe une combinaison de chambres qui correspond au nombre de
                // personnes
                ArrayList<ArrayList<Chambre>> listeCombinaisonsChambres = new ArrayList<>();
                Set<List<Integer>> combinaisonsDeLits = new HashSet<>();

                chercherCombinaison(chambresDisponibles, nombrePersonne, new ArrayList<>(),
                        combinaisonsDeLits, listeCombinaisonsChambres);

                for (ArrayList<Chambre> combinaisonChambresDisponibles : listeCombinaisonsChambres) {
                    Offre offre = new Offre(nombrePersonne, combinaisonChambresDisponibles.stream().mapToDouble(Chambre::getPrix).sum(), dateArrivee, dateDepart, combinaisonChambresDisponibles, this);
                    offres.add(offre);
                }

                return offres;
            }
        }
        return offres;
    }

    @Override
    public String toString() {
        return "L'hotel '" + this.nom + "' (" + this.nombreEtoiles
                + " étoiles) situé au " + this.adresse + ", possède " + this.chambres.size() + " chambres.";
    }

}