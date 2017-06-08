package ia04.model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {

	private static final long serialVersionUID = 4736324642205240207L;

	public static int GRID_SIZE = 75;
	public static int NUM_FOOD_CELL = 80;
	public static int NUM_CONTENDERS = 50;
	public static int NUM_ARME = 20;
	public static int MIN_VIE = 10;
	public static int MAX_VIE = 20;
	public static int MIN_ATTAQUE = 1;
	public static int MAX_ATTAQUE = 5;	
	public static int NUM_INSECT = 500;

	public SparseGrid2D yard =
			new SparseGrid2D(2*GRID_SIZE,GRID_SIZE);

	public Beings(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		yard.clear();
		addAgentsContender();
		addAgentsNourriture();
		addAgentsArme();
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
		while ((ag = yard.getObjectsAtLocation(location.x, location.y)) != null) {
			location = new Int2D(random.nextInt(yard.getWidth()),
					random.nextInt(yard.getHeight()) );
		}
		addAgentNourriture(location.x, location.y);
	}

	public void addAgentNourriture(int x, int y) {
		Int2D location = new Int2D(x,y);
		Nourriture a = new Nourriture(location.x,location.y);
		yard.setObjectLocation(a, location.x, location.y);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}
	
	private void addAgentsArme() {
		for(int i = 0; i < NUM_ARME; i++) {
			addAgentArme();
		}
	}
	
	public void addAgentArme() {
		Int2D location = new Int2D(random.nextInt(yard.getWidth()),
				random.nextInt(yard.getHeight()) );
		Object ag = null;
		while ((ag = yard.getObjectsAtLocation(location.x, location.y)) != null) {
			location = new Int2D(random.nextInt(yard.getWidth()),
					random.nextInt(yard.getHeight()) );
		}
		Arme a = new Arme(location.x,location.y);
		addAgentArme(location.x, location.y, a);
	}

	public void addAgentArme(int x, int y, Arme a) {
		Int2D location = new Int2D(x,y);
		yard.setObjectLocation(a, location.x, location.y);
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
			while ((ag = yard.getObjectsAtLocation(location.x, location.y)) != null) {
				location = new Int2D(random.nextInt(yard.getWidth()),
						random.nextInt(yard.getHeight()) );
			}
			addAgentContender(location.x, location.y);
		}
	}
	
	public void addAgentContender(int x, int y) {
		
		Int2D location = new Int2D(x,y);
		System.out.println();
		Contender a = new Contender(location.x, location.y);
		yard.setObjectLocation(a, location.x, location.y);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}

	public boolean free(int x, int y) {
		if(x<0 || x>GRID_SIZE || y<0 || y>GRID_SIZE) return false;
		return yard.getObjectsAtLocation(x, y)==null;
	}
}