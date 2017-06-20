package ia04.model;

import ia04.model.Map.Zone;
import sim.engine.SimState;
import sim.util.Bag;

public abstract class Personnage extends Element {

	private static final long serialVersionUID = 1142873167865702301L;
	public int vie;
	public int attaque;
	public int energie;
	public int distancePerception;

	public int personalMaxVie;
	public int personalNativeAttaque;

	@Override
	public abstract void step(SimState arg0);

	public Personnage(int x, int y, int vie, int attaque, int energie, int distancePerception) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
		this.distancePerception = distancePerception;
	}

	public Map getMap(int x, int y) {
		// CEtte méthode va être override par les enfants
		return null;
	}

	// se d�place de dist vers la case (x2,y2)
	public void MoveTowards(int x2, int y2, int dist, Beings beings, int energieDeplacement) {
		System.out.println("Currently at (" + x + "," + y + ")");
		if (getMap(x2, y2).z.equals(Zone.EAU)) {
			System.out.println("On ne va pas dans l'eau!");
			return;
		}
		System.out.println("moving towards (" + x2 + "," + y2 + ")");
		int i, dx, dy;
		boolean g_ok = true, d_ok = true, b_ok = true, h_ok = true;
		for (i = 0; i < dist; i++) {
			dx = x2 - x;
			dy = y2 - y;

			// On teste des directions possibles
			g_ok = isOk(x - 1, y);
			d_ok = isOk(x + 1, y);
			b_ok = isOk(x, y - 1);
			h_ok = isOk(x, y + 1);

			if (dx >= dy) {
				/**** x prioritaire ****/
				if ((g_ok) || (d_ok)) {
					if ((dx > 0) && (d_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						x++;
						beings.yard.setObjectLocation(this, x, y);
					} else if ((dx < 0) && (g_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						x--;
						beings.yard.setObjectLocation(this, x, y);
					} else {
						if ((dy > 0) && (h_ok)) {
							beings.yard.setObjectLocation(null, x, y);
							y++;
							beings.yard.setObjectLocation(this, x, y);
						} else if ((dy < 0) && (b_ok)) {
							beings.yard.setObjectLocation(null, x, y);
							y--;
							beings.yard.setObjectLocation(this, x, y);
						}
					}
				}
				/*** fin x prio ***/
			} else {
				/*** y prioritaire ***/
				if ((b_ok) || (h_ok)) {
					if ((dy > 0) && (h_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						y++;
						beings.yard.setObjectLocation(this, x, y);
					} else if ((dy < 0) && (b_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						y--;
						beings.yard.setObjectLocation(this, x, y);
					}
				} else {
					if ((dx > 0) && (d_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						x++;
						beings.yard.setObjectLocation(this, x, y);
					} else if ((dx < 0) && (g_ok)) {
						beings.yard.setObjectLocation(null, x, y);
						x--;
						beings.yard.setObjectLocation(this, x, y);
					}
				}
				/*** fin y prio ***/
			}
		}

		energie = energie - dist * energieDeplacement;
		if (energie <= 0) {
			energie = 0;
			vie--;
		}

		System.out.println("Now at " + x + "," + y);
	}

	public boolean isOk(int x, int y) {
		Map cMap = getMap(x, y);
		return (cMap.z != Zone.EAU);
	}

	// trouve l'ennemi le plus proche, retourne null si aucun n'est visible
	public Personnage getClosestEnemy(Beings beings) {
		// teste toutes les distances pour trouver le plus proche
		// (v�rifier si getNeighbors classe pas d�j� par proximit�)
		for (int i = 1; i <= distancePerception; i++) {
			Personnage closestEnemy = findEnemyAtRange(i, beings);
			if (closestEnemy != null) {
				System.out.println("ClosestEnemy found, at " + closestEnemy.x + " ; " + closestEnemy.y);
				return closestEnemy;
			}
		}
		System.out.println("No contender found within range " + distancePerception);
		return null;
	}

	// trouve un ennemi � une distance range
	@SuppressWarnings("deprecation")
	public Personnage findEnemyAtRange(int range, Beings beings) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier contender � une distance range
		for (Object o : b) {
			if (o instanceof Personnage) {
				Personnage cont = (Personnage) o;
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

	protected boolean isAtRange(Element cont, int range) {
		int dx = cont.x - x;
		int dy = cont.y - y;
		return (Math.abs(dx) <= range && Math.abs(dy) <= range);
	}

	public void attack(Personnage closestEnemy, int ENERGIE_PAR_ATT) {
		closestEnemy.vie -= attaque;
		if (vie < 0)
			vie = 0;
		energie -= ENERGIE_PAR_ATT;
		if (energie < 0)
			energie = 0;
		// Si un contender tue un monstre, on augmente son attaque. Il a le
		// droit à une belle récompense (boost)
		if ((closestEnemy.vie < 0) && (closestEnemy.getClass() == Monstre.class)) {
			attaque += 15;
		}
	}

}
