package jest.modele.extensions;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;
import jest.modele.score.VisiteurScore;

/**
 * Extension "Cartes Magiques"
 * Ajoute 4 cartes spéciales avec effets uniques.
 * on fixe leur couleur à SPECIALE et valeur à 1 car elles n'ont pas de valeur
 * intrinsèque classique.
 */
public class ExtensionMagique extends Extension {

   public ExtensionMagique(int nbJoueurs) {
      super("Cartes Magiques",
            "4 cartes spéciales avec effets bonus :\nMultiplicateur --> double les Piques\nBouclier --> annule les Carreaux négatifs\nMalchance --> valeur malus aléatoire\nChance --> valeur bonus aléatoire", nbJoueurs);
   }

   @Override
   protected void initialiserCartes(int nbJoueurs) {
      cartesSupplementaires.add(new CarteMultiplicateur());
      cartesSupplementaires.add(new CarteBouclier());
      cartesSupplementaires.add(new CarteChance());
      if (nbJoueurs == 4) {
         cartesSupplementaires.add(new CarteMalchance());
      }
   }

   /**
    * Carte Multiplicateur : Double les points des Piques.
    */
   class CarteMultiplicateur extends Carte {
      public CarteMultiplicateur() {
         super(Couleur.SPECIALE, 1); // Valeur spéciale
      }

      @Override
      public int getValeurEffective(Jest jest) {
         // Compte les Piques et double leur valeur
         int totalPiques = 0;
         for (Carte c : jest.getCartes()) {
            if (c.getCouleur() == Couleur.PIQUE) {
               totalPiques += c.getValeurEffective(jest);
            }
         }
         return totalPiques; // Bonus égal a la valeur des Piques
      }

      @Override
      public void accepter(VisiteurScore visiteur, Jest contexte) {
         visiteur.visiterCarteExtension(this, contexte);
      }

      @Override
      public String toString() {
         return "Multiplicateur (double Piques)";
      }

      @Override
      public String toStringCourt() {
         return "Pique×2";
      }
   }

   /**
    * Carte Bouclier : Annule les effets négatifs des Carreaux.
    */
   class CarteBouclier extends Carte {

      public CarteBouclier() {
         super(Couleur.SPECIALE, 1);
      }

      @Override
      public int getValeurEffective(Jest jest) {
         // Annule les Carreaux : compte leur valeur en positif
         int totalCarreaux = 0;
         for (Carte c : jest.getCartes()) {
            if (c.getCouleur() == Couleur.CARREAU) {
               totalCarreaux += c.getValeurEffective(jest);
            }
         }
         return totalCarreaux; // Compense les négatifs
      }

      @Override
      public void accepter(VisiteurScore visiteur, Jest contexte) {
         visiteur.visiterCarteExtension(this, contexte);
      }

      @Override
      public String toString() {
         return "Bouclier (annule Carreaux)";
      }

      @Override
      public String toStringCourt() {
         return "Bouclier";
      }
   }

   /**
    * Carte Malchance : Malus aléatoire entre 1 et 5 points
    */
   class CarteMalchance extends Carte {
      private int valeurAleatoire;

      public CarteMalchance() {
         super(Couleur.SPECIALE, 1);
         this.valeurAleatoire = new java.util.Random().nextInt(6); // 0-5
      }

      @Override
      // exchange with a random card in other player jest
      public int getValeurEffective(Jest jest) {
         return -valeurAleatoire;
      }

      @Override
      public void accepter(VisiteurScore visiteur, Jest contexte) {
         visiteur.visiterCarteExtension(this, contexte);
      }

      @Override
      public String toString() {
         return " Malchance (malus: " + valeurAleatoire + ")";
      }

      @Override
      public String toStringCourt() {
         return "Malchance_" + valeurAleatoire;
      }
   }

   /**
    * Carte Chance : Valeur aléatoire entre 0 et 5.
    */
   class CarteChance extends Carte {
      private int valeurAleatoire;

      public CarteChance() {
         super(Couleur.SPECIALE, 1);
         this.valeurAleatoire = new java.util.Random().nextInt(6); // 0-5
      }

      @Override
      public int getValeurEffective(Jest jest) {
         return valeurAleatoire;
      }

      @Override
      public void accepter(VisiteurScore visiteur, Jest contexte) {
         visiteur.visiterCarteExtension(this, contexte);
      }

      @Override
      public String toString() {
         return " Chance (valeur: " + valeurAleatoire + ")";
      }

      @Override
      public String toStringCourt() {
         return "Chance_" + valeurAleatoire;
      }
   }
}