package m1.archi.restagence.models.modelsHotel;
public class Adresse {
    private long idAdresse;
    private String numero;
    private String rue;
    private String ville;
    private String pays;
    private String position;

    public Adresse() {
    }

    public Adresse(String pays, String ville, String rue, String numero, String position) {
        this.pays = pays;
        this.ville = ville;
        this.rue = rue;
        this.numero = numero;
        this.position = position;
    }

    public long getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(long idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getPays() {
        return this.pays;
    }

    public String getVille() {
        return this.ville;
    }

    public String getRue() {
        return this.rue;
    }

    public String getNumero() {
        return this.numero;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return this.numero + " " + this.rue + " à " + this.ville + " (" + this.pays + ")";
    }
}
