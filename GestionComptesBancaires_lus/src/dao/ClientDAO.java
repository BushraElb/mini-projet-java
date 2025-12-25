package dao;

import modele.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClientDAO {

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

