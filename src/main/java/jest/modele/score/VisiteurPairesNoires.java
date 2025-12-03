package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;
import java.util.HashSet;
import java.util.Set;

/**
 * Visiteur calculant les bonus des paires noires.
 * Règle : Paire noire = Pique + Trèfle de même valeur → +2 points bonus.
 */
public class VisiteurPairesNoires implements VisiteurScore {
    private int scorePartiel;
    private Set<Integer> valeursTraitees; // Pour éviter de compter 2 fois la même paire
    
    public VisiteurPairesNoires() {
        this.scorePartiel = 0;
        this.valeursTraitees = new HashSet<>();
    }
    
    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        Couleur couleur = carte.getCouleur();
        int valeur = carte.getValeurFaciale();
        
        // Ne traiter que Piques et Trèfles
        if (couleur != Couleur.PIQUE && couleur != Couleur.TREFLE) {
            return;
        }
        
        // Éviter de compter 2 fois la même paire
        if (valeursTraitees.contains(valeur)) {
            return;
        }
        
        // Vérifier si paire noire existe
        if (contexte.contientPaireNoire(valeur)) {
            scorePartiel += 2;
            valeursTraitees.add(valeur);
        }
    }
    
    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        // Le Joker n'affecte pas les paires noires
    }
    
    @Override
    public int getScorePartiel() {
        return scorePartiel;
    }
    
    @Override
    public void reset() {
        scorePartiel = 0;
        valeursTraitees.clear();
    }
}