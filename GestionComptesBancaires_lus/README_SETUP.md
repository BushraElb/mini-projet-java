# Guide Complet : Configuration et Exécution de l'Application

## 📋 Prérequis

Avant de commencer, vous devez installer les logiciels suivants :

### 1. Java JDK (Java Development Kit)
- **Télécharger** : https://www.oracle.com/java/technologies/downloads/ (Java 8 ou supérieur)
- **Installer** : Suivez l'installateur
- **Vérifier** : Ouvrez un terminal/CMD et tapez :
  ```
  java -version
  ```
  Vous devriez voir la version de Java installée.

### 2. MySQL Server
- **Télécharger** : https://dev.mysql.com/downloads/mysql/
- **Installer** : Pendant l'installation, notez le mot de passe root que vous définissez
- **Démarrer MySQL** : Le service MySQL doit être en cours d'exécution

### 3. IntelliJ IDEA
- **Télécharger** : https://www.jetbrains.com/idea/download/
- **Version recommandée** : IntelliJ IDEA Community Edition (gratuite)
- **Installer** : Suivez l'installateur

---

## 🚀 Étapes de Configuration

### ÉTAPE 1 : Ouvrir le Projet dans IntelliJ IDEA

1. **Lancer IntelliJ IDEA**
2. **Ouvrir le projet** :
   - Cliquez sur `File` → `Open`
   - Naviguez vers le dossier `GestionComptesBancaires_lus`
   - Sélectionnez le dossier et cliquez sur `OK`
3. **Attendre** : IntelliJ va indexer les fichiers (cela peut prendre quelques minutes)

---

### ÉTAPE 2 : Configurer le JDK dans IntelliJ

1. **Ouvrir les paramètres** :
   - `File` → `Project Structure` (ou `Ctrl+Alt+Shift+S`)
2. **Configurer le SDK** :
   - Dans la section `Project`, vérifiez que `Project SDK` est défini
   - Si ce n'est pas le cas, cliquez sur `New...` et sélectionnez votre installation JDK
   - Cliquez sur `Apply` puis `OK`

---

### ÉTAPE 3 : Créer la Base de Données MySQL

1. **Ouvrir MySQL** :
   - Ouvrez MySQL Workbench ou un client MySQL
   - Connectez-vous avec l'utilisateur `root` et votre mot de passe

2. **Exécuter le script SQL** :
   - Ouvrez le fichier `database.sql` dans IntelliJ (dans la racine du projet)
   - Copiez tout le contenu
   - Dans MySQL Workbench, créez une nouvelle requête
   - Collez le contenu et exécutez-le (bouton ⚡ ou `Ctrl+Enter`)
   - Vérifiez que les tables `client` et `compte_bancaire` ont été créées

3. **Vérifier la connexion** :
   - La base de données `gestion_comptes_bancaires` doit maintenant exister
   - Elle contient des données de test

---

### ÉTAPE 4 : Configurer les Paramètres de Connexion MySQL

1. **Ouvrir le fichier de connexion** :
   - Dans IntelliJ, ouvrez : `src/dao/ConnexionBD.java`

