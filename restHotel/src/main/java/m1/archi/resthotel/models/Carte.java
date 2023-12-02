package m1.archi.resthotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Carte {
    @Id
    @GeneratedValue
    private long idCarte;
    private String nom;
    private String numero;
    private String dateExpiration;
    private String CCV;

    public Carte() {
    }

    public Carte(String nom, String numero, String dateExpiration, String CCV) {
        this.nom = nom;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.CCV = CCV;
    }

    public long getIdCarte() {
        return idCarte;
    }

    public void setIdCarte(long idCarte) {
        this.idCarte = idCarte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getCCV() {
        return CCV;
    }

    public void setCCV(String cCV) {
        CCV = cCV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return getIdCarte() == carte.getIdCarte() && Objects.equals(getNom(), carte.getNom()) && Objects.equals(getNumero(), carte.getNumero()) && Objects.equals(getDateExpiration(), carte.getDateExpiration()) && Objects.equals(getCCV(), carte.getCCV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCarte(), getNom(), getNumero(), getDateExpiration(), getCCV());
    }

    @Override
    public String toString() {
        return "Carte{" +
                "idCarte=" + idCarte +
                ", nom='" + nom + '\'' +
                ", numero='" + numero + '\'' +
                ", dateExpiration='" + dateExpiration + '\'' +
                ", CCV='" + CCV + '\'' +
                '}';
    }
}
