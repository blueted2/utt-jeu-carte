package fr.utt.sh.console_ui;

import fr.utt.sh.core.Carte;
import fr.utt.sh.core.Joueur;
import fr.utt.sh.core.tapis.Tapis_Rectangulaire;
import fr.utt.sh.core.tapis.Tapis_Triangulaire;

/**
 * @author grego
 *
 */
public interface VisitorAffichage {
	/**
	 * Visiter un {@link Tapis_Rectangulaire}, pour pouvoir créer une version affichable.
	 * @param tapis {@link Tapis_Rectangulaire}
	 */
	public void visit(Tapis_Rectangulaire tapis);
	
	/**Visiter un {@link Tapis_Triangulaire}, pour pouvoir créer une version affichable.
	 * @param tapis {@link Tapis_Triangulaire}
	 */
	public void visit(Tapis_Triangulaire tapis);
	
	/**
	 * Visiter une {@link Carte}, pour pouvoir créer une version affichable.
	 * @param carte {@link Carte}
	 */
	public void visit(Carte carte);

	public void visit(Joueur joueur);
	
}
