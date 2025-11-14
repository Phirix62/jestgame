package jest.modele.cartes;

import jest.modele.score.VisiteurScore;
import jest.modele.joueurs.Jest;

/**
 * Représente un As, carte spéciale avec valeur variable.
 * Règle : L'As vaut 5 s'il est la seule carte de sa couleur dans le Jest, sinon il vaut 1.
 */
public class As extends Carte {
    
    /**
     * Constructeur d'As.
     * @param couleur Couleur de l'As
     */
    public As(Couleur couleur) {
        super(couleur, 1); // Valeur faciale = 1
    }
    
    /**
     * Calcule la valeur effective de l'As selon le contexte.
     * @param jest Jest contenant cet As
     * @return 5 si seul de sa couleur, 1 sinon
     */
    @Override
    public int getValeurEffective(Jest jest) {
        int nbCartesCouleur = jest.compterCartesCouleur(this.couleur);
        return nbCartesCouleur == 1 ? 5 : 1;
    }
    
    /**
     * Accepte un visiteur pour le calcul de score.
     * @param visiteur Visiteur de score
     * @param contexte Jest contenant cet As
     */
    @Override
    public void accepter(VisiteurScore visiteur, Jest contexte) {
        // L'As est traité comme une CarteNormale par les visiteurs
        // mais avec sa valeur effective calculée dynamiquement
        visiteur.visiterCarteNormale(new CarteNormale(this.couleur, this.valeurFaciale) {
            @Override
            public int getValeurEffective(Jest j) {
                return As.this.getValeurEffective(j);
            }
        }, contexte);
    }
    
    @Override
    public String toString() {
        return "As de " + couleur.toString();
    }
    
    @Override
    public String toStringCourt() {
        return "A" + couleur.getSymbole();
    }
}