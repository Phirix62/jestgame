package jest.modele.utilitaires;

import jest.modele.joueurs.Joueur;
import jest.modele.cartes.Carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire de scores pour la fin de partie.
 * - Calcule les scores finaux
 * - Détermine le gagnant
 * - Établit le classement
 */
public class GestionnaireScores {
    
    /**
     * Détermine le joueur gagnant (score le plus élevé).
     * En cas d'égalité, compare la carte la plus haute.
     * 
     * @param joueurs Liste des joueurs
     * @return Joueur gagnant
     */
    public Joueur determinerGagnant(List<Joueur> joueurs) {
        Joueur gagnant = null;
        int scoreMax = Integer.MIN_VALUE;

        for (Joueur joueur : joueurs) {
            if (joueur.getScore() > scoreMax) {
                scoreMax = joueur.getScore();
                gagnant = joueur;
            } else if (joueur.getScore() == scoreMax) {
                // Cas d'égalité : carte la plus haute
                if (gagnant != null) {
                    Carte c1 = gagnant.getJest().getCartePlusHauteGlobale();
                    Carte c2 = joueur.getJest().getCartePlusHauteGlobale();
                    if (c2 != null && (c1 == null || c2.comparerForce(c1) > 0)) {
                        gagnant = joueur;
                    }
                }
            }
        }

        return gagnant;
    }
    
    /**
     * Obtient le classement des joueurs par score décroissant.
     * 
     * @param joueurs Liste des joueurs
     * @return Liste triée par score décroissant
     */
    public List<Joueur> obtenirClassement(List<Joueur> joueurs) {
        List<Joueur> classement = new ArrayList<>(joueurs);
        classement.sort((j1, j2) -> Integer.compare(j2.getScore(), j1.getScore()));
        return classement;
    }
}
