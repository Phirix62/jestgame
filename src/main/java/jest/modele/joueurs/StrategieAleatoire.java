package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import java.util.List;
import java.util.Random;

/**
 * Stratégie aléatoire : tous les choix sont faits au hasard.
 * Utilisation basique
 */
public class StrategieAleatoire implements StrategieJeu {
    private Random random;
    
    /**
     * Constructeur de StrategieAleatoire.
     */
    public StrategieAleatoire() {
        this.random = new Random();
    }
    
    @Override
    public Carte choisirCarteOffre(List<Carte> main, Jest jest) {
        return main.get(random.nextInt(main.size()));
    }
    
    @Override
    public Offre choisirOffreCible(List<Offre> offres, Jest jest) {
        return offres.get(random.nextInt(offres.size()));
    }
    
    @Override
    public Carte choisirCarteDansOffre(Offre offre, Jest jest) {
        return random.nextBoolean() ? offre.getCarteVisible() : offre.getCarteCachee();
    }
}