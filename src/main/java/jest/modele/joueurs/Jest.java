package jest.modele.joueurs;

import jest.modele.cartes.*;
import jest.modele.score.VisiteurScore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Représente le Jest (collection de cartes) d'un joueur.
 * Le Jest accumule les cartes collectées durant la partie.
 */
public class Jest implements Serializable{
    private static final long serialVersionUID = 1L;
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
     * Vérifie si le Jest contient un Joker.
     * @return true si le Jest contient un Joker, false sinon.
     */
    public boolean contientJoker() {
        for (Carte carte : cartes) {
            if (carte instanceof Joker) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si le Jest contient une paire noire pour une valeur donnée.
     * Une paire noire est composée d'un Pique et d'un Trèfle de même valeur.
     * @param valeur Valeur faciale à vérifier
     * @return true si la paire noire existe, false sinon
     */
    public boolean contientPaireNoire(int valeur) {
        boolean aPique = false;
        boolean aTrefle = false;
        
        for (Carte carte : cartes) {
            if (carte.getValeurFaciale() == valeur) {
                if (carte.getCouleur() == Couleur.PIQUE) {
                    aPique = true;
                } else if (carte.getCouleur() == Couleur.TREFLE) {
                    aTrefle = true;
                }
            }
        }
        return aPique && aTrefle;
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
    public Carte getCartePlusHauteCouleur(Couleur couleur) {
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
     * Retourne la carte avec la plus haute valeur effective dans le Jest.
     * @return Carte la plus haute, ou null si le Jest est vide
     */
    public Carte getCartePlusHauteGlobale() {
        Carte carteMax = null;
        for (Carte carte : cartes) {
            if (carteMax == null || carte.comparerForce(carteMax) > 0) {
                carteMax = carte;
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
     */
    public int accepterVisiteur(VisiteurScore visiteur, boolean sansTrophees) {
        visiteur.reset();
        for (Carte carte : cartes) {
            carte.accepter(visiteur, this);
        }
        if (!sansTrophees) {
            for (Trophee trophee : trophees) {
                trophee.accepter(visiteur, this);
            }
            
        }
        return visiteur.getScorePartiel();
    }
    /**
     * Révèle toutes les cartes du Jest.
     */
    public void revelerCartes() {
        for (Carte carte : cartes) {
            carte.reveler();
        }
    }

    /**
     * Affiche les détails du Jest (cartes et trophées).
     * @return Chaîne de caractères représentant le Jest
     */
    public String afficherDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartes: ");
        sb.append(cartes.stream()
                .map(Carte::toString)
                .collect(Collectors.joining(", ")));
        // TODO revoir affichage trophées
        //sb.append(" | Trophées: ");
        //sb.append(trophees.stream()
        //       .map(Trophee::toString)
        //       .collect(Collectors.joining(", ")));
        return sb.toString();
    }

    
}