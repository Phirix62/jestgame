package jest.vue;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Permet d'utiliser le terminal ET la GUI simultanément.
 * Le joueur peut répondre dans l'une ou l'autre interface - la première réponse gagne.
 */
public class GestionnaireConcurrent implements GestionnaireInteraction {
    private final VueTerminal vueTerminal;
    private final VueGraphique vueGraphique;
    
    public GestionnaireConcurrent(VueTerminal terminal, VueGraphique graphique) {
        this.vueTerminal = terminal;
        this.vueGraphique = graphique;
    }
    
    @Override
    public Carte choisirCarteOffre(String joueurNom, List<Carte> main) {
        AtomicReference<Carte> resultat = new AtomicReference<>();
        AtomicBoolean termine = new AtomicBoolean(false);
        Object lock = new Object();
        
        // Thread 1 : Terminal
        Thread threadTerminal = new Thread(() -> {
            try {
                Carte choix = vueTerminal.choisirCarteOffre(joueurNom, main);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        // Thread 2 : GUI
        Thread threadGUI = new Thread(() -> {
            try {
                Carte choix = vueGraphique.choisirCarteOffre(joueurNom, main);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        threadTerminal.start();
        threadGUI.start();
        
        synchronized (lock) {
            while (!termine.get()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    return main.get(0);
                }
            }
        }
        
        // Interrompre les threads pour arrêter les lectures en attente
        threadTerminal.interrupt();
        threadGUI.interrupt();
        
        return resultat.get();
    }
    
    @Override
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        AtomicReference<Offre> resultat = new AtomicReference<>();
        AtomicBoolean termine = new AtomicBoolean(false);
        Object lock = new Object();
        
        Thread threadTerminal = new Thread(() -> {
            try {
                Offre choix = vueTerminal.choisirOffreCible(joueurNom, offres);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        Thread threadGUI = new Thread(() -> {
            try {
                Offre choix = vueGraphique.choisirOffreCible(joueurNom, offres);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        threadTerminal.start();
        threadGUI.start();
        
        synchronized (lock) {
            while (!termine.get()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    return offres.get(0);
                }
            }
        }
        
        threadTerminal.interrupt();
        threadGUI.interrupt();
        
        return resultat.get();
    }
    
    @Override
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        AtomicReference<Carte> resultat = new AtomicReference<>();
        AtomicBoolean termine = new AtomicBoolean(false);
        Object lock = new Object();
        
        Thread threadTerminal = new Thread(() -> {
            try {
                Carte choix = vueTerminal.choisirCarteDansOffre(joueurNom, offre);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        Thread threadGUI = new Thread(() -> {
            try {
                Carte choix = vueGraphique.choisirCarteDansOffre(joueurNom, offre);
                if (termine.compareAndSet(false, true)) {
                    resultat.set(choix);
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                // Ignorer l'erreur si l'autre thread a déjà gagné
            }
        });
        
        threadTerminal.start();
        threadGUI.start();
        
        synchronized (lock) {
            while (!termine.get()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    List<Carte> visibles = offre.getCartesVisibles();
                    return !visibles.isEmpty() ? visibles.get(0) : offre.getCarteCachee();
                }
            }
        }
        
        threadTerminal.interrupt();
        threadGUI.interrupt();
        
        return resultat.get();
    }
}
