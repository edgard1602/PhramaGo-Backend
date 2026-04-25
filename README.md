# PharmaGo Backend

API REST backend pour **PharmaGo**, une application mobile de localisation
des pharmacies de garde à Lomé, Togo.

> PharmaGo sera intégré comme module pharmacie au sein de la plateforme
> de santé **SanoConnect**.

## Stack Technique

- **Java 21** — Virtual Threads, Records, Pattern Matching
- **Spring Boot 4.0.x** — Web, Security, Data JPA, Validation
- **PostgreSQL 16** — Base de données principale
- **Flyway** — Migrations versionnées
- **MapStruct** — Mapping entités/DTOs
- **Docker & Docker Compose** — Environnement de développement
- **OpenAPI / Swagger** — Documentation API

## Prérequis

- Java 21+
- Docker & Docker Compose
- Maven 3.9+

## Lancer le projet en local

### 1. Cloner le repo

```bash
git clone https://github.com/ton-username/pharmago-backend.git
cd pharmago-backend
```

### 2. Démarrer la base de données

```bash
docker-compose up -d
```

### 3. Lancer l'application

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Accéder à la documentation API
http://localhost:8080/swagger-ui.html

## Structure du Projet

src/main/java/com/pharmago/backend/
├── config/         # Configuration Spring (Security, OpenAPI)
├── controller/     # Couche HTTP (endpoints)
├── service/        # Logique métier
├── repository/     # Accès base de données
├── entity/         # Entités JPA
├── dto/            # Records Java (request/response)
├── mapper/         # MapStruct mappers
├── exception/      # Exceptions métier + handler global
└── util/           # Utilitaires (calcul distance Haversine)

## Endpoints Principaux

### Publics (Application Mobile)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/pharmacies/garde` | Pharmacies de garde actives |
| GET | `/api/v1/pharmacies/{id}` | Détail d'une pharmacie |
| POST | `/api/v1/signalements` | Signaler une erreur |

### Admin (Back-office)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/admin/pharmacies` | Créer une pharmacie |
| PUT | `/api/v1/admin/pharmacies/{id}` | Modifier une pharmacie |
| DELETE | `/api/v1/admin/pharmacies/{id}` | Supprimer une pharmacie |
| POST | `/api/v1/admin/gardes` | Ajouter une garde |
| GET | `/api/v1/admin/signalements` | Consulter les signalements |
| PATCH | `/api/v1/admin/signalements/{id}` | Traiter un signalement |

## Variables d'Environnement (Production)

```env
DATABASE_URL=jdbc:postgresql://host:5432/pharmago
DATABASE_USER=your_user
DATABASE_PASSWORD=your_password
ADMIN_API_KEY=your_secret_key
```

## Lancer les Tests

```bash
./mvnw test
```

> Les tests d'intégration utilisent Testcontainers.
> Docker doit être actif pour les lancer.

## Conventions Git

Ce projet suit les **Conventional Commits** :

feat(scope)     : nouvelle fonctionnalité
fix(scope)      : correction de bug
chore(scope)    : tâches techniques
test(scope)     : ajout ou modification de tests
docs(scope)     : documentation
refactor(scope) : refactoring sans changement de comportement

### Branches

main        → code stable, déployable uniquement
develop     → branche d'intégration
feature/*   → nouvelles fonctionnalités
fix/*       → corrections de bugs

## Roadmap

- [x] Setup projet et architecture
- [x] Configuration Docker + PostgreSQL
- [x] Migrations Flyway
- [ ] Entités JPA (Pharmacie, Garde, Signalement)
- [ ] Endpoints publics (géolocalisation + fallback quartier)
- [ ] Back-office admin
- [ ] Système de signalement
- [ ] Sécurité (API Key admin)
- [ ] Tests unitaires et d'intégration
- [ ] Déploiement (Railway / Render)
- [ ] Cache Redis (V2)
- [ ] Notifications push hebdomadaires (V2)
- [ ] Intégration plateforme SanoConnect (V2)

## Auteur

**Edgard** — Junior Backend Developer
Spécialisation : Java, Spring Boot, Docker

---

> **PharmaGo** est la première brique d'une vision plus large :
> **SanoConnect**, une plateforme de santé digitale complète pour l'Afrique.