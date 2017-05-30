package ia04.model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	public static int GRID_SIZE = 100;
	public static int NUM_INSECT = 500;
	public static int NUM_FOOD_CELL = 500;
	public static int NUM_CONTENDERS = 15;
	public static int MIN_VIE = 10;
	public static int MAX_VIE = 20;
	public static int MIN_ATTAQUE = 1;
	public static int MAX_ATTAQUE = 5;	
	public ObjectGrid2D yard =
			new ObjectGrid2D(GRID_SIZE,GRID_SIZE);

	public Beings(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		yard.clear();
		addAgentsContender();
	}

	private void addAgentsNourriture() {
		for(int i = 0; i < NUM_FOOD_CELL; i++) {
			addAgentNourriture();
		}
	}
	
	public void addAgentNourriture() {
		Int2D location = new Int2D(random.nextInt(yard.getWidth()),
				random.nextInt(yard.getHeight()) );
		Object ag = null;
		while ((ag = yard.get(location.x,location.y)) != null) {
			location = new Int2D(random.nextInt(yard.getWidth()),
					random.nextInt(yard.getHeight()) );
		}
		addAgentNourriture(location.x, location.y);
	}

	public void addAgentNourriture(int x, int y) {
		Int2D location = new Int2D(x,y);
		Nourriture a = new Nourriture(location.x,location.y);
		yard.set(location.x,location.y,a);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}
	

	private void addAgentsInsecte() {
		for(int i = 0; i < NUM_INSECT; i++) {
			Int2D location = new Int2D(random.nextInt(yard.getWidth()),
					random.nextInt(yard.getHeight()) );
			Object ag = null;
			while ((ag = yard.get(location.x,location.y)) != null) {
				location = new Int2D(random.nextInt(yard.getWidth()),
						random.nextInt(yard.getHeight()) );
			}
			addAgentInsecte(location.x, location.y);
		}
	}
	
	public void addAgentInsecte(int x, int y) {
		Int2D location = new Int2D(x,y);
		Insecte a = new Insecte(location.x, location.y);
		yard.set(location.x,location.y,a);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}
	
	private void addAgentsContender() {
		for(int i = 0; i < NUM_CONTENDERS; i++) {
			Int2D location = new Int2D(random.nextInt(yard.getWidth()),
					random.nextInt(yard.getHeight()) );
			Object ag = null;
			while ((ag = yard.get(location.x,location.y)) != null) {
				location = new Int2D(random.nextInt(yard.getWidth()),
						random.nextInt(yard.getHeight()) );
			}
			addAgentContender(location.x, location.y);
		}
	}
	
	public void addAgentContender(int x, int y) {
		int attaque = 2;
		int vie = 10;
		//TODO: set attaque et vie différemment
		
		Int2D location = new Int2D(x,y);
		Contender a = new Contender(location.x, location.y, vie, attaque);
		yard.set(location.x,location.y,a);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}

	public boolean free(int x, int y) {
		if(x<0 || x>GRID_SIZE || y<0 || y>GRID_SIZE) return false;
		return yard.get(x, y)==null;
	}
}