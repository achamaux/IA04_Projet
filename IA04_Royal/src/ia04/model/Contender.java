package ia04.model;

import java.util.Random;

import sim.engine.SimState;
import sim.util.Bag;

public class Contender extends MySteppable {

	private static final long serialVersionUID = 6893667159868881758L;
	
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
	public static int VIE_CRITIQUE = 10;
	
	public int vie;
	public int attaque;
	public int energie = MAX_ENERGIE;
	public int distancePerception = DIST_PERCEPTION;
	public int nourriture = 0;
	public Arme arme;

	private Beings beings;

	public Contender(int x, int y, int attaque, int vie, int energie) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
	}

	public Contender(int x, int y, Beings b ) {
		super(x, y);
		beings = b;
		Random rand = new Random();
		int attaque = rand.nextInt(MAX_ATTAQUE - MIN_ATTAQUE + 1) + MIN_ATTAQUE;
		int vie = rand.nextInt(MAX_VIE - MIN_VIE + 1) + MIN_VIE;
		this.vie = vie;
		this.attaque = attaque;
		this.energie = MAX_ENERGIE;
		arme = null;
		Arme a = new Arme(x, y);
		takeWeapon(a);
	}

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		boolean roundDone = false;
		if (vie <= 0){
			meurt(beings);
		}
		else {
			System.out.println("\n\n Contender begins step : vie=" + vie +" ; energie =" + energie);
			System.out.println("Currently at (" + x + "," + y + ")");
			/****************Recherche soin prioritaire*****************/
			if(vie < VIE_CRITIQUE)
			{
				Soin closestSoin = getClosestSoin();
				if(closestSoin != null)
				{
					if((isAtRange(closestSoin,0) || isAtRange(closestSoin, 1)) && closestSoin.quantite > 0){
						seSoigner(closestSoin);
						roundDone = true;
					}
					else
					{
						MoveTowards(closestSoin.x, closestSoin.y, MAX_DEP);
						roundDone = true;
					}
				}

			}
			/****************Traitement bouffe*****************/
			if(energie < BOUFFE_CRITIQUE && !roundDone){
				System.out.println("Energy running low");
				if(nourriture > 0){
					eat();
					roundDone = true;
				}
				else 
				{
					Nourriture food = getClosestFood();
					if(food != null){
						if((isAtRange(food,0) || isAtRange(food, 1)) && food.quantite > 0){
							takeFood(food);
							roundDone = true;
						}
						else
						{
							MoveTowards(food.x, food.y, MAX_DEP);
							roundDone = true;
						}
					}
				}
			}
			if(!roundDone)
			{
				/****************Traitement enemi*****************/
				Contender closestEnemy = getClosestEnemy();
				if (closestEnemy != null) {
					if (!isAtRange(closestEnemy, 1)) {
						MoveTowards(closestEnemy.x, closestEnemy.y, 1);
						roundDone = true;
					} else {
						System.out.println("ennemi � port�e, je le tape ou je fuis");
						if (closestEnemy.attaque * 2 > vie)
							escapeFrom(closestEnemy);
						else
							attack(closestEnemy);
					}
				} else {
					// pas d'ennemis trouv�, marche vers là où il y a des armes mieux 
					//et si non, vers le centre
					Arme a = getClosestWeapon();
					if (a != null){
						if (a.x == x && a.y == y){
							roundDone = takeWeapon(a);
							if (!roundDone){
								MoveTowards(Beings.GRID_SIZE / 2, Beings.GRID_SIZE / 2, 1);
								roundDone = true;
							}
							else{
								MoveTowards(a.x, a.y, MAX_DEP);
								roundDone = true;
							}
						}
						else{
							MoveTowards(Beings.GRID_SIZE / 2, Beings.GRID_SIZE / 2, 1);
							roundDone = true;
						}
					}
					else{
						MoveTowards(Beings.GRID_SIZE / 2, Beings.GRID_SIZE / 2, 1);
						roundDone = true;
					}
				}
			}
		}
	}

	private void seSoigner(Soin soin) {
		while (soin.quantite > 0 && vie < MAX_VIE) {
			vie++;
			soin.quantite--;
			System.out.println("I'm getting better !");
		}
		energie--;
	}

	private Soin getClosestSoin() {
		// teste toutes les distances pour trouver le plus proche
		for (int i = 1; i <= distancePerception; i++) {
			Soin closestSoin = findHealAtRange(i);
			if (closestSoin != null) {
				System.out.println("Heal found, at " + closestSoin.x + " ; " + closestSoin.y);
				return closestSoin;
			}
		}
		System.out.println("No heal found within range " + distancePerception);
		return null;
	}

	private Soin findHealAtRange(int range) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier soin � une distance range
		for (Object o : b) {
			if (o instanceof Soin) {
				Soin soin = (Soin) o;
				return soin;
			}
		}
		return null;
	}
	
	// trouve l'ennemi le plus proche, retourne null si aucun n'est visible
	public Contender getClosestEnemy() {
		// teste toutes les distances pour trouver le plus proche
		// (v�rifier si getNeighbors classe pas d�j� par proximit�)
		for (int i = 1; i <= distancePerception; i++) {
			Contender closestEnemy = findEnemyAtRange(i);
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
			energie = energie - dist * ENERGIE_PAR_DEP;
		else
			vie--;
		
		System.out.println("Now at " + x + "," + y);
	}

	private boolean isAtRange(MySteppable cont, int range) {
		int dx = cont.x - x;
		int dy = cont.y - y;
		return (Math.abs(dx) <= range && Math.abs(dy) <= range);
	}

	public void attack(Contender cont) {
		cont.vie -= attaque;
		energie -= ENERGIE_PAR_ATT;
	}

	// se déplace dans la direction opposée à celle de l'enemi
	public void escapeFrom(Contender cont) {
		int dirx, diry;
		int dx = cont.x - x;
		int dy = cont.y - y;
		dirx = (dx > 0) ? x - MAX_DEP : x + MAX_DEP;
		if (dirx > Beings.GRID_SIZE)
			dirx = Beings.GRID_SIZE - 1;
		if (dirx < 0)
			dirx = 0;
		diry = (dy > 0) ? y - MAX_DEP : y + MAX_DEP;
		if (diry > Beings.GRID_SIZE)
			diry = Beings.GRID_SIZE - 1;
		if (diry < 0)
			diry = 0;

		System.out.println("Now escaping from enemy at " + cont.x + " ; " + cont.y);
		MoveTowards(dirx, diry, MAX_DEP);
	}

	public void eat() {
		while (energie < MAX_ENERGIE - ENERGIE_PAR_BOUFFE && nourriture > 0) {
			nourriture--;
			energie += ENERGIE_PAR_BOUFFE;
			System.out.println("Food eaten ! Food left :" + nourriture + " ; enery = " + energie);
		}
	}

	// trouve l'ennemi le plus proche, retourne null si aucun n'est visible
	public Nourriture getClosestFood() {
		// teste toutes les distances pour trouver le plus proche
		// (v�rifier si getNeighbors classe pas d�j� par proximit�)
		for (int i = 1; i <= distancePerception; i++) {
			Nourriture closestFood = findFoodAtRange(i);
			if (closestFood != null) {
				System.out.println("ClosestFood found, at " + closestFood.x + " ; " + closestFood.y);
				return closestFood;
			}
		}
		System.out.println("No food found within range " + distancePerception);
		return null;
	}

	@SuppressWarnings("deprecation")
	public Nourriture findFoodAtRange(int range) {
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

	public void takeFood(Nourriture food) {
		while (food.quantite > 0 && nourriture < NOURRITURE_MAX) {
			nourriture++;
			food.quantite--;
			System.out.println("Food taken ! Food left :" + nourriture + " ; energy = " + energie);
		}
		energie--;
	}

	public boolean takeWeapon(Arme weapon){
		//Si weapon est meilleur que l'arme actuelle OU qu'on n'a pas d'arme actuelle
		if((arme != null && arme.getpower() < weapon.getpower()) || (arme == null)){
			beings.yard.remove(weapon);
			if(arme!=null) //on repose l'arme qu'on avait
			{
				attaque -= arme.getpower();
				beings.addAgentArme(x, y, arme);
				System.out.println("Weapon changed!");
			}
			arme = weapon;
			attaque += arme.getpower();
			energie --;
			return true;
		}
		return false;
	}
	
	public Arme getClosestWeapon() {
		// teste toutes les distances pour trouver le plus proche
		// (v�rifier si getNeighbors classe pas d�j� par proximit�)
		for (int i = 1; i <= distancePerception; i++) {
			Arme closestWeapon = findWeaponAtRange(i);
			if (closestWeapon != null) {
				System.out.println("ClosestWeapon found, at " + closestWeapon.x + " ; " + closestWeapon.y);
				return closestWeapon;
			}
		}
		System.out.println("No weapon found within range " + distancePerception);
		return null;
	}


	// trouve un ennemi � une distance range
	@SuppressWarnings("deprecation")
	public Arme findWeaponAtRange(int range) {
		Bag b = beings.yard.getNeighborsMaxDistance(x, y, range, true, null, null, null);
		// retourne le premier armeender � une distance range
		for (Object o : b) {
			if (o instanceof Arme) {
				Arme arme = (Arme) o;
				System.out.println("weapon found in range " + range);
				System.out.println("its location : " + arme.x + "," + arme.y + "and power :" + arme.getpower());
				return arme;
			}
		}
		return null;
	}

}
