package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;

/**
 * Visiteur calculant les points des Cœurs.
 * Règle complexe dépendant du Joker :
 * - Sans Joker : Cœurs valent 0
 * - Avec Joker + 0 Cœurs : Bonus Joker +4
 * - Avec Joker + 1-3 Cœurs : Cœurs valent négatif, Joker vaut 0
 * - Avec Joker + 4 Cœurs : Cœurs valent positif, Joker vaut 0
 */
public class VisiteurCoeurs implements VisiteurScore {
    private int scorePartiel;
    
    public VisiteurCoeurs() {
        this.scorePartiel = 0;
    }
    
    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        if (carte.getCouleur() == Couleur.COEUR) {
            boolean aJoker = contexte.contientJoker();
            int nbCoeurs = contexte.compterCartesCouleur(Couleur.COEUR);
            
            if (!aJoker) {
                // Sans Joker : Cœurs valent 0
                return;
            }
            
            if (nbCoeurs >= 1 && nbCoeurs <= 3) {
                // 1-3 Cœurs avec Joker : négatif
                scorePartiel -= carte.getValeurEffective(contexte);
            } else if (nbCoeurs == 4) {
                // 4 Cœurs avec Joker : positif
                scorePartiel += carte.getValeurEffective(contexte);
            }
        }
    }
    
    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        int nbCoeurs = contexte.compterCartesCouleur(Couleur.COEUR);
        
        if (nbCoeurs == 0) {
            // Joker seul : bonus +4
            scorePartiel += 4;
        }
        // Sinon : Joker vaut 0 (géré par la logique des Cœurs)
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