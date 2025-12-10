package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.io.Serializable;
import java.util.List;

/**
 * Classe abstraite représentant un joueur du jeu Jest.
 * Définit les comportements communs et les méthodes abstraites à implémenter.
 * Design pattern template : squelette défini, détails dans sous-classes.
 */
public abstract class Joueur implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nom;
    protected Jest jest;
    protected int scoreCalcule;
    
    /**
     * Constructeur de Joueur.
     * @param nom Nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.scoreCalcule = 0;
    }
    
    /**
     * Retourne le nom du joueur.
     * @return Nom
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Retourne le Jest du joueur.
     * @return Jest
     */
    public Jest getJest() {
        return jest;
    }
    
    /**
     * Ajoute une carte au Jest du joueur.
     * @param carte Carte à ajouter
     */
    public void ajouterCarteAuJest(Carte carte) {
        jest.ajouterCarte(carte);
    }
    
    /**
     * Retourne le score calculé du joueur.
     * @return Score
     */
    public int getScore() {
        return scoreCalcule;
    }
    
    /**
     * Définit le score du joueur.
     * @param score Nouveau score
     */
    public void setScore(int score) {
        this.scoreCalcule = score;
    }
    
    /**
     * Méthode abstraite : Choisit quelle carte jouer face cachée dans l'offre.
     * @param main Cartes en main (2 cartes)
     * @return Carte choisie pour être face cachée
     */
    public abstract Carte choisirCarteOffre(List<Carte> main);
    
    /**
     * Méthode abstraite : Choisit de quelle offre prendre une carte.
     * @param offres Offres disponibles (autres joueurs)
     * @return Offre choisie
     */
    public abstract Offre choisirOffreCible(List<Offre> offres);
    
    /**
     * Méthode abstraite : Choisit quelle carte prendre dans une offre.
     * @param offre Offre dans laquelle piocher
     * @return Carte choisie (visible ou cachée)
     */
    public abstract Carte choisirCarteDansOffre(Offre offre);
    
    /**
     * Affiche l'état actuel du joueur.
     */
    public void afficherEtat() {
        System.out.println(nom + " - " + jest.toString() + " - Score: " + scoreCalcule);
    }
    
    @Override
    public String toString() {
        return nom;
    }
}