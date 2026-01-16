/**
 * Gestion des cartes du jeu Jest.
 * 
 * <h2>Contenu</h2>
 * <p>Ce package contient toutes les classes relatives aux cartes du jeu :
 * cartes normales, jokers, as, ainsi que les trophées et le paquet.</p>
 * 
 * <h2>Hiérarchie des cartes</h2>
 * <ul>
 *   <li>{@link jest.modele.cartes.Carte} - Classe abstraite de base</li>
 *   <li>{@link jest.modele.cartes.CarteNormale} - Cartes standards (valeur + couleur)</li>
 *   <li>{@link jest.modele.cartes.Joker} - Carte spéciale Joker</li>
 *   <li>{@link jest.modele.cartes.As} - Carte spéciale As</li>
 * </ul>
 * 
 * <h2>Classes utilitaires</h2>
 * <ul>
 *   <li>{@link jest.modele.cartes.Couleur} - Énumération des couleurs</li>
 *   <li>{@link jest.modele.cartes.Paquet} - Collection de cartes</li>
 *   <li>{@link jest.modele.cartes.Trophee} - Trophées à gagner</li>
 *   <li>{@link jest.modele.cartes.ConditionTrophee} - Conditions d'obtention des trophées</li>
 * </ul>
 */
package jest.modele.cartes;
