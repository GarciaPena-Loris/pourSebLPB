package m1.archi.resthotel.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;

@Entity
public class Client {
    @Id
    @GeneratedValue
    private long idClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    @OneToOne
    private Carte carte;
    @OneToMany
    private ArrayList<Reservation> historiqueReservations;

    public Client() {
    }

    public Client(String nom, String prenom, String email, String telephone, Carte carte) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.carte = carte;

        this.historiqueReservations = new ArrayList<Reservation>();
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    // Historique des réservations

    public ArrayList<Reservation> getHistoriqueReservations() {
        return historiqueReservations;
    }

    public void setHistoriqueReservations(ArrayList<Reservation> historiqueReservations) {
        this.historiqueReservations = historiqueReservations;
    }

    public void addReservationToHistorique(Reservation reservation) {
        this.historiqueReservations.add(reservation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getIdClient(), client.getIdClient()) && Objects.equals(getNom(), client.getNom()) && Objects.equals(getPrenom(), client.getPrenom()) && Objects.equals(getEmail(), client.getEmail()) && Objects.equals(getTelephone(), client.getTelephone()) && Objects.equals(getCarte(), client.getCarte()) && Objects.equals(getHistoriqueReservations(), client.getHistoriqueReservations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdClient(), getNom(), getPrenom(), getEmail(), getTelephone(), getCarte(), getHistoriqueReservations());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(this.nom + " " + this.prenom + " (" + this.email + ")\n");
        res.append("Carte ").append(this.carte.getNumero()).append("\n");
        res.append("\nHistorique des réservations : ");
        if (this.historiqueReservations.isEmpty()) {
            res.append("aucune");
        } else {
            for (Reservation reservation : this.historiqueReservations) {
                res.append(reservation.getIdReservation()).append(" ");
            }
        }
        return res.toString();
    }
}
