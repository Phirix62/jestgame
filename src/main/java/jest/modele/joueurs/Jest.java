package jest.modele.joueurs;

import jest.modele.cartes.*;
import jest.modele.score.VisiteurScore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Représente le Jest (collection de cartes) d'un joueur.
 * Le Jest accumule les cartes collectées durant la partie.
 */
public class Jest {
    private List<Carte> cartes;
    private List<Trophee> trophees;
    
    /**
     * Constructeur de Jest.
     * Crée un Jest vide.
     */
    public Jest() {
        this.cartes = new ArrayList<>();
        this.trophees = new ArrayList<>();
    }

    /**
     * Ajoute une carte au Jest.
     * @param carte La carte à ajouter.
     */
    public void ajouterCarte(Carte carte) {
        cartes.add(carte);
    }
    
    /**
     * Ajoute un trophée au Jest.
     * @param trophee Le trophée à ajouter.
     */
    public void ajouterTrophee(Trophee trophee) {
        trophees.add(trophee);
    }
    
    /**
     * Retourne la liste des cartes du Jest.
     * @return Liste des cartes
     */
    public List<Carte> getCartes() {
        return cartes;
    }
    
    /**
     * Retourne la liste des trophées du Jest.
     * @return Liste des trophées
     */
    public List<Trophee> getTrophees() {
        return trophees;
    }

    /**
     * Compte le nombre de cartes d'une couleur spécifique dans le Jest.
     * @param couleur Couleur à compter
     * @return Nombre de cartes de cette couleur
     */
    public int compterCartesCouleur(Couleur couleur) {
        int count = 0;
        for (Carte carte : cartes) {
            if (carte.getCouleur() == couleur) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compte le nombre de cartes d'une valeur spécifique dans le Jest.
     * @param valeur Valeur à compter
     * @return Nombre de cartes de cette valeur
     */
    public int compterCartesValeur(int valeur) {
        int count = 0;
        for (Carte carte : cartes) {
            if (carte.getValeurFaciale() == valeur) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retourne la carte de la couleur spécifiée avec la plus haute valeur effective.
     * @param couleur Couleur recherchée
     * @return Carte la plus haute de cette couleur, ou null si aucune
     */
    public Carte getCartePlusHaute(Couleur couleur) {
        Carte carteMax = null;
        for (Carte carte : cartes) {
            if (carte.getCouleur() == couleur) {
                if (carteMax == null || carte.comparerForce(carteMax) > 0) {
                    carteMax = carte;
                }
            }
        }
        return carteMax;
    }

    /**
     * Retourne la carte avec la plus haute valeur effective parmi celles ayant la valeur faciale spécifiée.
     * @param valeur Valeur faciale recherchée
     * @return Carte la plus haute de cette valeur, ou null si aucune
     */    public Carte getCartePlusHauteValeur(int valeur) {
        Carte carteMax = null;
        for (Carte carte : cartes) {
            if (carte.getValeurFaciale() == valeur) {
                if (carteMax == null || carte.comparerForce(carteMax) > 0) {
                    carteMax = carte;
                }
            }
        }
        return carteMax;
    }

    /**
     * Retourne la carte de la couleur spécifiée avec la plus basse valeur effective.
     * @param couleur Couleur recherchée
     * @return Carte la plus basse de cette couleur, ou null si aucune
     */    public Carte getCartePlusBasse(Couleur couleur) {
        Carte carteMin = null;
        for (Carte carte : cartes) {
            if (carte.getCouleur() == couleur) {
                if (carteMin == null || carte.comparerForce(carteMin) < 0) {
                    carteMin = carte;
                }
            }
        }
        return carteMin;
    }

    /**
     * Accepte un visiteur pour calculer le score du Jest.
     * @param visiteur Visiteur de score
     * @return Score calculé
     */    public int accepterVisiteur(VisiteurScore visiteur) {
        return visiteur.calculerScore(this);
    }

    
}