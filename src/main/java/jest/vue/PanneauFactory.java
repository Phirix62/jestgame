package jest.vue;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import jest.modele.joueurs.Joueur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Factory pour créer les différents panneaux de l'interface.
 */
public class PanneauFactory {
    
    /**
     * Crée un panneau pour afficher un joueur et son Jest.
     */
    public static JPanel creerPanneauJoueur(Joueur joueur) {
        JPanel panneau = new JPanel(new BorderLayout(5, 5));
        panneau.setBackground(Color.WHITE);
        panneau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Nom et score
        JLabel labelNom = new JLabel(joueur.getNom(), SwingConstants.CENTER);
        labelNom.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel labelScore = new JLabel("Score: " + joueur.getScore(), SwingConstants.CENTER);
        labelScore.setFont(new Font("Arial", Font.PLAIN, 12));

        // Jest (cartes collectées)
        JPanel panneauJest = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        panneauJest.setBackground(Color.LIGHT_GRAY);
        panneauJest.setBorder(BorderFactory.createTitledBorder("Jest"));

        List<Carte> cartesJest = joueur.getJest().getCartes();
        for (Carte carte : cartesJest) {
            panneauJest.add(CarteRenderer.creerMiniCarte(carte));
        }

        JPanel panneauInfo = new JPanel(new GridLayout(2, 1));
        panneauInfo.setOpaque(false);
        panneauInfo.add(labelNom);
        panneauInfo.add(labelScore);

        panneau.add(panneauInfo, BorderLayout.NORTH);
        panneau.add(panneauJest, BorderLayout.CENTER);

        return panneau;
    }
    
    /**
     * Crée un panneau pour afficher une offre.
     */
    public static JPanel creerPanneauOffre(Joueur joueur, Offre offre) {
        JPanel panneauOffre = new JPanel(new BorderLayout(5, 5));
        panneauOffre.setBackground(Color.WHITE);
        panneauOffre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.ORANGE, 2),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JLabel labelJoueur = new JLabel(joueur.getNom(), SwingConstants.CENTER);
        labelJoueur.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel panneauCartes = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panneauCartes.setBackground(Color.LIGHT_GRAY);

        // Cartes visibles
        for (Carte carte : offre.getCartesVisibles()) {
            panneauCartes.add(CarteRenderer.creerMiniCarte(carte));
        }

        // Carte cachée
        if (offre.getCarteCachee() != null) {
            panneauCartes.add(CarteRenderer.creerMiniCarte(offre.getCarteCachee()));
        }

        panneauOffre.add(labelJoueur, BorderLayout.NORTH);
        panneauOffre.add(panneauCartes, BorderLayout.CENTER);

        return panneauOffre;
    }
}
