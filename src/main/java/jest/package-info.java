/**
 * Jest Game - Jeu de cartes stratégique.
 * 
 * <h2>Vue d'ensemble</h2>
 * <p>Application complète du jeu Jest implémentant l'architecture MVC.
 * Le jeu permet à 3 ou 4 joueurs (humains ou IA) de constituer le meilleur Jest (collection de cartes)
 * en choisissant stratégiquement dans les offres des adversaires.</p>
 * 
 * <h2>Fonctionnalités principales</h2>
 * <ul>
 *   <li>3 ou 4 joueurs (humains ou IA avec différentes stratégies)</li>
 *   <li>Extensions de jeu</li>
 *   <li>Variantes de règles</li>
 *   <li>Double interface : Terminal + Interface Graphique Swing</li>
 *   <li>Système de sauvegarde/chargement</li>
 * </ul>
 * 
 * <h2>Architecture MVC</h2>
 * <ul>
 *   <li>{@link jest.modele} - <b>MODEL</b> : Logique métier du jeu</li>
 *   <li>{@link jest.controleur} - <b>CONTROLLER</b> : Orchestration et contrôle de l'application</li>
 *   <li>{@link jest.vue} - <b>VIEW</b> : Interfaces utilisateur</li>
 * </ul>
 * 
 * <h2>Patterns de conception</h2>
 * <ul>
 *   <li><b>MVC</b> : Séparation stricte Model-View-Controller</li>
 *   <li><b>Observer</b> : Les vues observent le modèle via {@link jest.controleur.ObservateurPartie}</li>
 *   <li><b>Strategy</b> : Stratégies IA interchangeables ({@link jest.modele.joueurs})</li>
 *   <li><b>Visitor</b> : Calcul des scores par couleur ({@link jest.modele.score})</li>
 *   <li><b>Factory</b> : {@link jest.vue.graphique.PanneauFactory}</li>
 * </ul>
 * 
 * <h2>Point d'entrée</h2>
 * @see jest.Main#main(String[])
 * 
 * @author Nathan Honoré et Ayat Atraoui
 * @version 2.1.0
 */
package jest;
