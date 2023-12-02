package m1.archi.restcomparateur.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Comparateur {
    @Id
    @GeneratedValue
    private long idComparateur;
    private String nom;
    @ElementCollection
    private List<Long> idAgences;

    public Comparateur() {
    }

    public Comparateur(String nom) {
        this.nom = nom;
        this.idAgences = new ArrayList<>();
    }

    public long getIdComparateur() {
        return idComparateur;
    }

    public void setIdComparateur(long idComparateur) {
        this.idComparateur = idComparateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Long> getIdAgences() {
        return idAgences;
    }

    public void setIdAgences(List<Long> idAgences) {
        this.idAgences = idAgences;
    }
    
    public void addIdAgence(Long idAgence) {
        this.idAgences.add(idAgence);
    }
    
    public void removeIdAgence(Long idAgence) {
        this.idAgences.remove(idAgence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comparateur that = (Comparateur) o;
        return getIdComparateur() == that.getIdComparateur() && Objects.equals(getNom(), that.getNom()) && Objects.equals(getIdAgences(), that.getIdAgences());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdComparateur(), getNom(), getIdAgences());
    }

    @Override
    public String toString() {
        return "Comparateur{" +
                "idComparateur=" + idComparateur +
                ", nom='" + nom + '\'' +
                ", idAgences=" + idAgences +
                '}';
    }
}
