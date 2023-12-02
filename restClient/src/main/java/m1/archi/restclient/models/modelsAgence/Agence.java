package m1.archi.restclient.models.modelsAgence;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Agence {
    @JsonProperty("idAgence")
    private long idAgence;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("reductionHotels")
    private List<ReductionHotel> reductionHotels;

    @JsonProperty("listeUtilisateurs")
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
        return "Agence{" +
                "idAgence=" + getIdAgence() +
                ", nom='" + getNom() + '\'' +
                ", reductionHotels=" + reductionHotels +
                ", listeUtilisateurs=" + listeUtilisateurs +
                '}';
    }
}
