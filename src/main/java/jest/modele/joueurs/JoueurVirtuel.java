package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import java.util.List;

/**
 * Représente un joueur virtuel (IA) qui utilise une stratégie de jeu.
 * Pattern Strategy : délègue toutes les décisions à la stratégie.
 */
public class JoueurVirtuel extends Joueur {
    private StrategieJeu strategie;
    
    /**
     * Constructeur de JoueurVirtuel.
     * @param nom Nom du joueur virtuel
     * @param strategie Stratégie de jeu à utiliser
     */
    public JoueurVirtuel(String nom, StrategieJeu strategie) {
        super(nom);
        this.strategie = strategie;
    }
    
    /**
     * Change la stratégie du joueur virtuel.
     * @param strategie Nouvelle stratégie
     */
    public void setStrategie(StrategieJeu strategie) {
        this.strategie = strategie;
    }
    
    /**
     * Retourne la stratégie actuelle.
     * @return Stratégie
     */
    public StrategieJeu getStrategie() {
        return strategie;
    }
    
    @Override
    public Carte choisirCarteOffre(List<Carte> main) {
        Carte choix = strategie.choisirCarteOffre(main, jest);
        System.out.println(nom + " (IA) choisit une carte pour son offre.");
        return choix;
    }
    
    @Override
    public Offre choisirOffreCible(List<Offre> offres) {
        Offre choix = strategie.choisirOffreCible(offres, jest);
        System.out.println(nom + " (IA) choisit l'offre de " + choix.getProprietaire().getNom());
        return choix;
    }
    
    @Override
    public Carte choisirCarteDansOffre(Offre offre) {
        Carte choix = strategie.choisirCarteDansOffre(offre, jest);
        String type = choix == offre.getCarteVisible() ? "visible" : "cachée";
        System.out.println(nom + " (IA) prend la carte " + type);
        return choix;
    }
}