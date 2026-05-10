# PharmaGo Backend

API REST backend pour **PharmaGo**, une application mobile de localisation
des pharmacies de garde à Lomé, Togo.

> PharmaGo sera intégré comme module pharmacie au sein de la plateforme
> de santé **SanoConnect**.

## 🚀 API en Production

```
https://phramago-backend-production.up.railway.app
```

### Tester l'API

```bash
# Pharmacies de garde actives
curl https://phramago-backend-production.up.railway.app/api/v1/pharmacies/garde

# Documentation Swagger
https://phramago-backend-production.up.railway.app/swagger-ui.html
```

## Stack Technique

- **Java 21** — Virtual Threads, Records, Pattern Matching
- **Spring Boot 4.0.5** — Web, Security, Data JPA, Validation
- **PostgreSQL 17** — Base de données principale
- **Flyway** — Migrations versionnées
- **MapStruct** — Mapping entités/DTOs
- **Docker & Docker Compose** — Environnement de développement
- **OpenAPI / Swagger** — Documentation API
- **Railway** — Déploiement en production

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
./mvnw spring-boot:run
```

### 4. Accéder à la documentation API

```
http://localhost:9090/swagger-ui.html
```

## Structure du Projet

```
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
```

## Endpoints

### Publics (Application Mobile)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/pharmacies/garde` | Pharmacies de garde actives |
| GET | `/api/v1/pharmacies/garde?latitude=6.13&longitude=1.21` | Triées par distance |
| GET | `/api/v1/pharmacies/garde?quartier=Hédzranawoé` | Filtrées par quartier |
| GET | `/api/v1/pharmacies/{id}` | Détail d'une pharmacie |
| GET | `/api/v1/pharmacies/quartiers` | Liste des quartiers |
| POST | `/api/v1/signalements` | Signaler une erreur |

### Admin (Back-office)

> Nécessite le header `X-API-Key`

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/admin/pharmacies` | Créer une pharmacie |
| PUT | `/api/v1/admin/pharmacies/{id}` | Modifier une pharmacie |
| DELETE | `/api/v1/admin/pharmacies/{id}` | Supprimer une pharmacie |
| PATCH | `/api/v1/admin/pharmacies/{id}/toggle-actif` | Activer/désactiver |
| POST | `/api/v1/admin/gardes` | Ajouter une garde |
| GET | `/api/v1/admin/gardes/pharmacie/{id}` | Gardes d'une pharmacie |
| DELETE | `/api/v1/admin/gardes/{id}` | Supprimer une garde |
| GET | `/api/v1/admin/signalements` | Consulter les signalements |
| PATCH | `/api/v1/admin/signalements/{id}` | Traiter un signalement |

## Variables d'Environnement (Production)

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/pharmago
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password
ADMIN_API_KEY=your_secret_key
PORT=8080
```

## Lancer les Tests

```bash
./mvnw test
```

> Les tests d'intégration utilisent Testcontainers.
> Docker doit être actif pour les lancer.

## Conventions Git

Ce projet suit les **Conventional Commits** :

```
feat(scope)     : nouvelle fonctionnalité
fix(scope)      : correction de bug
chore(scope)    : tâches techniques
test(scope)     : ajout ou modification de tests
docs(scope)     : documentation
refactor(scope) : refactoring sans changement de comportement
```

### Branches

```
main        → code stable, déployé en production
develop     → branche d'intégration
feature/*   → nouvelles fonctionnalités
fix/*       → corrections de bugs
```

## Roadmap

- [x] Setup projet et architecture
- [x] Configuration Docker + PostgreSQL
- [x] Migrations Flyway
- [x] Entités JPA (Pharmacie, Garde, Signalement)
- [x] Endpoints publics (géolocalisation + fallback quartier)
- [x] Back-office admin
- [x] Système de signalement
- [x] Sécurité (API Key)
- [x] Tests unitaires et d'intégration
- [x] Déploiement Railway
- [ ] Saisie des pharmacies de Lomé
- [ ] Cache Redis (V2)
- [ ] Notifications push hebdomadaires (V2)
- [ ] Intégration plateforme SanoConnect (V2)

## Auteur

**Edgard** — Junior Backend Developer
Spécialisation : Java, Spring Boot, Docker

---

> **PharmaGo** est la première brique d'une vision plus large :
> **SanoConnect**, une plateforme de santé digitale complète et adapté aux réalités du Togo et par extention aux réalités de l'Afrique de l'ouest.
