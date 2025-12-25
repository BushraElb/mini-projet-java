package presentation;

import metier.ITraitement;
import modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Formulaire pour ajouter un nouveau client
 */
public class FormulaireClient extends JDialog {
    
    private ITraitement traitement;
    private FenetrePrincipale fenetrePrincipale;
    
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;
    private JButton btnValider;
    private JButton btnAnnuler;
    
    public FormulaireClient(FenetrePrincipale parent, ITraitement traitement) {
        super(parent, "Ajouter un client", true);
        this.fenetrePrincipale = parent;
        this.traitement = traitement;
        
        initialiserInterface();
    }
    
    /**
     * Initialise l'interface graphique
     */
    private void initialiserInterface() {
        setSize(400, 200);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau des champs
        JPanel panelChamps = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelChamps.add(new JLabel("Nom * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNom = new JTextField(20);
        panelChamps.add(txtNom, gbc);
        
        // Prénom
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Prénom * :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtPrenom = new JTextField(20);
        panelChamps.add(txtPrenom, gbc);
        
        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panelChamps.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTelephone = new JTextField(20);
        panelChamps.add(txtTelephone, gbc);
        
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
     * Valide et enregistre le formulaire
     */
    private void validerFormulaire() {
        // Validation des champs
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
        
        Client nouveauClient = new Client();
        nouveauClient.setNom(txtNom.getText().trim());
        nouveauClient.setPrenom(txtPrenom.getText().trim());
        nouveauClient.setTelephone(txtTelephone.getText().trim());
        
        traitement.ajouterClient(nouveauClient);
        
        // Vérifier que l'ID a été assigné
        if (nouveauClient.getId() > 0) {
            JOptionPane.showMessageDialog(this,
                "Client ajouté avec succès (ID: " + nouveauClient.getId() + ").",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Client ajouté avec succès, mais l'ID n'a pas été récupéré.",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
        }
        
        dispose();
    }
}

