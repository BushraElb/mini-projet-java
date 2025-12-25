package modele;

import java.util.Date;

/**
 * Modèle représentant un transfert entre deux comptes bancaires
 */
public class Transfert {
    
    private long id;
    private CompteBancaire compteSource;
    private CompteBancaire compteDestination;
    private double montant;
    private Date dateTransfert;
    private double soldeSourceAvant;
    private double soldeSourceApres;
    private double soldeDestAvant;
    private double soldeDestApres;
    
    public Transfert() {
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public CompteBancaire getCompteSource() {
        return compteSource;
    }
    
    public void setCompteSource(CompteBancaire compteSource) {
        this.compteSource = compteSource;
    }
    
    public CompteBancaire getCompteDestination() {
        return compteDestination;
    }
    
    public void setCompteDestination(CompteBancaire compteDestination) {
        this.compteDestination = compteDestination;
    }
    
    public double getMontant() {
        return montant;
    }
    
    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    public Date getDateTransfert() {
        return dateTransfert;
    }
    
    public void setDateTransfert(Date dateTransfert) {
        this.dateTransfert = dateTransfert;
    }
    
    public double getSoldeSourceAvant() {
        return soldeSourceAvant;
    }
    
    public void setSoldeSourceAvant(double soldeSourceAvant) {
        this.soldeSourceAvant = soldeSourceAvant;
    }
    
    public double getSoldeSourceApres() {
        return soldeSourceApres;
    }
    
    public void setSoldeSourceApres(double soldeSourceApres) {
        this.soldeSourceApres = soldeSourceApres;
    }
    
    public double getSoldeDestAvant() {
        return soldeDestAvant;
    }
    
    public void setSoldeDestAvant(double soldeDestAvant) {
        this.soldeDestAvant = soldeDestAvant;
    }
    
    public double getSoldeDestApres() {
        return soldeDestApres;
    }
    
    public void setSoldeDestApres(double soldeDestApres) {
        this.soldeDestApres = soldeDestApres;
    }
    
    @Override
    public String toString() {
        return "Transfert{" +
                "id=" + id +
                ", montant=" + montant +
                ", dateTransfert=" + dateTransfert +
                '}';
    }
}

