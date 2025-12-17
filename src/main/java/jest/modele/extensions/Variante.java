package jest.modele.extensions;

import jest.modele.jeu.Tour;
import jest.modele.score.CalculateurScore;
import java.io.Serializable;

/**
 * Interface définissant une variante de règles du jeu.
 * Les variantes peuvent modifier la distribution, le scoring, ou les conditions de fin.
 */
public interface Variante extends Serializable{
    static final long serialVersionUID = 1L;
    
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
     * @param taillePioche Taille actuelle de la pioche
     * @param nbJoueurs Nombre de joueurs dans la partie
     * @return Nombre de cartes à distribuer par joueur (ou -1 pour règle standard)
     */
    int modifierDistribution(Tour tour, int taillePioche, int nbJoueurs);
    
    /**
     * Modifie les règles de calcul de score.
     * @param calculateur Calculateur à modifier
     */
    void modifierScoring(CalculateurScore calculateur);
    
    /**
     * Détermine si la partie doit se terminer.
     * @param cartesInsuffisantes true si les cartes résiduelles + pioche sont insuffisantes pour une distribution complète
     * @param numeroTour Numéro du tour actuel
     * @return true si fin de partie
     */
    boolean verifierFinPartie(boolean cartesInsuffisantes, int numeroTour);
    
    /**
     * Nombre de trophées à placer.
     * @param nbJoueurs Nombre de joueurs
     * @return Nombre de trophées (-1 pour règle standard)
     */
    int nombreTrophees(int nbJoueurs);
}