package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.cartes.Couleur;

import jest.modele.jeu.Offre;
import java.util.List;

/**
 * Stratégie défensive : préserver son score en évitant les Carreaux à tout prix.
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
            if (carte.getValeurEffective(jest) < carteMin.getValeurEffective(jest)) {
                carteMin = carte;
            }
        }
        return carteMin;
    }

    @Override
    public Offre choisirOffreCible(List<Offre> offres, Jest jest) {
        int nbCarreaux = 0;
        int minCarreaux = 2;
        Offre choix = offres.get(0);
        for (Offre offre : offres) {
            for (Carte carte : offre.getCartesVisibles()) {
                if (carte.getCouleur() == Couleur.CARREAU) {
                    nbCarreaux++;
                }
            }
            if (nbCarreaux < minCarreaux) {
                minCarreaux = nbCarreaux;
                choix = offre;
            }
        }
        return choix;
    }

    @Override
    public Carte choisirCarteDansOffre(Offre offre, Jest jest) {
        
        for (Carte carte : offre.getCartesVisibles()) {
            if (carte.getCouleur() != Couleur.CARREAU) {
                return carte;
            }
        }
        return offre.getCartesVisibles().get(0);
    }
}
