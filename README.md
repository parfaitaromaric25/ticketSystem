# Système de Gestion de Tickets

## Description
Application Spring Boot pour la gestion de tickets avec authentification, contrôle d'accès et API REST documentée.

## Technologies Utilisées
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- Base de données H2
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5 & Mockito

## Design Patterns Implémentés
1. **Generic Service Pattern** : Service générique pour réduire la duplication de code
2. **DTO Pattern** : Séparation entre entités et objets de transfert
3. **Mapper Pattern** : Conversion entre entités et DTOs
4. **Repository Pattern** : Abstraction de la couche de données
5. **Template Method Pattern** : Classe BaseEntity pour les champs communs
6. **Strategy Pattern** : Services interchangeables via interfaces

## Prérequis
- JDK 17 ou supérieur
- Maven 3.6+ ou Gradle 7+

## Installation et Lancement

### Avec Maven
```bash
# Cloner le projet
git clone <repository-url>
cd ticket-management

# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

### Avec Gradle
```bash
# Compiler le projet
./gradlew build

# Lancer l'application
./gradlew bootRun
```

## Accès aux Endpoints

### URL de Base
```
http://localhost:8080
```

### Documentation API (Swagger)
```
http://localhost:8080/swagger-ui.html
```

### Console H2
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:ticketdb
Username: sa
Password: (laisser vide)
```

## Authentification

### Utilisateurs par défaut
- **Admin** : username=`admin`, password=`admin123`
- **User** : username=`user1`, password=`user123`

### Utilisation avec cURL
```bash
# Authentification Basic
curl -u admin:admin123 http://localhost:8080/api/users
```

## Endpoints Principaux

### Utilisateurs
- `GET /api/users` - Liste tous les utilisateurs
- `GET /api/users/{id}` - Récupère un utilisateur
- `GET /api/users/{id}/tickets` - Tickets d'un utilisateur
- `POST /api/users` - Crée un utilisateur
- `PUT /api/users/{id}` - Modifie un utilisateur
- `DELETE /api/users/{id}` - Supprime un utilisateur

### Tickets
- `GET /api/tickets` - Liste tous les tickets (filtrés par rôle)
- `GET /api/tickets/{id}` - Récupère un ticket
- `POST /api/tickets` - Crée un ticket
- `PUT /api/tickets/{id}` - Modifie un ticket
- `PUT /api/tickets/{id}/assign/{userId}` - Assigne un ticket
- `DELETE /api/tickets/{id}` - Supprime un ticket (admin uniquement)

## Contrôle d'Accès

### Règles de sécurité
- **ADMIN** : Accès complet à tous les tickets et utilisateurs
- **USER** : Accès uniquement à ses propres tickets
- Suppression de tickets : admin uniquement
- Création d'utilisateurs : sans authentification
- Autres opérations : authentification requise

## Tests

### Lancer tous les tests
```bash
mvn test
```

### Couverture de tests
Les tests couvrent :
- Services (UserService, TicketService)
- Contrôleurs (UserController, TicketController)
- Gestion des exceptions
- Contrôle d'accès

## Exemples de Requêtes

### Créer un utilisateur
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "email": "alice@example.com"
  }'
```

### Créer un ticket
```bash
curl -X POST http://localhost:8080/api/tickets \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Nouveau ticket",
    "description": "Description du ticket",
    "status": "EN_COURS"
  }'
```

### Assigner un ticket
```bash
curl -X PUT http://localhost:8080/api/tickets/1/assign/2 \
  -u admin:admin123
```

## Gestion des Erreurs

L'application gère les erreurs suivantes :
- **404** : Ressource non trouvée
- **403** : Accès non autorisé
- **400** : Validation échouée
- **500** : Erreur serveur
## Structure du Projet
```
src/
├── main/
│   ├── java/com/ticketsystem/
│   │   ├── config/          # Configuration Spring
│   │   ├── controller/      # Contrôleurs REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Gestion des exceptions
│   │   ├── model/           # Entités JPA
│   │   ├── repository/      # Repositories Spring Data
│   │   └── service/         # Logique métier
│   └── resources/
│       ├── application.properties
│       └── data.sql (optionnel)
└── test/                    # Tests unitaires
```


## Auteur
Développé dans le cadre d'un test technique Java/Spring Boot


```

---

## Résumé du Projet

### ✅ Fonctionnalités Implémentées

1. **Architecture complète** avec séparation des couches (Controller, Service, Repository)
2. **Entités JPA** : User et Ticket avec relations bidirectionnelles
3. **DTOs et Mappers** pour la conversion des données
4. **Services génériques** utilisant les Generics Java
5. **Contrôle d'accès** avec Spring Security (ADMIN/USER)
6. **Validation** des données avec Bean Validation
7. **Gestion des exceptions** centralisée
8. **Documentation API** avec Swagger/OpenAPI
9. **Tests unitaires** complets avec JUnit et Mockito
10. **Base de données H2** en mémoire

### 🎯 Design Patterns Utilisés

- **Generic Service** : Réutilisation du code CRUD
- **DTO Pattern** : Découplage entre API et modèle
- **Mapper Pattern** : Transformation de données
- **Repository Pattern** : Abstraction de la persistance
- **Template Method** : BaseEntity pour les champs communs
- **Builder Pattern** : Construction d'objets (Lombok)

### 🚀 Pour Démarrer

1. Copier tous les fichiers dans votre projet
2. Exécuter `mvn clean install`
3. Lancer avec `mvn spring-boot:run`
4. Accéder à Swagger : `http://localhost:8080/swagger-ui.html`
5. Tester avec les credentials : `admin/admin123`

Le projet est complet, testé et prêt pour la production ! 🎉
