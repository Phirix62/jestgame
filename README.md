# 🃏 Jest Game

Jeu de cartes stratégique développé en Java dans le cadre du projet LO02.

## 📋 Description

Jest est un jeu de cartes compétitif où les joueurs doivent constituer la meilleure collection (Jest) en choisissant stratégiquement des cartes parmi les offres des autres joueurs. Le jeu propose plusieurs variantes de règles et extensions pour enrichir l'expérience.

### Caractéristiques

- 🎮 **3 ou 4 joueurs** (humains ou IA)
- 🤖 **Stratégies IA variées** (aléatoire, gloutonnee, défensive, etc.)
- 🎴 **Extensions** avec cartes spéciales (Cartes Magiques)
- ⚙️ **Variantes de règles** (Standard, Tactique, Rapide)
- 💾 **Système de sauvegarde/chargement**
- 🏆 **Système de trophées** et scoring

## 🛠️ Technologies

- **Java 25**
- **Maven 3.x**
- **Architecture MVC** avec patterns (Visitor, Strategy, Facade)

## 📦 Installation

### Prérequis

- Java JDK 11 ou supérieur
- Maven 3.6 ou supérieur

### Cloner le projet

```bash
git clone https://github.com/Phirix62/jestgame.git
cd jest-game
```

## 🚀 Utilisation

### Lancer le jeu

```bash
# Compiler et exécuter directement
mvn clean compile exec:java

# Ou simplement (si déjà compilé)
mvn exec:java
```

### Créer un JAR exécutable

```bash
# Compiler et créer le JAR
mvn clean package

# Le JAR est créé dans target/
# Exécuter le JAR
java -jar target/jestgame-1.0-SNAPSHOT.jar
```

### Modes de jeu

Au lancement, vous pouvez :
1. **Nouvelle partie** - Configurer et démarrer une nouvelle partie
2. **Charger une partie** - Reprendre une partie sauvegardée
3. **Quitter**

## 📚 Documentation

### Générer la Javadoc

```bash
# Générer la documentation dans le dossier docs/
mvn javadoc:javadoc

# Ouvrir la documentation
# Windows
start docs\index.html

# Linux
xdg-open docs/index.html

# Mac
open docs/index.html
```

### Générer un JAR de documentation

```bash
# Crée target/jestgame-1.0-SNAPSHOT-javadoc.jar
mvn javadoc:jar
```

## 🏗️ Structure du projet

```
jest-game/
├── src/
│   └── main/
│       └── java/
│           └── jest/
│               ├── modele/
│               │   ├── cartes/        # Classes des cartes
│               │   ├── jeu/           # Moteur de jeu
│               │   ├── joueurs/       # Joueurs et stratégies
│               │   ├── score/         # Calcul des scores
│               │   └── extensions/    # Extensions et variantes
│               └── utilitaires/       # Outils (sauvegarde, etc.)
├── docs/                              # Documentation Javadoc
├── sauvegardes/                       # Parties sauvegardées
├── pom.xml                            # Configuration Maven
└── README.md
```

## 🎯 Commandes Maven utiles

### Compilation et build

```bash
# Nettoyer le projet
mvn clean

# Compiler uniquement
mvn compile

# Exécuter les tests (si présents)
mvn test

# Créer le package (JAR)
mvn package

# Cycle complet
mvn clean package
```

### Exécution

```bash
# Exécuter l'application
mvn exec:java

# Avec arguments personnalisés
mvn exec:java -Dexec.args="arg1 arg2"
```

### Documentation

```bash
# Générer la Javadoc
mvn javadoc:javadoc

# Générer un JAR de documentation
mvn javadoc:jar

# Générer le site complet (avec profil)
mvn site -Pdocumentation
```

### Nettoyage

```bash
# Supprimer target/ et docs/
mvn clean

# Supprimer uniquement la Javadoc
mvn clean:clean@clean-javadoc
```

## 🎮 Règles du jeu

### Objectif

Collecter les cartes qui rapportent le plus de points en fin de partie pour remporter les trophées.

### Déroulement

1. **Distribution** : Chaque joueur reçoit 2 cartes (ou plus selon variante)
2. **Création d'offres** : Les joueurs créent une offre (1 carte cachée, les autres visibles)
3. **Prises de cartes** : À tour de rôle, chaque joueur prend une carte dans l'offre d'un adversaire
4. **Fin de partie** : Quand il n'y a plus assez de cartes pour distribuer (pioche vide)

### Scoring

- Voir règles officielles

### Trophées

- **Highest** : Plus haute valeur faciale d'une couleur
- **Lowest** : Plus basse valeur faciale d'une couleur
- **Majority** : Plus de cartes d'une même valeur
- **Best Jest** : Meilleur Jest
- **Joker** : Détient le Joker

## 🤖 Stratégies IA disponibles

- **Aléatoire** : Choix au hasard
- **Gloutonne** : Privilégie les cartes à haute valeur faciale
- **Défensive** : Evite les carreaux et ne prend pas de risques

## 🎴 Extensions

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
- Plus de choix stratégiques

### Rapide
- Partie limitée à 3 tours

## 💾 Système de sauvegarde

Les parties sont sauvegardées dans le dossier `sauvegardes/` au format `.jest` (sérialisation Java).

```bash
# Structure d'une sauvegarde
sauvegardes/
├── ma_partie.jest
├── partie_1234567890.jest
└── ...
```

## 🏗️ Status

![Build Status](https://github.com/Phirix62/jestgame/workflows/CI%20Status/badge.svg)
![Javadoc](https://github.com/Phirix62/jestgame/workflows/Generate%20and%20Deploy%20Javadoc/badge.svg)


## 👥 Auteurs

- **Nathan Honoré et Ayat Atraoui** - Projet LO02 - UTT
## 📄 Licence

Ce projet est développé dans le cadre académique du cours LO02 à l'UTT.

## 🔗 Liens utiles

- [Documentation Java](https://docs.oracle.com/en/java/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Note** : Ce projet nécessite Java 11+ pour fonctionner correctement.
