package m1.archi.resthotel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Chambre {
    @Id
    @GeneratedValue
    private long idChambre;
    private int numero;
    private double prix;
    private int nombreLits;
    @ManyToOne
    private Hotel hotel;
    //@JsonIgnore
    @Column(length = 10000000)
    private String imageChambre;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Chambre() {
    }

    public Chambre(int numero, double prix, int nombreLits, Hotel hotel, String imageChambre) {
        this.numero = numero;
        this.prix = prix;
        this.nombreLits = nombreLits;
        this.hotel = hotel;
        this.imageChambre = imageChambre;
    }

    public long getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(long idChambre) {
        this.idChambre = idChambre;
    }

    public int getNumero() {
        return this.numero;
    }

    public double getPrix() {
        return this.prix;
    }

    public int getNombreLits() {
        return this.nombreLits;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setNombreLits(int nombreLits) {
        this.nombreLits = nombreLits;
    }

    public String getImageChambre() {
        return imageChambre;
    }

    public void setImageChambre(String imageChambre) {
        this.imageChambre = imageChambre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chambre chambre = (Chambre) o;
        return getIdChambre() == chambre.getIdChambre() && getNumero() == chambre.getNumero() && Double.compare(getPrix(), chambre.getPrix()) == 0 && getNombreLits() == chambre.getNombreLits();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdChambre(), getNumero(), getPrix(), getNombreLits());
    }

    @Override
    public String toString() {
        return "Chambre " + this.numero + " (" + this.nombreLits + " lits), coute " + this.prix + " euros";
    }
}
