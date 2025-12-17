# 🃏 Jest Game

Jeu de cartes stratégique développé en Java dans le cadre du projet LO02.

## 📋 Description

Jest est un jeu de cartes compétitif où les joueurs doivent constituer la meilleure collection (Jest) en choisissant stratégiquement des cartes parmi les offres des autres joueurs. Le jeu propose plusieurs variantes de règles et extensions pour enrichir l'expérience.

### Caractéristiques

- 🎮 **3 ou 4 joueurs** (humains ou IA)
- 🤖 **Stratégies IA variées** (aléatoire, offensive, défensive, etc.)
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
git clone https://github.com/votre-username/jest-game.git
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
4. **Fin de partie** : Quand il n'y a plus assez de cartes pour distribuer

### Scoring

- **Piques ♠️** : Augmentent la valeur du Jest
- **Trèfles ♣️** : Augmentent la valeur du Jest
- **Carreaux ♦️** : Diminuent la valeur du Jest
- **Cœurs ♥️** : Diminuent la valeur du Jest
- **Joker 🃏** : Bonus de 4 points
- **As** : 5 points (ou 1 si isolé)

### Trophées

- **Highest** : Plus haute valeur faciale
- **Lowest** : Plus basse valeur faciale
- **Majority** : Plus de cartes d'une même valeur
- **Best Jest** : Meilleur Jest (selon les couleurs)
- **Joker** : Détient le Joker

## 🤖 Stratégies IA disponibles

- **Aléatoire** : Choix au hasard
- **Valeur Haute** : Privilégie les cartes fortes
- **Éviter Joker** : Évite Jokers et Cœurs
- **Pique-Trèfle** : Maximise Piques et Trèfles
- **Majorité** : Vise le trophée "Majorité"
- **Best Jest** : Optimise pour le meilleur Jest

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
- 3 cartes par main
- Partie limitée à 6 tours

## 💾 Système de sauvegarde

Les parties sont sauvegardées dans le dossier `sauvegardes/` au format `.jest` (sérialisation Java).

```bash
# Structure d'une sauvegarde
sauvegardes/
├── ma_partie.jest
├── partie_1234567890.jest
└── ...
```

## 🐛 Dépannage

### Le jeu ne se lance pas

```bash
# Vérifier la version Java
java -version

# Recompiler complètement
mvn clean compile

# Vérifier les dépendances
mvn dependency:tree
```

### Erreur de compilation

```bash
# Nettoyer et recompiler
mvn clean install -U
```

### Problème de sauvegarde

Vérifiez que le dossier `sauvegardes/` existe et est accessible en écriture.

## 📝 Scripts utiles

### Windows

**Lancer le jeu** (`lancer.bat`)
```bat
@echo off
mvn clean compile exec:java
pause
```

**Build complet** (`build.bat`)
```bat
@echo off
echo Build complet...
mvn clean package javadoc:javadoc
echo.
echo JAR: target\jestgame-1.0-SNAPSHOT.jar
echo Doc: docs\index.html
pause
```

### Linux/Mac

**Lancer le jeu** (`lancer.sh`)
```bash
#!/bin/bash
mvn clean compile exec:java
```

**Build complet** (`build.sh`)
```bash
#!/bin/bash
echo "Build complet..."
mvn clean package javadoc:javadoc
echo ""
echo "JAR: target/jestgame-1.0-SNAPSHOT.jar"
echo "Doc: docs/index.html"
```

Rendre exécutable :
```bash
chmod +x lancer.sh build.sh
```

## 👥 Auteurs

- **Votre Nom** - Projet LO02 - UTC

## 📄 Licence

Ce projet est développé dans le cadre académique du cours LO02 à l'UTC.

## 🔗 Liens utiles

- [Règles officielles Jest](https://www.goodlittlegames.co.uk/jest)
- [Documentation Java](https://docs.oracle.com/en/java/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Note** : Ce projet nécessite Java 11+ pour fonctionner correctement.
