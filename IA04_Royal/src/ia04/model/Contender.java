package ia04.model;

import sim.engine.SimState;

public class Contender extends MySteppable{
	
	public int vie;
	public int attaque=1;
	public int distancePerception=5;

	public Contender(int x, int y,int vie, int attaque) {
		super(x, y);
		this.vie = vie;
		this.attaque = attaque;
	}

	@Override
	public void step(SimState arg0) {
		
	}

}
