package jest.modele.cartes;

import jest.modele.joueurs.Joueur;
import jest.modele.joueurs.Jest;
import jest.modele.score.VisiteurScore;
import java.util.List;
import java.io.Serializable;

/**
 * Représente un trophée du jeu Jest.
 * Un trophée est une carte spéciale avec une condition d'attribution.
 * Le joueur qui remporte le trophée l'ajoute à son Jest (effet sur le score).
 */
public class Trophee extends Carte implements Serializable {
    private static final long serialVersionUID = 1L;
    private ConditionTrophee condition;
    private Carte carteAssociee;

    /**
     * Constructeur de Trophee.
     * 
     * @param carte Carte associée au trophée (détermine couleur, valeur et condition)
     */
    public Trophee(Carte carte) {
        super(carte.getCouleur(), carte.getValeurFaciale());
        this.carteAssociee = carte;
        this.condition = determinerCondition(carte.getCouleur(), carte.getValeurFaciale());
    }

    /**
     * Détermine la condition du trophée selon sa carte.
     * Mapping fixe : chaque carte correspond à une condition spécifique.
     * 
     * @param couleur Couleur de la carte
     * @param valeur  Valeur faciale
     * @return ConditionTrophee correspondante
     */
    private ConditionTrophee determinerCondition(Couleur couleur, int valeur) {
        // Mapping selon couleur et valeur
        switch (couleur) {
            case PIQUE:
                switch (valeur) {
                    case 1:
                        return ConditionTrophee.HIGHEST_TREFLE;
                    case 2:
                        return ConditionTrophee.MAJORITY_TROIS;
                    case 3:
                        return ConditionTrophee.MAJORITY_DEUX;
                    case 4:
                        return ConditionTrophee.LOWEST_TREFLE;
                }
                break;
            case TREFLE:
                switch (valeur) {
                    case 1:
                        return ConditionTrophee.HIGHEST_PIQUE;
                    case 2:
                        return ConditionTrophee.LOWEST_COEUR;
                    case 3:
                        return ConditionTrophee.HIGHEST_COEUR;
                    case 4:
                        return ConditionTrophee.LOWEST_PIQUE;
                }
                break;
            case CARREAU:
                switch (valeur) {
                    case 1:
                        return ConditionTrophee.MAJORITY_QUATRE;
                    case 2:
                        return ConditionTrophee.HIGHEST_CARREAU;
                    case 3:
                        return ConditionTrophee.LOWEST_CARREAU;
                    case 4:
                        return ConditionTrophee.BEST_JEST_NO_JOKER;
                }
                break;
            case COEUR:
                switch (valeur) {
                    case 1:
                        return ConditionTrophee.HAS_JOKER;
                    case 2:
                        return ConditionTrophee.HAS_JOKER;
                    case 3:
                        return ConditionTrophee.HAS_JOKER;
                    case 4:
                        return ConditionTrophee.HAS_JOKER;
                }
                break;
            default:
                return ConditionTrophee.BEST_JEST;
        }
        // Par défaut
        return ConditionTrophee.BEST_JEST;
    }

    /**
     * Retourne la condition d'attribution du trophée.
     * 
     * @return Condition
     */
    public ConditionTrophee getCondition() {
        return condition;
    }

    /**
     * Retourne la carte associée au trophée.
     * 
     * @return Carte associée
     */
    public Carte getCarteAssociee() {
        return carteAssociee;
    }

    /**
     * Évalue la condition et détermine quel joueur gagne ce trophée.
     * 
     * @param joueurs Liste des joueurs en compétition
     * @return Joueur gagnant ou null
     */
    public Joueur evaluerCondition(List<Joueur> joueurs) {
        return condition.evaluer(joueurs);
    }

    /**
     * La valeur effective d'un trophée est sa valeur faciale la plupart du temps,
     * sinon recalculer au niveau du visiteur.
     * 
     * @param jest Non utilisé pour les trophées
     * @return Valeur faciale
     */
    @Override
    public int getValeurEffective(Jest jest) {
        return carteAssociee.getValeurEffective(jest);
    }

    /**
     * Les trophées sont visités pour le calcul final du score
     * 
     * @param visiteur Visiteur
     * @param contexte Contexte
     */
    @Override
    public void accepter(VisiteurScore visiteur, Jest contexte) {
        carteAssociee.accepter(visiteur, contexte);
    }

    @Override
    public String toString() {
        return "Trophée [" + condition.getDescription() + "] - " +
                carteAssociee.toStringCourt();
    }
}