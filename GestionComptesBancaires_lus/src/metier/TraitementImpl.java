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

    // ===== CLIENT =====
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
        return id > 0 ? clientDAO.getById(id) : null;
    }

    @Override
    public List<Client> getClients() {
        return clientDAO.getAll();
    }

    // ===== COMPTE BANCAIRE =====
    @Override
    public void ajouterCompte(CompteBancaire compte) {
        if (compte != null
                && compte.getRIB() != null
                && !compte.getRIB().trim().isEmpty()
                && compte.getClient() != null
                && compte.getClient().getId() > 0) {

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
    public boolean modifierSolde(long id, double nouveauSolde) {
        if (id > 0 && nouveauSolde >= 0) {
            return compteBancaireDAO.modifierSolde(id, nouveauSolde);
        }
        return false;
    }

    @Override
    public boolean transfert(CompteBancaire source,
                             CompteBancaire dest,
                             double somme) {

        if (source == null || dest == null || somme <= 0) return false;
        if (!source.isActif() || !dest.isActif()) return false;
        if (source.getSolde() < somme) return false;

        double soldeSource = source.getSolde() - somme;
        double soldeDest = dest.getSolde() + somme;

        boolean ok1 = compteBancaireDAO.modifierSolde(source.getId(), soldeSource);
        boolean ok2 = compteBancaireDAO.modifierSolde(dest.getId(), soldeDest);

        if (ok1 && ok2) {
            source.setSolde(soldeSource);
            dest.setSolde(soldeDest);
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
        return id_client > 0 ? compteBancaireDAO.getByClientId(id_client) : null;
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
}
