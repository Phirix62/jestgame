package jest.vue.graphique;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsable de l'affichage des dialogues de choix pour le joueur.
 * Gère l'annulation des dialogues pour la concurrence entre vues.
 */
public class DialogueChoix {
    private final JFrame parent;
    private volatile JDialog dialogueActif = null;
    private volatile boolean annule = false;
    
    public DialogueChoix(JFrame parent) {
        this.parent = parent;
    }
    
    /**
     * Annule et ferme le dialogue actuellement ouvert.
     * Utilisé quand l'autre vue a répondu en premier.
     */
    public void annulerDialogue() {
        annule = true;
        if (dialogueActif != null) {
            SwingUtilities.invokeLater(() -> {
                if (dialogueActif != null) {
                    dialogueActif.dispose();
                    dialogueActif = null;
                }
            });
        }
    }
    
    /**
     * Dialogue pour choisir une carte à jouer face cachée.
     */
    public Carte choisirCarteOffre(String joueurNom, List<Carte> main) {
        annule = false;
        final Carte[] resultat = new Carte[1];
        final Object lock = new Object();
        
        SwingUtilities.invokeLater(() -> {
            if (annule) {
                synchronized (lock) {
                    resultat[0] = main.get(0);
                    lock.notify();
                }
                return;
            }
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JLabel instruction = new JLabel("<html><b>" + joueurNom + 
                    ", choisissez la carte à jouer FACE CACHÉE :</b></html>");
            instruction.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(instruction);
            
            ButtonGroup group = new ButtonGroup();
            JRadioButton[] boutons = new JRadioButton[main.size()];
            
            for (int i = 0; i < main.size(); i++) {
                Carte carte = main.get(i);
                JRadioButton bouton = new JRadioButton(carte.toString());
                bouton.setFont(new Font("Arial", Font.PLAIN, 12));
                Color couleur = CarteRenderer.getCouleurCarte(carte.getCouleur());
                bouton.setForeground(couleur);
                
                group.add(bouton);
                boutons[i] = bouton;
                panel.add(bouton);
            }
            
            boutons[0].setSelected(true);
            
            JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
            );
            dialogueActif = optionPane.createDialog(parent, "Créer votre offre");
            dialogueActif.setVisible(true);
            
            Object value = optionPane.getValue();
            int choix = (value == null || annule) ? JOptionPane.CLOSED_OPTION : 
                       ((Integer) value).intValue();
            dialogueActif = null;
            
            if (choix == JOptionPane.OK_OPTION) {
                for (int i = 0; i < boutons.length; i++) {
                    if (boutons[i].isSelected()) {
                        resultat[0] = main.get(i);
                        break;
                    }
                }
            } else {
                resultat[0] = main.get(0);
            }
            
            synchronized (lock) {
                lock.notify();
            }
        });
        
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return main.get(0);
            }
        }
        
        return resultat[0];
    }
    
    /**
     * Dialogue pour choisir une offre cible.
     */
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        annule = false;
        final Offre[] resultat = new Offre[1];
        final Object lock = new Object();
        
        SwingUtilities.invokeLater(() -> {
            if (annule) {
                synchronized (lock) {
                    resultat[0] = offres.get(0);
                    lock.notify();
                }
                return;
            }
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JLabel instruction = new JLabel("<html><b>" + joueurNom + 
                    ", choisissez une offre :</b></html>");
            instruction.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(instruction);
            
            ButtonGroup group = new ButtonGroup();
            JRadioButton[] boutons = new JRadioButton[offres.size()];
            
            for (int i = 0; i < offres.size(); i++) {
                Offre offre = offres.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("<html>Offre de <b>").append(offre.getProprietaire().getNom()).append("</b><br>");
                
                List<Carte> cartesVisibles = offre.getCartesVisibles();
                if (!cartesVisibles.isEmpty()) {
                    sb.append("  Visible(s): ");
                    for (int j = 0; j < cartesVisibles.size(); j++) {
                        sb.append(cartesVisibles.get(j).toStringCourt());
                        if (j < cartesVisibles.size() - 1) sb.append(", ");
                    }
                    sb.append("<br>");
                }
                sb.append("  Cachée: ???</html>");
                
                JRadioButton bouton = new JRadioButton(sb.toString());
                bouton.setFont(new Font("Arial", Font.PLAIN, 12));
                
                group.add(bouton);
                boutons[i] = bouton;
                panel.add(bouton);
            }
            
            boutons[0].setSelected(true);
            
            JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
            );
            dialogueActif = optionPane.createDialog(parent, "Choisir une offre");
            dialogueActif.setVisible(true);
            
            Object value = optionPane.getValue();
            int choix = (value == null || annule) ? JOptionPane.CLOSED_OPTION : 
                       ((Integer) value).intValue();
            dialogueActif = null;
            
            if (choix == JOptionPane.OK_OPTION) {
                for (int i = 0; i < boutons.length; i++) {
                    if (boutons[i].isSelected()) {
                        resultat[0] = offres.get(i);
                        break;
                    }
                }
            } else {
                resultat[0] = offres.get(0);
            }
            
            synchronized (lock) {
                lock.notify();
            }
        });
        
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return offres.get(0);
            }
        }
        
        return resultat[0];
    }
    
    /**
     * Dialogue pour choisir une carte dans une offre.
     */
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        annule = false;
        final Carte[] resultat = new Carte[1];
        final Object lock = new Object();
        
        List<Carte> cartesVisibles = offre.getCartesVisibles();
        final List<Carte> toutesCartes = new ArrayList<>(cartesVisibles);
        if (offre.getCarteCachee() != null) {
            toutesCartes.add(offre.getCarteCachee());
        }
        
        SwingUtilities.invokeLater(() -> {
            if (annule) {
                synchronized (lock) {
                    resultat[0] = toutesCartes.get(0);
                    lock.notify();
                }
                return;
            }
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JLabel instruction = new JLabel("<html><b>" + joueurNom + 
                    ", choisissez une carte dans l'offre de " + 
                    offre.getProprietaire().getNom() + " :</b></html>");
            instruction.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(instruction);
            
            ButtonGroup group = new ButtonGroup();
            JRadioButton[] boutons = new JRadioButton[toutesCartes.size()];
            
            for (int i = 0; i < cartesVisibles.size(); i++) {
                Carte carte = cartesVisibles.get(i);
                JRadioButton bouton = new JRadioButton("Carte visible: " + carte.toString());
                bouton.setFont(new Font("Arial", Font.PLAIN, 12));
                Color couleur = CarteRenderer.getCouleurCarte(carte.getCouleur());
                bouton.setForeground(couleur);
                
                group.add(bouton);
                boutons[i] = bouton;
                panel.add(bouton);
            }
            
            if (offre.getCarteCachee() != null) {
                JRadioButton bouton = new JRadioButton("Carte cachée: ???");
                bouton.setFont(new Font("Arial", Font.PLAIN, 12));
                bouton.setForeground(Color.GRAY);
                
                group.add(bouton);
                boutons[cartesVisibles.size()] = bouton;
                panel.add(bouton);
            }
            
            boutons[0].setSelected(true);
            
            JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
            );
            dialogueActif = optionPane.createDialog(parent, "Choisir une carte");
            dialogueActif.setVisible(true);
            
            Object value = optionPane.getValue();
            int choix = (value == null || annule) ? JOptionPane.CLOSED_OPTION : 
                       ((Integer) value).intValue();
            dialogueActif = null;
            
            if (choix == JOptionPane.OK_OPTION) {
                for (int i = 0; i < boutons.length; i++) {
                    if (boutons[i].isSelected()) {
                        resultat[0] = toutesCartes.get(i);
                        break;
                    }
                }
            } else {
                resultat[0] = toutesCartes.get(0);
            }
            
            synchronized (lock) {
                lock.notify();
            }
        });
        
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return toutesCartes.get(0);
            }
        }
        
        return resultat[0];
    }
}
