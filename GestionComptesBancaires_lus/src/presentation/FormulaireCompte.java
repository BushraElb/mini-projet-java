package presentation;

import metier.ITraitement;
import modele.Client;
import modele.CompteBancaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/**
 * Formulaire pour ajouter ou modifier un compte bancaire
 */
public class FormulaireCompte extends JDialog {
    
    private ITraitement traitement;
    private CompteBancaire compteModifie;
    private FenetrePrincipale fenetrePrincipale;
    
    private JTextField txtRIB;
    private JTextField txtSolde;
    private JComboBox<Client> comboClient;
    private JCheckBox checkActif;
    private JButton btnValider;
    private JButton btnAnnuler;
    
    public FormulaireCompte(FenetrePrincipale parent, ITraitement traitement, CompteBancaire compte) {
        super(parent, compte == null ? "Ajouter un compte" : "Modifier un compte", true);
        this.fenetrePrincipale = parent;
        this.traitement = traitement;
        this.compteModifie = compte;
        
        initialiserInterface();
        chargerClients();
        
        if (compte != null) {
            remplirFormulaire(compte);
        }
    }
    
    /**
     * Initialise l'interface graphique
     */
    private void initialiserInterface() {
        setSize(500, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau des champs
        JPanel panelChamps = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // RIB
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelChamps.add(new JLabel("RIB * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtRIB = new JTextField(20);
        panelChamps.add(txtRIB, gbc);
        
        // Client
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Client * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        comboClient = new JComboBox<>();
        panelChamps.add(comboClient, gbc);
        
        // Solde
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Solde initial * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtSolde = new JTextField(20);
        panelChamps.add(txtSolde, gbc);
        
        // État actif
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("État :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        checkActif = new JCheckBox("Compte actif");
        checkActif.setSelected(true);
        panelChamps.add(checkActif, gbc);
        
        panelPrincipal.add(panelChamps, BorderLayout.CENTER);
        
        // Panneau des boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnValider = new JButton("Valider");
        btnValider.setPreferredSize(new Dimension(100, 30));
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerFormulaire();
            }
        });
        
        btnAnnuler = new JButton("Annuler");
        btnAnnuler.setPreferredSize(new Dimension(100, 30));
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
     * Charge la liste des clients dans le combo box
     */
    private void chargerClients() {
        List<Client> clients = traitement.getClients();
        comboClient.removeAllItems();
        
        for (Client client : clients) {
            comboClient.addItem(client);
        }
        
        // Afficher le nom et prénom dans le combo
        comboClient.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    Client c = (Client) value;
                    setText(c.getNom() + " " + c.getPrenom() + " (ID: " + c.getId() + ")");
                }
                return this;
            }
        });
    }
    
    /**
     * Remplit le formulaire avec les données du compte à modifier
     */
    private void remplirFormulaire(CompteBancaire compte) {
        txtRIB.setText(compte.getRIB());
        txtRIB.setEditable(false); // Le RIB ne peut pas être modifié
        txtSolde.setText(String.valueOf(compte.getSolde()));
        checkActif.setSelected(compte.isActif());
        
        // Sélectionner le client dans le combo
        for (int i = 0; i < comboClient.getItemCount(); i++) {
            Client client = comboClient.getItemAt(i);
            if (client.getId() == compte.getClient().getId()) {
                comboClient.setSelectedIndex(i);
                break;
            }
        }
        comboClient.setEnabled(false); // Le client ne peut pas être modifié
    }
    
    /**
     * Valide et enregistre le formulaire
     */
    private void validerFormulaire() {
        // Validation des champs
        if (txtRIB.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le RIB est obligatoire.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (comboClient.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un client.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double solde;
        try {
            solde = Double.parseDouble(txtSolde.getText().trim());
            if (solde < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le solde doit être un nombre positif.",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Client clientSelectionne = (Client) comboClient.getSelectedItem();
        
        if (compteModifie == null) {
            // Ajout d'un nouveau compte
            CompteBancaire nouveauCompte = new CompteBancaire();
            nouveauCompte.setRIB(txtRIB.getText().trim());
            nouveauCompte.setSolde(solde);
            nouveauCompte.setDatecreation(new Date());
            nouveauCompte.setActif(checkActif.isSelected());
            nouveauCompte.setClient(clientSelectionne);
            
            traitement.ajouterCompte(nouveauCompte);
            JOptionPane.showMessageDialog(this,
                "Compte ajouté avec succès.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Modification d'un compte existant
            traitement.modifierCompte(compteModifie.getId(), checkActif.isSelected());
            // Mettre à jour le solde
            traitement.modifierSolde(compteModifie.getId(), solde);
            
            JOptionPane.showMessageDialog(this,
                "Compte modifié avec succès.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        fenetrePrincipale.actualiserTable();
        dispose();
    }
}

