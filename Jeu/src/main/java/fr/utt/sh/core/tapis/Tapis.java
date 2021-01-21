package fr.utt.sh.core.tapis;

import fr.utt.sh.console_ui.VisitableAffichage;
import fr.utt.sh.core.Carte;
import fr.utt.sh.core.Position;
import fr.utt.sh.core.score.VisitableComptageScore;
import java.util.Observable;

/**
 * {@link Tapis} est une classe abstraite de base pour toutes les variantes de
 * tapis possibles. <br>
 * 
 * La position d'une carte est donnée par deux parametres integers, pouvant
 * representer soit des coordonnées cartésiens comme pour {@link TapisRect},
 * soit polaire pour une implémentation de tapis circulaire (actuelement il n'y
 * a pas d'implémentations polaires de tapis). <br>
 * {@link Tapis} doit permet un deplacement dynamique du jeu. Cela se fait en
 * ayant les emplacements du bord toujours vides. Si une carte est posé sur ces
 * emplacements, le {@link Tapis} tente de decaler toutes les cartes, en
 * assurant que la taille totales du tapis est toujours respecté.
 * 
 * 
 * 
 * @author grego
 *
 */
public abstract class Tapis extends Observable implements VisitableAffichage, VisitableComptageScore {

	/**
	 * Echanger la position de deux cartes posées sur le tapis. Cette méthode prend
	 * en compte si
	 * 
	 * @param x1 Abscisse depart de la carte.
	 * @param y1 Ordonnee depart de la carte.
	 * @param x2 Abscisse arrivée de la carte.
	 * @param y2 Ordonnee arrivée de la carte.
	 * @return {@code true} si l'échange a pu etre effectuer, {@code false} sinon.
	 */
	public boolean deplacerCarte(int x1, int y1, int x2, int y2) {
		if (!positionLegale(x1, y1))
			return false;
		if (!positionLegale(x2, y2))
			return false;

		if (getCarteAt(x1, y1) == null)
			return false;

		if (positionSurTapis(x2, y2)) {
			if (getCarteAt(x2, y2) != null) {
				// Si la positoin d'arrivée est jouable et contient une carte, retournée false;
				return false;
			}

			Carte carteDeplacee = getCarteAt(x1, y1); // Obtenir la carte a decplacer.
			setCarteAt(null, x1, y1); // Supprimer temporairement la carte.

			if (poserCarte(carteDeplacee, x2, y2)) {
				this.setChanged();
				this.notifyObservers();
				return true;
			}

			setCarteAt(carteDeplacee, x1, y1); // Si pas pu poser, le remettre d'ou elle vient.
			return false;
		}

		Carte carteDeplacee = getCarteAt(x1, y1); // Obtenir la carte a decplacer.
		setCarteAt(null, x1, y1); // Supprimer temporairement la carte.

		if (poserCarte(carteDeplacee, x2, y2)) { // Essayer de la poser
			this.setChanged();
			this.notifyObservers();
			return true;
		}

		setCarteAt(carteDeplacee, x1, y1); // Si pas pu poser, le remettre d'ou elle vient.
		return false;
	}

	/**
	 * Obtenir la {@link Carte} a une position donnée.
	 * 
	 * @param x Abscisse de la carte.
	 * @param y Ordonnee de la carte.
	 * @return {@code null} si la position donnee est invalide, ou il n'u a pas de
	 *         carte a la position spécifiée , sinon une {@link Carte}.
	 */
	public abstract Carte getCarteAt(int x, int y);

	abstract boolean setCarteAt(Carte carte, int x, int y);

	/**
	 * Poser une {@link Carte} a une certaine position.
	 * 
	 * @param carte La carte a poser.
	 * @param x     Abscisse de la carte.
	 * @param y     Ordonnée de la carte.
	 * @return {@link Carte} si il y a bien une carte a la position specifiée,
	 *         {@code null} si il n'y a pas de carte, ou la position n'est pas
	 *         valide.
	 */
	public abstract boolean poserCarte(Carte carte, int x, int y);

	/**
	 * @return {@code true} si le terrain ne peut plus accepter de cartes,
	 *         {@code false} sinon
	 */
	public abstract boolean estRempli();

	/**
	 * Donne un clone du tapis, afin d'avoir access direct au tapis, tout en
	 * protegeant celui utilisé pour le jeu.
	 * 
	 * @return Un {@code Tapis} clone.
	 */
	public abstract Tapis getClone();

	/**
	 * Determiner si a la position donnée, une carte peut etre posée, incluant les
	 * bords, c'est-a-dire les positions qui peuvent aussi faire deplacer le tapis.
	 * 
	 * @param x Abscisse de la position.
	 * @param y Ordonnée de la position.
	 * @return {@code true} si la position est legale, {@code false} sinon.
	 */
	public abstract boolean positionLegale(int x, int y);

	/**
	 * Determiner si a la position donnée, une carte peut etre posée, n'incluant pas
	 * les bords. <br>
	 * Different de {@link #positionLegale(int, int)}.
	 * 
	 * @param x Abscisse de la position.
	 * @param y Ordonnée de la position.
	 * @return {@code true} si la position est jouable, {@code false} sinon.
	 */
	public abstract boolean positionSurTapis(int x, int y);

	/**
	 * Retire une carte du tapis a la position specifié.
	 * 
	 * @param x Abscisse de la carte.
	 * @param y Ordonnée de la carte.
	 */
	public void retirerCarte(int x, int y) {
		setCarteAt(null, x, y);
	}

	/**
	 * La position donnée a-t-elle un (des) voisin(s) ?
	 * 
	 * @param x Abscisse position.
	 * @param y Ordonnée position.
	 * @return {@code true} si la positon a au moins un voisin, {@code false} sinon.
	 */
	public abstract boolean positionAVoisins(int x, int y);

	/**
	 * Obtenir la largeur du tapis.
	 * 
	 * @return {@code int}.
	 */
	public abstract int getLargeur();

	/**
	 * Obtenir la hauteur du tapis.
	 * 
	 * @return {@code int}.
	 */
	public abstract int getHauteur();

	/**
	 * Indique sur le tapis est completement vide.
	 * 
	 * @return Boolean
	 */
	public abstract boolean estVide();

	/**
	 * Raccoursis pour {@link #getCarteAt(int, int)}.
	 * 
	 * @param posCarte La position de la carte a obtenir.
	 * @return La carte a la position donnée.
	 */
	public Carte getCarteAt(Position posCarte) {
		return getCarteAt(posCarte.getX(), posCarte.getY());
	}

	/**
	 * Vider le tapis.
	 */
	public abstract void clear();
}