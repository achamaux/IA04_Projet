package ia04.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class DataTracker implements Steppable{

	private Beings beings;
	public Stoppable stoppable;
	
	public DataTracker(Beings beings) {
		super();
		this.beings = beings;
	}

	@Override
	public void step(SimState arg0) {
		beings.averageLifeOfContender = numberOfPV()/ (float) beings.livingContenders;
		beings.averageAttackOfContender = numberOfAttack()/ (float) beings.livingContenders;
		beings.averageEnergyOfContender = numberOfEnergy()/ (float) beings.livingContenders;
		beings.averagePerceptionOfContender = numberOfPerception()/ (float) beings.livingContenders;
		beings.averageFoodOfContender = numberOfFood()/ (float) beings.livingContenders;
		beings.averagePersMaxVie = numberOfPersMaxVie()/ (float) beings.livingContenders;
		beings.averagePersNativeAttaque = numberOfPersNativeAttaque()/ (float) beings.livingContenders;
		beings.averagePersMaxEnergie = numberOfPersMaxEnergie()/ (float) beings.livingContenders;
	}
	
	public int numberOfPV(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.vie;
			}
		}
		return n;
	}
	
	public int numberOfAttack(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.attaque;
			}
		}
		return n;
	}
	
	public int numberOfEnergy(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.energie;
			}
		}
		return n;
	}
	
	public int numberOfPerception(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.distancePerception;
			}
		}
		return n;
	}
	
	public int numberOfFood(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.nourriture;
			}
		}
		return n;
	}
	
	public int numberOfPersMaxVie(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.personalMaxVie;
			}
		}
		return n;
	}
	
	public int numberOfPersNativeAttaque(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.personalNativeAttaque;
			}
		}
		return n;
	}
	
	public int numberOfPersMaxEnergie(){
		Bag b = beings.yard.getAllObjects();
		int n = 0;
		for (Object o : b) {
			if (o instanceof Contender) {
				Contender c = (Contender) o;
				n+= c.personalMaxEnergie;
			}
		}
		return n;
	}

}
