package ia04.model;

import sim.engine.SimState;

public class Map extends MySteppable { 

	private static final long serialVersionUID = -3233964416056145588L;
	
	public enum Zone {EAU, JUNGLE, DESERT};
	public Zone z;

	public Map(int x, int y) {
		super(x,y);
		z = Zone.DESERT;
		if ((x<20) && (y <20)){
			z = Zone.EAU;
		}
	}

	@Override
	public void step(SimState state) {
		//
	}
}
