package dao;

import modele.Transfert;
import modele.CompteBancaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations sur la table Transfert
 */
public class TransfertDAO {
    
    /**
     * Enregistre un transfert dans la base de données
     * @param transfert Le transfert à enregistrer
     * @return true si l'enregistrement est réussi, false sinon
     */
    public boolean ajouter(Transfert transfert) {
        String sql = "INSERT INTO transfert (id_compte_source, id_compte_destination, montant, " +
                     "date_transfert, solde_source_avant, solde_source_apres, " +
                     "solde_dest_avant, solde_dest_apres) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setLong(1, transfert.getCompteSource().getId());
            ps.setLong(2, transfert.getCompteDestination().getId());
            ps.setDouble(3, transfert.getMontant());
            ps.setTimestamp(4, new Timestamp(transfert.getDateTransfert().getTime()));
            ps.setDouble(5, transfert.getSoldeSourceAvant());
            ps.setDouble(6, transfert.getSoldeSourceApres());
            ps.setDouble(7, transfert.getSoldeDestAvant());
            ps.setDouble(8, transfert.getSoldeDestApres());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    transfert.setId(rs.getLong(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du transfert : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère tous les transferts
     * @return Liste de tous les transferts
     */
    public List<Transfert> getAll() {
        List<Transfert> transferts = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "cs.id as cs_id, cs.rib as cs_rib, cs.solde as cs_solde, " +
                     "cd.id as cd_id, cd.rib as cd_rib, cd.solde as cd_solde " +
                     "FROM transfert t " +
                     "INNER JOIN compte_bancaire cs ON t.id_compte_source = cs.id " +
                     "INNER JOIN compte_bancaire cd ON t.id_compte_destination = cd.id " +
                     "ORDER BY t.date_transfert DESC";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Transfert transfert = mapResultSetToTransfert(rs);
                transferts.add(transfert);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des transferts : " + e.getMessage());
        }
        return transferts;
    }
    
    /**
     * Récupère les transferts d'un compte spécifique (en tant que source ou destination)
     * @param idCompte L'ID du compte
     * @return Liste des transferts du compte
     */
    public List<Transfert> getByCompteId(long idCompte) {
        List<Transfert> transferts = new ArrayList<>();
        String sql = "SELECT t.*, " +
                     "cs.id as cs_id, cs.rib as cs_rib, cs.solde as cs_solde, " +
                     "cd.id as cd_id, cd.rib as cd_rib, cd.solde as cd_solde " +
                     "FROM transfert t " +
                     "INNER JOIN compte_bancaire cs ON t.id_compte_source = cs.id " +
                     "INNER JOIN compte_bancaire cd ON t.id_compte_destination = cd.id " +
                     "WHERE t.id_compte_source = ? OR t.id_compte_destination = ? " +
                     "ORDER BY t.date_transfert DESC";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, idCompte);
            ps.setLong(2, idCompte);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Transfert transfert = mapResultSetToTransfert(rs);
                transferts.add(transfert);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des transferts : " + e.getMessage());
        }
        return transferts;
    }
    
    /**
     * Mappe un ResultSet vers un objet Transfert
     * @param rs Le ResultSet
     * @return Un objet Transfert
     * @throws SQLException en cas d'erreur SQL
     */
    private Transfert mapResultSetToTransfert(ResultSet rs) throws SQLException {
        Transfert transfert = new Transfert();
        transfert.setId(rs.getLong("id"));
        transfert.setMontant(rs.getDouble("montant"));
        transfert.setDateTransfert(rs.getTimestamp("date_transfert"));
        transfert.setSoldeSourceAvant(rs.getDouble("solde_source_avant"));
        transfert.setSoldeSourceApres(rs.getDouble("solde_source_apres"));
        transfert.setSoldeDestAvant(rs.getDouble("solde_dest_avant"));
        transfert.setSoldeDestApres(rs.getDouble("solde_dest_apres"));
        
        // Créer le compte source
        CompteBancaire compteSource = new CompteBancaire();
        compteSource.setId(rs.getLong("cs_id"));
        compteSource.setRIB(rs.getString("cs_rib"));
        compteSource.setSolde(rs.getDouble("cs_solde"));
        transfert.setCompteSource(compteSource);
        
        // Créer le compte destination
        CompteBancaire compteDest = new CompteBancaire();
        compteDest.setId(rs.getLong("cd_id"));
        compteDest.setRIB(rs.getString("cd_rib"));
        compteDest.setSolde(rs.getDouble("cd_solde"));
        transfert.setCompteDestination(compteDest);
        
        return transfert;
    }
}

