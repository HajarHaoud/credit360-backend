# Crédit 360° – Backend REST API (Banque Populaire)

Backend complet développé sous **Spring Boot 3.4.5**, **Java 17/27**, **Spring Data JPA**, **Spring Security 6 (JWT)** et **Base de données H2**.

Ce backend implémente l'ensemble du périmètre fonctionnel et technique défini dans le référentiel des API Crédit 360°.

---

## 🏛️ Architecture Technique

```
ma.credit360
├── config/                  # Configuration Spring, DataInitializer, OpenAPI, Sécurité
├── entity/                  # 12 Entités JPA & Enums (Dossier, Utilisateur, Decision...)
│   └── enums/               # Enums métier (Role, StatutDossier, FamilleCredit...)
├── repository/              # 16 Interfaces Spring Data JPA
├── security/                # JWT Token Provider, Filtre d'authentification, UserPrincipal
├── service/                 # Services métier (Auth, Dossier, Workflow, Decision...)
│   └── pdf/                 # Moteur de génération de documents PDF (SimplePdfBuilder)
├── web/                     # 13 Contrôleurs REST
│   ├── dto/                 # Objets de transfert de données (DTOs Request/Response)
│   └── exception/           # Gestionnaire d'exceptions global (GlobalExceptionHandler)
```

---

## 🔑 Comptes de démonstration préconfigurés

Tous les comptes sont initialisés avec le mot de passe : **`demo1234`**

| Rôle | Email | Nom complet | Fonction | Périmètre d'accès |
| :--- | :--- | :--- | :--- | :--- |
| **CONSEILLER** | `conseiller@credit360.ma` | Asmaa El Habachi | Conseillère Commerciale | Ses propres dossiers uniquement |
| **ANALYSTE** | `analyste@credit360.ma` | Youssef Amrani | Analyste Crédit | Instruction & Analyse financière |
| **RESPONSABLE** | `responsable@credit360.ma` | Karim Tazi | Responsable Engagements | Validation jusqu'à 2 000 000 DH |
| **ADMINISTRATEUR** | `admin@credit360.ma` | Admin Système | Administrateur SI | Paramétrage global, Habilitations, Audit |

---

## 🚀 Démarrage Rapide

### Option 1 : Via le script automatisé
Double-cliquez sur `run.bat` à la racine du projet ou exécutez dans un terminal :
```cmd
run.bat
```

### Option 2 : Via Maven
```bash
mvn clean spring-boot:run
```

L'application écoute sur le port **`8080`**.
- **Console H2 Database :** `http://localhost:8080/h2-console`
  - JDBC URL : `jdbc:h2:mem:credit360db`
  - User : `sa` / Password : *(vide)*
- **Documentation Swagger UI :** `http://localhost:8080/swagger-ui.html`
- **Spécification OpenAPI JSON :** `http://localhost:8080/v3/api-docs`

---

## 📬 Collection Postman Prête à l'Emploi

Une collection Postman complète est fournie dans le dossier :
`postman/Credit360_API.postman_collection.json`

### Fonctionnalités de la collection :
- **Authentification automatique :** Dès l'exécution de la requête de Login, le token JWT est automatiquement extrait et injecté dans les variables de collection (`{{token}}`).
- **Plus de 35 requêtes préparées** couvrant l'ensemble des 12 modules.
- **Corps de requêtes et exemples de tests intégrés**.

---

## 📋 Inventaire des Endpoints Implémentés

### Module 1 – Authentification
- `POST /api/auth/login` : Connexion et délivrance JWT
- `GET /api/auth/me` : Profil de l'utilisateur connecté
- `POST /api/auth/logout` : Déconnexion
- `POST /api/auth/change-password` : Changement du mot de passe
- `POST /api/auth/forgot-password` : Demande de réinitialisation
- `POST /api/auth/reset-password` : Réinitialisation du mot de passe

