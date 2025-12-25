-- Création de la base de données

CREATE DATABASE IF NOT EXISTS gestion_comptes_bancaires
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_comptes_bancaires;

CREATE TABLE IF NOT EXISTS client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    INDEX idx_nom (nom),
    INDEX idx_prenom (prenom)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS compte_bancaire (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rib VARCHAR(50) NOT NULL UNIQUE,
    solde DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    datecreation DATE NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    id_client INT NOT NULL,
    FOREIGN KEY (id_client) REFERENCES client(id) ON DELETE CASCADE,
    INDEX idx_rib (rib),
    INDEX idx_client (id_client),
    INDEX idx_actif (actif)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table : transfert (historique des transferts)
-- ============================================
CREATE TABLE IF NOT EXISTS transfert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_compte_source BIGINT NOT NULL,
    id_compte_destination BIGINT NOT NULL,
    montant DECIMAL(15, 2) NOT NULL,
    date_transfert DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    solde_source_avant DECIMAL(15, 2) NOT NULL,
    solde_source_apres DECIMAL(15, 2) NOT NULL,
    solde_dest_avant DECIMAL(15, 2) NOT NULL,
    solde_dest_apres DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (id_compte_source) REFERENCES compte_bancaire(id) ON DELETE CASCADE,
    FOREIGN KEY (id_compte_destination) REFERENCES compte_bancaire(id) ON DELETE CASCADE,
    INDEX idx_compte_source (id_compte_source),
    INDEX idx_compte_destination (id_compte_destination),
    INDEX idx_date_transfert (date_transfert)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO client (nom, prenom, telephone) VALUES
('ELBADAOUI', 'Bouchra', '0612345678'),
('ALAMI', 'Ahmed', '0623456789'),
('BENNANI', 'Fatima', '0634567890'),
('CHAKIR', 'Mohammed', '0645678901');


INSERT INTO compte_bancaire (rib, solde, datecreation, actif, id_client) VALUES
('RIB001234567890123456789012', 5000.00, CURDATE(), TRUE, 1),
('RIB002345678901234567890123', 12000.50, CURDATE(), TRUE, 1),
('RIB003456789012345678901234', 7500.00, CURDATE(), TRUE, 2),
('RIB004567890123456789012345', 3000.00, CURDATE(), FALSE, 3),
('RIB005678901234567890123456', 15000.75, CURDATE(), TRUE, 4);


SELECT 'Clients insérés :' AS '';
SELECT * FROM client;

SELECT 'Comptes bancaires insérés :' AS '';
SELECT cb.id, cb.rib, cb.solde, cb.datecreation, cb.actif, 
       c.nom, c.prenom 
FROM compte_bancaire cb 
INNER JOIN client c ON cb.id_client = c.id;

