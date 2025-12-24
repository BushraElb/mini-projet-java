package dao;

import modele.CompteBancaire;
import modele.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur la table CompteBancaire
 */
public class CompteBancaireDAO {
    
    /**
     * Ajoute un nouveau compte bancaire dans la base de données
     * @param compte Le compte à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean ajouter(CompteBancaire compte) {
        String sql = "INSERT INTO compte_bancaire (rib, solde, datecreation, actif, id_client) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, compte.getRIB());
            ps.setDouble(2, compte.getSolde());
            ps.setDate(3, new Date(compte.getDatecreation().getTime()));
            ps.setBoolean(4, compte.isActif());
            ps.setInt(5, compte.getClient().getId());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    compte.setId(rs.getLong(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du compte bancaire : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Met à jour l'état (actif/inactif) d'un compte bancaire
     * @param id L'ID du compte
     * @param actif L'état du compte
     * @return true si la modification est réussie, false sinon
     */
    public boolean modifierEtat(long id, boolean actif) {
        String sql = "UPDATE compte_bancaire SET actif = ? WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, actif);
            ps.setLong(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du compte bancaire : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Met à jour le solde d'un compte bancaire
     * @param id L'ID du compte
     * @param nouveauSolde Le nouveau solde
     * @return true si la modification est réussie, false sinon
     */
    public boolean modifierSolde(long id, double nouveauSolde) {
        String sql = "UPDATE compte_bancaire SET solde = ? WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, nouveauSolde);
            ps.setLong(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du solde : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Supprime un compte bancaire par son ID
     * @param id L'ID du compte à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    public boolean supprimer(long id) {
        String sql = "DELETE FROM compte_bancaire WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du compte bancaire : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un compte bancaire par son ID
     * @param id L'ID du compte
     * @return Le compte trouvé ou null
     */
    public CompteBancaire getById(long id) {
        String sql = "SELECT cb.*, c.id as client_id, c.nom, c.prenom, c.telephone " +
                     "FROM compte_bancaire cb " +
                     "INNER JOIN client c ON cb.id_client = c.id " +
                     "WHERE cb.id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCompte(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du compte bancaire : " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère un compte bancaire par l'ID du client et le RIB
     * @param idClient L'ID du client
     * @param rib Le RIB du compte
     * @return Le compte trouvé ou null
     */
    public CompteBancaire getByClientIdAndRIB(int idClient, String rib) {
        String sql = "SELECT cb.*, c.id as client_id, c.nom, c.prenom, c.telephone " +
                     "FROM compte_bancaire cb " +
                     "INNER JOIN client c ON cb.id_client = c.id " +
                     "WHERE cb.id_client = ? AND cb.rib = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idClient);
            ps.setString(2, rib);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCompte(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du compte bancaire : " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les comptes bancaires d'un client
     * @param idClient L'ID du client
     * @return Liste des comptes du client
     */
    public List<CompteBancaire> getByClientId(int idClient) {
        List<CompteBancaire> comptes = new ArrayList<>();
        String sql = "SELECT cb.*, c.id as client_id, c.nom, c.prenom, c.telephone " +
                     "FROM compte_bancaire cb " +
                     "INNER JOIN client c ON cb.id_client = c.id " +
                     "WHERE cb.id_client = ? ORDER BY cb.id";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des comptes bancaires : " + e.getMessage());
        }
        return comptes;
    }
    
    /**
     * Récupère tous les comptes bancaires
     * @return Liste de tous les comptes
     */
    public List<CompteBancaire> getAll() {
        List<CompteBancaire> comptes = new ArrayList<>();
        String sql = "SELECT cb.*, c.id as client_id, c.nom, c.prenom, c.telephone " +
                     "FROM compte_bancaire cb " +
                     "INNER JOIN client c ON cb.id_client = c.id " +
                     "ORDER BY cb.id";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des comptes bancaires : " + e.getMessage());
        }
        return comptes;
    }
    
    /**
     * Mappe un ResultSet vers un objet CompteBancaire
     * @param rs Le ResultSet
     * @return Un objet CompteBancaire
     * @throws SQLException en cas d'erreur SQL
     */
    private CompteBancaire mapResultSetToCompte(ResultSet rs) throws SQLException {
        CompteBancaire compte = new CompteBancaire();
        compte.setId(rs.getLong("id"));
        compte.setRIB(rs.getString("rib"));
        compte.setSolde(rs.getDouble("solde"));
        compte.setDatecreation(rs.getDate("datecreation"));
        compte.setActif(rs.getBoolean("actif"));
        
        // Créer et associer le client
        Client client = new Client();
        client.setId(rs.getInt("client_id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setTelephone(rs.getString("telephone"));
        compte.setClient(client);
        
        return compte;
    }
}

