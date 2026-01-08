# Resources pour Jest

Ce dossier contient les ressources graphiques pour le jeu Jest.

## Structure des images de cartes

Les images des cartes doivent être placées dans le dossier `images/cartes/` avec la convention de nommage suivante :

### Cartes normales
- Format: `{COULEUR}_{VALEUR}.png`
- Exemples:
  - `COEUR_1.png` - As de Cœur
  - `COEUR_2.png` - 2 de Cœur
  - `CARREAU_3.png` - 3 de Carreau
  - `TREFLE_4.png` - 4 de Trèfle
  - `PIQUE_5.png` - 5 de Pique

### Cartes spéciales
- `JOKER.png` - Joker
- `CARTE_DOS.png` - Dos de carte (pour les cartes cachées)

### Cartes magiques (extension)
- `BOUCLIER.png`
- `CHANCE.png`
- `MALCHANCE.png`
- `MULTIPLICATEUR.png`

## Dimensions recommandées

- Cartes: 100x140 pixels
- Miniatures (pour l'interface): Les images seront redimensionnées automatiquement

## Format

- Format d'image: PNG avec transparence
- Résolution: 72 DPI minimum

## Note

Pour l'instant, l'interface graphique affiche des représentations textuelles des cartes.
Vous pouvez ajouter vos propres images en suivant la convention ci-dessus.
