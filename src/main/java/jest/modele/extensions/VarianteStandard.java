package jest.modele.extensions;

import jest.modele.jeu.Tour;
import jest.modele.score.CalculateurScore;

/**
 * Variante standard (règles de base du jeu).
 */
public class VarianteStandard implements Variante {
    
    @Override
    public String getNom() {
        return "Standard";
    }
    
    @Override
    public String getDescription() {
        return "Règles de base du jeu Jest";
    }
    
    @Override
    public int modifierDistribution(Tour tour, int numeroTour) {
        return 2; // Distribution standard
    }
    
    @Override
    public void modifierScoring(CalculateurScore calculateur) {
        // Pas de modification
    }
    
    @Override
    public boolean verifierFinPartie(boolean piocheVide, int numeroTour) {
        return piocheVide; // Fin quand pioche vide
    }
    
    @Override
    public int nombreTrophees(int nbJoueurs) {
        return nbJoueurs == 3 ? 2 : 1; // Règle standard
    }
}