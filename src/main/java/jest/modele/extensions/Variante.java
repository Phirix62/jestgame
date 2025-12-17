package jest.modele.extensions;

import jest.modele.jeu.Tour;
import jest.modele.score.CalculateurScore;

/**
 * Interface définissant une variante de règles du jeu.
 * Les variantes peuvent modifier la distribution, le scoring, ou les conditions de fin.
 */
public interface Variante {
    
    /**
     * Retourne le nom de la variante.
     * @return Nom
     */
    String getNom();
    
    /**
     * Retourne la description des modifications.
     * @return Description
     */
    String getDescription();
    
    /**
     * Modifie les règles de distribution des cartes.
     * @param tour Tour à modifier
     * @param numeroTour Numéro du tour actuel
     * @return Nombre de cartes à distribuer par joueur (ou -1 pour règle standard)
     */
    int modifierDistribution(Tour tour, int numeroTour);
    
    /**
     * Modifie les règles de calcul de score.
     * @param calculateur Calculateur à modifier
     */
    void modifierScoring(CalculateurScore calculateur);
    
    /**
     * Détermine si la partie doit se terminer.
     * @param piocheVide true si pioche vide
     * @param numeroTour Numéro du tour actuel
     * @return true si fin de partie
     */
    boolean verifierFinPartie(boolean piocheVide, int numeroTour);
    
    /**
     * Nombre de trophées à placer.
     * @param nbJoueurs Nombre de joueurs
     * @return Nombre de trophées (-1 pour règle standard)
     */
    int nombreTrophees(int nbJoueurs);
}