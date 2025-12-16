package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;

/**
 * Visiteur calculant les points des Piques.
 * Règle : Les Piques ajoutent toujours leur valeur effective (positive).
 */
public class VisiteurPiques implements VisiteurScore {
    private int scorePartiel;
    
    public VisiteurPiques() {
        this.scorePartiel = 0;
    }
    
    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        if (carte.getCouleur() == Couleur.PIQUE) {
            scorePartiel += carte.getValeurEffective(contexte);
        }
    }
    
    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        // Le Joker n'affecte pas les Piques
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