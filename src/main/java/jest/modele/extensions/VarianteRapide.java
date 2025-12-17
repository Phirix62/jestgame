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
    
    private static final int NB_TOURS_MAX = 4;
    
    @Override
    public String getNom() {
        return "Partie Rapide";
    }
    
    @Override
    public String getDescription() {
        return "4 tours maximum, paires noires +3, 3 trophées";
    }
    
    @Override
    public int modifierDistribution(Tour tour, int numeroTour) {
        return 2; // Distribution standard
    }
    
    @Override
    public void modifierScoring(CalculateurScore calculateur) {
        // TODO: Modifier le bonus des paires noires
        // Nécessite refactorisation du CalculateurScore pour injecter des modificateurs
    }
    
    @Override
    public boolean verifierFinPartie(boolean piocheVide, int numeroTour) {
        // Fin après 4 tours OU pioche vide
        return piocheVide || numeroTour >= NB_TOURS_MAX;
    }
    
    @Override
    public int nombreTrophees(int nbJoueurs) {
        return 3; // Toujours 3 trophées
    }
}