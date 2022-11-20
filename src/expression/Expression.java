package expression;

import arbre.*;
import robdd.*;

import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public abstract class Expression {

	// renvoie la liste (non ordonnées) des atomes associées à l'objet courant
	public abstract Set<String> atomes();

	// renvoie la valeur (booléenne) de l'objet courant.
	// Si celui-ci ne peut pas être évalué (présence d'atomes), on lève une
	// exception.
	public abstract boolean evalue() throws RuntimeException;

	// remplace l'atome s de l'objet courant par le booléen b
	// renvoie l'expression correspondante
	public abstract Expression remplace(String s, boolean b);

	// renvoie une version simplifiée de l'expression this
	// selon les règles du tableau du TD 5, partie 2.3
	public abstract Expression simplifier();

	public boolean estVrai() {
		try {
			return evalue();
		} catch (Exception e) {
			// if the expression cannot be evaluated (e.g. contains atoms) : return false
			return false;
		}
	}

	public boolean estFaux() {
		try {
			return !evalue();
		} catch (Exception e) {
			// if the expression cannot be evaluated (e.g. contains atoms) : return false
			return false;
		}
	}

	// construit l'arbre de shannon correspondant à l'expression courante en prenant
	// comme ordre l'ordre indiqué par l'argument ordre_atomes
	public ArbreShannon arbre(List<String> ordre_atomes) {
		if (this.atomes().isEmpty()) {
			return new FeuilleShannon(evalue());
		} else {
			assert !ordre_atomes.isEmpty() : "Toutes les variables n'apparaissaient pas dans ordre_atomes !";
			List<String> ordre_atomes2 = new LinkedList<String>(ordre_atomes); // copie pour que arbre(ordre) ne modifie pas
																																					// ordre
			String name = ordre_atomes2.remove(0);
			Expression e1 = remplace(name, false);
			Expression e2 = remplace(name, true);
			return new NoeudShannon(name, e1.arbre(ordre_atomes2), e2.arbre(ordre_atomes2));
		}
	}

	// renvoie le ROBDD correspondant à l'expression courante en prenant comme ordre
	// l'ordre indiqué par l'argument ordre_atomes
	public ROBDD robdd(List<String> atomes_ordonnes) {
		// On commence par vérifier que la liste atomes_ordonnes
		// contient bien tous les atomes de l'expression courante.
		if (!this.atomes().equals(new HashSet<String>(atomes_ordonnes))) {
			System.err
					.println("robdd: atomes_ordonnes ne contient pas tous les atomes de l'expression, ou en contient en trop.");
			System.exit(0);
		}
		// On crée un ROBDD vide
		ROBDD R = new ROBDD();
		// On construit le ROBDD
		this.construireROBDD(R, atomes_ordonnes);
		return R;
	}

	// fonction récursive qui renvoie le noeud ROBDD associé à l'expression courante
	// et construit le ROBDD (représenté par la liste G)
	private int construireROBDD(ROBDD G, List<String> atomes_ordonnes) {
		Expression F = this.simplifier();
		if (F.estVrai()) {
			return 1;
		} else if (F.estFaux()) {
			return 0;
		} else {
			String p = atomes_ordonnes.get(0);
			List<String> atomes_ordonnes2 = new LinkedList<String>(atomes_ordonnes);
			atomes_ordonnes2.remove(0);
			Expression left = F.remplace(p, false);
			Expression right = F.remplace(p, true);

			int n1 = left.construireROBDD(G, atomes_ordonnes2);
			int n2 = right.construireROBDD(G, atomes_ordonnes2);

			return integrer(n1, n2, G, p);
		}
	}

	// renvoie le ROBDD correspondant à l'expression courante avec un ordre
	// aléatoire (donné par this.atomes())
	public ROBDD robdd() {
		return robdd(new LinkedList<String>(this.atomes()));
	}

	public int integrer(int n1, int n2, ROBDD G, String p) {
		if (n1 == n2) {
			return n1;
		}

		// Check if the node already exists
		int n = G.obtenirROBDDIndex(p, n1, n2);
		if (n == -1) {
			// If not, create it and return its index
			Noeud_ROBDD newNode = new Noeud_ROBDD(p, n1, n2);
			G.ajouter(newNode);
			return newNode.getId();
		} else {
			// If yes, return its index
			return n;
		}
	}

	public abstract String toString(int depth);

	public String toString() {
		return toString(0);
	}

	/*	
	 * Used to print the expression with a depth
	 */
	public static String indent(int depth) {
		String s = "";
		for (int i = 0; i < depth; i++) {
			s += " ";
		}
		return s;
	}
}
