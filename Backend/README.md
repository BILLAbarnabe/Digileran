# DigiLearn — Backend

API REST de DigiLearn, construite avec Spring Boot 3.4.4 et Java 25. Le backend suit une architecture modulaire organisée autour des couches `Controller → Service → Repository`.

Pour les instructions de lancement de l’ensemble du projet, les prérequis et les routes frontend, consultez le [README à la racine](../README.md).

## Organisation

- `config` : configuration de l’application et de Spring Security.
- `security` : authentification JWT et encodage des mots de passe avec BCrypt.
- `common` : gestion des erreurs API, audit et conservation des données.
- `modules` : `auth`, `user`, `campaign`, `candidacy`, `training`, `session`, `attendance`, `evaluation` et `certificate`.

## Sécurité et comportements métier

- Les jetons d’accès JWT expirent après 15 minutes et sont transmis dans l’en-tête `Authorization`.
- Le jeton de renouvellement est placé dans le cookie `DIGILEARN_REFRESH_TOKEN`, configuré `HttpOnly`, `Secure` et `SameSite=Strict`, avec une durée de 7 jours.
- Les mots de passe sont encodés avec BCrypt (facteur de coût 12).
- Le service de présence fusionne les intervalles réseau qui se chevauchent et valide la présence à partir de 80 %.
- Les certificats PDF sont générés avec OpenPDF et comportent un UUID ainsi qu’un QR code vers l’URL publique de vérification.
- Les événements d’audit utilisent le logger `DIGILEARN_AUDIT`. La politique de conservation des journaux est configurée dans `logback-spring.xml`.
- La tâche de rétention s’exécute quotidiennement à 03:00 UTC et anonymise les enregistrements `personal_data_records` arrivés à échéance, selon le nombre de jours configuré.
- Les API d’inscription, d’activation, de connexion, de renouvellement et de révocation sont exposées sous `/api/auth`.
- Les campagnes, candidatures, formations, séances, présences et évaluations disposent de contrôles d’accès par rôle.

## Configuration

Les valeurs sont définies dans `src/main/resources/application.yml` et peuvent être remplacées avec les variables d’environnement suivantes :

| Variable | Description | Valeur par défaut |
|---|---|---|
| `DIGILEARN_JWT_SECRET` | Clé de signature JWT, au moins 32 caractères | Valeur de développement à remplacer |
| `DIGILEARN_CERTIFICATE_URL` | URL publique de vérification des certificats | `http://localhost:8080/api/certificates/verify` |
| `DIGILEARN_PERSONAL_DATA_RETENTION_DAYS` | Durée de conservation des données personnelles | `1825` jours |

Le profil `dev` définit également un compte administrateur de développement configurable avec `DIGILEARN_DEV_ADMIN_EMAIL` et `DIGILEARN_DEV_ADMIN_PASSWORD`. Réservez ce profil et ses valeurs par défaut au développement local.

La configuration par défaut utilise H2 en mémoire ; les données sont perdues à l’arrêt de l’application. La console H2 est désactivée.

## Lancer et tester

Depuis le dossier `Backend/` :

```bash
mvn test
mvn spring-boot:run
```

L’API est alors accessible sur `http://localhost:8080`.
