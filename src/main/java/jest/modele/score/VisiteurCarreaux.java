package jest.modele.score;

import jest.modele. cartes.*;
import jest.modele.joueurs.Jest;

/**
 * Visiteur calculant les points des Carreaux.
 * Règle : Les Carreaux retirent toujours leur valeur effective (négatif).
 */
public class VisiteurCarreaux implements VisiteurScore {
    private int scorePartiel;
    
    public VisiteurCarreaux() {
        this.scorePartiel = 0;
    }
    
    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        if (carte.getCouleur() == Couleur.CARREAU) {
            scorePartiel -= carte.getValeurEffective(contexte);
        }
    }
    
    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        // Le Joker n'affecte pas les Carreaux
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