### Module 1.1 – Administration Utilisateurs (ADMIN)
- `GET /api/admin/utilisateurs` : Liste des utilisateurs
- `GET /api/admin/utilisateurs/{id}` : Détail d'un utilisateur
- `POST /api/admin/utilisateurs` : Création de compte
- `PUT /api/admin/utilisateurs/{id}` : Modification de compte
- `PATCH /api/admin/utilisateurs/{id}/statut` : Activation / Désactivation
- `PATCH /api/admin/utilisateurs/{id}/role` : Modification du rôle
- `POST /api/admin/utilisateurs/{id}/reinitialiser-mot-de-passe` : Reset mot de passe

### Module 2 – Référentiel des Crédits
- `GET /api/types-credits` : Liste des types de crédit actifs
- `GET /api/types-credits/{id}` : Détail d'un type de crédit
- `POST /api/admin/types-credits` : Créer un type de crédit (Admin)
- `PUT /api/admin/types-credits/{id}` : Modifier un type (Admin)
- `PATCH /api/admin/types-credits/{id}/statut` : Activer/désactiver un type (Admin)
- `GET /api/types-credits/{typeId}/pieces-requises` : Pièces requises
- `POST /api/admin/types-credits/{typeId}/pieces-requises` : Ajouter une pièce requise
- `PUT /api/admin/pieces-requises/{pieceId}` : Modifier une pièce
- `DELETE /api/admin/pieces-requises/{pieceId}` : Supprimer une pièce
- `PATCH /api/admin/pieces-requises/{pieceId}/obligatoire` : Modifier caractère obligatoire
- `PATCH /api/admin/types-credits/{typeId}/pieces-requises/ordre` : Réordonner les pièces

### Module 3 – Simulation de Crédit
- `GET /api/simulations/parametres` : Paramètres de simulation
- `POST /api/simulations` : Calculer mensualités, taux d'endettement et éligibilité
- `GET /api/simulations/{id}` : Consulter une simulation par ID
- `GET /api/dossiers/{reference}/simulations` : Historique des simulations d'un dossier
- `POST /api/dossiers/{reference}/simulations` : Sauvegarder simulation attachée à un dossier

### Module 4 – Gestion des Dossiers
- `GET /api/dossiers` : Liste paginée avec recherche (`q`), filtres (`statut`, `type`)
- `GET /api/dossiers/recents` : 5 dossiers récents
- `GET /api/dossiers/stats` : Statistiques globales par statut
- `GET /api/dossiers/{reference}` : Détail complet du dossier
- `POST /api/dossiers` : Création d'un dossier (référence auto `CRD00001`...)
- `PUT /api/dossiers/{reference}` : Modification des données du dossier
- `PATCH /api/dossiers/{reference}/annuler` : Annulation du dossier

### Module 5 – Pièces Justificatives
- `GET /api/dossiers/{reference}/pieces` : Checklist documentaire avec complétude
- `POST /api/dossiers/{reference}/pieces/{pieceId}` : Dépôt multipart (PDF/JPG/PNG <= 5Mo)
- `DELETE /api/dossiers/{reference}/pieces/{pieceId}` : Suppression de pièce
- `GET /api/dossiers/{reference}/pieces/{pieceId}/fichier` : Téléchargement du justificatif
- `POST /api/dossiers/{reference}/soumettre` : Soumission du dossier complet à l'analyse

### Module 6 – Workflow d'Analyse
- `GET /api/analyses/a-traiter` : Dossiers en attente d'instruction
- `GET /api/analyses/mes-dossiers` : Dossiers affectés à l'analyste connecté
- `POST /api/dossiers/{reference}/prendre-en-charge` : Prise en charge du dossier
- `POST /api/dossiers/{reference}/liberer` : Libérer l'affectation du dossier
- `GET /api/dossiers/{reference}/analyse` : Données de l'analyse financière
- `POST /api/dossiers/{reference}/analyse` : Enregistrer l'analyse
- `PUT /api/dossiers/{reference}/analyse` : Mettre à jour l'analyse
- `POST /api/dossiers/{reference}/analyse/terminer` : Clôturer l'analyse
- `POST /api/dossiers/{reference}/complements` : Émettre une demande de complément
- `GET /api/dossiers/{reference}/complements` : Liste des demandes de complément
- `POST /api/dossiers/{reference}/complements/{complementId}/repondre` : Répondre à un complément
- `POST /api/dossiers/{reference}/complements/{complementId}/cloturer` : Clôturer le complément
- `GET /api/dossiers/{reference}/historique` : Historique chronologique d'audit métier

