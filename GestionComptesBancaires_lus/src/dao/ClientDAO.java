package dao;

import modele.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur la table Client
 */
public class ClientDAO {
    
    /**
     * Ajoute un nouveau client dans la base de données
     * @param client Le client à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean ajouter(Client client) {
        String sql = "INSERT INTO client (nom, prenom, telephone) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Met à jour un client existant
     * @param client Le client à modifier
     * @return true si la modification est réussie, false sinon
     */
    public boolean modifier(Client client) {
        String sql = "UPDATE client SET nom = ?, prenom = ?, telephone = ? WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setInt(4, client.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Supprime un client par son ID
     * @param id L'ID du client à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère un client par son ID
     * @param id L'ID du client
     * @return Le client trouvé ou null
     */
    public Client getById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setTelephone(rs.getString("telephone"));
                return client;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère tous les clients
     * @return Liste de tous les clients
     */
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY id";
        
        try (Connection conn = ConnexionBD.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setTelephone(rs.getString("telephone"));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
        return clients;
    }
}

