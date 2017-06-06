package ia04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sim.engine.SimState;
import sim.util.Bag;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class Contender extends MySteppable {
	
	public static int DIST_PERCEPTION = 5;
	public static int MAX_DEP = 4;
	public static int MAX_ENERGIE = 20;
	public static int MIN_VIE = 10;
	public static int MAX_VIE = 20;
	public static int MIN_ATTAQUE = 1;
	public static int MAX_ATTAQUE = 5;
	public static int ENERGIE_PAR_DEP = 1;
	public static int ENERGIE_PAR_ATT = 2;
	public static int NOURRITURE_MAX = 4;
	public static int ENERGIE_PAR_BOUFFE = 4;
	public static int BOUFFE_CRITIQUE = DIST_PERCEPTION;
	
	public int vie;
	public int attaque;
	public int energie = MAX_ENERGIE;
	public int distancePerception = DIST_PERCEPTION;
	public int nourriture = 0;


	private Beings beings;
	
	

	public Contender(int x, int y, int attaque, int vie, int energie) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
	}
	
	public Contender(int x, int y) {
		super(x, y);
		Random rand = new Random();
		int attaque = rand.nextInt(MAX_ATTAQUE - MIN_ATTAQUE + 1) + MIN_ATTAQUE;
		int vie = rand.nextInt(MAX_VIE - MIN_VIE + 1) + MIN_VIE;
		this.vie = vie;
		this.attaque = attaque;
		this.energie = MAX_ENERGIE;
	}

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		if (vie <= 0)
			meurt(beings);
		else {
			System.out.println("\n\n Contender begins step : vie=" + vie +" ; energie =" + energie);
			/****************Traitement bouffe*****************/
			if(energie < BOUFFE_CRITIQUE){
				
			}
			
			
			/****************Traitement enemi*****************/
			Contender closestEnemy = getClosestEnemy();
			// TODO: Va vers lui si dist>1, castagne sinon (tape, l'autre
			// devrait nous taper aussi normalement)
			if (closestEnemy != null) {
				if (!isAtRange(closestEnemy)) {
					MoveTowards(closestEnemy.x, closestEnemy.y, 1);
					//si on emp�che le d�placement vers l'endroit o� on est, certains contestants ne bougent pas,
					//je sais pas pourquoi
				} else {
					System.out.println("ennemi � port�e, je le tape ou je fuis");
					if (closestEnemy.attaque * 2 > vie)
						escapeFrom(closestEnemy);
					else
						attack(closestEnemy);
				}
			} else {
				// pas d'ennemis trouv�, marche vers le centre
				MoveTowards(Beings.GRID_SIZE / 2, Beings.GRID_SIZE / 2, 1);
			}
		}
	}

	// trouve l'enn//emi le plus proche, retourne null si aucun n'est visible
	public Contender getClosestEnemy() {
		// teste toutes les distances pour trouver le plus proche
		// (v�rifier si getNeighbors classe pas d�j� par proximit�)
		for (int i = 1; i <= distancePerception; i++) {
			Contender closestEnemy = findEnemyAtRange(i);
			if (closestEnemy != null) {
				System.out.println("ClosestEnemy found, at "+ closestEnemy.x + " ; " + closestEnemy.y);
				return closestEnemy;
			}
		}
		System.out.println("No contender found within range " + distancePerception);
		return null;
	}

	// trouve un ennemi � une distance range
	public Contender findEnemyAtRange(int range) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier contender � une distance range
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

	// se d�place de dist vers la case (x2,y2)
	public void MoveTowards(int x2, int y2, int dist) {
		System.out.println("Currently at (" + x + "," + y + ")");
		System.out.println("moving towards (" + x2 + "," + y2 + ")");
		int i, dx, dy;
		for (i = 0 ; i < dist ; i++)
		{
			dx = x2 - x;
			dy = y2 - y;
			if (Math.abs(dx) > Math.abs(dy)) {
				if (dx > 0) {
					beings.yard.set(x, y, null);
					x++;
					beings.yard.set(x, y, this);
				} else if (dx < 0){
					beings.yard.set(x, y, null);
					x--;
					beings.yard.set(x, y, this);
				}
			} else {
				if (dy > 0) {
					beings.yard.set(x, y, null);
					y++;
					beings.yard.set(x, y, this);
				} else if (dy < 0){
					beings.yard.set(x, y, null);
					y--;
					beings.yard.set(x, y, this);
				}
			}
		}
		
		if (energie > 0)
			energie = energie - dist*ENERGIE_PAR_DEP;
		else
			vie --;
		System.out.println("Now at " + x + "," + y);
	}

	private boolean isAtRange(Contender cont) {
		int dx = cont.x - x;
		int dy = cont.y - y;
		return (Math.abs(dx) <= 1 && Math.abs(dy) <= 1);
	}

	public void attack(Contender cont) {
		cont.vie -= attaque;
		energie -= ENERGIE_PAR_ATT;
	}
	
	//se déplace dans la direction opposée à celle de l'enemi
	public void escapeFrom(Contender cont){
		int dirx, diry;
		int dx = cont.x - x;
		int dy = cont.y - y;
		dirx = (dx > 0)? x - MAX_DEP : x + MAX_DEP;
		if (dirx > Beings.GRID_SIZE)
			dirx = Beings.GRID_SIZE - 1;
		if (dirx < 0)
			dirx = 0;
		diry = (dy > 0)? y - MAX_DEP : y + MAX_DEP;
		if (diry > Beings.GRID_SIZE)
			diry = Beings.GRID_SIZE - 1;
		if(diry > 0)
			diry = 0;

		System.out.println("Now escaping from enemy at " + cont.x + " ; " + cont.y);
		MoveTowards(dirx, diry, MAX_DEP);
	}
	
	public void eat(){
		while(energie < MAX_ENERGIE - ENERGIE_PAR_BOUFFE && nourriture > 0)
		{
			nourriture --;
			energie += ENERGIE_PAR_BOUFFE;
		}
	}
	
	
	// trouve l'enn//emi le plus proche, retourne null si aucun n'est visible
		public Nourriture getClosestFood() {
			// teste toutes les distances pour trouver le plus proche
			// (v�rifier si getNeighbors classe pas d�j� par proximit�)
			for (int i = 1; i <= distancePerception; i++) {
				Nourriture closestFood = findFoodAtRange(i);
				if (closestFood != null) {
					System.out.println("ClosestEnemy found, at "+ closestFood.x + " ; " + closestFood.y);
					return closestFood;
				}
			}
			System.out.println("No contender found within range " + distancePerception);
			return null;
		}
	
		
	public Nourriture findFoodAtRange(int range){
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier contender � une distance range
		for (Object o : b) {
			if (o instanceof Nourriture) {
				Nourriture food = (Nourriture) o;

				System.out.println("Food found in range " + range);
				System.out.println("Its location : " + food.x + "," + food.y);
				return food;

			}
		}
		return null;
	}
	
	public void takeFood(Nourriture food){
		while(food.quantite >0 && nourriture < NOURRITURE_MAX){
			nourriture ++;
			food.quantite--;
		}
		energie --;
	}
	
}
