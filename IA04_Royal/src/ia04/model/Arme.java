package ia04.model;

import java.util.Random;

import sim.engine.SimState;

public class Arme extends Element { 

	
	private static final long serialVersionUID = 7180337908965110967L;
	
	static public int MAX_PUISSANCE=5;
	static public int MIN_PUISSANCE=1;
	
	public int puissance;
	
	public Arme(int x, int y) {
		super(x,y);
		Random rand = new Random();
		puissance = rand.nextInt(MAX_PUISSANCE - MIN_PUISSANCE + 1) + MIN_PUISSANCE;
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