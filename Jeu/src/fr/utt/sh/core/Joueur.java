/**
 * 
 */
package fr.utt.sh.core;

import java.util.ArrayList;

import fr.utt.sh.core.strategy.Strategy;
import fr.utt.sh.core.strategy.StrategyJoueurConsole;

/**
 * Cette classe représente un joueur. Elle implémente le patron de conception
 * {@code Strategy}. Chaque tour, ControlleurJeu appelle la méthode
 * {@link Joueur#jouer()}, ce qui en tour appelle la méthode exécute d'une
 * {@link Strategy}.
 * 
 * @author grego
 *
 */
public class Joueur {

	ArrayList<Carte> cartes = new ArrayList<Carte>();
	Carte            cartePiochee;
	Carte            carteVictoire;

	ControlleurJeu cj = ControlleurJeu.getInstance();

	String id;

	Strategy strategy = new StrategyJoueurConsole();

	/**
	 * Constructeur par défaut.
	 */
	public Joueur() {
		this.id = "defaut";
	}
	
	public Joueur(String id, Strategy strategy) {
		this.id = id;
		this.strategy = strategy;
	}

	/**
	 * Constructeur clonage.
	 * 
	 * @param joueur Le {@link Joueur} a cloner.
	 */
	public Joueur(Joueur joueur) {
		this.id            = joueur.id;
		this.strategy      = joueur.strategy;
		this.cartePiochee  = joueur.cartePiochee;
		this.carteVictoire = joueur.carteVictoire;
	}

	/**
	 * Seulement utilisé pour les règles standards.
	 * 
	 * @return La {@link Carte} précédemment piochée.
	 */
	public Carte getCartePiochee() {
		return cartePiochee;
	}

	public void setCartePiochee(Carte carte) {
		cartePiochee = carte;
	}

	/**
	 * Seulement utilisé pour les règles standards.
	 * 
	 * @return La {@link Carte} victoire.
	 */
	public Carte getCarteVictoire() {
		return carteVictoire;
	}

	public void setCarteVictoire(Carte carte) {
		carteVictoire = carte;
	}

	/**
	 * Donne la carte a l'index donné.
	 * 
	 * @param index L'indice de la carte demandée.
	 * @return {@link Carte}
	 */
	public Carte getCarte(int index) {
		if (cartes.size() <= index) {
			return null;
		}

		return cartes.get(index);
	}

	/**
	 * @return {@code int} le nombre de cartes.
	 */
	public int getNombreCartes() {
		return cartes.size();
	}

	/**
	 * 
	 * @return Une {@link ArrayList} des cartes dans le main du joueur.
	 */
	public ArrayList<Carte> getCartes() {
		return cartes;
	}

	/**
	 * Exécute la strategy donné lors de la construction du joueur.
	 * 
	 * @return {@code true} si le joueur a fini son tour, {@code false} sinon.
	 */
	public boolean jouer() {
		if (strategy.execute())
			return true;
		return false;
	}

	public enum Actions {
		PiocherCarte, PoserCarte, DeplacerCarte
	}

	public String toString() {
		return "Joueur_" + id;
	}

	public Joueur getClone() {
		return new Joueur(this);
	}
}
