package jest.modele.score;

import jest.modele.cartes.*;
import jest.modele.joueurs.Jest;

/**
 * Interface du pattern Visitor pour le calcul des scores.
 * Chaque visiteur implémente une règle de scoring spécifique.
 */
public interface VisiteurScore {
    
    /**
     * Visite une carte normale.
     * @param carte Carte à visiter
     * @param contexte Jest contenant la carte (pour règles contextuelles)
     */
    void visiterCarteNormale(CarteNormale carte, Jest contexte);
    
    /**
     * Visite le Joker.
     * @param joker Joker à visiter
     * @param contexte Jest contenant le Joker
     */
    void visiterJoker(Joker joker, Jest contexte);

    /**
     * Visite 
     * @param carte
     * @param contexte
     */
    default void visiterCarteExtension(Carte carte, Jest contexte){}; // ne fait rien par défaut

    /**
     * Retourne le score partiel calculé par ce visiteur.
     * @return Score partiel
     */
    int getScorePartiel();

    /**
     * Réinitialise le score partiel (pour nouvelle évaluation).
     */
    void reset();
}