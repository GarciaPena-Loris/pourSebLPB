package m1.archi.restagence.models;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Agence {
    @Id
    @GeneratedValue
    private long idAgence;
    private String nom;
    @OneToMany
    private List<ReductionHotel> reductionHotels;
    @ManyToMany
    private List<Utilisateur> listeUtilisateurs;

    public Agence() {
    }

    public Agence(String nom) {
        this.nom = nom;
        this.listeUtilisateurs = new ArrayList<>();
        this.reductionHotels = new ArrayList<>();
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<ReductionHotel> getReductionHotels() {
        return reductionHotels;
    }

    public void setReductionHotels(List<ReductionHotel> reductionHotels) {
        this.reductionHotels = reductionHotels;
    }

    public void addReductionHotel(ReductionHotel reductionHotel) {
        reductionHotels.add(reductionHotel);
    }

    public List<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void setListeUtilisateurs(List<Utilisateur> listeUtilisateurs) {
        this.listeUtilisateurs = listeUtilisateurs;
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        this.listeUtilisateurs.add(utilisateur);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("L'" + this.nom + " (" + this.getIdAgence() + ") possède " + this.getReductionHotels().size() + " hotels partenaires :\n");

        int compteur = 1;
        for (ReductionHotel reductionHotel : this.getReductionHotels()) {
            res.append("\t").append(compteur).append("- L'hotel (").append(reductionHotel.getIdHotel()).append(") avec une reduction de ").append(reductionHotel.getReduction()).append("% sur les chambres.\n");
            compteur++;
        }
        return res.toString();
    }
}
