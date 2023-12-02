package m1.archi.restclient.models.modelsAgence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Utilisateur {
 
    private long idUtilisateur;
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private Agence agence;
 
    private List<Long> idReservations;

    public Utilisateur() {
    }

    public Utilisateur(String email, String motDePasse, String nom, String prenom, Agence agence) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.agence = agence;
        this.idReservations = new ArrayList<>();
    }

    public long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<Long> getIdReservations() {
        return idReservations;
    }

    public void setIdReservations(List<Long> idReservations) {
        this.idReservations = idReservations;
    }

    public boolean addReservation(long idReservation) {
        return idReservations.add(idReservation);
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur utilisateur = (Utilisateur) o;
        return getIdUtilisateur() == utilisateur.getIdUtilisateur() && Objects.equals(getEmail(), utilisateur.getEmail()) && Objects.equals(getMotDePasse(), utilisateur.getMotDePasse()) && Objects.equals(getNom(), utilisateur.getNom()) && Objects.equals(getPrenom(), utilisateur.getPrenom()) && Objects.equals(getAgence(), utilisateur.getAgence()) && Objects.equals(getIdReservations(), utilisateur.getIdReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUtilisateur(), getEmail(), getMotDePasse(), getNom(), getPrenom(), getAgence(), getIdReservations());
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", idReservations=" + idReservations +
                '}';
    }
}
