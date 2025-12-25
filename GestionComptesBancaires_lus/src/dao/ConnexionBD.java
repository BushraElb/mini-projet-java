package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {
    
    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_comptes_bancaires";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection connexion = null;

    public static Connection getConnexion() throws SQLException {
        if (connexion == null || connexion.isClosed()) {
            try {
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                connexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données réussie !");
            } catch (ClassNotFoundException e) {
                System.err.println("Driver MySQL non trouvé !");
                throw new SQLException("Driver MySQL non trouvé", e);
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
                throw e;
            }
        }
        return connexion;
    }

    public static void fermerConnexion() {
        if (connexion != null) {
            try {
                connexion.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}

