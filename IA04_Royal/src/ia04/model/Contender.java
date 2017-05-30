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
		Contender closestEnemy = getClosestEnemy();
		//TODO: Va vers lui si dist>1, castagne sinon (tape, l'autre devrait nous taper aussi normalement)
	}

	// trouve l'ennemi le plus proche, retourne null si aucun n'est visible
	public Contender getClosestEnemy() {
		//teste toutes les distances pour trouver le plus proche
		//(vérifier si getNeighbors classe pas déjà par proximité)
		for (int i = 1; i <= distancePerception; i++) {
			Contender closestEnemy = findEnemyAtRange(i);
			if (closestEnemy != null) {
				System.out.println(closestEnemy);
				return closestEnemy;
			}
		}
		System.out.println("No contender found within range "+distancePerception);
		return null;
	}

	// trouve un ennemi à une distance range
	public Contender findEnemyAtRange(int range) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier contender à une distance range
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender cont = (Contender) o;
				//le contender est inclut dans le bag, il faut pas le prendre en compte
				if (cont.x != x || cont.y != y) {
					System.out.println("contender found in range " + range);
					System.out.println("his location : " + cont.x + "," + cont.y);
					return cont;
				}
			}
		}
		return null;
	}

}
