package m1.archi.resthotel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import m1.archi.resthotel.exceptions.DateNonValideException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private long idReservation;
    @ManyToOne
    private Hotel hotel;
    @ManyToMany
    private List<Chambre> chambresReservees;
    @ManyToOne
    private Client clientPrincipal;
    private LocalDateTime dateArrivee;
    private LocalDateTime dateDepart;
    private int nombrePersonnes;
    private double montantReservation;
    private boolean petitDejeuner;

    public Reservation() {
    }

    public Reservation(Hotel hotel, List<Chambre> chambresReservees, Client clientPrincipal,
                       LocalDateTime dateArrivee, LocalDateTime dateDepart, int nombrePersonnes, boolean petitDejeuner) throws DateNonValideException {
        if (dateArrivee.isAfter(dateDepart)) {
            throw new DateNonValideException("La date d'arrivée doit être avant la date de départ");
        }
        this.hotel = hotel;
        this.chambresReservees = chambresReservees;
        this.clientPrincipal = clientPrincipal;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombrePersonnes = nombrePersonnes;
        double montantReservation = 0;

        long daysDifference = ChronoUnit.DAYS.between(dateArrivee, dateDepart);

        for (Chambre chambre : chambresReservees) {
            montantReservation += chambre.getPrix() * daysDifference;
        }
        if (petitDejeuner) {
            montantReservation += ((long) hotel.getNombreEtoiles() * (new Random().nextInt(3) + 5)) * nombrePersonnes
                    * daysDifference;
        }
        this.montantReservation = montantReservation;
        this.petitDejeuner = petitDejeuner;
    }

    // #region Getters and Setters
    public long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(long idReservation) {
        this.idReservation = idReservation;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Chambre> getChambresReservees() {
        return chambresReservees;
    }

    public void setChambresReservees(List<Chambre> chambresReservees) {
        this.chambresReservees = chambresReservees;
    }

    public Client getClientPrincipal() {
        return clientPrincipal;
    }

    public void setClientPrincipal(Client clientPrincipal) {
        this.clientPrincipal = clientPrincipal;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public double getMontantReservation() {
        return montantReservation;
    }

    public void setMontantReservation(double montantReservation) {
        this.montantReservation = montantReservation;
    }

    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    // #endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return getIdReservation() == that.getIdReservation() && getNombrePersonnes() == that.getNombrePersonnes() && Double.compare(getMontantReservation(), that.getMontantReservation()) == 0 && isPetitDejeuner() == that.isPetitDejeuner() && Objects.equals(getHotel(), that.getHotel()) && Objects.equals(getChambresReservees(), that.getChambresReservees()) && Objects.equals(getClientPrincipal(), that.getClientPrincipal()) && Objects.equals(getDateArrivee(), that.getDateArrivee()) && Objects.equals(getDateDepart(), that.getDateDepart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdReservation(), getHotel(), getChambresReservees(), getClientPrincipal(), getDateArrivee(), getDateDepart(), getNombrePersonnes(), getMontantReservation(), isPetitDejeuner());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Reservation " + idReservation + " : " + hotel.getNom() + " (" + hotel.getAdresse().getVille() + ")\n");
        res.append("Chambres réservées : ");
        for (Chambre chambre : chambresReservees) {
            res.append(chambre.getNumero()).append(" ");
        }
        res.append("\n");
        res.append("Client principal : ").append(clientPrincipal.getNom()).append(" ").append(clientPrincipal.getPrenom()).append("\n");
        res.append("Du ").append(dateArrivee).append(" au ").append(dateDepart).append("\n");
        res.append("Nombre de personnes : ").append(nombrePersonnes).append("\n");
        res.append("Petit déjeuner : ").append(petitDejeuner).append("\n");
        res.append("Montant de la réservation : ").append(montantReservation).append("€\n");
        return res.toString();
    }

}
