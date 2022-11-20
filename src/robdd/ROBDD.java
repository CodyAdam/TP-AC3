package robdd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import expression.Atome;
import expression.Constante;
import expression.Et;
import expression.Expression;
import expression.Non;
import expression.Ou;

//Représente un ROBDD sous forme de liste de Noeud_ROBDD
public class ROBDD {

	// Constantes pour les numéros des "feuilles" VRAI et FAUX
	public static final int idFalse = 0;
	public static final int idTrue = 1;

	// liste représentant le ROBDD
	private List<Noeud_ROBDD> R;

	// construit un ROBDD vide
	public ROBDD() {
		R = new LinkedList<Noeud_ROBDD>();
	}

	// ajoute le Noeud_ROBDD n au ROBDD courant
	public void ajouter(Noeud_ROBDD n) {
		R.add(n);
	}

	// renvoie le nombre de noeuds du ROBDD
	public int nb_noeuds() {
		return R.size() + 2; // longueur de la liste R + les 2 noeuds correspondants à VRAI et FAUX
	}

	@Override
	public String toString() {
		return R.toString();
	}

	// renvoie l'index, dans la liste R, du noeud BDD associé à la variable nom et
	// dont les fils droit et gauche sont d'indices respectifs fd et fg.
	// Si ce noeud n'existe pas dans le diagramme, la fonction renvoie -1.
	public int obtenirROBDDIndex(String nom, int fg, int fd) {
		for (int i = 0; i < R.size(); i++) {
			Noeud_ROBDD noeud = R.get(i);
			if (noeud.getNom().equals(nom) &&
					noeud.getIdFilsDroit() == fd &&
					noeud.getIdFilsGauche() == fg) {
				return noeud.getId();
			}
		}
		return -1;
	}

	// seek one of the path leading to 1 in the tree
	// return the path as a string
	public String trouve_sat() {
		// find path that leads to 1
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(1);
		String res = "";
		while (q.size() > 0) {
			int id = q.poll();
			for (Noeud_ROBDD n : R) {
				if (n.getIdFilsDroit() == id || n.getIdFilsGauche() == id) {
					res += n.getNom();
					if (n.getIdFilsDroit() == id) {
						res += "=1  ";
					} else {
						res += "=0  ";
					}
					q.add(n.getId());
					break;
				}
			}
		}
		return res.equals("") ? "Pas de chemin" : res;
	}

	// seek one of the path leading to 1 in the tree
	// return only the variables that are set to 1
	public List<String> trouve_sat_list() {
		// find path that leads to 1
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(1);
		List<String> res = new ArrayList<String>();
		while (q.size() > 0) {
			int id = q.poll();
			for (Noeud_ROBDD n : R) {
				if (n.getIdFilsDroit() == id || n.getIdFilsGauche() == id) {
					if (n.getIdFilsDroit() == id) {
						res.add(n.getNom());
					}
					q.add(n.getId());
					break;
				}
			}
		}
		return res;
	}

	public static void reines_affiche_sat(int n) {
		Expression queen = generateNQueenExpr(n);
		// print the expression
		// System.out.println(queen);

		ROBDD robdd_queen = queen.robdd();
		List<String> result = robdd_queen.trouve_sat_list();
		if (result.size() == 0) {
			System.out.println("Pas de solution pour N=" + n + " reines");
		} else {
			System.out.println("Une solution pour N=" + n + " reines");

			// Draw the chessboard
			
			// draw a board with boarders using ┌─│├┤└┘┴┬┼

			// Exemple :
			// ┌──┬──┬──┬──┐
			// │  │Q │  │  │
			// ├──┼──┼──┼──┤
			// │  │  │  │Q │
			// ├──┼──┼──┼──┤
			// │Q │  │  │  │
			// ├──┼──┼──┼──┤
			// │  │  │Q │  │
			// └──┴──┴──┴──┘

			for (int y = 0; y <= n; y++) {
				for (int x = 0; x < n; x++) {
					if (y == 0 && x == 0) {
						// print the top border "┌──┬──┬──┬──┐"
						System.out.print("┌──");
						for (int i = 0; i < n; i++) {
							if (i == n - 1) {
								System.out.print("┐");
							} else {
								System.out.print("┬──");
							}
						}
					} else if (x == 0 && y == n) {
						// print the bottom border "└──┴──┴──┴──┘"
						System.out.print("└──");
						for (int i = 0; i < n; i++) {
							if (i == n - 1) {
								System.out.print("┘");
							} else {
								System.out.print("┴──");
							}
						}
						System.out.println();
						return;
					} else if (x == 0) {
						// print the middle border "├──┼──┼──┼──┤"
						System.out.print("├──");
						for (int i = 0; i < n; i++) {
							if (i == n - 1) {
								System.out.print("┤");
							} else {
								System.out.print("┼──");
							}
						}
					}

					if (x == 0) {
						System.out.print("\n│");
					}

					// print the content of the tile
					if (result.contains(x + "," + y)) {
						System.out.print("Q │");
					} else {
						System.out.print("  │");
					}

				}
				System.out.println();
			}
		}

	}

	public static List<String> generateOrder(int N) {
		List<String> order = new LinkedList<String>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				order.add(i + "," + j);
			}
		}
		return order;
	}

	public static Expression generateNQueenExpr(int N) {
		Expression and = new Constante(true);
		for (int y = 0; y < N; y++) {
			Expression or = new Constante(false);
			for (int x = 0; x < N; x++) {
				Expression queenTargets = getQueenTargets(x, y, N);
				or = new Ou(or, new Et(new Atome(x + "," + y), queenTargets));
			}
			and = new Et(and, or);
		}
		return and.simplifier();
	}

	public static Expression getQueenTargets(int x, int y, int N) {
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
			if (yy >= 0 && yy < N && xx != x) {
				and = new Et(and, new Non(new Atome(xx + "," + yy)));
			}

			// diagonal \
			yy = (y - x) + xx;
			if (yy >= 0 && yy < N && xx != x) {
				and = new Et(and, new Non(new Atome(xx + "," + yy)));
			}
		}
		return and;
	}
}
