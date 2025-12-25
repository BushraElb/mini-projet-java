package presentation;

import metier.ITraitement;
import modele.CompteBancaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Formulaire pour effectuer un transfert entre deux comptes bancaires
 */
public class FormulaireTransfert extends JDialog {
    
    private ITraitement traitement;
    private FenetrePrincipale fenetrePrincipale;
    
    private JComboBox<CompteBancaire> comboCompteSource;
    private JComboBox<CompteBancaire> comboCompteDest;
    private JTextField txtMontant;
    private JLabel lblSoldeSource;
    private JLabel lblSoldeDest;
    private JButton btnValider;
    private JButton btnAnnuler;
    
    public FormulaireTransfert(FenetrePrincipale parent, ITraitement traitement) {
        super(parent, "Transfert entre comptes", true);
        this.fenetrePrincipale = parent;
        this.traitement = traitement;
        
        initialiserInterface();
        chargerComptes();
        
        // Écouter les changements de sélection pour mettre à jour les soldes
        comboCompteSource.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mettreAJourSoldes();
            }
        });
        
        comboCompteDest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mettreAJourSoldes();
            }
        });
    }
    
    /**
     * Initialise l'interface graphique
     */
    private void initialiserInterface() {
        setSize(550, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau des champs
        JPanel panelChamps = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Titre
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titre = new JLabel("Effectuer un Transfert");
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        panelChamps.add(titre, gbc);
        gbc.gridwidth = 1;
        
        // Séparateur
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelChamps.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        
        // Compte Source
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Compte Source * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        comboCompteSource = new JComboBox<>();
        panelChamps.add(comboCompteSource, gbc);
        row++;
        
        // Solde du compte source
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Solde disponible :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblSoldeSource = new JLabel("0.00 DH");
        lblSoldeSource.setFont(new Font("Arial", Font.BOLD, 12));
        lblSoldeSource.setForeground(new Color(0, 100, 0));
        panelChamps.add(lblSoldeSource, gbc);
        row++;
        
        // Séparateur
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelChamps.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        
        // Compte Destination
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Compte Destination * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        comboCompteDest = new JComboBox<>();
        panelChamps.add(comboCompteDest, gbc);
        row++;
        
        // Solde du compte destination
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Solde actuel :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblSoldeDest = new JLabel("0.00 DH");
        lblSoldeDest.setFont(new Font("Arial", Font.BOLD, 12));
        panelChamps.add(lblSoldeDest, gbc);
        row++;
        
        // Séparateur
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelChamps.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        
        // Montant
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Montant à transférer * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtMontant = new JTextField(20);
        panelChamps.add(txtMontant, gbc);
        row++;
        
        // Label d'aide
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblAide = new JLabel("<html><i>Le montant sera débité du compte source et crédité au compte destination.</i></html>");
        lblAide.setFont(new Font("Arial", Font.ITALIC, 10));
        lblAide.setForeground(Color.GRAY);
        panelChamps.add(lblAide, gbc);
        gbc.gridwidth = 1;
        
        panelPrincipal.add(panelChamps, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnValider = new JButton("Effectuer le Transfert");
        btnValider.setPreferredSize(new Dimension(180, 35));
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerTransfert();
            }
        });
        
        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setPreferredSize(new Dimension(100, 35));
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);
        
        panelPrincipal.add(panelBoutons, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Charge la liste des comptes dans les combo boxes
     */
    private void chargerComptes() {
        List<CompteBancaire> comptes = traitement.getComptes();
        comboCompteSource.removeAllItems();
        comboCompteDest.removeAllItems();
        
        if (comptes != null && !comptes.isEmpty()) {
            for (CompteBancaire compte : comptes) {
                // Ajouter uniquement les comptes actifs
                if (compte.isActif()) {
                    comboCompteSource.addItem(compte);
                    comboCompteDest.addItem(compte);
                }
            }
        }
        
        // Renderer pour afficher les informations du compte
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CompteBancaire) {
                    CompteBancaire c = (CompteBancaire) value;
                    setText(c.getRIB() + " - " + c.getClient().getNom() + " " + 
                           c.getClient().getPrenom() + " (Solde: " + 
                           String.format("%.2f", c.getSolde()) + " DH)");
                }
                return this;
            }
        };
        
        comboCompteSource.setRenderer(renderer);
        comboCompteDest.setRenderer(renderer);
        
        mettreAJourSoldes();
    }
    
    /**
     * Met à jour l'affichage des soldes
     */
    private void mettreAJourSoldes() {
        CompteBancaire compteSource = (CompteBancaire) comboCompteSource.getSelectedItem();
        CompteBancaire compteDest = (CompteBancaire) comboCompteDest.getSelectedItem();
        
        if (compteSource != null) {
            lblSoldeSource.setText(String.format("%.2f", compteSource.getSolde()) + " DH");
        } else {
            lblSoldeSource.setText("0.00 DH");
        }
        
        if (compteDest != null) {
            lblSoldeDest.setText(String.format("%.2f", compteDest.getSolde()) + " DH");
        } else {
            lblSoldeDest.setText("0.00 DH");
        }
    }
    
    /**
     * Valide et effectue le transfert
     */
    private void validerTransfert() {
        // Validation des sélections
        CompteBancaire compteSource = (CompteBancaire) comboCompteSource.getSelectedItem();
        CompteBancaire compteDest = (CompteBancaire) comboCompteDest.getSelectedItem();
        
        if (compteSource == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un compte source.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (compteDest == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un compte destination.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Vérifier que les comptes sont différents
        if (compteSource.getId() == compteDest.getId()) {
            JOptionPane.showMessageDialog(this,
                "Le compte source et le compte destination doivent être différents.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation du montant
        double montant;
        try {
            montant = Double.parseDouble(txtMontant.getText().trim());
            if (montant <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le montant doit être un nombre positif.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Vérifier la disponibilité du solde
        if (!compteSource.isActif()) {
            JOptionPane.showMessageDialog(this,
                "Le compte source n'est pas actif. Impossible d'effectuer le transfert.",
                "Compte inactif",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!compteDest.isActif()) {
            JOptionPane.showMessageDialog(this,
                "Le compte destination n'est pas actif. Impossible d'effectuer le transfert.",
                "Compte inactif",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer les comptes à jour depuis la base de données
        CompteBancaire compteSourceActuel = traitement.getCompteById(compteSource.getId());
        CompteBancaire compteDestActuel = traitement.getCompteById(compteDest.getId());
        
        if (compteSourceActuel == null || compteDestActuel == null) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la récupération des comptes.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Vérifier le solde disponible
        if (compteSourceActuel.getSolde() < montant) {
            JOptionPane.showMessageDialog(this,
                String.format("Solde insuffisant !\n" +
                             "Solde disponible : %.2f DH\n" +
                             "Montant demandé : %.2f DH",
                             compteSourceActuel.getSolde(), montant),
                "Solde insuffisant",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirmation du transfert
        int confirmation = JOptionPane.showConfirmDialog(this,
            String.format("Confirmer le transfert ?\n\n" +
                         "De : %s (%s %s)\n" +
                         "Vers : %s (%s %s)\n" +
                         "Montant : %.2f DH\n\n" +
                         "Nouveau solde source : %.2f DH\n" +
                         "Nouveau solde destination : %.2f DH",
                         compteSourceActuel.getRIB(),
                         compteSourceActuel.getClient().getNom(),
                         compteSourceActuel.getClient().getPrenom(),
                         compteDestActuel.getRIB(),
                         compteDestActuel.getClient().getNom(),
                         compteDestActuel.getClient().getPrenom(),
                         montant,
                         compteSourceActuel.getSolde() - montant,
                         compteDestActuel.getSolde() + montant),
            "Confirmation de transfert",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // Effectuer le transfert
            boolean success = traitement.transfert(compteSourceActuel, compteDestActuel, montant);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    String.format("Transfert effectué avec succès !\n\n" +
                                 "Montant transféré : %.2f DH\n" +
                                 "Nouveau solde source : %.2f DH\n" +
                                 "Nouveau solde destination : %.2f DH",
                                 montant,
                                 compteSourceActuel.getSolde(),
                                 compteDestActuel.getSolde()),
                    "Transfert réussi",
                    JOptionPane.INFORMATION_MESSAGE);
                
                fenetrePrincipale.actualiserTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du transfert. Veuillez réessayer.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

