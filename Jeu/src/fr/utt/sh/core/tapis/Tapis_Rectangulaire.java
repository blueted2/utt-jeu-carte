package fr.utt.sh.core.tapis;

import fr.utt.sh.console_ui.VisitorAffichage;
import fr.utt.sh.core.Carte;
import fr.utt.sh.core.score.VisitorComptageScore;

/**
 * Le tapis de jeu standard. Un rectangle de taille largeur x hauteur, mais
 * acceptant des positions -1 a largeur et -1 a hauteur inclus, afin de permttre
 * au jeu de se decaler.
 * 
 * @author grego
 *
 */
public class Tapis_Rectangulaire extends Tapis {

	private Carte[][] cartes;
	boolean           premiereCartePosee = false;

	private int largeur;
	private int hauteur;

	/**
	 * Constructeur pour un tapis rectangulaire general.
	 * 
	 * @param largeur Largeur du jeu.
	 * @param hauteur Hauteur du jeu.
	 */
	public Tapis_Rectangulaire(int largeur, int hauteur) {
		this.largeur = largeur;
		this.hauteur = hauteur;

		cartes = new Carte[largeur][hauteur];
	}

	/**
	 * Constructeur pour cloner un tapis.
	 * 
	 * @param cartes Une liste 2d des cartes deja jouées.
	 */
	public Tapis_Rectangulaire(Carte[][] cartes) {
		this.cartes = new Carte[cartes.length][];
		for (int i = 0; i < cartes.length; i++) {
			this.cartes[i] = new Carte[cartes[i].length];
			
			for(int j =0; j<cartes[i].length; j++) {
				this.cartes[i][j] = cartes[i][j];
			}
		}

		largeur = cartes.length;
		hauteur = cartes[0].length;
	}

	/**
	 * Getter largeur du tapis.
	 * 
	 * @return int
	 */
	public int getLargeur() {
		return largeur;
	}

	/**
	 * Getter hauteur du tapis.
	 * 
	 * @return int
	 */
	public int getHauteur() {
		return hauteur;
	}

	// La position est elle valide, c'est-a-dire dans les bornes du tapis ? Peu
	// inclure les bords pour permettre au tapis de se decaller.
	public boolean positionLegale(int x, int y) {
		if (x < -1 || x > largeur)
			return false;
		if (y < -1 || y > hauteur)
			return false;

		return true;
	}

	// La position est elle valide, mais cette fois sans les bords.
	public boolean positionJouable(int x, int y) {
		if (x < 0 || x > largeur - 1)
			return false;
		if (y < 0 || y > hauteur - 1)
			return false;

		return true;
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

	@Override
	public Carte getCarteAt(int x, int y) {
		return cartes[x][y];
	}

	boolean setCarteAt(Carte carte, int x, int y) {
		if (!positionLegale(x, y))
			return false;
		cartes[x][y] = carte;
		return true;
	}

	@Override
	public boolean poserCarte(Carte carte, int x, int y) {

		if (!positionLegale(x, y))
			return false;

		// Cas particulier pour la première carte, car elle ne peut jamais avoir de
		// voisins
		if (premiereCartePosee && !positionAVoisins(x, y))
			return false;

		// Si la position n'est pas sur les bords, est ce qu'il y a une carte ?
		if (positionJouable(x, y)) {
			if (getCarteAt(x, y) != null)
				return false;
			setCarteAt(carte, x, y);
			premiereCartePosee = true;
			return true;
		}

		if (x == -1) {
			if (!decalerADroite())
				return false;
			setCarteAt(carte, 0, y);
		} else if (x == largeur) {
			if (!decalerAGauche())
				return false;
			setCarteAt(carte, 0, largeur - 1);

		} else if (y == -1) {
			if (!decalerEnBas())
				return false;
			setCarteAt(carte, x, 0);
		} else if (x == largeur) {
			if (!decalerEnHaut())
				return false;
			setCarteAt(carte, x, hauteur - 1);
		}

		premiereCartePosee = true;
		return true;
	}

	boolean decalerAGauche() {
		for (int y = 0; y < hauteur; y++) {
			// Si il y a une carte sur la colonne de gauche, les cartes ne peuvent pas être
			// décalés.
			if (getCarteAt(0, y) != null) {
				return false;
			}
		}

		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur - 1; x++) {
				Carte c = getCarteAt(x + 1, y);
				setCarteAt(c, x, y);
			}
			setCarteAt(null, largeur - 1, y);
		}
		return true;
	}

	boolean decalerADroite() {
		for (int y = 0; y < hauteur; y++) {
			// Si il y a une carte sur la colonne de gauche, les cartes ne peuvent pas être
			// décalés.
			if (getCarteAt(largeur - 1, y) != null) {
				return false;
			}
		}

		for (int y = 0; y < hauteur; y++) {
			for (int x = largeur - 1; x > 0; x--) {
				Carte c = getCarteAt(x - 1, y);
				setCarteAt(c, x, y);
			}
			setCarteAt(null, 0, y);
		}
		return true;
	}

	boolean decalerEnHaut() {
		for (int x = 0; x < largeur; x++) {
			if (getCarteAt(x, 0) != null)
				return false;
		}

		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur - 1; y++) {
				Carte c = getCarteAt(x, y + 1);
				setCarteAt(c, x, y);
			}
			setCarteAt(null, x, hauteur - 1);
		}
		return true;

	}

	boolean decalerEnBas() {
		for (int x = 0; x < largeur; x++) {
			if (getCarteAt(x, hauteur - 1) != null)
				return false;
		}

		for (int x = 0; x < largeur; x++) {
			for (int y = hauteur - 1; y > 0; y--) {
				Carte c = getCarteAt(x, y - 1);
				setCarteAt(c, x, y);
			}
			setCarteAt(null, x, 0);
		}
		return true;

	}

	public boolean estRempli() {
		for (int y = 0; y < hauteur; y++) {
			for (int x = 0; x < largeur; x++) {
				if (getCarteAt(x, y) == null)
					return false;
			}
		}
		return true;
	}

	@Override
	public Tapis getClone() {
		return new Tapis_Rectangulaire(cartes);
	}

	@Override
	public void accept(VisitorAffichage v) {
		v.visit(this);
	}

	@Override
	public void accept(VisitorComptageScore v) {
		v.visit(this);
	}

}
