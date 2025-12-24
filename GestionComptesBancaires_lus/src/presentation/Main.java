package presentation;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principale pour lancer l'application de gestion des comptes bancaires
 */
public class Main {
    
    public static void main(String[] args) {
        // Utiliser le look and feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du look and feel : " + e.getMessage());
        }
        
        // Lancer l'interface graphique dans le thread EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FenetrePrincipale fenetre = new FenetrePrincipale();
                fenetre.setVisible(true);
            }
        });
    }
}

