package ia04.model;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.util.Bag;

public class Contender extends MySteppable {

	public int vie;
	public int attaque;
	public int distancePerception = 5;

	private Beings beings;

	public Contender(int x, int y, int attaque, int vie) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
	}

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		if (vie <= 0)
			meurt(beings);
		else {

			Contender closestEnemy = getClosestEnemy();
			// TODO: Va vers lui si dist>1, castagne sinon (tape, l'autre
			// devrait
			// nous taper aussi normalement)
			if (closestEnemy != null) {
				if (!isAtRange(closestEnemy)) {
					MoveTowards(closestEnemy.x, closestEnemy.y, 1);
					//si on empêche le déplacement vers l'endroit où on est, certains contestants ne bougent pas, je sais pas pourquoi
				} else {
					System.out.println("ennemi à portée, je le tape");
					attack(closestEnemy);
				}
			} else {
				// pas d'ennemis trouvé, marche vers le centre
				MoveTowards(beings.GRID_SIZE / 2, beings.GRID_SIZE / 2, 1);
			}
		}
	}

	// trouve l'ennemi le plus proche, retourne null si aucun n'est visible
	public Contender getClosestEnemy() {
		// teste toutes les distances pour trouver le plus proche
		// (vérifier si getNeighbors classe pas déjà par proximité)
		for (int i = 1; i <= distancePerception; i++) {
			Contender closestEnemy = findEnemyAtRange(i);
			if (closestEnemy != null) {
				System.out.println(closestEnemy);
				return closestEnemy;
			}
		}
		System.out.println("No contender found within range " + distancePerception);
		return null;
	}

	// trouve un ennemi à une distance range
	public Contender findEnemyAtRange(int range) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier contender à une distance range
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender cont = (Contender) o;
				// le contender est inclut dans le bag, il faut pas le prendre
				// en compte
				if (cont.x != x || cont.y != y) {
					System.out.println("contender found in range " + range);
					System.out.println("his location : " + cont.x + "," + cont.y);
					return cont;
				}
			}
		}
		return null;
	}

	// se déplace de 1 vers la case (x2,y2)
	public void MoveTowards(int x2, int y2, int dist) {
		System.out.println("Currently at (" + x + "," + y + ")");
		System.out.println("moving towards (" + x2 + "," + y2 + ")");
		int dx = x2 - x;
		int dy = y2 - y;
		if (Math.abs(dx) > Math.abs(dy)) {
			if (dx > 0) {
				beings.yard.set(x, y, null);
				x++;
				beings.yard.set(x, y, this);
			} else {
				beings.yard.set(x, y, null);
				x--;
				beings.yard.set(x, y, this);
			}
		} else {
			if (dy > 0) {
				beings.yard.set(x, y, null);
				y++;
				beings.yard.set(x, y, this);
			} else {
				beings.yard.set(x, y, null);
				y--;
				beings.yard.set(x, y, this);
			}

		}
		System.out.println("Now at " + x + "," + y);
	}

	private boolean isAtRange(Contender cont) {
		int dx = cont.x - x;
		int dy = cont.y - y;
		return (Math.abs(dx) <= 1 && Math.abs(dy) <= 1);
	}

	public void attack(Contender cont) {
		cont.vie -= attaque;
	}
}
