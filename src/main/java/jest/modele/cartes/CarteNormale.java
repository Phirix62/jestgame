package jest.modele.cartes;

import jest.modele.score.VisiteurScore;
import jest.modele.joueurs.Jest;

/**
 * Représente une carte normale du jeu ( AS(1), 2, 3 ou 4 d'une couleur).
 * Note : Les As sont gérés par la classe As séparée.
 */
public class CarteNormale extends Carte {
    
    /**
     * Constructeur de CarteNormale.
     * @param couleur Couleur de la carte
     * @param valeurFaciale Valeur (2, 3 ou 4)
     */
    public CarteNormale(Couleur couleur, int valeurFaciale) {
        super(couleur, valeurFaciale);
        if (valeurFaciale < 1 || valeurFaciale > 4) {
            throw new IllegalArgumentException("Valeur faciale invalide pour CarteNormale : " + valeurFaciale);
        }
    }
    
    /**
     * Constructeur protégé pour usage interne (notamment dans As.accepter()).
     * @param couleur Couleur de la carte
     * @param valeurFaciale Valeur
     * @param skipValidation true pour ignorer la validation
     */
    protected CarteNormale(Couleur couleur, int valeurFaciale, boolean skipValidation) {
        super(couleur, valeurFaciale);
    }
    
    /**
     * Calcule la valeur effective de la carte.
     * Pour les cartes normales (2-4), la valeur est toujours la valeur faciale (as gérée séparément).
     * @param jest Jest contenant cette carte
     * @return Valeur faciale (2-4)
     */
    @Override
    public int getValeurEffective(Jest jest) {
        return valeurFaciale;
    }
    
    /**
     * Accepte un visiteur pour le calcul de score.
     * @param visiteur Visiteur de score
     * @param contexte Jest contenant cette carte
     */
    @Override
    public void accepter(VisiteurScore visiteur, Jest contexte) {
        visiteur.visiterCarteNormale(this, contexte);
    }
}