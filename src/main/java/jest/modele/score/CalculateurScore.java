package jest.modele.score;

import jest.modele.cartes.Trophee;
import jest.modele.joueurs.Jest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculateur central des scores du jeu Jest.
 * Utilise le pattern Visitor pour appliquer les règles de scoring.
 * Orchestre plusieurs visiteurs et agrège leurs résultats.
 */
public class CalculateurScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<VisiteurScore> visiteurs;
    
    /**
     * Constructeur de CalculateurScore.
     * Initialise tous les visiteurs de scoring.
     */
    public CalculateurScore() {
        this.visiteurs = new ArrayList<>();
        visiteurs.add(new VisiteurPiques());
        visiteurs.add(new VisiteurTrefles());
        visiteurs.add(new VisiteurCarreaux());
        visiteurs.add(new VisiteurCoeurs());
        visiteurs.add(new VisiteurPairesNoires());
    }
    
    /**
     * Calcule le score total d'un Jest.
     * @param jest Jest à évaluer
     * @return Score total
     */
    public int calculerScore(Jest jest) {
        return calculerScore(jest, false);
    }
    
    /**
     * Calcule le score d'un Jest, avec option d'exclure les trophées.
     * @param jest Jest à évaluer
     * @param sansTrophees true pour calculer sans effet des trophées
     * @return Score total
     */
    public int calculerScore(Jest jest, boolean sansTrophees) {
        // Réinitialiser tous les visiteurs
        for (VisiteurScore visiteur : visiteurs) {
            visiteur.reset();
        }
        
        // Faire visiter le Jest par tous les visiteurs
        for (VisiteurScore visiteur : visiteurs) {
            jest.accepterVisiteur(visiteur);
        }
        
        // Agréger les scores partiels
        int scoreBase = 0;
        for (VisiteurScore visiteur : visiteurs) {
            scoreBase += visiteur.getScorePartiel();
        }
        
        // Appliquer les effets des trophées si demandé
        if (!sansTrophees) {
            scoreBase = appliquerEffetsTrophees(jest, scoreBase);
        }
        
        return scoreBase;
    }
    
    /**
     * Applique les effets des trophées au score.
     * @param jest Jest contenant les trophées
     * @param scoreBase Score de base sans trophées
     * @return Score final avec trophées
     */
    private int appliquerEffetsTrophees(Jest jest, int scoreBase) {
        int scoreFinal = scoreBase;
        for (Trophee trophee : jest.getTrophees()) {
            scoreFinal += trophee.getEffetScore();
        }
        return scoreFinal;
    }
    
    /**
     * Affiche le détail du calcul de score (debug/affichage).
     * @param jest Jest à analyser
     * @return Description détaillée
     */
    public String afficherDetailScore(Jest jest) {
        StringBuilder sb = new StringBuilder();
        sb.append("Détail du calcul de score :\n");
        
        // Réinitialiser et visiter
        for (VisiteurScore visiteur : visiteurs) {
            visiteur.reset();
            jest.accepterVisiteur(visiteur);
        }
        
        // Afficher chaque composante
        sb.append("  Piques:        ").append(visiteurs.get(0).getScorePartiel()).append("\n");
        sb.append("  Trèfles:       ").append(visiteurs.get(1).getScorePartiel()).append("\n");
        sb.append("  Carreaux:      ").append(visiteurs.get(2).getScorePartiel()).append("\n");
        sb.append("  Cœurs/Joker:   ").append(visiteurs.get(3).getScorePartiel()).append("\n");
        sb.append("  Paires noires: ").append(visiteurs.get(4).getScorePartiel()).append("\n");
        
        int scoreBase = 0;
        for (VisiteurScore v : visiteurs) {
            scoreBase += v.getScorePartiel();
        }
        sb.append("  Score base:    ").append(scoreBase).append("\n");
        
        if (!jest.getTrophees().isEmpty()) {
            int bonusTrophees = 0;
            for (Trophee t : jest.getTrophees()) {
                bonusTrophees += t.getEffetScore();
            }
            sb.append("  Bonus trophées: ").append(bonusTrophees).append("\n");
            sb.append("  SCORE FINAL:   ").append(scoreBase + bonusTrophees).append("\n");
        } else {
            sb.append("  SCORE FINAL:   ").append(scoreBase).append("\n");
        }
        
        return sb.toString();
    }
}