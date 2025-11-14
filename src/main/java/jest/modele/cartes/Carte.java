package jest.modele.cartes;

import jest.modele.score.VisiteurScore;
import jest.modele.joueurs.Jest;

/**
 * Classe abstraite représentant une carte générique du jeu Jest.
 * Une carte possède une couleur, une valeur faciale et un état de visibilité.
 */
public abstract class Carte {
    protected Couleur couleur;
    protected int valeurFaciale;
    protected boolean estVisible;

    /**
     * Constructeur de Carte.
     * 
     * @param couleur       Couleur de la carte
     * @param valeurFaciale Valeur imprimée sur la carte (1-4, 0 pour Joker)
     */
    public Carte(Couleur couleur, int valeurFaciale) {
        this.couleur = couleur;
        this.valeurFaciale = valeurFaciale;
        this.estVisible = false;
    }

    /**
     * Retourne la couleur de la carte.
     * 
     * @return Couleur
     */
    public Couleur getCouleur() {
        return couleur;
    }

    /**
     * Retourne la valeur faciale de la carte.
     * 
     * @return Valeur faciale (1-5 ou 0)
     */
    public int getValeurFaciale() {
        return valeurFaciale;
    }

    /**
     * Calcule la valeur effective de la carte dans le contexte d'un Jest.
     * Méthode abstraite implémentée par les sous-classes.
     * 
     * @param jest Jest contenant cette carte (pour règles contextuelles)
     * @return Valeur effective
     */
    public abstract int getValeurEffective(Jest jest);

    /**
     * Vérifie si la carte est un As.
     * 
     * @return true si As (valeur faciale = 1)
     */
    public boolean estAs() {
        return false; // Rédéfini dans As
    }

    /**
     * Vérifie si la carte est un Joker.
     * 
     * @return true si Joker
     */
    public boolean estJoker() {
        return false; // Redéfini dans Joker
    }

    /**
     * Vérifie si la carte est visible.
     * 
     * @return true si face visible
     */
    public boolean estVisible() {
        return estVisible;
    }

    /**
     * Révèle la carte (passe en face visible).
     */
    public void reveler() {
        this.estVisible = true;
    }

    /**
     * Cache la carte (passe en face cachée).
     */
    public void cacher() {
        this.estVisible = false;
    }

    /**
     * Compare la force de cette carte avec une autre.
     * Comparaison basée sur valeur faciale puis couleur.
     * 
     * @param autre Carte à comparer
     * @return Valeur négative si this < autre, 0 si égale, positive si this > autre
     */
    public int comparerForce(Carte autre) {
        // Comparer d'abord par valeur faciale
        int compareValeur = Integer.compare(this.valeurFaciale, autre.valeurFaciale);
        if (compareValeur != 0) {
            return compareValeur;
        }
        // Si égalité, comparer par couleur
        return this.couleur.compareTo(autre.couleur);
    }

    /**
     * Accepte un visiteur pour le pattern Visitor.
     * 
     * @param visiteur Visiteur de score
     */
    public abstract void accepter(VisiteurScore visiteur, Jest contexte);

    @Override
    public String toString() {
        String valeur;
        if (this.estAs()) {
            valeur = "As";
        } else {
            valeur = String.valueOf(valeurFaciale);
        }
        return valeur + " de " + couleur.toString();
    }

    /**
     * Représentation courte pour affichage compact.
     * 
     * @return Représentation abrégée (ex: "3♠", "A♠")
     */
    public String toStringCourt() {
        String valeur;
        if (this.estAs()) {
            valeur = "A";
        } else {
            valeur = String.valueOf(valeurFaciale);
        }
        return valeur + couleur.getSymbole();
    }
}