package jest.modele.cartes;

import jest.modele.score.VisiteurScore;
import jest.modele.joueurs.Jest;

/**
 * Représente le Joker, carte spéciale du jeu Jest.
 * Le Joker n'a pas de couleur définie et a une valeur faciale de 0.
 * Son effet dépend des Cœurs présents dans le Jest.
 */
public class Joker extends Carte {
    
    /**
     * Constructeur de Joker.
     * Le Joker n'a pas de couleur réelle (on utilise arbitrairement COEUR).
     */
    public Joker() {
        super(Couleur.COEUR, 0); // Couleur arbitraire
    }
    
    /**
     * Identifie cette carte comme un Joker.
     * @return true
     */
    @Override
    public boolean estJoker() {
        return true;
    }
    
    /**
     * Calcule la valeur effective du Joker.
     * Règles :
     * - Sans Cœurs : +4 points
     * - Avec 1-3 Cœurs : 0 points (les Cœurs deviennent négatifs)
     * - Avec 4 Cœurs : 0 points (les Cœurs deviennent positifs)
     * @param jest Jest contenant le Joker
     * @return Valeur effective (0 ou 4)
     */
    @Override
    public int getValeurEffective(Jest jest) {
        int nbCoeurs = jest.compterCartesCouleur(Couleur.COEUR);
        if (nbCoeurs == 0) {
            return 4;
        } else {
            return 0;
        }
    }
    
    /**
     * Accepte un visiteur pour le calcul de score.
     * @param visiteur Visiteur de score
     * @param contexte Jest contenant le Joker
     */
    @Override
    public void accepter(VisiteurScore visiteur, Jest contexte) {
        visiteur.visiterJoker(this, contexte);
    }
    
    @Override
    public String toString() {
        return "Joker 🃏";
    }
    
    @Override
    public String toStringCourt() {
        return "Jkr";
    }
}