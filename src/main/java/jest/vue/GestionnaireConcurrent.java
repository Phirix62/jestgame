package jest.vue;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import jest.vue.terminal.VueTerminal;
import jest.vue.graphique.VueGraphique;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Permet d'utiliser le terminal ET la GUI simultanément.
 * Le joueur peut répondre dans l'une ou l'autre interface - la première réponse gagne.
 * Essaye de gérer proprement l'annulation de l'interface perdante.
 * Dans les fait ce mécanisme n'est pas fonctionelle à 100%
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
        AtomicBoolean terminalGagne = new AtomicBoolean(false);
        Object lock = new Object();
        
        // Thread 1 : Terminal
        Thread threadTerminal = new Thread(() -> {
            try {
                Carte choix = vueTerminal.choisirCarteOffre(joueurNom, main);
                if (termine.compareAndSet(false, true)) {
                    terminalGagne.set(true);
                    resultat.set(choix);
                    // Annuler l'interface graphique
                    vueGraphique.annulerDialogues();
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
                    terminalGagne.set(false);
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
                    threadTerminal.interrupt();
                    threadGUI.interrupt();
                    vueGraphique.annulerDialogues();
                    return main.get(0);
                }
            }
        }
        
        // Interrompre le thread perdant
        if (terminalGagne.get()) {
            threadGUI.interrupt();
        } else {
            threadTerminal.interrupt();
        }
        
        // Attendre un peu que les threads se terminent proprement
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return resultat.get();
    }
    
    @Override
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        AtomicReference<Offre> resultat = new AtomicReference<>();
        AtomicBoolean termine = new AtomicBoolean(false);
        AtomicBoolean terminalGagne = new AtomicBoolean(false);
        Object lock = new Object();
        
        Thread threadTerminal = new Thread(() -> {
            try {
                Offre choix = vueTerminal.choisirOffreCible(joueurNom, offres);
                if (termine.compareAndSet(false, true)) {
                    terminalGagne.set(true);
                    resultat.set(choix);
                    vueGraphique.annulerDialogues();
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
                    terminalGagne.set(false);
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
                    threadTerminal.interrupt();
                    threadGUI.interrupt();
                    vueGraphique.annulerDialogues();
                    return offres.get(0);
                }
            }
        }
        
        if (terminalGagne.get()) {
            threadGUI.interrupt();
        } else {
            threadTerminal.interrupt();
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return resultat.get();
    }
    
    @Override
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        AtomicReference<Carte> resultat = new AtomicReference<>();
        AtomicBoolean termine = new AtomicBoolean(false);
        AtomicBoolean terminalGagne = new AtomicBoolean(false);
        Object lock = new Object();
        
        Thread threadTerminal = new Thread(() -> {
            try {
                Carte choix = vueTerminal.choisirCarteDansOffre(joueurNom, offre);
                if (termine.compareAndSet(false, true)) {
                    terminalGagne.set(true);
                    resultat.set(choix);
                    vueGraphique.annulerDialogues();
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
                    terminalGagne.set(false);
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
                    threadTerminal.interrupt();
                    threadGUI.interrupt();
                    vueGraphique.annulerDialogues();
                    List<Carte> visibles = offre.getCartesVisibles();
                    return !visibles.isEmpty() ? visibles.get(0) : offre.getCarteCachee();
                }
            }
        }
        
        if (terminalGagne.get()) {
            threadGUI.interrupt();
        } else {
            threadTerminal.interrupt();
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return resultat.get();
    }
}
