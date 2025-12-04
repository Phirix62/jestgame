package jest.modele.cartes;

import jest.modele.joueurs.Joueur;
import jest.modele.joueurs.Jest;
import jest.modele.score.VisiteurScore;
import java.util.List;

/**
 * Représente un trophée du jeu Jest.
 * Un trophée est une carte spéciale avec une condition d'attribution.
 * Le joueur qui remporte le trophée l'ajoute à son Jest (effet sur le score).
 */
public class Trophee extends Carte {
    private ConditionTrophee condition;
    
    /**
     * Constructeur de Trophee.
     * @param couleur Couleur du trophée (sa valeur en tant que carte)
     * @param valeur Valeur faciale du trophée
     * @param condition Condition pour gagner ce trophée
     */
    public Trophee(Couleur couleur, int valeur, ConditionTrophee condition) {
        super(couleur, valeur);
        this.condition = condition;
    }
    
    /**
     * Retourne la condition d'attribution du trophée.
     * @return Condition
     */
    public ConditionTrophee getCondition() {
        return condition;
    }
    
    /**
     * Évalue la condition et détermine quel joueur gagne ce trophée.
     * @param joueurs Liste des joueurs en compétition
     * @return Joueur gagnant ou null
     */
    public Joueur evaluerCondition(List<Joueur> joueurs) {
        return condition.evaluer(joueurs);
    }
    
    /**
     * Retourne l'effet du trophée sur le score (sa valeur faciale).
     * @return Valeur faciale du trophée
     */
    public int getEffetScore() {
        return valeurFaciale;
    }
    
    /**
     * La valeur effective d'un trophée est toujours sa valeur faciale.
     * @param jest Non utilisé pour les trophées
     * @return Valeur faciale
     */
    @Override
    public int getValeurEffective(Jest jest) {
        return valeurFaciale;
    }
    
    /**
     * Les trophées ne sont pas visités (pas de règle de scoring complexe).
     * @param visiteur Visiteur (ignoré)
     * @param contexte Contexte (ignoré)
     */
    @Override
    public void accepter(VisiteurScore visiteur, Jest contexte) {
        // Les trophées ne participent pas au pattern Visitor
        // Leur effet est appliqué directement via getEffetScore()
    }
    
    @Override
    public String toString() {
        return "Trophée [" + condition.getDescription() + "] - " + 
               valeurFaciale + couleur.getSymbole();
    }
    
    @Override
    public String toStringCourt() {
        return "Trophée " + valeurFaciale + couleur.getSymbole();
    }
}