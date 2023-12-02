package m1.archi.restclient.models.modelsAgence;

import java.util.Objects;


public class ReductionHotel {

    private long idReduction;
    private long idHotel;
    private int reduction;
    private Agence agence;

    public ReductionHotel() {
    }

    public ReductionHotel(long idHotel, int reduction, Agence agence) {
        this.idHotel = idHotel;
        this.reduction = reduction;
        this.agence = agence;
    }

    public long getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(long idHotel) {
        this.idHotel = idHotel;
    }

    public long getIdReduction() {
        return idReduction;
    }

    public void setIdReduction(long idReduction) {
        this.idReduction = idReduction;
    }

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
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
        ReductionHotel that = (ReductionHotel) o;
        return getIdReduction() == that.getIdReduction() && getIdHotel() == that.getIdHotel() && getReduction() == that.getReduction() && Objects.equals(getAgence(), that.getAgence());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdReduction(), getIdHotel(), getReduction(), getAgence());
    }

    @Override
    public String toString() {
        return "ReductionHotel{" +
                "idReduction=" + idReduction +
                ", idHotel=" + idHotel +
                ", reduction=" + reduction +
                ", agence=" + agence +
                '}';
    }
}
