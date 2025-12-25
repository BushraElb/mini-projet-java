package metier;

import dao.ClientDAO;
import dao.CompteBancaireDAO;
import modele.Client;
import modele.CompteBancaire;

import java.util.List;

/**
 * Implémentation de l'interface ITraitement
 * Gère la logique métier pour les clients et les comptes bancaires
 */
public class TraitementImpl implements ITraitement {
    
    private ClientDAO clientDAO;
    private CompteBancaireDAO compteBancaireDAO;
    
    public TraitementImpl() {
        this.clientDAO = new ClientDAO();
        this.compteBancaireDAO = new CompteBancaireDAO();
    }
    
    // ========== Opérations sur les clients ==========
    
    @Override
    public void ajouterClient(Client client) {
        if (client != null && client.getNom() != null && !client.getNom().trim().isEmpty()) {
            clientDAO.ajouter(client);
        }
    }
    
    @Override
    public void modifierClient(Client client) {
        if (client != null && client.getId() > 0) {
            clientDAO.modifier(client);
        }
    }
    
    @Override
    public void supprimerClient(int id) {
        if (id > 0) {
            clientDAO.supprimer(id);
        }
    }
    
    @Override
    public Client getClient(int id) {
        if (id > 0) {
            return clientDAO.getById(id);
        }
        return null;
    }
    
    @Override
    public List<Client> getClients() {
        return clientDAO.getAll();
    }
    
    // ========== Opérations sur les comptes bancaires ==========
    
    @Override
    public void ajouterCompte(CompteBancaire compte) {
        if (compte != null && compte.getRIB() != null && !compte.getRIB().trim().isEmpty() 
            && compte.getClient() != null && compte.getClient().getId() > 0) {
            compteBancaireDAO.ajouter(compte);
        }
    }
    
    @Override
    public void modifierCompte(long id, boolean etat) {
        if (id > 0) {
            compteBancaireDAO.modifierEtat(id, etat);
        }
    }
    
    @Override
    public boolean transfert(CompteBancaire compteSource, CompteBancaire compteDest, double somme) {
        if (compteSource == null || compteDest == null || somme <= 0) {
            return false;
        }
        
        // Vérifier que le compte source est actif et a suffisamment de solde
        if (!compteSource.isActif() || compteSource.getSolde() < somme) {
            return false;
        }
        
        // Vérifier que le compte destination est actif
        if (!compteDest.isActif()) {
            return false;
        }
        
        // Effectuer le transfert
        double nouveauSoldeSource = compteSource.getSolde() - somme;
        double nouveauSoldeDest = compteDest.getSolde() + somme;
        
        boolean success1 = compteBancaireDAO.modifierSolde(compteSource.getId(), nouveauSoldeSource);
        boolean success2 = compteBancaireDAO.modifierSolde(compteDest.getId(), nouveauSoldeDest);
        
        if (success1 && success2) {
            // Mettre à jour les objets en mémoire
            compteSource.setSolde(nouveauSoldeSource);
            compteDest.setSolde(nouveauSoldeDest);
            return true;
        }
        
        return false;
    }
    
    @Override
    public CompteBancaire getCompte(int id_client, String rib) {
        if (id_client > 0 && rib != null && !rib.trim().isEmpty()) {
            return compteBancaireDAO.getByClientIdAndRIB(id_client, rib);
        }
        return null;
    }
    
    @Override
    public List<CompteBancaire> getComptes(int id_client) {
        if (id_client > 0) {
            return compteBancaireDAO.getByClientId(id_client);
        }
        return null;
    }
    
    @Override
    public List<CompteBancaire> getComptes() {
        return compteBancaireDAO.getAll();
    }
    
    /**
     * Méthode supplémentaire pour supprimer un compte bancaire
     * @param id L'ID du compte à supprimer
     * @return true si la suppression est réussie
     */
    @Override
    public boolean supprimerCompte(long id) {
        if (id > 0) {
            return compteBancaireDAO.supprimer(id);
        }
        return false;
    }
    
    /**
     * Méthode supplémentaire pour récupérer un compte par son ID
     * @param id L'ID du compte
     * @return Le compte trouvé ou null
     */
    @Override
    public CompteBancaire getCompteById(long id) {
        if (id > 0) {
            return compteBancaireDAO.getById(id);
        }
        return null;
    }
    
    /**
     * Méthode supplémentaire pour modifier le solde d'un compte
     * @param id L'ID du compte
     * @param nouveauSolde Le nouveau solde
     * @return true si la modification est réussie
     */
    @Override
    public boolean modifierSolde(long id, double nouveauSolde) {
        if (id > 0 && nouveauSolde >= 0) {
            return compteBancaireDAO.modifierSolde(id, nouveauSolde);
        }
        return false;
    }
}

