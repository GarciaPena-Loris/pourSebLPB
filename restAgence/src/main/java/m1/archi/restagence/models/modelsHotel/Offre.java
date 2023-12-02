package m1.archi.restagence.models.modelsHotel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class Offre {
    private long idOffre;
    private int nombreLitsTotal;
    private double prix;
    private double prixAvecReduction;
    private LocalDateTime dateArrivee;
    private LocalDateTime dateDepart;
    private LocalDateTime dateExpiration;
    private List<Chambre> chambres;
    private Hotel hotel;

    public Offre() {
    }

    public Offre(int nombreLitsTotal, double prix, LocalDateTime dateArrivee, LocalDateTime dateDepart, List<Chambre> chambres, Hotel hotel) {
        this.nombreLitsTotal = nombreLitsTotal;
        this.prix = prix;
        this.prixAvecReduction = prix;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.chambres = chambres;
        this.hotel = hotel;
        this.dateExpiration = LocalDateTime.now().plusHours(1);
    }

    public long getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(long idOffre) {
        this.idOffre = idOffre;
    }

    public int getNombreLitsTotal() {
        return nombreLitsTotal;
    }

    public void setNombreLitsTotal(int nombreLitsTotal) {
        this.nombreLitsTotal = nombreLitsTotal;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getPrixAvecReduction() {
        return prixAvecReduction;
    }

    public void setPrixAvecReduction(double prixAvecReduction) {
        this.prixAvecReduction = prixAvecReduction;
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

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offre offre = (Offre) o;
        return getIdOffre() == offre.getIdOffre() && getNombreLitsTotal() == offre.getNombreLitsTotal() && Double.compare(getPrix(), offre.getPrix()) == 0 && Double.compare(getPrixAvecReduction(), offre.getPrixAvecReduction()) == 0 && Objects.equals(getDateArrivee(), offre.getDateArrivee()) && Objects.equals(getDateDepart(), offre.getDateDepart()) && Objects.equals(getDateExpiration(), offre.getDateExpiration()) && Objects.equals(getChambres(), offre.getChambres()) && Objects.equals(getHotel(), offre.getHotel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdOffre(), getNombreLitsTotal(), getPrix(), getPrixAvecReduction(), getDateArrivee(), getDateDepart(), getDateExpiration(), getChambres(), getHotel());
    }

    @Override
    public String toString() {
        return "Offre{" +
                "idOffre=" + idOffre +
                ", nombreLitsTotal=" + nombreLitsTotal +
                ", prix=" + prix +
                ", prixAvecReduction=" + prixAvecReduction +
                ", dateArrivee=" + dateArrivee +
                ", dateDepart=" + dateDepart +
                ", dateExpiration=" + dateExpiration +
                ", chambres=" + chambres +
                '}';
    }
}
