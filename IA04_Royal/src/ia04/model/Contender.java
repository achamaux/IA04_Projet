package ia04.model;

import sim.engine.SimState;

public class Contender extends MySteppable{
	
	public int vie;
	public int attaque;
	public int distancePerception=1;

	public Contender(int x, int y,int vie, int attaque,int distancePerception) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
		this.distancePerception = distancePerception;
	}

	@Override
	public void step(SimState arg0) {
		
	}

}
