package jest.modele.extensions;

import jest.modele.jeu.Tour;
import jest.modele.score.CalculateurScore;

/**
 * Variante "Tactique" :
 * - Les joueurs voient 1 carte de la pioche avant de créer leur offre
 * - Les As valent toujours 5 (pas de condition de solitude)
 * - Distribution de 3 cartes au lieu de 2 (choix plus stratégique)
 */
public class VarianteTactique implements Variante {
    
    @Override
    public String getNom() {
        return "Tactique";
    }
    
    @Override
    public String getDescription() {
        return "3 cartes par main";
    }
    
    @Override
    public int modifierDistribution(Tour tour, int numeroTour) {
        return 3; // Distribuer 3 cartes au lieu de 2
    }
    
    @Override
    public void modifierScoring(CalculateurScore calculateur) {
        // TODO: Modifier la règle des As
        // Nécessite refactorisation de As.getValeurEffective()
        // ou injection d'un modificateur dans le calculateur
    }
    
    @Override
    public boolean verifierFinPartie(boolean piocheVide, int numeroTour) {
        return piocheVide; // Règle standard
    }
    
    @Override
    public int nombreTrophees(int nbJoueurs) {
        return nbJoueurs == 3 ? 2 : 1; // Règle standard
    }
}