package ia04.model;

import sim.engine.SimState;
import sim.util.Bag;

public abstract class Personnage extends MySteppable {
	
	
	private static final long serialVersionUID = 1142873167865702301L;
	public int vie;
	public int attaque;
	public int energie;
	public int distancePerception;
	
	@Override
	public abstract void step(SimState arg0);

	public Personnage(int x, int y, int vie, int attaque, int energie, int distancePerception) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
		this.distancePerception = distancePerception;
	}
	
	// se d�place de dist vers la case (x2,y2)
	public void MoveTowards(int x2, int y2, int dist, Beings beings, int energieDeplacement) {
		System.out.println("Currently at (" + x + "," + y + ")");
		System.out.println("moving towards (" + x2 + "," + y2 + ")");
		int i, dx, dy;
		for (i = 0; i < dist; i++) {
			dx = x2 - x;
			dy = y2 - y;
			if (Math.abs(dx) > Math.abs(dy)) {
				if (dx > 0) {
					beings.yard.setObjectLocation(null, x, y);
					x++;
					beings.yard.setObjectLocation(this, x, y);
				} else if (dx < 0) {
					beings.yard.setObjectLocation(null, x, y);
					x--;
					beings.yard.setObjectLocation(this, x, y);
				}
			} else {
				if (dy > 0) {
					beings.yard.setObjectLocation(null, x, y);
					y++;
					beings.yard.setObjectLocation(this, x, y);
				} else if (dy < 0) {
					beings.yard.setObjectLocation(null, x, y);
					y--;
					beings.yard.setObjectLocation(this, x, y);
				}
			}
		}

		if (energie > 0)
			energie = energie - dist * energieDeplacement;

		else
			vie--;
		
		System.out.println("Now at " + x + "," + y);
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
	
	protected boolean isAtRange(MySteppable cont, int range) {
		int dx = cont.x - x;
		int dy = cont.y - y;
		return (Math.abs(dx) <= range && Math.abs(dy) <= range);
	}

	public void attack(Personnage closestEnemy, int ENERGIE_PAR_ATT) {
		closestEnemy.vie -= attaque;
		if (vie < 0) vie = 0;
		energie -= ENERGIE_PAR_ATT;
		//Si un contender tue un monstre, on augmente son attaque. Il a le droit à une belle récompense (boost)
		if ((closestEnemy.vie < 0) && (closestEnemy.getClass() == Monstre.class)){
			attaque += 15;
		}
	}

}
