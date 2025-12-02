package jest.modele.jeu;

import jest.modele.cartes.Carte;
import java.util.*;

/**
 * Représente la pioche du jeu Jest.
 * Les cartes sont empilées et piochées du dessus (comportement Stack).
 */
public class Pioche {
    private Stack<Carte> cartes;
    private Random random;
    
    /**
     * Constructeur de Pioche.
     * @param cartesInitiales Cartes à mettre dans la pioche
     */
    public Pioche(List<Carte> cartesInitiales) {
        this.cartes = new Stack<>();
        this.random = new Random();
        if (cartesInitiales != null) {
            cartes.addAll(cartesInitiales);
        }
    }
    
    /**
     * Pioche une carte du dessus de la pile.
     * @return Carte piochée
     * @throws IllegalStateException Si la pioche est vide
     */
    public Carte piocher() {
        if (estVide()) {
            throw new IllegalStateException("La pioche est vide");
        }
        return cartes.pop();
    }
    
    /**
     * Pioche plusieurs cartes.
     * @param nombre Nombre de cartes à piocher
     * @return Liste des cartes piochées
     * @throws IllegalStateException Si pas assez de cartes
     */
    public List<Carte> piocher(int nombre) {
        if (nombre > cartes.size()) {
            throw new IllegalStateException("Pas assez de cartes dans la pioche");
        }
        
        List<Carte> piochees = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            piochees.add(piocher());
        }
        return piochees;
    }
    
    /**
     * Vérifie si la pioche est vide.
     * @return true si vide
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }
    
    /**
     * Retourne le nombre de cartes restantes.
     * @return Taille de la pioche
     */
    public int getTaille() {
        return cartes.size();
    }
    
    /**
     * Ajoute des cartes à la pioche.
     * @param nouvelles Cartes à ajouter
     */
    public void ajouterCartes(List<Carte> nouvelles) {
        if (nouvelles != null) {
            cartes.addAll(nouvelles);
        }
    }
    
    /**
     * Mélange les cartes de la pioche.
     */
    public void melanger() {
        List<Carte> temp = new ArrayList<>(cartes);
        Collections.shuffle(temp, random);
        cartes.clear();
        cartes.addAll(temp);
    }
    
    @Override
    public String toString() {
        return "Pioche [" + cartes.size() + " cartes]";
    }
}