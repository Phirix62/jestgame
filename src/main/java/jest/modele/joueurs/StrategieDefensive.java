package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.cartes.Couleur;

import jest.modele.jeu.Offre;
import java.util.List;

/**
 * Stratégie défensive : préserver son score en évitant les Carreaux à 
 * tout prix.
 */
public class StrategieDefensive implements StrategieJeu {

   
    public StrategieDefensive() {
    }

    @Override
    public Carte choisirCarteOffre(List<Carte> main, Jest jest) {
        
        for (Carte carte : main) {
            if (carte.getCouleur() == Couleur.CARREAU) {
                return carte;
            }
        }
        
        Carte carteMin = main.get(0);
        for (Carte carte : main) {
            if (carte.getValeurFaciale() < carteMin.getValeurFaciale()) {
                carteMin = carte;
            }
        }
        return carteMin;
    }

    @Override
    public Offre choisirOffreCible(List<Offre> offres, Jest jest) {
        for (Offre offre : offres) {
            if (offre.getCarteVisible().getCouleur() != Couleur.CARREAU) {
                return offre;
            }
        }
        
        Offre offreMin = offres.get(0);
        for (Offre offre : offres) {
            if (offre.getCarteVisible().getValeurFaciale() < offreMin.getCarteVisible().getValeurFaciale()) {
                offreMin = offre;
            }
        }
        return offreMin;
    }

    @Override
    public Carte choisirCarteDansOffre(Offre offre, Jest jest) {
        
        if (offre.getCarteVisible().getCouleur() == Couleur.CARREAU) {
            return offre.getCarteCachee();
        } else {
            return offre.getCarteVisible();
        }
    }
}
