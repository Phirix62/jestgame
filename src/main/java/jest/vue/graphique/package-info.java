/**
 * Vue graphique (interface Swing).
 * 
 * <h2>Responsabilité</h2>
 * <p>Gère l'affichage et l'interaction via une interface graphique Swing.
 * Utilise des composants Swing ({@link javax.swing.JFrame}, {@link javax.swing.JPanel}, etc).
 * Disposition des panneaux gérée avec BorderLayout.</p>
 * 
 * <h2>Classes principales</h2>
 * <ul>
 *   <li>{@link jest.vue.graphique.VueGraphique} - Vue principale</li>
 *   <li>{@link jest.vue.graphique.DialogueChoix} - Dialogues modaux pour les choix joueur</li>
 *   <li>{@link jest.vue.graphique.ConstructeurInterfaceGraphique} - Construction des panneaux Swing</li>
 *   <li>{@link jest.vue.graphique.PanneauFactory} - Factory pour les panneaux</li>
 *   <li>{@link jest.vue.graphique.CarteRenderer} - Rendu visuel des cartes</li>
 * </ul>
 * <h2>Pattern Factory</h2>
 * <p>{@link jest.vue.graphique.PanneauFactory} utilise le pattern Factory pour créer
 * des composants Swing réutilisables</p>
 */
package jest.vue.graphique;
