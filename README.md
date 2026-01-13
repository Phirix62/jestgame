# Jest Game

Projet LO02 - Jeu de cartes Jest en Java

## Description

Implémentation du jeu de cartes Jest où les joueurs doivent constituer le meilleur Jest en choisissant stratégiquement dans les offres des adversaires.

## Fonctionnalités

- 3 ou 4 joueurs (humains ou IA)
- 3 stratégies IA : aléatoire, gloutonne, défensive
- Extension Cartes Magiques
- 3 variantes : Standard, Tactique, Rapide
- Sauvegarde/chargement de parties
- **Double interface** : Terminal + Interface Graphique (Swing)
- La concurrence entre les vues n'est pas fonctionelle.

## Technos

- Java 25
- Maven 3.9
- Swing pour l'interface graphique

## Commandes utiles

### Execution
```bash
# Compiler
mvn clean compile

# Lancer
mvn exec:java
```

### Générer la Javadoc

```bash
# Générer la documentation dans le dossier docs/
mvn javadoc:javadoc
```

### Générer le jar 

```bash
# Générer la documentation dans le dossier docs/
mvn clean package
```

##  Structure du projet

Structure MVC respecté au maximum.

- Le modèle contient tout le coeur du jeu
- Le contrôleur gère l'initialisation et les interactions entre les vues et le modèle via le pattern observer

Les principes SOLID ont été appliqué au maximum, en essayant de garder le code clair et lisible. Quelques fichiers font exception : 

- partie.java qui est la façade du modèle
- Les 2 fichiers Vue qui affichent beaucoup d'élements, récupèrent les notifications. Typiquement VueTerminal.java et VueGraphique.java


##  Règles du jeu

### Objectif

Collecter les cartes qui rapportent le plus de points en fin de partie pour remporter les trophées.

### Déroulement

1. **Distribution** : Chaque joueur reçoit 2 cartes (ou plus selon variante)
2. **Création d'offres** : Les joueurs créent une offre (1 carte cachée, les autres visibles)
3. **Prises de cartes** : À tour de rôle, chaque joueur prend une carte dans l'offre d'un adversaire
4. **Fin de partie** : Quand il n'y a plus assez de cartes pour distribuer (pioche vide)

### Scoring

- Voir règles sur le pdf

### Trophées

- **Highest** : Plus haute valeur faciale d'une couleur
- **Lowest** : Plus basse valeur faciale d'une couleur
- **Majority** : Plus de cartes d'une même valeur
- **Best Jest** : Meilleur Jest
- **Joker** : Détient le Joker

##  Stratégies IA disponibles

- **Aléatoire** : Choix au hasard
- **Gloutonne** : Privilégie les cartes à haute valeur faciale
- **Défensive** : Evite les carreaux et ne prend pas de risques

##  Extensions

### Cartes Magiques

- **Multiplicateur** : Double les points des Piques
- **Bouclier** : Annule les Carreaux négatifs
- **Chance** : Bonus aléatoire (0-5 points)
- **Malchance** : Malus aléatoire (1-5 points)

## ⚙️ Variantes

### Standard
Règles classiques du jeu (2 cartes par main)

### Tactique
- 3 cartes par main

### Rapide
- 3 tour maximum


## 🔗 Liens utiles

- [Documentation Java](https://docs.oracle.com/en/java/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## Sauvegarde

Fichiers `.jest` dans le dossier `sauvegardes/`

## 📄 Licence

Ce projet est développé dans le cadre académique du cours LO02 à l'UTT.

## Auteurs

Nathan Honoré et Ayat Atraoui - LO02 - UTT
