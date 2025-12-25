package presentation;

import metier.ITraitement;
import modele.Client;
import modele.CompteBancaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Formulaire pour ajouter un nouveau client avec son compte bancaire
 * ou modifier un compte existant
 */
public class FormulaireCompte extends JDialog {
    
    private ITraitement traitement;
    private CompteBancaire compteModifie;
    private FenetrePrincipale fenetrePrincipale;
    
    // Champs pour le client (uniquement en mode ajout)
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;
    
    // Champs pour le compte
    private JTextField txtRIB;
    private JTextField txtSolde;
    private JCheckBox checkActif;
    
    private JButton btnValider;
    private JButton btnAnnuler;
    
    public FormulaireCompte(FenetrePrincipale parent, ITraitement traitement, CompteBancaire compte) {
        super(parent, compte == null ? "Ajouter un client et son compte" : "Modifier un compte", true);
        this.fenetrePrincipale = parent;
        this.traitement = traitement;
        this.compteModifie = compte;
        
        initialiserInterface();
        
        if (compte != null) {
            remplirFormulaire(compte);
        }
    }
    

    private void initialiserInterface() {
        setSize(500, compteModifie == null ? 400 : 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau des champs
        JPanel panelChamps = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Si c'est un ajout, afficher les champs du client
        if (compteModifie == null) {
            // Titre section client
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel titreClient = new JLabel("Informations du Client");
            titreClient.setFont(new Font("Arial", Font.BOLD, 14));
            panelChamps.add(titreClient, gbc);
            gbc.gridwidth = 1;
            
            // Nom
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            panelChamps.add(new JLabel("Nom * :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            txtNom = new JTextField(20);
            panelChamps.add(txtNom, gbc);
            row++;
            
            // Prénom
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            panelChamps.add(new JLabel("Prénom * :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            txtPrenom = new JTextField(20);
            panelChamps.add(txtPrenom, gbc);
            row++;
            
            // Téléphone
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            panelChamps.add(new JLabel("Téléphone :"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            txtTelephone = new JTextField(20);
            panelChamps.add(txtTelephone, gbc);
            row++;
            
            // Séparateur
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panelChamps.add(new JSeparator(), gbc);
            gbc.gridwidth = 1;
            
            // Titre section compte
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel titreCompte = new JLabel("Informations du Compte Bancaire");
            titreCompte.setFont(new Font("Arial", Font.BOLD, 14));
            panelChamps.add(titreCompte, gbc);
            gbc.gridwidth = 1;
        }
        
        // RIB
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("RIB * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtRIB = new JTextField(20);
        if (compteModifie != null) {
            txtRIB.setEditable(false); // Le RIB ne peut pas être modifié
        }
        panelChamps.add(txtRIB, gbc);
        row++;
        
        // Solde
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Solde initial * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtSolde = new JTextField(20);
        panelChamps.add(txtSolde, gbc);
        row++;
        
        // État actif
        gbc.gridx = 0;
        gbc.gridy = row;
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
    

    private void remplirFormulaire(CompteBancaire compte) {
        txtRIB.setText(compte.getRIB());
        txtSolde.setText(String.valueOf(compte.getSolde()));
        checkActif.setSelected(compte.isActif());
    }
    

    private void validerFormulaire() {
        if (compteModifie == null) {
            // Mode ajout : créer un nouveau client et son compte
            
            // Validation des champs client
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le nom est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (txtPrenom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le prénom est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validation des champs compte
            if (txtRIB.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le RIB est obligatoire.",
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
            
            // Créer le client
            Client nouveauClient = new Client();
            nouveauClient.setNom(txtNom.getText().trim());
            nouveauClient.setPrenom(txtPrenom.getText().trim());
            nouveauClient.setTelephone(txtTelephone.getText().trim());
            
            traitement.ajouterClient(nouveauClient);
            
            // Vérifier que le client a été créé avec un ID
            if (nouveauClient.getId() <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création du client.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Créer le compte bancaire
            CompteBancaire nouveauCompte = new CompteBancaire();
            nouveauCompte.setRIB(txtRIB.getText().trim());
            nouveauCompte.setSolde(solde);
            nouveauCompte.setDatecreation(new Date());
            nouveauCompte.setActif(checkActif.isSelected());
            nouveauCompte.setClient(nouveauClient);
            
            traitement.ajouterCompte(nouveauCompte);
            
            JOptionPane.showMessageDialog(this,
                "Client et compte ajoutés avec succès.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            // Mode modification : modifier uniquement le compte existant
            
            // Validation des champs compte
            if (txtRIB.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le RIB est obligatoire.",
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
            
            // Modification du compte existant
            traitement.modifierCompte(compteModifie.getId(), checkActif.isSelected());
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
