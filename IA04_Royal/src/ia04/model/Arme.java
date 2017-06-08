package ia04.model;

import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public class Arme extends MySteppable { 

	
	static public int MAX_PUISSANCE=1;
	static public int MIN_PUISSANCE=5;
	
	public int puissance;
	
	public Arme(int x, int y) {
		super(x,y);
		Random rand = new Random();
		//puissance = rand.nextInt(MAX_PUISSANCE - MIN_PUISSANCE + 1) + MIN_PUISSANCE;
	}


	public int getpower() {
		return puissance;
	}

	@Override
	public void step(SimState state) {
	}
	
	@Override
	public void meurt(Beings state) {
	}

}