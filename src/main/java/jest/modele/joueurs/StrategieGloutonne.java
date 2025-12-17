package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.util.Comparator;
import java.util.List;
/**
 * Stratégie gloutonne : privilégie toujours les cartes de plus haute 
 * valeur
 */
public class StrategieGloutonne implements StrategieJeu{
	
	
	public StrategieGloutonne() {
    }
    
	
    @Override
    public Carte choisirCarteOffre(List<Carte> main, Jest jest) {
        return main.stream()
                .min(Comparator.comparingInt(Carte::getValeurFaciale))
                .orElse(main.get(0));
    }
    
    
    @Override
    public Offre choisirOffreCible(List<Offre> offres, Jest jest) {
        Offre offreMax = offres.get(0);
        for (Offre offre : offres) {
            if (offre.getCarteVisible().getValeurFaciale() > offreMax.getCarteVisible().getValeurFaciale()) {
                offreMax = offre;
            }
        }
        return offreMax;  
    }
    
    @Override
    public Carte choisirCarteDansOffre(Offre offre, Jest jest) {
        return offre.getCarteVisible();
    }
}

