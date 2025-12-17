package jest.modele.jeu;

import jest.modele.cartes.Carte;
import jest.modele.joueurs.Joueur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une offre de x cartes (x-1 visible, 1 cachée) faite par un joueur.
 * Une offre est créée à chaque tour et peut perdre des cartes quand les autres joueurs piochent.
 */
public class Offre implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Carte> cartesVisibles;
    private Carte carteCachee;
    private Joueur proprietaire;
    
    /**
     * Constructeur d'Offre.
     * @param proprietaire Joueur qui fait l'offre
     */
    public Offre(Joueur proprietaire) {
        this.proprietaire = proprietaire;
        this.cartesVisibles = new ArrayList<>();
        this.carteCachee = null;
    }
    
    /**
     * Ajoute une carte à l'offre.
     * @param carte Carte à ajouter
     * @param visible true si carte face visible, false si cachée
     */
    public void ajouterCarte(Carte carte, boolean visible) {
        if (visible) {
            carte.reveler();
            this.cartesVisibles.add(carte);
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
        if (cartesVisibles.remove(carte)) {
            return carte;
        } else if (carte == carteCachee) {
            Carte temp = carteCachee;
            carteCachee = null;
            return temp;
        }
        throw new IllegalArgumentException("Carte non trouvée dans l'offre");
    }
    
    /**
     * Retourne la première carte visible de l'offre.
     * @return Carte visible ou null
     */
    public Carte getCarteVisible() {
        return cartesVisibles.isEmpty() ? null : cartesVisibles.get(0);
    }

    /**
     * Retourne toutes les cartes visibles.
     * @return Liste des cartes visibles
     */
    public List<Carte> getCartesVisibles() {
        return new ArrayList<>(cartesVisibles);
    }

    public int getNombreCartesVisibles() {
        return cartesVisibles.size();
    }

    /**
     * Retourne la carte visible la plus forte (pour déterminer ordre de jeu).
     * @return Carte la plus forte
     */
    public Carte getCartePlusFortVisible() {
        if (cartesVisibles.isEmpty()) return null;
        
        Carte plusForte = cartesVisibles.get(0);
        for (Carte c : cartesVisibles) {
            if (c.comparerForce(plusForte) > 0) {
                plusForte = c;
            }
        }
        return plusForte;
    }
    
    /**
     * Retourne la carte cachée de l'offre.
     * @return Carte cachée ou null
     */
    public Carte getCarteCachee() {
        return carteCachee;
    }
    
    /**
     * Vérifie si l'offre est complète (au moins 2 cartes).
     * @return true si au moins 2 cartes présentes
     */
    public boolean estComplete() {
        return getNombreCartesRestantes() >= 2;
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
     * @return Liste des cartes
     */
    public List<Carte> getCartesRestantes() {
        List<Carte> cartes = new ArrayList<>(cartesVisibles);
        if (carteCachee != null) cartes.add(carteCachee);
        return cartes;
    }
    
    /**
     * Retourne le nombre de cartes restantes.
     * @return Nombre de cartes
     */
    public int getNombreCartesRestantes() {
        int count = cartesVisibles.size();
        if (carteCachee != null) count++;
        return count;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Offre de ").append(proprietaire.getNom()).append(" [");
        
        if (!cartesVisibles.isEmpty()) {
            sb.append("Visibles: ");
            for (int i = 0; i < cartesVisibles.size(); i++) {
                sb.append(cartesVisibles.get(i).toStringCourt());
                if (i < cartesVisibles.size() - 1) sb.append(", ");
            }
        }
        
        sb.append(", Cachée: ").append(carteCachee != null ? "?" : "X");
        sb.append("]");
        
        return sb.toString();
    }
}