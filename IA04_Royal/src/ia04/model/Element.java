package ia04.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public abstract class Element implements Steppable {

	private static final long serialVersionUID = -8776806986381983043L;
	
	public int x;
	public int y;
	public Stoppable stoppable;
	
	@Override
	public abstract void step(SimState arg0);
	
	protected void meurt(Beings beings) {
		beings.yard.remove(this);
		stoppable.stop();
	}

	public Element(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return this.getClass().getName();
	}
}
