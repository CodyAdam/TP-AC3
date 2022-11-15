package robdd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
}
