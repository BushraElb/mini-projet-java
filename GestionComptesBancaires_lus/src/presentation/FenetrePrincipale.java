package presentation;

import metier.ITraitement;
import metier.TraitementImpl;
import modele.CompteBancaire;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;


public class FenetrePrincipale extends JFrame {
    
    private ITraitement traitement;
    private JTable tableComptes;
    private DefaultTableModel modelTable;
    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    
    public FenetrePrincipale() {
        traitement = new TraitementImpl();
        initialiserInterface();
        chargerComptes();
    }
    

    private void initialiserInterface() {
        setTitle("Gestion des Comptes Bancaires");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panneau principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titre = new JLabel("Liste des Comptes Bancaires", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(titre, BorderLayout.NORTH);
        
        // Table des comptes
        String[] colonnes = {"ID", "RIB", "Client", "Solde", "Date Création", "État"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre la table non éditable
            }
        };
        
        tableComptes = new JTable(modelTable);
        tableComptes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComptes.setRowHeight(25);
        tableComptes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Ajuster la largeur des colonnes
        tableComptes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableComptes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableComptes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableComptes.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableComptes.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableComptes.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableComptes);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setPreferredSize(new Dimension(120, 35));
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirFormulaireAjout();
            }
        });
        
        btnModifier = new JButton("Modifier");
        btnModifier.setPreferredSize(new Dimension(120, 35));
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierCompteSelectionne();
            }
        });
        
        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setPreferredSize(new Dimension(120, 35));
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerCompteSelectionne();
            }
        });
        
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        
        panelPrincipal.add(panelBoutons, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    

    private void chargerComptes() {
        modelTable.setRowCount(0); // Vider la table
        
        List<CompteBancaire> comptes = traitement.getComptes();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (CompteBancaire compte : comptes) {
            Object[] ligne = {
                compte.getId(),
                compte.getRIB(),
                compte.getClient().getNom() + " " + compte.getClient().getPrenom(),
                String.format("%.2f", compte.getSolde()) + " DH",
                sdf.format(compte.getDatecreation()),
                compte.isActif() ? "Actif" : "Inactif"
            };
            modelTable.addRow(ligne);
        }
    }
    

    private void ouvrirFormulaireAjout() {
        FormulaireCompte formulaire = new FormulaireCompte(this, traitement, null);
        formulaire.setVisible(true);
    }
    

    private void modifierCompteSelectionne() {
        int ligneSelectionnee = tableComptes.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un compte à modifier.", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long idCompte = (Long) modelTable.getValueAt(ligneSelectionnee, 0);
        CompteBancaire compte = traitement.getCompteById(idCompte);
        
        if (compte != null) {
            FormulaireCompte formulaire = new FormulaireCompte(this, traitement, compte);
            formulaire.setVisible(true);
        }
    }
    

    private void supprimerCompteSelectionne() {
        int ligneSelectionnee = tableComptes.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un compte à supprimer.", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long idCompte = (Long) modelTable.getValueAt(ligneSelectionnee, 0);
        String rib = (String) modelTable.getValueAt(ligneSelectionnee, 1);
        
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer le compte avec le RIB : " + rib + " ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean success = traitement.supprimerCompte(idCompte);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Compte supprimé avec succès.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                chargerComptes();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression du compte.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    public void actualiserTable() {
        chargerComptes();
    }
}

