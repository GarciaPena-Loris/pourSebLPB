package m1.archi.restclient.models.modelsHotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hotel {

    private long idHotel;
    private String nom;
    private Adresse adresse;
    private int nombreEtoiles;
    private String imageHotel;
    private List<Chambre> chambres;
    private List<Reservation> reservations;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return getNombreEtoiles() == hotel.getNombreEtoiles() && Objects.equals(getIdHotel(), hotel.getIdHotel()) && Objects.equals(getNom(), hotel.getNom()) && Objects.equals(getAdresse(), hotel.getAdresse()) && Objects.equals(getImageHotel(), hotel.getImageHotel()) && Objects.equals(getChambres(), hotel.getChambres()) && Objects.equals(getReservations(), hotel.getReservations());
    }


    @Override
    public String toString() {
        return "Hotel{" +
                "idHotel=" + idHotel +
                ", nom='" + nom + '\'' +
                ", adresse=" + adresse +
                ", nombreEtoiles=" + nombreEtoiles +
                ", imageHotel='" + imageHotel + '\'' +
                ", chambres=" + chambres +
                ", reservations=" + reservations +
                ", offres=" + offres +
                '}';
    }

    public void setIdHotel(long idHotel) {
        this.idHotel = idHotel;
    }
}