package jest.modele.extensions;

import jest.modele.jeu.Tour;
import jest.modele.score.CalculateurScore;

/**
 * Variante "Partie Rapide" :
 * - Seulement 4 tours fixes
 * - Paires noires valent +3 au lieu de +2
 * - 3 trophées au lieu de 2
 */
public class VarianteRapide implements Variante {
    private static final long serialVersionUID = 1L;
    
    private static final int NB_TOURS_MAX = 4;
    
    @Override
    public String getNom() {
        return "Partie Rapide";
    }
    
    @Override
    public String getDescription() {
        return "3 tours maximum, trophées";
    }
    
    @Override
    public int modifierDistribution(Tour tour, int taillePioche, int nbJoueurs) {
        return 2; // Distribution standard
    }
    
    @Override
    public void modifierScoring(CalculateurScore calculateur) {
    }
    
    @Override
    public boolean verifierFinPartie(boolean cartesInsuffisantes, int numeroTour) {
        // Fin après 4 tours OU cartes insuffisantes
        return cartesInsuffisantes || numeroTour >= NB_TOURS_MAX;
    }
    
    @Override
    public int nombreTrophees(int nbJoueurs) {
        return nbJoueurs == 3 ? 2 : 1;
    }
}