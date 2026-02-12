# Airline Booking System - agenntGrid.io-airways

Ce projet est une application de gestion de réservations de vols aériens développée avec **Spring Boot**. Elle permet de gérer les vols, les passagers, les réservations et les paiements via une API REST.

## 🚀 Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA** : Pour la persistance des données.
- **H2 Database** : Base de données en mémoire pour le développement et les tests.
- **PostgreSQL** : Supporté pour la production.
- **Lombok** : Pour réduire le code boilerplate (getters, setters, etc.).
- **Validation API** : Pour la validation des données d'entrée.
- **Maven** : Pour la gestion des dépendances et du build.

## 🛠️ Fonctionnalités Principales

- **Gestion des Vols** : Création, affichage et recherche de vols par origine, destination et date.
- **Gestion des Passagers** : Enregistrement et recherche de passagers (par email ou passeport).
- **Système de Réservation** : Création de réservations pour un vol et un passager spécifique, avec gestion du numéro de siège et annulation.
- **Gestion des Paiements** : Traitement des paiements liés aux réservations avec différents modes (Carte, PayPal, etc.).

## 📂 Structure de l'API

L'API suit les conventions RESTful. Voici les principaux points d'entrée :

### Vols (`/api/flights`)
- `GET /api/flights` : Liste tous les vols.
- `GET /api/flights/search` : Recherche des vols (paramètres: `origin`, `destination`, `start`, `end`).
- `POST /api/flights` : Ajoute un nouveau vol.

### Passagers (`/api/passengers`)
- `GET /api/passengers` : Liste tous les passagers.
- `GET /api/passengers/{id}` : Récupère un passager par ID.
- `POST /api/passengers` : Enregistre un nouveau passager.
- `DELETE /api/passengers/{id}` : Supprime un passager.

### Réservations (`/api/bookings`)
- `POST /api/bookings` : Crée une nouvelle réservation.
- `GET /api/bookings/{id}` : Détails d'une réservation.
- `GET /api/bookings/reference/{ref}` : Recherche par référence de réservation.
- `PUT /api/bookings/{id}/cancel` : Annule une réservation.

### Paiements (`/api/payments`)
- `POST /api/payments` : Effectue un paiement pour une réservation.
- `GET /api/payments/{id}` : Détails d'un paiement.

## ⚙️ Installation et Démarrage

### Prérequis
- JDK 17 ou supérieur
- Maven 3.6+

### Étapes
1. **Cloner le dépôt** :
   ```bash
   git clone <repository-url>
   cd agenntGrid.io-airways
   ```

2. **Configurer la base de données** :
   Par défaut, l'application utilise une base de données **H2** en mémoire. Vous pouvez modifier les paramètres dans `src/main/resources/application.properties`.
   
   Pour accéder à la console H2 (en mode debug) : `http://localhost:8080/h2-console`
   - **JDBC URL** : `jdbc:h2:mem:testdb`
   - **User** : `sa`
   - **Password** : (vide)

3. **Lancer l'application** :
   ```bash
   ./mvnw spring-boot:run
   ```
   L'application sera disponible sur `http://localhost:8080`.

## 🧪 Tests et Développement

- Pour lancer les tests unitaires et d'intégration :
  ```bash
  ./mvnw test
  ```
- Une collection Postman est disponible à la racine du projet : `agenntGrid-io-airways.postman_collection.json` pour tester les différents endpoints.

## 📝 Initialisation des données
Le fichier `src/main/resources/data.sql` contient des données de test qui sont chargées automatiquement au démarrage de l'application si la configuration `spring.sql.init.mode=always` est activée.
