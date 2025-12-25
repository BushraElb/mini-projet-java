# Rapport de Projet
## Application de Gestion des Comptes Bancaires

---

**Auteur :** Bouchra ELBADAOUI  
**Date :** Décembre 2025  
**Langage :** Java  
**Architecture :** MVC (Model-View-Controller)

---

## 📋 Table des Matières

1. [Introduction](#introduction)
2. [Objectifs du Projet](#objectifs-du-projet)
3. [Spécifications Techniques](#spécifications-techniques)
4. [Architecture du Projet](#architecture-du-projet)
5. [Structure de la Base de Données](#structure-de-la-base-de-données)
6. [Fonctionnalités Implémentées](#fonctionnalités-implémentées)
7. [Guide d'Utilisation](#guide-dutilisation)
8. [Technologies Utilisées](#technologies-utilisées)
9. [Structure des Fichiers](#structure-des-fichiers)
10. [Conclusion](#conclusion)

---

## 1. Introduction

Ce projet consiste en le développement d'une application desktop de gestion des comptes bancaires en Java. L'application permet de gérer les clients et leurs comptes bancaires associés à travers une interface graphique intuitive développée avec Swing.

L'application suit l'architecture MVC (Model-View-Controller) pour une séparation claire des responsabilités et une maintenabilité optimale du code.

---

## 2. Objectifs du Projet

### Objectifs Principaux

- ✅ Créer une application desktop fonctionnelle en Java
- ✅ Implémenter une connexion à une base de données MySQL
- ✅ Réaliser les opérations CRUD (Create, Read, Update, Delete) sur les comptes bancaires
- ✅ Développer une interface graphique conviviale avec Swing
- ✅ Respecter l'architecture MVC
- ✅ Assurer la gestion de la relation entre clients et comptes bancaires

### Contraintes Respectées

- Chaque client possède un seul compte bancaire
- Validation des données avant insertion
- Gestion des erreurs et messages informatifs
- Interface en temps réel (mise à jour automatique)

---

## 3. Spécifications Techniques

### Environnement de Développement

- **Langage :** Java (JDK 8 ou supérieur)
- **IDE :** IntelliJ IDEA
- **Base de données :** MySQL 5.7+
- **Driver JDBC :** MySQL Connector/J 8.0+
- **Interface Graphique :** Java Swing

### Prérequis Système

- Java JDK installé
- MySQL Server installé et démarré
- IntelliJ IDEA (ou autre IDE Java)
- MySQL Connector/J ajouté au projet

---

## 4. Architecture du Projet

### Architecture MVC

Le projet suit le pattern MVC (Model-View-Controller) pour une séparation claire des couches :

```
┌─────────────────────────────────────────┐
│         PRESENTATION (View)             │
│  - FenetrePrincipale                   │
│  - FormulaireCompte                     │
│  - FormulaireClient                     │
│  - Main                                 │
└─────────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────────┐
│         METIER (Controller)             │
│  - ITraitement (Interface)              │
│  - TraitementImpl (Implémentation)     │
└─────────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────────┐
│         DAO (Data Access)                │
│  - ConnexionBD                          │
│  - ClientDAO                            │
│  - CompteBancaireDAO                    │
└─────────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────────┐
│         MODELE (Model)                   │
│  - Client                                │
│  - CompteBancaire                       │
└─────────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────────┐
│         BASE DE DONNEES                  │
│         MySQL                            │
└─────────────────────────────────────────┘
```

### Couches de l'Application

#### 4.1. Couche Présentation (View)
- **Responsabilité :** Interface utilisateur et interaction
- **Classes :**
  - `FenetrePrincipale` : Fenêtre principale avec tableau des comptes
  - `FormulaireCompte` : Formulaire d'ajout/modification de compte
  - `FormulaireClient` : Formulaire d'ajout de client (utilisé en interne)
  - `Main` : Point d'entrée de l'application

#### 4.2. Couche Métier (Controller)
- **Responsabilité :** Logique métier et orchestration
- **Classes :**
  - `ITraitement` : Interface définissant les opérations métier
  - `TraitementImpl` : Implémentation de la logique métier

#### 4.3. Couche DAO (Data Access Object)
- **Responsabilité :** Accès aux données et requêtes SQL
- **Classes :**
  - `ConnexionBD` : Gestion de la connexion MySQL
  - `ClientDAO` : Opérations CRUD sur les clients
  - `CompteBancaireDAO` : Opérations CRUD sur les comptes bancaires

#### 4.4. Couche Modèle (Model)
- **Responsabilité :** Représentation des entités métier
- **Classes :**
  - `Client` : Modèle de données client
  - `CompteBancaire` : Modèle de données compte bancaire

---

## 5. Structure de la Base de Données

### Schéma de la Base de Données

#### Table `client`

| Colonne      | Type         | Contraintes                    |
|--------------|--------------|--------------------------------|
| id           | INT          | PRIMARY KEY, AUTO_INCREMENT    |
| nom          | VARCHAR(100) | NOT NULL                       |
| prenom       | VARCHAR(100) | NOT NULL                       |
| telephone    | VARCHAR(20)  | NULL                           |

**Index :**
- `idx_nom` sur `nom`
- `idx_prenom` sur `prenom`

#### Table `compte_bancaire`

| Colonne      | Type            | Contraintes                    |
|--------------|-----------------|--------------------------------|
| id           | BIGINT          | PRIMARY KEY, AUTO_INCREMENT    |
| rib          | VARCHAR(50)     | NOT NULL, UNIQUE               |
| solde        | DECIMAL(15,2)   | NOT NULL, DEFAULT 0.00         |
| datecreation | DATE            | NOT NULL                       |
| actif        | BOOLEAN         | NOT NULL, DEFAULT TRUE         |
| id_client    | INT             | NOT NULL, FOREIGN KEY          |

**Index :**
- `idx_rib` sur `rib`
- `idx_client` sur `id_client`
- `idx_actif` sur `actif`

**Clé étrangère :**
- `id_client` → `client(id)` ON DELETE CASCADE

### Relation entre les Tables

```
client (1) ────────< (1) compte_bancaire
```

- Un client peut avoir **un seul** compte bancaire
- Un compte bancaire appartient à **un seul** client
- Relation 1:1 avec contrainte d'intégrité référentielle

---

## 6. Fonctionnalités Implémentées

### 6.1. Gestion des Clients

#### Ajout de Client
- Formulaire intégré dans l'ajout de compte
- Validation des champs obligatoires (nom, prénom)
- Attribution automatique d'un ID unique
- Téléphone optionnel

#### Affichage des Clients
- Liste des clients dans le formulaire de compte (mode modification uniquement)
- Affichage formaté : "Nom Prénom (ID: X)"

### 6.2. Gestion des Comptes Bancaires

#### Ajout de Compte
- **Workflow unifié :** Création du client ET de son compte en une seule étape
- Formulaire unique contenant :
  - Informations client (nom, prénom, téléphone)
  - Informations compte (RIB, solde initial, état)
- Validation complète des données
- Génération automatique de la date de création
- Attribution automatique d'un ID unique

#### Modification de Compte
- Modification du solde
- Modification de l'état (actif/inactif)
- Le RIB et le client ne peuvent pas être modifiés
- Mise à jour automatique de la table

#### Suppression de Compte
- Confirmation avant suppression
- Suppression en cascade (si configuré)
- Mise à jour automatique de la table

#### Affichage des Comptes
- Tableau avec colonnes :
  - ID
  - RIB
  - Client (nom et prénom)
  - Solde (formaté avec 2 décimales)
  - Date de création (format dd/MM/yyyy)
  - État (Actif/Inactif)
- Tri par ID
- Mise à jour en temps réel

### 6.3. Fonctionnalités Avancées

#### Transfert entre Comptes
- Méthode `transfert()` implémentée dans la couche métier
- Vérification des soldes suffisants
- Vérification de l'état des comptes (actifs)
- Transaction atomique (tout ou rien)

#### Validation des Données
- Validation côté client (interface)
- Validation côté serveur (base de données)
- Messages d'erreur explicites
- Prévention des doublons (RIB unique)

---

## 7. Guide d'Utilisation

### 7.1. Installation et Configuration

#### Étape 1 : Prérequis
1. Installer Java JDK (8 ou supérieur)
2. Installer MySQL Server
3. Installer IntelliJ IDEA

#### Étape 2 : Configuration de la Base de Données
1. Ouvrir MySQL Workbench
2. Exécuter le script `database.sql`
3. Vérifier la création des tables

#### Étape 3 : Configuration du Projet
1. Ouvrir le projet dans IntelliJ IDEA
2. Ajouter MySQL Connector/J via Maven ou manuellement
3. Vérifier les paramètres de connexion dans `ConnexionBD.java`

#### Étape 4 : Exécution
1. Lancer la classe `Main.java`
2. L'interface graphique s'ouvre automatiquement

### 7.2. Utilisation de l'Application

#### Ajouter un Client avec son Compte

1. Cliquer sur le bouton **"Ajouter"**
2. Remplir le formulaire :
   - **Section Client :**
     - Nom (obligatoire)
     - Prénom (obligatoire)
     - Téléphone (optionnel)
   - **Section Compte :**
     - RIB (obligatoire, unique)
     - Solde initial (obligatoire, >= 0)
     - État (actif/inactif)
3. Cliquer sur **"Valider"**
4. Le client et son compte apparaissent automatiquement dans la table

#### Modifier un Compte

1. Sélectionner un compte dans la table
2. Cliquer sur **"Modifier"**
3. Modifier le solde et/ou l'état
4. Cliquer sur **"Valider"**
5. Les modifications apparaissent automatiquement

#### Supprimer un Compte

1. Sélectionner un compte dans la table
2. Cliquer sur **"Supprimer"**
3. Confirmer la suppression
4. Le compte disparaît automatiquement de la table

---

## 8. Technologies Utilisées

### 8.1. Langage et Framework

- **Java SE** : Langage de programmation principal
- **Java Swing** : Bibliothèque pour l'interface graphique
- **JDBC** : API pour la connexion à la base de données

### 8.2. Base de Données

- **MySQL** : Système de gestion de base de données relationnelle
- **MySQL Connector/J** : Driver JDBC pour MySQL

### 8.3. Outils de Développement

- **IntelliJ IDEA** : Environnement de développement intégré
- **MySQL Workbench** : Outil de gestion de base de données
- **Git** : Système de contrôle de version

### 8.4. Patterns et Concepts

- **MVC (Model-View-Controller)** : Architecture du projet
- **DAO (Data Access Object)** : Pattern d'accès aux données
- **Singleton** : Pour la connexion à la base de données
- **Observer** : Pour les événements Swing

---

## 9. Structure des Fichiers

```
GestionComptesBancaires_lus/
│
├── src/
│   ├── dao/
│   │   ├── ConnexionBD.java          # Gestion de la connexion MySQL
│   │   ├── ClientDAO.java            # CRUD pour les clients
│   │   └── CompteBancaireDAO.java    # CRUD pour les comptes
│   │
│   ├── metier/
│   │   ├── ITraitement.java          # Interface métier
│   │   └── TraitementImpl.java        # Implémentation métier
│   │
│   ├── modele/
│   │   ├── Client.java                # Modèle Client
│   │   └── CompteBancaire.java        # Modèle CompteBancaire
│   │
│   └── presentation/
│       ├── Main.java                  # Point d'entrée
│       ├── FenetrePrincipale.java     # Fenêtre principale
│       ├── FormulaireCompte.java      # Formulaire compte
│       └── FormulaireClient.java      # Formulaire client
│
├── database.sql                       # Script de création BDD
├── README_SETUP.md                    # Guide d'installation
├── RAPPORT_PROJET.md                  # Ce rapport
├── .gitignore                         # Fichiers ignorés par Git
└── GestionComptesBancaires.iml        # Configuration IntelliJ
```

### Description des Packages

#### `dao` (Data Access Object)
- **ConnexionBD** : Gère la connexion unique à MySQL (pattern Singleton)
- **ClientDAO** : Méthodes CRUD pour la table `client`
- **CompteBancaireDAO** : Méthodes CRUD pour la table `compte_bancaire`

#### `metier` (Business Logic)
- **ITraitement** : Interface définissant les opérations métier
- **TraitementImpl** : Implémentation de la logique métier avec validation

#### `modele` (Model)
- **Client** : Entité représentant un client avec ses attributs
- **CompteBancaire** : Entité représentant un compte bancaire avec association au client

#### `presentation` (View)
- **Main** : Classe principale lançant l'application
- **FenetrePrincipale** : Interface principale avec tableau des comptes
- **FormulaireCompte** : Formulaire unifié pour créer/modifier client+compte
- **FormulaireClient** : Formulaire pour créer un client (utilisé en interne)

---

## 10. Conclusion

### 10.1. Objectifs Atteints

✅ **Application fonctionnelle** : Toutes les fonctionnalités CRUD sont implémentées et opérationnelles  
✅ **Architecture MVC** : Séparation claire des responsabilités respectée  
✅ **Interface graphique** : Interface intuitive et conviviale développée avec Swing  
✅ **Base de données** : Connexion MySQL fonctionnelle avec gestion des relations  
✅ **Validation des données** : Validation complète côté client et serveur  
✅ **Gestion des erreurs** : Messages d'erreur explicites et gestion des exceptions  

### 10.2. Points Forts du Projet

- **Architecture propre** : Respect du pattern MVC facilitant la maintenance
- **Code modulaire** : Séparation claire des couches
- **Interface intuitive** : Workflow simplifié pour l'utilisateur
- **Temps réel** : Mise à jour automatique sans bouton "Actualiser"
- **Validation robuste** : Prévention des erreurs et données invalides
- **Documentation complète** : Code commenté et guides d'utilisation

### 10.3. Améliorations Possibles

- **Gestion des transactions** : Implémentation de transactions pour les opérations critiques
- **Journalisation** : Ajout d'un système de logs pour le débogage
- **Export de données** : Fonctionnalité d'export en CSV/PDF
- **Recherche et filtres** : Ajout de fonctionnalités de recherche dans la table
- **Statistiques** : Tableau de bord avec statistiques (nombre de comptes, total des soldes, etc.)
- **Sécurité** : Authentification utilisateur et gestion des rôles
- **Tests unitaires** : Ajout de tests JUnit pour valider le code

### 10.4. Compétences Développées

- ✅ Programmation orientée objet en Java
- ✅ Développement d'interfaces graphiques avec Swing
- ✅ Connexion et manipulation de bases de données MySQL via JDBC
- ✅ Implémentation de l'architecture MVC
- ✅ Gestion de projet et organisation du code
- ✅ Validation et gestion des erreurs

---

## 📊 Statistiques du Projet

- **Nombre de classes Java :** 11
- **Lignes de code :** ~1500+
- **Packages :** 4 (dao, metier, modele, presentation)
- **Tables de base de données :** 2
- **Fonctionnalités principales :** 4 (Ajouter, Modifier, Supprimer, Afficher)

---

## 📝 Notes Finales

Ce projet démontre une compréhension solide des concepts fondamentaux du développement Java, de l'architecture logicielle, et de la gestion de bases de données relationnelles. L'application est fonctionnelle, bien structurée, et prête pour une utilisation en production avec quelques améliorations supplémentaires.

---

**Fin du Rapport**

*Rapport généré le : Décembre 2025*

