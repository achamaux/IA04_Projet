package ia04.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import ia04.model.Map.Zone;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class Beings extends SimState {

	private static final long serialVersionUID = 4736324642205240207L;

	public static int GRID_SIZE = 75;
	public static int NUM_FOOD_CELL = 80;
	public static int NUM_CONTENDERS = 50;
	public static int NUM_HEAL = 50;
	public static int NUM_ARME = 20;
	public static int MIN_VIE = 10;
	public static int MAX_VIE = 20;
	public static int MIN_ATTAQUE = 1;
	public static int MAX_ATTAQUE = 5;	
	public static int NUM_INSECT = 500;
	
	static public int[][] mapMat = null;
	private String mapFile = "res/map/map1"; 

	public SparseGrid2D yard =
			new SparseGrid2D(2*GRID_SIZE,GRID_SIZE);

	public Beings(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		yard.clear();
		initMat();
		addAgentsMap();
		addAgentsContender();
		addAgentsNourriture();
		addAgentsArme();
		addAgentsHeal();
	}
	private void initMat()
	{
		// Nous déclarons nos objets en dehors du bloc try/catch
	    FileInputStream fis = null;
	    
		mapMat = new int[Beings.GRID_SIZE][Beings.GRID_SIZE* 2];
		int ii = 0;
		int jj = 0;
		try{
			Scanner sc = new Scanner(new File(mapFile));
			for (int i = 0; i < Beings.GRID_SIZE; i++){
              for (int j = 0; j < 2*Beings.GRID_SIZE; j++){
            	  ii = i;
            	  jj = j;
              	mapMat[i][j] = sc.nextInt();
              }
              //System.out.println("jj : "+jj);
          }
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("erreur fichier"+ii+" "+jj);
		}  

	}
	
	private void displayMat(){
		try{
			for (int i = 0; i < Beings.GRID_SIZE; i++){
	            for (int j = 0; j < 2*Beings.GRID_SIZE; j++){
	            	System.out.print(mapMat[i][j] + " ");
	            }
	            System.out.print('\n');
	        }
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("problem display mat");
		}
		
	}
	private void addAgentsMap() {
		for(int i = 0; i < 2*GRID_SIZE; i++)
		{
			for (int j = 0; j < GRID_SIZE; j++)
			{
				addAgentMap(i,j);
			}
		}
	}
	
	private void addAgentMap(int x, int y) {
		Int2D location = new Int2D(x,y);
		Map m = new Map(location.x,location.y);
		yard.setObjectLocation(m, location.x, location.y);
		m.x = location.x;
		m.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(m);
		m.stoppable=stoppable;
	}

	private void addAgentsHeal() {
		for(int i = 0; i < NUM_HEAL; i++)
		{
			addAgentHeal();
		}
	}

	private void addAgentHeal() {
		Map m = getNewLocation();
		while (((yard.numObjectsAtLocation(m.x, m.y)) > 1) || (m.z == Zone.EAU)) {
			m = getNewLocation();
		}
		addAgentHeal(m.x, m.y);
	}
	
	private Map getNewLocation() {
		Int2D location = new Int2D(random.nextInt(yard.getWidth()),
				random.nextInt(yard.getHeight()) );
		Bag b = yard.getObjectsAtLocation(location.x, location.y);
		for (Object o : b) {
			if (o instanceof Map) {
				Map m = (Map) o;
				return m;
				}
			}
		return null;
	}

	private void addAgentHeal(int x, int y) {
		Int2D location = new Int2D(x,y);
		Soin a = new Soin(location.x,location.y);
		yard.setObjectLocation(a, location.x, location.y);
		a.x = location.x;
		a.y = location.y;
		
		Stoppable stoppable=schedule.scheduleRepeating(a);
		a.stoppable=stoppable;
	}

	private void addAgentsNourriture() {
		for(int i = 0; i < NUM_FOOD_CELL; i++) {
			addAgentNourriture();
		}
	}
	
	public void addAgentNourriture() {
		Map m = getNewLocation();
		while (((yard.numObjectsAtLocation(m.x, m.y)) > 1) || (m.z == Zone.EAU)) {
			m = getNewLocation();
		}
		addAgentNourriture(m.x, m.y);
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
		Map m = getNewLocation();
		while (((yard.numObjectsAtLocation(m.x, m.y)) > 1) || (m.z == Zone.EAU)) {
			m = getNewLocation();
		}
		Arme a = new Arme(m.x,m.y);
		addAgentArme(m.x, m.y, a);
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
			Map m = getNewLocation();
			while (((yard.numObjectsAtLocation(m.x, m.y)) > 1) || (m.z == Zone.EAU)) {
				m = getNewLocation();
			}
			addAgentContender(m.x, m.y);
		}
	}
	
	public void addAgentContender(int x, int y) {
		
		Int2D location = new Int2D(x,y);
		System.out.println();
		Contender a = new Contender(location.x, location.y, this);
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