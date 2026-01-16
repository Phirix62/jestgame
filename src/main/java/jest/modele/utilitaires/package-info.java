/**
 * Utilitaires du modèle.
 * 
 * <h2>Contenu</h2>
 * <p>Ce package contient les classes utilitaires pour le modèle,
 * notamment la sauvegarde/chargement des parties et la gestion des scores.</p>
 * 
 * <h2>Classes principales</h2>
 * <ul>
 *   <li>{@link jest.modele.utilitaires.GestionnaireSauvegarde} - Sauvegarde/chargement via sérialisation Java</li>
 *   <li>{@link jest.modele.utilitaires.GestionnaireScores} - Classement et détermination du gagnant</li>
 * </ul>
 * 
 * <h2>Système de sauvegarde</h2>
 * <p>Les parties sont sauvegardées au format {@code .jest} dans le dossier {@code sauvegardes/}.
 * La sérialisation Java est utilisée pour préserver l'état complet de la partie.</p>
 */
package jest.modele.utilitaires;
