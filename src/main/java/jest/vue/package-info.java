/**
 * Vues de l'application Jest.
 * 
 * <h2>Interfaces utilisateur</h2>
 * <p>Ce package contient les deux interfaces utilisateur de l'application :
 * une vue terminal (ligne de commande) et une vue graphique (Swing).</p>
 * 
 * <h2>Sous-packages</h2>
 * <ul>
 *   <li>{@link jest.vue.terminal} - Vue terminal (console)</li>
 *   <li>{@link jest.vue.graphique} - Vue graphique (Swing)</li>
 * </ul>
 * 
 * <h2>Interfaces communes</h2>
 * <ul>
 *   <li>{@link jest.vue.Vue} - Interface commune des vues</li>
 *   <li>{@link jest.vue.GestionnaireInteraction} - Gestion des interactions joueur</li>
 * </ul>
 * 
 * <h2>Concurrence des vues</h2>
 * <ul>
 *   <li>{@link jest.vue.GestionnaireConcurrent} - Gère l'utilisation simultanée des deux vues</li>
 * </ul>
 * 
 * <h2>Pattern Observer</h2>
 * <p>Les vues implémentent {@link jest.controleur.ObservateurPartie} pour être notifiées
 * des changements d'état du modèle. Ce qui garantit une séparation stricte entre le modèle
 * et les vues.</p>
 */
package jest.vue;
