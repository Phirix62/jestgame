package jest.modele.extensions;

import jest.modele.cartes.*;
import java.util.*;
import java.io.Serializable;

/**
 * Représente une extension du jeu avec de nouvelles cartes.
 * Une extension peut ajouter des cartes avec des mécaniques spéciales.
 */
public abstract class Extension implements Serializable {
   private static final long serialVersionUID = 1L;
   protected String nom;
   protected String description;
   protected List<Carte> cartesSupplementaires;

   /**
    * Constructeur d'Extension.
    * 
    * @param nom         Nom de l'extension
    * @param description Description
    * @param nbJoueurs   Nombre de joueurs (3 ou 4) pour initialiser les cartes
    */
   public Extension(String nom, String description, int nbJoueurs) {
      this.nom = nom;
      this.description = description;
      this.cartesSupplementaires = new ArrayList<>();
      this.initialiserCartes(nbJoueurs);
   }

   /**
    * Initialise les cartes de l'extension.
    * À implémenter dans les sous-classes.
    */
   protected abstract void initialiserCartes(int nbJoueurs);

   /**
    * Retourne les cartes supplémentaires.
    * @return Liste des cartes
    */
   public List<Carte> getCartesSupplementaires() {
      return new ArrayList<>(cartesSupplementaires);
   }

   /**
    * Ajoute les cartes de l'extension à un paquet.
    * 
    * @param paquet Paquet à modifier
    */
   public void ajouterAuPaquet(Paquet paquet) {
      paquet.ajouterCartes(cartesSupplementaires);
   }

   /**
    * Retourne le nom de l'extension.
    * 
    * @return Nom
    */
   public String getNom() {
      return nom;
   }

   /**
    * Retourne la description.
    * 
    * @return Description
    */
   public String getDescription() {
      return description;
   }

   @Override
   public String toString() {
      return nom + " (" + cartesSupplementaires.size() + " cartes)";
   }
}