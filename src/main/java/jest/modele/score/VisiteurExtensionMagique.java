package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;

public class VisiteurExtensionMagique implements VisiteurScore {
    private int scorePartiel;

    public VisiteurExtensionMagique() {
        this.scorePartiel = 0;
    }

    @Override
    public void visiterCarteNormale(CarteNormale carte, Jest contexte) {
        // les cartes normales ne sont pas traitées
    }

    @Override
    public void visiterJoker(Joker joker, Jest contexte) {
        // Le Joker n'est pas traité
    }

    @Override
    public void visiterCarteExtension(Carte carte, Jest contexte) {
        if (carte.getCouleur() == Couleur.SPECIALE && !carte.estJoker()) {
            // Toutes les cartes magiques utilisent getValeurEffective pour le moment
            scorePartiel += carte.getValeurEffective(contexte);
        }
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
