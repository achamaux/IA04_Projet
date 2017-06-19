package ia04.model;

import java.util.Random;

import ia04.model.Map.Zone;
import sim.engine.SimState;
import sim.util.Bag;

public class Contender extends Personnage {

	private static final long serialVersionUID = 6893667159868881758L;
	
	public static int DIST_PERCEPTION = 5;
	public static int DIST_PERCEPTION_JUNGLE = 2;
	public static int MAX_DEP = 4;
	public static int MAX_ENERGIE = 20;
	public static int MIN_VIE = 10;
	public static int MAX_VIE = 20;
	public static int MIN_ATTAQUE = 1;
	public static int MAX_ATTAQUE = 5;
	public static int ENERGIE_PAR_DEP = 1;
	public static int ENERGIE_PAR_DEP_DESERT = 2*ENERGIE_PAR_DEP;
	public static int ENERGIE_PAR_ATT = 2;
	public static int NOURRITURE_MAX = 4;
	public static int ENERGIE_PAR_BOUFFE = 4;
	public static int BOUFFE_CRITIQUE = DIST_PERCEPTION;
	public static int VIE_CRITIQUE = 10;
	
	public int energieDeplacement = ENERGIE_PAR_DEP;
	public int nourriture = 0;
	public Arme arme;

	private Beings beings;

	public Contender(int x, int y, int attaque, int vie, int energie, int distancePerception) {
		super(x, y, vie, attaque, energie, distancePerception);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
		this.distancePerception = distancePerception;
	}

	public Contender(int x, int y, int vie, int attaque, int energie,int distancePerception, Beings b ) {
		super(x, y, vie, attaque, energie, distancePerception);
		beings = b;
		Random rand = new Random();
		attaque = rand.nextInt(MAX_ATTAQUE - MIN_ATTAQUE + 1) + MIN_ATTAQUE;
		vie = rand.nextInt(MAX_VIE - MIN_VIE + 1) + MIN_VIE;
		this.vie = vie;
		this.attaque = attaque;
		this.energie = MAX_ENERGIE;
		this.distancePerception = DIST_PERCEPTION;
		arme = null;
		Arme a = new Arme(x, y);
		takeWeapon(a);
	}

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		boolean roundDone = false;
		Map currentMap = getMap(x,y);
		if (vie <= 0 || currentMap.z.equals(Zone.EAU)){
			meurt(beings);
		}
		else {
			System.out.println("\n\n Contender begins step : vie=" + vie +" ; energie =" + energie);
			System.out.println("Currently at (" + x + "," + y + ")");
			
			/****************Je vérifie si pas tombé dans un piege*****************/
			isTrapped(x,y);
			
			/****************Récupération des infos de la map*****************/
			
			getEffectFromMap(currentMap);
			
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
						MoveTowards(closestSoin.x, closestSoin.y, MAX_DEP, beings, energieDeplacement);
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
							MoveTowards(food.x, food.y, MAX_DEP, beings, energieDeplacement);
							roundDone = true;
						}
					}
				}
			}
			if(!roundDone)
			{
				/****************Traitement ennemi*****************/
				Personnage closestEnemy = getClosestEnemy(beings);
				if (closestEnemy != null) {
					if (!isAtRange(closestEnemy, 1)) {
						MoveTowards(closestEnemy.x, closestEnemy.y, 1, beings, energieDeplacement);
						roundDone = true;
					} else {
						System.out.println("ennemi � port�e, je le tape ou je fuis");
						if (closestEnemy.attaque * 2 > vie)
							escapeFrom(closestEnemy);
						else
							attack(closestEnemy, ENERGIE_PAR_ATT);
					}
				} else {
					// pas d'ennemis trouv�, marche vers là où il y a des armes mieux 
					//et si non, vers le centre
					Arme a = getClosestWeapon();
					if (a != null){
						if (a.x == x && a.y == y){
							roundDone = takeWeapon(a);
							if (!roundDone){
								MoveTowards(Beings.GRID_SIZE, Beings.GRID_SIZE / 2, 1, beings, energieDeplacement);
								roundDone = true;
							}
							else{
								MoveTowards(a.x, a.y, MAX_DEP, beings, energieDeplacement);
								roundDone = true;
							}
						}
						else{
							MoveTowards(Beings.GRID_SIZE, Beings.GRID_SIZE / 2, 1, beings, energieDeplacement);
							roundDone = true;
						}
					}
					else{
						MoveTowards(Beings.GRID_SIZE, Beings.GRID_SIZE / 2, 1, beings, energieDeplacement);
						roundDone = true;
					}
				}
			}
		}
	}

	private void isTrapped(int x, int y) {
		// Je vérifie si un piège est présent sur ma position
		Bag b = beings.yard.getObjectsAtLocation(x, y);
		for (Object o : b) {
			if (o instanceof Piege) {
				Piege p = (Piege) o;
				getTrapped(p);
			}
		}
		
	}

	private void getTrapped(Piege p) {
		// Je perds de la vie selon les dégats du piege
		vie-=p.degat;
		if (vie < 0) vie = 0;
		p.meurt(beings);
	}

	private void seSoigner(Soin soin) {
		while (soin.quantite > 0 && vie < MAX_VIE) {
			vie++;
			soin.quantite--;
			System.out.println("I'm getting better !");
		}
		energie--;
		if (energie < 0) energie = 0;
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


	// se déplace dans la direction opposée à celle de l'enemi
	public void escapeFrom(Personnage closestEnemy) {
		int dirx, diry;
		int dx = closestEnemy.x - x;
		int dy = closestEnemy.y - y;
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

		System.out.println("Now escaping from enemy at " + closestEnemy.x + " ; " + closestEnemy.y);
		MoveTowards(dirx, diry, MAX_DEP, beings, energieDeplacement);
	}

	public void eat() {
		while (energie < MAX_ENERGIE - ENERGIE_PAR_BOUFFE && nourriture > 0) {
			nourriture--;
			energie += ENERGIE_PAR_BOUFFE;
			System.out.println("Food eaten ! Food left :" + nourriture + " ; enery = " + energie);
		}
	}

	// retourne l'objet Map de la case
	public Map getMap(int x, int y) {
		Bag b = beings.yard.getObjectsAtLocation(x, y);
		for (Object o : b) {
			if (o instanceof Map) {
				Map m = (Map) o;
				return m;
			}
		}
		return null;
	}

	// affecte le contender 
	void getEffectFromMap(Map m){
		//Récupération de la map
		Zone z = m.z;
		if(z.equals(Zone.EAU)){
			meurt(beings);
			System.out.println("Je meurs car je suis dans l'eau, glou glou");}
		else if (z.equals(Zone.JUNGLE))
			distancePerception = DIST_PERCEPTION_JUNGLE;
		else if (z.equals(Zone.DESERT))
			energieDeplacement = ENERGIE_PAR_DEP_DESERT;
		else if (z.equals(Zone.PLAINE))
			energieDeplacement = ENERGIE_PAR_DEP;		
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
		if (energie < 0) energie = 0;
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
			if (energie < 0) energie = 0;
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
