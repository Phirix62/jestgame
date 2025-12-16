package jest.modele.jeu;

import jest.modele.cartes.Carte;
import jest.modele.joueurs.Joueur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une offre de 2 cartes (1 visible, 1 cachée) faite par un joueur.
 * Une offre est créée à chaque tour et peut perdre des cartes quand les autres joueurs piochent.
 */
public class Offre implements Serializable{
    private static final long serialVersionUID = 1L;
    private Carte carteVisible;
    private Carte carteCachee;
    private Joueur proprietaire;
    
    /**
     * Constructeur d'Offre.
     * @param proprietaire Joueur qui fait l'offre
     */
    public Offre(Joueur proprietaire) {
        this.proprietaire = proprietaire;
        this.carteVisible = null;
        this.carteCachee = null;
    }
    
    /**
     * Ajoute une carte à l'offre.
     * @param carte Carte à ajouter
     * @param visible true si carte face visible, false si cachée
     */
    public void ajouterCarte(Carte carte, boolean visible) {
        if (visible) {
            if (carteVisible != null) {
                throw new IllegalStateException("Une carte visible existe déjà");
            }
            carte.reveler();
            this.carteVisible = carte;
        } else {
            if (carteCachee != null) {
                throw new IllegalStateException("Une carte cachée existe déjà");
            }
            carte.cacher();
            this.carteCachee = carte;
        }
    }
    
    /**
     * Retire une carte de l'offre.
     * @param carte Carte à retirer
     * @return Carte retirée
     * @throws IllegalArgumentException Si la carte n'est pas dans l'offre
     */
    public Carte retirerCarte(Carte carte) {
        if (carte == carteVisible) {
            Carte temp = carteVisible;
            carteVisible = null;
            return temp;
        } else if (carte == carteCachee) {
            Carte temp = carteCachee;
            carteCachee = null;
            return temp;
        }
        throw new IllegalArgumentException("Carte non trouvée dans l'offre");
    }
    
    /**
     * Retourne la carte visible de l'offre.
     * @return Carte visible ou null
     */
    public Carte getCarteVisible() {
        return carteVisible;
    }
    
    /**
     * Retourne la carte cachée de l'offre.
     * @return Carte cachée ou null
     */
    public Carte getCarteCachee() {
        return carteCachee;
    }
    
    /**
     * Vérifie si l'offre est complète (2 cartes).
     * @return true si 2 cartes présentes
     */
    public boolean estComplete() {
        return carteVisible != null && carteCachee != null;
    }
    
    /**
     * Retourne le propriétaire de l'offre.
     * @return Joueur propriétaire
     */
    public Joueur getProprietaire() {
        return proprietaire;
    }
    
    /**
     * Retourne les cartes restantes dans l'offre.
     * @return Liste des cartes (0, 1 ou 2 cartes)
     */
    public List<Carte> getCartesRestantes() {
        List<Carte> cartes = new ArrayList<>();
        if (carteVisible != null) cartes.add(carteVisible);
        if (carteCachee != null) cartes.add(carteCachee);
        return cartes;
    }
    
    /**
     * Retourne le nombre de cartes restantes.
     * @return 0, 1 ou 2
     */
    public int getNombreCartesRestantes() {
        int count = 0;
        if (carteVisible != null) count++;
        if (carteCachee != null) count++;
        return count;
    }
    
    @Override
    public String toString() {
        return "Offre de " + proprietaire.getNom() + 
               " [Visible: " + (carteVisible != null ? carteVisible.toStringCourt() : "X") +
               ", Cachée: " + (carteCachee != null ? "?" : "X") + "]";
    }
}