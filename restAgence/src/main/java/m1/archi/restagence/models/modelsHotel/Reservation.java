package m1.archi.restagence.models.modelsHotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {
    private long idReservation;
    private Hotel hotel;
    private List<Chambre> chambresReservees;
    private int nombrePersonnes;
    private double montantReservation;
    private boolean petitDejeuner;

    public Reservation() {
    }

    public Reservation(long idReservation, Hotel hotel, List<Chambre> chambresReservees, int nombrePersonnes, double montantReservation, boolean petitDejeuner) {
        this.idReservation = idReservation;
        this.hotel = hotel;
        this.chambresReservees = chambresReservees;
        this.nombrePersonnes = nombrePersonnes;
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
        return getIdReservation() == that.getIdReservation() && getNombrePersonnes() == that.getNombrePersonnes() && Double.compare(getMontantReservation(), that.getMontantReservation()) == 0 && isPetitDejeuner() == that.isPetitDejeuner() && Objects.equals(getHotel(), that.getHotel()) && Objects.equals(getChambresReservees(), that.getChambresReservees());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdReservation(), getHotel(), getChambresReservees(), getNombrePersonnes(), getMontantReservation(), isPetitDejeuner());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", hotel=" + hotel +
                ", chambresReservees=" + chambresReservees +
                ", nombrePersonnes=" + nombrePersonnes +
                ", montantReservation=" + montantReservation +
                ", petitDejeuner=" + petitDejeuner +
                '}';
    }
}