2. **Modifier les paramètres si nécessaire** :
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/gestion_comptes_bancaires";
   private static final String USER = "root";
   private static final String PASSWORD = "";  // ← Mettez votre mot de passe MySQL ici
   ```
   - Si votre mot de passe MySQL n'est pas vide, modifiez la ligne `PASSWORD`
   - Si votre MySQL n'utilise pas le port 3306, modifiez l'URL

---

### ÉTAPE 5 : Ajouter le Driver MySQL (MySQL Connector)

#### Option A : Téléchargement Automatique (Recommandé)

1. **Ouvrir les dépendances** :
   - Clic droit sur le projet → `Open Module Settings` (ou `F4`)
   - Ou `File` → `Project Structure`

2. **Ajouter une bibliothèque** :
   - Allez dans l'onglet `Libraries`
   - Cliquez sur `+` → `From Maven...`
   - Tapez : `mysql:mysql-connector-java:8.0.33`
   - Cliquez sur `OK`
   - Cliquez sur `Apply` puis `OK`

#### Option B : Téléchargement Manuel

1. **Télécharger le driver** :
   - Allez sur : https://dev.mysql.com/downloads/connector/j/
   - Téléchargez le fichier JAR (Platform Independent)

2. **Ajouter au projet** :
   - Dans IntelliJ : `File` → `Project Structure` → `Libraries`
   - Cliquez sur `+` → `Java`
   - Sélectionnez le fichier JAR téléchargé
   - Cliquez sur `OK`

---

### ÉTAPE 6 : Configurer la Classe Main

1. **Ouvrir la classe Main** :
   - Naviguez vers `src/presentation/Main.java`

2. **Configurer comme point d'entrée** :
   - Clic droit sur le fichier `Main.java`
   - Sélectionnez `Run 'Main.main()'`
   - OU : Cliquez sur la flèche verte à côté de `public static void main`

---

### ÉTAPE 7 : Exécuter l'Application

#### Méthode 1 : Depuis IntelliJ (Recommandé)

1. **Ouvrir Main.java** :
   - Double-cliquez sur `src/presentation/Main.java`

2. **Lancer l'application** :
   - Cliquez sur la flèche verte ▶ à côté de `public static void main`
   - OU : Clic droit sur `Main.java` → `Run 'Main.main()'`
   - OU : Appuyez sur `Shift+F10`

3. **Vérifier** :
   - Une fenêtre graphique devrait s'ouvrir
   - Vous devriez voir un tableau avec les comptes bancaires de test

#### Méthode 2 : Créer une Configuration de Run

1. **Créer une configuration** :
   - `Run` → `Edit Configurations...`
   - Cliquez sur `+` → `Application`
   - Nom : `Gestion Comptes Bancaires`
   - Main class : `presentation.Main`
   - Module : Sélectionnez votre module
   - Cliquez sur `Apply` puis `OK`

2. **Exécuter** :
   - Cliquez sur le menu déroulant en haut à droite
   - Sélectionnez `Gestion Comptes Bancaires`
   - Cliquez sur ▶ ou appuyez sur `Shift+F10`

---

## 🎯 Utilisation de l'Application

### Interface Principale

L'application affiche :
- **Tableau** : Liste de tous les comptes bancaires
- **Boutons** :
  - `Ajouter` : Créer un nouveau compte bancaire
  - `Modifier` : Modifier le compte sélectionné
  - `Supprimer` : Supprimer le compte sélectionné
  - `Actualiser` : Rafraîchir la liste
  - `Ajouter Client` : Ajouter un nouveau client

### Ajouter un Compte Bancaire

1. Cliquez sur `Ajouter Client` si vous n'avez pas encore de client
2. Remplissez le formulaire client (Nom, Prénom, Téléphone)
3. Cliquez sur `Ajouter` pour créer un compte
4. Sélectionnez un client dans la liste
5. Entrez le RIB et le solde initial
6. Cliquez sur `Valider`

---

## ⚠️ Résolution de Problèmes

### Erreur : "Driver MySQL non trouvé"
- **Solution** : Vérifiez que vous avez bien ajouté le MySQL Connector (Étape 5)

### Erreur : "Access denied for user 'root'"
- **Solution** : Vérifiez le mot de passe dans `ConnexionBD.java` (Étape 4)

### Erreur : "Unknown database 'gestion_comptes_bancaires'"
- **Solution** : Exécutez le script `database.sql` (Étape 3)

### Erreur : "Port 3306 is not available"
- **Solution** : Vérifiez que MySQL est démarré, ou changez le port dans `ConnexionBD.java`

### L'application ne se lance pas
- **Vérifiez** : Que Java JDK est bien configuré (Étape 2)
- **Vérifiez** : Que la classe Main est bien configurée (Étape 6)

### Le tableau est vide
- **Vérifiez** : Que vous avez exécuté le script SQL avec les données de test
- Cliquez sur `Actualiser` pour recharger les données

---

## 📝 Notes Importantes

- **Première exécution** : La première fois peut être plus lente (compilation)
- **MySQL doit être démarré** : Assurez-vous que le service MySQL est en cours d'exécution
- **Port par défaut** : MySQL utilise le port 3306 par défaut
- **Données de test** : Le script SQL inclut des données de test pour vous aider à démarrer

---

## 🎓 Structure du Projet

```
GestionComptesBancaires_lus/
├── src/
│   ├── dao/              # Accès aux données (Database)
│   ├── metier/           # Logique métier (Business Logic)
│   ├── modele/           # Modèles de données (Models)
│   └── presentation/     # Interface graphique (GUI)
├── database.sql          # Script de création de la base de données
└── README_SETUP.md       # Ce fichier
```

---

## ✅ Checklist de Vérification

Avant de lancer l'application, assurez-vous que :

- [ ] Java JDK est installé et configuré
- [ ] MySQL est installé et démarré
- [ ] La base de données `gestion_comptes_bancaires` existe
- [ ] Les tables `client` et `compte_bancaire` existent
- [ ] Le MySQL Connector est ajouté au projet
- [ ] Les paramètres de connexion dans `ConnexionBD.java` sont corrects
- [ ] IntelliJ a indexé tous les fichiers (barre de progression en bas)

---

**Bon développement ! 🚀**

