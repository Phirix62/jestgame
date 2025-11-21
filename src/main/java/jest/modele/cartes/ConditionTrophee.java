package jest.modele.cartes;

import jest.modele.joueurs.Jest;
import jest.modele.joueurs.Joueur;
import java.util.List;

/**
 * Énumération des conditions possibles pour gagner un trophée.
 */
public enum ConditionTrophee {
    MAJORITY_AS("Majorité d'As"),
    MAJORITY_DEUX("Majorité de 2"),
    MAJORITY_TROIS("Majorité de 3"),
    MAJORITY_QUATRE("Majorité de 4"),
    HIGHEST_PIQUE("Pique le plus haut"),
    HIGHEST_TREFLE("Trèfle le plus haut"),
    HIGHEST_CARREAU("Carreau le plus haut"),
    HIGHEST_COEUR("Cœur le plus haut"),
    LOWEST_PIQUE("Pique le plus bas"),
    LOWEST_TREFLE("Trèfle le plus bas"),
    LOWEST_CARREAU("Carreau le plus bas"),
    LOWEST_COEUR("Cœur le plus bas"),
    HAS_JOKER("Possède le Joker"),
    BEST_JEST("Meilleur Jest"),
    BEST_JEST_NO_JOKER("Meilleur Jest sans Joker");
    
    private final String description;
    
    ConditionTrophee(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Évalue la condition et retourne le joueur qui la remplit.
     * @param joueurs Liste des joueurs
     * @return Joueur gagnant ou null si aucun
     */
    public Joueur evaluer(List<Joueur> joueurs) {
        switch (this) {
            case MAJORITY_AS:
                return evaluerMajorite(joueurs, 1);
            case MAJORITY_DEUX:
                return evaluerMajorite(joueurs, 2);
            case MAJORITY_TROIS:
                return evaluerMajorite(joueurs, 3);
            case MAJORITY_QUATRE:
                return evaluerMajorite(joueurs, 4);
            case HIGHEST_PIQUE:
                return evaluerCartePlusHaute(joueurs, Couleur.PIQUE);
            case HIGHEST_TREFLE:
                return evaluerCartePlusHaute(joueurs, Couleur.TREFLE);
            case HIGHEST_CARREAU:
                return evaluerCartePlusHaute(joueurs, Couleur.CARREAU);
            case HIGHEST_COEUR:
                return evaluerCartePlusHaute(joueurs, Couleur.COEUR);
            case LOWEST_PIQUE:
                return evaluerCartePlusBasse(joueurs, Couleur.PIQUE);
            case LOWEST_TREFLE:
                return evaluerCartePlusBasse(joueurs, Couleur.TREFLE);
            case LOWEST_CARREAU:
                return evaluerCartePlusBasse(joueurs, Couleur.CARREAU);
            case LOWEST_COEUR:
                return evaluerCartePlusBasse(joueurs, Couleur.COEUR);
            case HAS_JOKER:
                return evaluerPossessionJoker(joueurs);
            case BEST_JEST:
                return evaluerMeilleurScore(joueurs, false);
            case BEST_JEST_NO_JOKER:
                return evaluerMeilleurScore(joueurs, true);
            default:
                return null;
        }
    }
    
    private Joueur evaluerMajorite(List<Joueur> joueurs, int valeur) {
        Joueur gagnant = null;
        int maxCount = 0;
        
        for (Joueur j : joueurs) {
            int count;
            count = j.getJest().compterCartesValeur(valeur);
            
            
            if (count > maxCount) {
                maxCount = count;
                gagnant = j;
            } else if (count == maxCount && count > 0) {
                // Bris d'égalité : carte la plus forte
                gagnant = briserEgaliteMajorite(gagnant, j, valeur);
            }
        }
        return gagnant;
    }
    
    private Joueur briserEgaliteMajorite(Joueur j1, Joueur j2, int valeur) {
        Carte carte1, carte2;
        carte1 = j1.getJest().getCartePlusHauteValeur(valeur);
        carte2 = j2.getJest().getCartePlusHauteValeur(valeur);
        
        if (carte1 == null) return j2;
        if (carte2 == null) return j1;
        
        if (carte2.comparerForce(carte1) > 0) {
            return j2;
        } else {
            return j1;
        }
    }
    
    private Joueur evaluerCartePlusHaute(List<Joueur> joueurs, Couleur couleur) {
        Joueur gagnant = null;
        Carte carteMax = null;
        
        for (Joueur j : joueurs) {
            Carte carte = j.getJest().getCartePlusHaute(couleur);
            if (carte != null && (carteMax == null || carte.comparerForce(carteMax) > 0)) {
                carteMax = carte;
                gagnant = j;
            }
        }
        return gagnant;
    }
    
    private Joueur evaluerCartePlusBasse(List<Joueur> joueurs, Couleur couleur) {
        Joueur gagnant = null;
        Carte carteMin = null;
        
        for (Joueur j : joueurs) {
            Carte carte = j.getJest().getCartePlusBasse(couleur);
            if (carte != null && (carteMin == null || carte.comparerForce(carteMin) < 0)) {
                carteMin = carte;
                gagnant = j;
            }
        }
        return gagnant;
    }
    
    private Joueur evaluerPossessionJoker(List<Joueur> joueurs) {
        for (Joueur j : joueurs) {
            if (j.getJest().contientJoker()) {
                return j;
            }
        }
        return null;
    }
    
    private Joueur evaluerMeilleurScore(List<Joueur> joueurs, boolean sansJoker) {
        Joueur gagnant = null;
        int maxScore = Integer.MIN_VALUE;
        
        for (Joueur j : joueurs) {
            if (sansJoker && j.getJest().contientJoker()) {
                continue;
            }
            if (j.getScore() > maxScore) {
                maxScore = j.getScore();
                gagnant = j;
            } else if (j.getScore() == maxScore) {
                // Bris d'égalité : carte la plus haute
                Carte c1 = gagnant.getJest().getCartePlusHauteGlobale();
                Carte c2 = j.getJest().getCartePlusHauteGlobale();
                if (c2.comparerForce(c1) > 0) {
                    gagnant = j;
                }
            }
        }
        return gagnant;
    }
}