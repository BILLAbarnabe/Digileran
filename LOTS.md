# Répartition du projet en 4 lots

Proposition de découpage des travaux de DigiLearn, dont le frontend est développé avec Angular et l'API avec Spring Boot.

## Lot 1 — Persistance et modèles de données (`lot1-persistance`)

**Backend — Spring Boot**

- Définir les entités JPA principales : `User`, `Campaign`, `Candidacy`, `Training`, `Session`, `Attendance`, `Evaluation` et `Certificate`.
- Configurer les relations entre entités (`@OneToMany`, `@ManyToOne`, etc.) et les contraintes de persistance.
- Créer les interfaces Repository basées sur `JpaRepository`.

**Base de données — PostgreSQL**

- Définir le schéma et les scripts d'initialisation, dont `schema.sql`.
- Valider les contraintes d'intégrité : clés étrangères, champs obligatoires, unicité et index.
- Utiliser des UUID pour identifier les certificats.

**Livrable :** un modèle de données et une couche de persistance prêts à être utilisés par les services métier.

## Lot 2 — Services métier, sécurité et API (`lot2-services-securite`)

**Backend — Spring Boot**

- Implémenter les services métier et les endpoints `/api` : authentification, utilisateurs, campagnes, candidatures, formations, séances, présences, évaluations et certificats.
- Appliquer la règle de validation des 80 % de présence.
- Générer les certificats PDF avec OpenPDF.
- Sécuriser les endpoints avec Spring Security et JWT, avec des autorisations adaptées aux rôles.

**Frontend — Angular**

- Mettre en place `AuthService` et les services d'accès à l'API.
- Configurer les intercepteurs HTTP pour transmettre le jeton JWT.
- Centraliser les éléments transverses dans `CoreModule` ou les structures équivalentes du projet.

**Livrable :** une API sécurisée et un socle Angular connecté à l'authentification.

## Lot 3 — Interfaces et parcours principaux (`lot3-interface`)

**Frontend — Angular**

- Réaliser les interfaces avec Angular Material : catalogue des campagnes, formulaire de candidature et activation de compte.
- Créer les espaces de gestionnaire, formateur et apprenant, y compris les tableaux de bord principaux.
- Configurer le routage et le lazy loading pour séparer les espaces `manager/`, `formateur/` et `apprenant/`.
- Relier les écrans aux services et endpoints du lot 2.

**Livrable :** les principaux parcours utilisateurs intégrés à l'API.

## Lot 4 — Visualisation, PWA et finitions (`lot4-visualisation`)

**Frontend — Angular**

- Présenter les indicateurs du gestionnaire, notamment le taux de réussite et la présence moyenne, avec Chart.js ou Ngx-charts.
- Configurer le Service Worker et `ngsw-config.json` pour les fonctionnalités PWA.
- Permettre la consultation hors ligne des cours et la synchronisation de la progression à la reconnexion.
- Finaliser l'intégration, les tests de bout en bout et les ajustements d'interface.

**Livrable :** des tableaux de bord intégrés et une application finalisée avec ses fonctionnalités PWA.

## Ordre et dépendances

L'ordre recommandé est **Lot 1 → Lot 2 → Lot 3 → Lot 4**. Le socle frontend du lot 2 peut commencer en parallèle si les contrats d'API sont convenus à l'avance. Le mode hors ligne du lot 4 doit être validé en tenant compte des autorisations et des conflits de synchronisation.
