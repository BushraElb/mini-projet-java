package modele;

import java.util.Date;

public class CompteBancaire {

    private long id;
    private String RIB;
    private double solde;
    private Date datecreation;
    private boolean actif; // true : actif, false :  desactivé

    // association
    private Client client;

    public Client getClient() {
        return client;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public double getSolde() {
        return solde;
    }

    public long getId() {
        return id;
    }

    public String getRIB() {
        return RIB;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public void setRIB(String RIB) {
        this.RIB = RIB;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public String toString() {
        return "CompteBancaire{" +
                "id=" + id +
                ", RIB='" + RIB + '\'' +
                ", solde=" + solde +
                ", datecreation=" + datecreation +
                ", client=" + client +
                ", actif=" + actif +
                '}';
    }
}

