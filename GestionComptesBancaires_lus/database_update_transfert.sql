-- ============================================
-- Script de mise à jour : Ajout de la table transfert
-- Pour les bases de données existantes
-- ============================================

USE gestion_comptes_bancaires;

-- Créer la table transfert si elle n'existe pas
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

SELECT 'Table transfert créée avec succès !' AS '';

