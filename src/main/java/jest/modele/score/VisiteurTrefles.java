package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;

/**
 * Visiteur calculant les points des Trèfles.
 * Règle : Les Trèfles ajoutent toujours leur valeur effective (positive).
 */
public class VisiteurTrefles implements VisiteurScore {
    private int scorePartiel;
    
    public VisiteurTrefles() {
        this.scorePartiel = 0;
    }
    
    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        if (carte.getCouleur() == Couleur.TREFLE) {
            scorePartiel += carte.getValeurEffective(contexte);
        }
    }
    
    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        // Le Joker n'affecte pas les Trèfles
    }
    
    @Override
    public int getScorePartiel() {
        return scorePartiel;
    }
    
    @Override
    public void reset() {
        scorePartiel = 0;
    }
}