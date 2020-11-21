package fr.utt.sh.core.tapis;

import java.util.Arrays;

import fr.utt.sh.console_ui.VisitorAffichage;
import fr.utt.sh.core.Carte;
import fr.utt.sh.core.score.VisitorComptageScore;

/**
 * Classe de base pour créer des tapis triangulaires. La hauteur et la largeur
 * seront egaux.
 * 
 * @author grego
 *
 */
public class Tapis_Triangulaire extends Tapis {

	int taille;

	boolean premiereCartePosee = false;

	Carte cartes[][];

	/**
	 * Créer un tapis triangulaire de taille {@code taille}.
	 * 
	 * @param taille
	 */
	public Tapis_Triangulaire(int taille) {
		this.taille = taille;

		cartes = new Carte[taille][];
		// Créer une matrice / liste 2d triangulaire.
		for (int i = 0; i < taille; i++) {
			cartes[i] = new Carte[i + 1];
		}
	}

	/**
	 * Construteur pour cloner un tapis.
	 * 
	 * @param cartes Une liste 2-dimmensionnelle, dont la longeur determine la
	 *               taille du jeu. LA n-ieme sous-liste est de longueur n+1 ( si n
	 *               commence a 0 ).
	 */
	public Tapis_Triangulaire(Carte[][] cartes) {
		this.cartes = new Carte[cartes.length][];
		for (int i = 0; i < cartes.length; i++) {
			this.cartes[i] = new Carte[cartes[i].length];

			for (int j = 0; j < cartes[i].length; j++) {
				this.cartes[i][j] = cartes[i][j];
			}
		}
		this.taille = cartes.length;
	}

	@Override
	public boolean poserCarte(Carte carte, int x, int y) {
		if (!positionLegale(x, y))
			return false;

		if (!positionAVoisins(x, y))
			if (premiereCartePosee)
				return false;
			else
				premiereCartePosee = true;

		if (positionJouable(x, y)) {
			if (getCarteAt(x, y) != null)
				return false;
			setCarteAt(carte, x, y);
			return true;
		}

		if (x == -1) {
			if (decalerADroite()) {
				setCarteAt(carte, 0, y);
				return true;
			}
			return false;
		} else if (y == -1) {
			if (decalerEnBas()) {
				setCarteAt(carte, x, 0);
				return true;
			}
			return false;

		} else if (y >= taille) { // Carte en-dessous du tapis
			if (decalerEnHaut()) {
				setCarteAt(carte, x, taille - 1);
				return true;
			}
			return false;
		}

		// Carte a droite du tapis
		else if (x > y) {
			if (decalerAGauche()) {
				setCarteAt(carte, y, y);
				return true;
			}

			if (decalerEnBas()) {
				setCarteAt(carte, x, x);
				return true;
			}
			return false;

		}

		return false;
	}

	// L'emplacement donné a-t-il une carte voisine ?.
	boolean positionAVoisins(int x, int y) {

		int[][] decalages = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }; // Positions relatives des cartes voisines

		for (int[] decalage : decalages) {
			int xVoisin = decalage[0] + x;
			int yVoisin = decalage[1] + y;

			if (positionJouable(xVoisin, yVoisin)) {
				if (getCarteAt(xVoisin, yVoisin) != null) {
					return true;
				}
			}
		}

		return false;

	}

	boolean decalerAGauche() {

		// Verifier que colonne gauche est vide.
		for (int y = 0; y < taille; y++) {
			if (getCarteAt(0, y) != null)
				return false;
		}

		// On commence a y=1, car si ya pas de carte dans la premiere colonne, y'en aura
		// pas une tout en haut.
		for (int y = 1; y < taille; y++) {
			for (int x = 0; x < y - 1; x++) {
				setCarteAt(getCarteAt(x + 1, y), x, y);
			}
			setCarteAt(null, y - 1, y - 1);
		}
		return true;
	}

	boolean decalerADroite() {

		// Verifier sur le coté droit qu'il n'y a pas de cartes.
		for (int y = 0; y < taille; y++) {
			if (getCarteAt(y, y) != null)
				return false;
		}

		// On commence a y=1, car si ya pas de carte a droite, y'en aura pas une en
		// haut.
		// pas une tout en haut.
		for (int y = 1; y < taille; y++) {
			for (int x = y; x > 0; x--) {
				setCarteAt(getCarteAt(x - 1, y), x, y);
			}
			setCarteAt(null, 0, y);
		}
		return true;
	}

	boolean decalerEnHaut() {
		// Verifier sur le coté droit qu'il n'y a pas de cartes.
		for (int y = 0; y < taille; y++) {
			if (getCarteAt(y, y) != null)
				return false;
		}

		for (int x = 0; x < taille; x++) {
			for (int y = x; y < taille - 1; y++) {
				setCarteAt(getCarteAt(x, y + 1), x, y);
			}
			setCarteAt(null, x, taille - 1);
		}
		return true;
	}

	boolean decalerEnBas() {
		for (int x = 0; x < taille; x++) {
			if (getCarteAt(x, taille - 1) != null)
				return false;
		}

		for (int x = 0; x < taille; x++) {
			for (int y = taille - 1; y > x; y--) {
				setCarteAt(getCarteAt(x, y - 1), x, y);
			}
			setCarteAt(null, x, x);
		}
		return true;
	}

	boolean setCarteAt(Carte carte, int x, int y) {
		if (!positionJouable(x, y))
			return false;

		cartes[y][x] = carte;
		return true;
	}

	@Override
	public Carte getCarteAt(int x, int y) {
		if (!positionJouable(x, y))
			return null;

		return cartes[y][x];
	}

	@Override
	public boolean estRempli() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Tapis getClone() {
		return new Tapis_Triangulaire(cartes);
	}

	@Override
	public void retirerCarte(int x, int y) {
		setCarteAt(null, x, y);
	}

	@Override
	public boolean positionLegale(int x, int y) {
		if (x < -1)
			return false;
		if (y < -1)
			return false;
		if (x > y + 1)
			return false;

		if (y >= taille + 1)
			return false;
		return true;

	}

	@Override
	public boolean positionJouable(int x, int y) {
		if (x < 0)
			return false;
		if (y < 0)
			return false;
		if (x > y)
			return false;
		if (y >= taille)
			return false;

		return true;
	}

	@Override
	public int getLargeur() {
		return taille;
	}

	@Override
	public int getHauteur() {
		return taille;
	}

	public void accept(VisitorAffichage v) {
		v.visit(this);
	}

	public void accept(VisitorComptageScore v) {
		v.visit(this);

	}

}
