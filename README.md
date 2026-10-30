# DigiLearn

DigiLearn est une plateforme de formation composée d’un frontend Angular et d’une API REST Spring Boot. Le dépôt regroupe les deux applications, leurs instructions de lancement et les principaux points d’entrée de l’API.

## Contenu du dépôt

```text
.
├── Backend/       API REST Spring Boot, sécurité et modules métier
├── front-end/     Application web Angular
└── README.md
```

Le backend est organisé par domaines métier, avec une séparation Controller–Service–Repository. Le frontend utilise Angular avec des composants chargés à la demande. Les parcours présentés côté web comprennent les espaces public, authentification, gestionnaire, formateur et apprenant.

## Prérequis

- Java JDK 25
- Apache Maven
- Node.js et npm (versions compatibles avec Angular 19)

Vérifiez les outils disponibles :

```bash
java -version
mvn -version
node --version
npm --version
```

## Démarrage en local

Lancer l’API dans un premier terminal, depuis la racine du dépôt :

```bash
cd Backend
mvn spring-boot:run
```

L’API écoute par défaut sur `http://localhost:8080`.

Dans un second terminal, installer les dépendances et lancer le frontend :

```bash
cd front-end
npm ci
npm start
```

Ouvrez ensuite `http://localhost:4200`. Le serveur Angular recharge l’application après les changements de code.

## Configuration du backend

Les paramètres par défaut se trouvent dans `Backend/src/main/resources/application.yml`. Les valeurs suivantes peuvent être remplacées par des variables d’environnement :

| Variable | Description | Valeur par défaut |
|---|---|---|
| `DIGILEARN_JWT_SECRET` | Clé de signature des JWT (au moins 32 caractères) | Valeur de développement à remplacer |
| `DIGILEARN_CERTIFICATE_URL` | URL publique de vérification des certificats | `http://localhost:8080/api/certificates/verify` |
| `DIGILEARN_PERSONAL_DATA_RETENTION_DAYS` | Durée de conservation des données personnelles, en jours | `1825` |
| `DIGILEARN_DEV_ADMIN_EMAIL` | Adresse du compte administrateur de développement | `admin@digilearn.local` |
| `DIGILEARN_DEV_ADMIN_PASSWORD` | Mot de passe du compte administrateur de développement | Valeur de développement à remplacer |

Le compte administrateur de développement est défini dans `application-dev.yml` et n’est à utiliser qu’avec le profil `dev`. N’utilisez pas les valeurs de développement en production. En production, configurez des secrets forts via un gestionnaire de secrets ou l’environnement d’exécution.

Le profil par défaut utilise une base H2 en mémoire : les données ne sont donc pas conservées après l’arrêt de l’application. La console H2 est désactivée par défaut.

## Fonctionnalités et API

Les principaux domaines du backend sont :

- **Authentification et utilisateurs** : inscription, activation, connexion, renouvellement et révocation de session, profil utilisateur et contrôle d’accès par rôle.
- **Campagnes et candidatures** : consultation et gestion des campagnes, dépôt et traitement des candidatures.
- **Formations et séances** : consultation et publication des formations, création de séances et génération de QR codes.
- **Présences** : enregistrement d’événements réseau et calcul de la présence à partir des intervalles fusionnés (seuil de validation de 80 %).
- **Évaluations** : création de barèmes, saisie de notes et consultation des résultats autorisés.
- **Certificats** : génération de certificats PDF avec QR code et vérification publique.
- **Audit et conservation** : journal d’audit dédié et tâche quotidienne de traitement des données arrivées à échéance selon la politique de rétention.

Les points d’entrée principaux sont préfixés par `/api` :

| Domaine | Préfixe |
|---|---|
| Authentification | `/api/auth` |
| Utilisateurs | `/api/users` |
| Campagnes | `/api/campaigns` |
| Candidatures | `/api/candidacies` |
| Formations | `/api/trainings` |
| Séances | `/api/sessions` |
| Présences | `/api/attendance` |
| Évaluations | `/api/evaluations` |
| Certificats | `/api/certificates` |

Les routes web Angular sont `/`, `/auth`, `/manager`, `/formateur` et `/apprenant`. Le service de progression hors ligne du frontend stocke localement les positions de lecture dans IndexedDB.

Les requêtes authentifiées transmettent le jeton d’accès dans l’en-tête `Authorization`. Sa durée de validité est de 15 minutes. Le jeton de renouvellement est placé dans un cookie `HttpOnly` d’une durée de 7 jours.

## Répartition du projet en 4 lots

La proposition de découpage Spring Boot + Angular est détaillée dans [LOTS.md](LOTS.md). Les lots couvrent la persistance, les services métier et la sécurité, les interfaces principales, puis les indicateurs et les finitions PWA.

## Tests et compilation

Depuis `Backend/`, exécuter les tests du backend :

```bash
mvn test
```

Depuis `front-end/`, exécuter les tests unitaires Angular :

```bash
npm test
```

Compiler le frontend :

```bash
npm run build
```

La sortie de compilation Angular est produite dans `front-end/dist/`.

## Documentation détaillée

- [Documentation du backend](Backend/README.md)
- [Documentation Angular](front-end/README.md)
