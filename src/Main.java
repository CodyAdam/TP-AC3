import java.util.LinkedList;
import java.util.List;

import robdd.ROBDD;
import expression.*;

public class Main {

	public static void main(String[] args) {
		/*
		 * // EXEMPLE
		 * Expression exp = new Et(new Atome("x"), new Atome("y")); // représente (x ^
		 * y)
		 * System.out.println(exp.atomes()); // affiche la liste des atomes (=variables
		 * booléennes) présents dans exp
		 * exp = exp.remplace("x", true); // exp vaut maintenant (true ^ y)
		 * System.out.println(exp.evalue()); // <- erreur car (true ^ y) ne peut pas
		 * être évalué
		 * exp = exp.remplace("y", false); // exp vaut maintenant (true ^ false)
		 * System.out.println(exp.evalue());
		 * 
		 * // Affichage de l'arbre associé à l'expression exp pour l'ordre x > y
		 * List<String> ordre_atomes = new LinkedList<String>();
		 * ordre_atomes.add("x");
		 * ordre_atomes.add("y");
		 * System.out.println("\n Arbre de exp : \n" + exp.arbre(ordre_atomes)); // <-
		 * que se passe-t-il ?
		 * Expression exp2 = new Et(new Atome("x"), new Atome("y")); // représente (x ^
		 * y)
		 * System.out.println("\nArbre de exp2 : \n" + exp2.arbre(ordre_atomes));
		 */

		// EXO 2
		// représente ( (x1 <=> y1) ^ (x2 <=> y2) )
		Expression exp3 = new Et(new Equiv(new Atome("x1"), new Atome("y1")), new Equiv(new Atome("x2"), new Atome("y2")));
		System.out.println(exp3.atomes()); // affiche la liste des atomes (=variables booléennes) présents dans exp

		// Affichage de l'arbre associé à l'expression exp pour l'ordre x1 > y1 > x2 >
		// y2

		List<String> ordre_atomes3 = new LinkedList<String>();
		ordre_atomes3.add("x1");
		ordre_atomes3.add("y1");
		ordre_atomes3.add("x2");
		ordre_atomes3.add("y2");
		System.out.println("\nArbre de exp3 : \n" + exp3.arbre(ordre_atomes3));

		// Affichage de l'arbre associé à l'expression exp pour l'ordre x1 > x2 > y1 >
		// y2
		List<String> ordre_atomes4 = new LinkedList<String>();
		ordre_atomes4.add("x1");
		ordre_atomes4.add("x2");
		ordre_atomes4.add("y1");
		ordre_atomes4.add("y2");
		System.out.println("\nArbre de exp4 : \n" + exp3.arbre(ordre_atomes4));

		Expression exp = new Constante(true);
		System.out.println(exp.estVrai()); // affiche true
		exp = new Non(new Constante(true));
		System.out.println(exp.estVrai()); // affiche false

		// ----------------- Exo3 -----------------

		// représente ( (x1 <=> y1) ^ (x2 <=> y2) )
		System.out.println(exp3.robdd(ordre_atomes3));

	}
}
