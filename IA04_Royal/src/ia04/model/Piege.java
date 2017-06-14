package ia04.model;

import sim.engine.SimState;

public class Piege extends MySteppable { 

	private static final long serialVersionUID = -3233964416056133588L;
	
	
	public Piege(int x, int y) {
		super(x, y);
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