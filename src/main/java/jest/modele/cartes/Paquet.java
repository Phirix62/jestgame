package jest.modele.cartes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Représente le paquet complet de cartes du jeu Jest.
 * Contient les 16 cartes standard + Joker + éventuellement des extensions.
 */
public class Paquet {
    private List<Carte> cartes;
    private Random random;
    
    /**
     * Constructeur de Paquet.
     * Crée un paquet vide.
     */
    public Paquet() {
        this.cartes = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Initialise le paquet avec les cartes de base et optionnellement des extensions.
     * @param avecExtension true pour inclure les cartes d'extension
     */
    public void initialiser(boolean avecExtension) {
        cartes.clear();
        
        // Ajouter les 16 cartes standard (4 couleurs × 4 valeurs)
        for (Couleur couleur : Couleur.values()) {
            // Ajouter l'As (classe spéciale)
            cartes.add(new As(couleur));
            
            // Ajouter les cartes 2, 3, 4
            for (int valeur = 2; valeur <= 4; valeur++) {
                cartes.add(new CarteNormale(couleur, valeur));
            }
        }
        
        // Ajouter le Joker
        cartes.add(new Joker());
        
        // TODO: Ajouter cartes d'extension si activées
        if (avecExtension) {
            // Extension à implémenter
        }
    }
    
    /**
     * Mélange le paquet.
     */
    public void melanger() {
        Collections.shuffle(cartes, random);
    }
    
    /**
     * Distribue un nombre de cartes depuis le paquet.
     * @param nombre Nombre de cartes à distribuer
     * @return Liste des cartes distribuées
     * @throws IllegalStateException Si pas assez de cartes
     */
    public List<Carte> distribuer(int nombre) {
        if (nombre > cartes.size()) {
            throw new IllegalStateException("Pas assez de cartes dans le paquet");
        }
        
        List<Carte> distribuees = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            distribuees.add(cartes.remove(0));
        }
        return distribuees;
    }
    
    /**
     * Vérifie si le paquet est vide.
     * @return true si vide
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }
    
    /**
     * Retourne le nombre de cartes restantes.
     * @return Taille du paquet
     */
    public int getTaille() {
        return cartes.size();
    }
    
    /**
     * Retourne toutes les cartes restantes.
     * @return Liste des cartes
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }
    
    /**
     * Ajoute des cartes au paquet.
     * @param nouvelles Cartes à ajouter
     */
    public void ajouterCartes(List<Carte> nouvelles) {
        cartes.addAll(nouvelles);
    }
}