### Module 7 – Décision et Habilitations
- `GET /api/dossiers/{reference}/decisions` : Historique des décisions
- `POST /api/dossiers/{reference}/decisions` : Décision (VALIDATION, REFUS, AJOURNEMENT) avec contrôle des seuils
- `GET /api/dossiers/{reference}/decision-courante` : Décision en vigueur
- `GET /api/admin/habilitations` : Seuils d'habilitation par rôle (Admin)
- `POST /api/admin/habilitations` : Créer seuil d'habilitation (Admin)
- `PUT /api/admin/habilitations/{id}` : Modifier seuil d'habilitation (Admin)
- `DELETE /api/admin/habilitations/{id}` : Supprimer seuil d'habilitation (Admin)
- `GET /api/dossiers/{reference}/habilitations` : Vérification d'habilitation pour un dossier

### Module 8 – Documents PDF
- `GET /api/dossiers/{reference}/documents` : Liste des documents générés
- `POST /api/dossiers/{reference}/documents/generer` : Générer un PDF (`FICHE_ANALYSE`, `OFFRE_CREDIT`, `RECAPITULATIF`...)
- `GET /api/dossiers/{reference}/documents/{documentId}` : Métadonnées du document
- `GET /api/dossiers/{reference}/documents/{documentId}/fichier` : Téléchargement du fichier PDF
- `DELETE /api/dossiers/{reference}/documents/{documentId}` : Suppression de document

### Module 9 – Dashboard et Reporting
- `GET /api/dashboard` : Synthèse globale du dashboard
- `GET /api/dashboard/stats` : KPIs chiffrés
- `GET /api/dashboard/evolution` : Évolution temporelle des dossiers
- `GET /api/dashboard/repartition-statuts` : Répartition par statut
- `GET /api/dashboard/repartition-types` : Répartition par produit de crédit
- `GET /api/dashboard/activite-recente` : Dernières opérations
- `GET /api/rapports/dossiers/export` : Export CSV des dossiers
- `GET /api/rapports/activite/export` : Export CSV des pistes d'audit
- `GET /api/rapports/statistiques/export` : Export CSV des statistiques

### Module 10 – Notifications
- `GET /api/notifications` : Liste des notifications utilisateur
- `GET /api/notifications/non-lues/count` : Compteur de non-lues
- `PATCH /api/notifications/{id}/lue` : Marquer comme lue
- `PATCH /api/notifications/lire-tout` : Tout marquer comme lu
- `DELETE /api/notifications/{id}` : Supprimer une notification

### Module 11 – Administration Paramètres & Permissions
- `GET /api/admin/parametres` : Lister les paramètres système
- `GET /api/admin/parametres/{cle}` : Détail d'un paramètre
- `PUT /api/admin/parametres/{cle}` : Modifier un paramètre
- `GET /api/admin/roles` : Liste des rôles
- `GET /api/admin/permissions` : Liste des permissions du système
- `GET /api/admin/roles/{role}/permissions` : Permissions d'un rôle
- `PUT /api/admin/roles/{role}/permissions` : Mettre à jour les permissions

### Module 12 – Audit et Traçabilité
- `GET /api/admin/audit` : Journal complet des pistes d'audit
- `GET /api/admin/audit/{id}` : Détail d'un événement d'audit
- `GET /api/admin/audit/utilisateur/{userId}` : Événements par utilisateur
- `GET /api/admin/audit/dossier/{reference}` : Événements par dossier
