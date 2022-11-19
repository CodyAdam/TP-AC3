import java.util.LinkedList;
import java.util.List;

import robdd.ROBDD;
import expression.*;

public class Main {

	public static void partie1et2() {

		// ----------------- Exo1 -----------------
		Expression exp = new Et(new Atome("x"), new Atome("y")); // représente (x ^ y)
		System.out.println(exp.atomes()); // affiche la liste des atomes (=variablesbooléennes) présents dans exp
		exp = exp.remplace("x", true); // exp vaut maintenant (true ^ y)
		// System.out.println(exp.evalue()); // <- erreur car (true ^ y) ne peut pasêtre
		// évalué
		exp = exp.remplace("y", false); // exp vaut maintenant (true ^ false)
		System.out.println(exp.evalue());

		// Affichage de l'arbre associé à l'expression exp pour l'ordre x > y
		List<String> ordre_atomes = new LinkedList<String>();
		ordre_atomes.add("x");
		ordre_atomes.add("y");
		System.out.println("\n Arbre de exp : \n" + exp.arbre(ordre_atomes)); // <-que se passe-t-il ?
		Expression exp2 = new Et(new Atome("x"), new Atome("y")); // représente (x ^y)
		System.out.println("\nArbre de exp2 : \n" + exp2.arbre(ordre_atomes));

		// ----------------- Exo2 -----------------
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

		Expression exp4 = new Constante(true);
		System.out.println(exp4.estVrai()); // affiche true
		exp4 = new Non(new Constante(true));
		System.out.println(exp4.estVrai()); // affiche false

		// ----------------- Exo5 -----------------

		// représente ( (x1 <=> y1) ^ (x2 <=> y2) )
		ROBDD robdd_exp3_ordre1 = exp3.robdd(ordre_atomes3);
		ROBDD robdd_exp3_ordre2 = exp3.robdd(ordre_atomes4);
		System.out.println("\nROBDD de exp3 avec x1 > y1 > x2 > y2: \n" + robdd_exp3_ordre1);
		System.out.println("\nROBDD de exp3 avec x1 > x2 > y1 > y2: \n" + robdd_exp3_ordre2);

		// ----------------- Exo6 -----------------

		System.out.println("\nPath satisfiability exp3: " + robdd_exp3_ordre1.trouve_sat());
		System.out.println("\nPath satisfiability exp3: " + robdd_exp3_ordre2.trouve_sat());
	}

	public static void partie3() {
		int N = 2;
		Expression queen = generateNQueenExpr(N);
		// print the tree
		System.out.println(queen.arbre(generateOrder(N)));
		
		ROBDD robdd_queen = queen.robdd();
		System.out.println(robdd_queen);
		System.out.println(robdd_queen.trouve_sat_queen());
	}

	public static List<String> generateOrder(int N) {
		List<String> order = new LinkedList<String>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				order.add(i + ","+ j);
			}
		}
		return order;
	}

	public static Expression generateNQueenExpr(int N){
		Expression and = new Constante(true);
		for (int y = 0; y < N; y++) {
			Expression or = new Constante(false);
			for (int x = 0; x < N; x++) {
				or = new Ou(or, new Atome( x + ","+ y));
				Expression queenTargets = getQueenTargets(x, y, N);
				or = new Ou(or, queenTargets);
			}
			and = new Et(and, or);
		} 
		return and;
	}

	public static Expression getQueenTargets(int x, int y, int N) {
		if (N == 1) {
			return new Constante(false);
		}
		Expression and = new Constante(true);

		// no need for the horizontal check
		// becquse it is already done in the first loop
		
		// vertical check
		for (int yy = 0; yy < N; yy++) {
			if (yy != y) {
				and = new Et(and, new Non(new Atome(x + "," + yy)));
			}
		}

		// diagonal check
		for (int xx = 0; xx < N; xx++) {

			// diagonal /
			int yy = (x + y) - xx;
			if (yy < 0 && yy >= N && xx != x) {
				and = new Et(and, new Non(new Atome(xx + "," + yy)));
			}
			
			// diagonal \
			yy = xx - y;
			if (yy < 0 && yy >= N && xx != x) {
				and = new Et(and, new Non(new Atome(xx + "," + yy)));
			}
		}
		return and;
	}

	public static void main(String[] args) {
		// partie1et2();
		partie3();
	}
}
