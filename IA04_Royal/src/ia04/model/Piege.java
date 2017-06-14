package ia04.model;

import java.util.Random;

import sim.engine.SimState;

public class Piege extends MySteppable { 

	private static final long serialVersionUID = -3233964416056133588L;
	
	static public int MAX_DEGAT=10;
	static public int MIN_DEGAT=5;
	
	public int degat;

	public Piege(int x, int y) {
		super(x, y);
		Random rand = new Random();
		degat = rand.nextInt(MAX_DEGAT - MIN_DEGAT + 1) + MIN_DEGAT;
	}
	
	@Override
	public void step(SimState state) {
		//
	}
	
	@Override
	public void meurt(Beings state) {
		super.meurt(state);
	}

}