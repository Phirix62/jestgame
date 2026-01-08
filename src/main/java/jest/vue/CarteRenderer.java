package jest.vue;

import jest.modele.cartes.Carte;
import jest.modele.cartes.Couleur;

import javax.swing.*;
import java.awt.*;

/**
 * Responsable du rendu visuel des cartes.
 */
public class CarteRenderer {
    
    /**
     * Crée une représentation visuelle d'une carte.
     */
    public static JLabel creerMiniCarte(Carte carte) {
        String texte;
        Color couleur;

        if (carte.estVisible()) {
            texte = carte.toStringCourt();
            couleur = getCouleurCarte(carte.getCouleur());
        } else {
            texte = "??";
            couleur = Color.GRAY;
        }

        JLabel label = new JLabel(texte, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(couleur);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 10));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        label.setPreferredSize(new Dimension(30, 40));

        return label;
    }
    
    /**
     * Retourne la couleur AWT correspondant à la couleur de carte.
     */
    public static Color getCouleurCarte(Couleur couleur) {
        switch (couleur) {
            case COEUR:
                return new Color(200, 0, 0);
            case CARREAU:
                return new Color(255, 140, 0);
            case TREFLE:
                return new Color(0, 100, 0);
            case PIQUE:
                return new Color(0, 0, 0);
            case SPECIALE:
                return new Color(128, 0, 128);
            default:
                return Color.GRAY;
        }
    }
}
