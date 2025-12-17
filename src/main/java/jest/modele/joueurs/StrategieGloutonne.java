package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.util.List;
/**
 * Stratégie gloutonne : l'ia privilégie les gain immédiats (carte de plus haute valeur faciale disponible).
 * Utilisation simple et peu efficace
 */
public class StrategieGloutonne implements StrategieJeu{
	
	
	public StrategieGloutonne() {
    }
    
	
    @Override
    public Carte choisirCarteOffre(List<Carte> main, Jest jest) {
        Carte carteMax = main.get(0);
        for (Carte carte : main) {
            if (carte.getValeurFaciale() > carteMax.getValeurFaciale()) {
                carteMax = carte;
            }
        }
        return carteMax;
    }
    
    
    @Override
    public Offre choisirOffreCible(List<Offre> offres, Jest jest) {
        Offre offreMax = offres.get(0);
        for (Offre offre : offres) {
            if (offre.getCartePlusFortVisible().getValeurFaciale() > offreMax.getCartePlusFortVisible().getValeurFaciale()) {
                offreMax = offre;
            }
        }
        return offreMax;  
    }

    @Override
    public Carte choisirCarteDansOffre(Offre offre, Jest jest) {
        return offre.getCartePlusFortVisible();
    }
}

