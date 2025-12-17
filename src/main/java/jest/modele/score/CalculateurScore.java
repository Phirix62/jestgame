package jest.modele.score;

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
        visiteurs.add(new VisiteurExtensionMagique());
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
            jest.accepterVisiteur(visiteur, sansTrophees);
        }
        
        // Agréger les scores partiels
        int scoreBase = 0;
        for (VisiteurScore visiteur : visiteurs) {
            scoreBase += visiteur.getScorePartiel();
        }
        return scoreBase;
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
            jest.accepterVisiteur(visiteur, false);
        }
        
        // Afficher chaque composante
        sb.append("  Piques:        ").append(visiteurs.get(0).getScorePartiel()).append("\n");
        sb.append("  Trèfles:       ").append(visiteurs.get(1).getScorePartiel()).append("\n");
        sb.append("  Carreaux:      ").append(visiteurs.get(2).getScorePartiel()).append("\n");
        sb.append("  Coeurs/Joker:   ").append(visiteurs.get(3).getScorePartiel()).append("\n");
        sb.append("  Paires noires: ").append(visiteurs.get(4).getScorePartiel()).append("\n");
        sb.append("  Extension Magique: ").append(visiteurs.get(5).getScorePartiel()).append("\n");
        
        int scoreBase = 0;
        for (VisiteurScore v : visiteurs) {
            scoreBase += v.getScorePartiel();
        }
        sb.append("  Score :    ").append(scoreBase).append("\n");
        
        return sb.toString();
    }
}