package ia04.model;

import java.util.Random;

import ia04.gui.BeingsWithUI;
import ia04.model.Map.Zone;
import sim.engine.SimState;
import sim.util.Bag;

public class Monstre extends Personnage {

	public static int DIST_PERCEPTION = 10;
	public static int MAX_DEP = 2;
	public static int ATTAQUE = 10;
	public static int VIE = 25;
	public static int MAX_ENERGIE = 50;
	
	public int ANTRE_X;
	public int ANTRE_Y;

	public enum Type {
		KRAKEN, TAUREAU
	};

	public Type t;
	public int maxDeplacement;
	private static final long serialVersionUID = -8909074924721698002L;

	public Monstre(int x, int y, int attaque, int vie, int energie, int distancePerception) {
		super(x, y, vie, attaque, energie, distancePerception);
		this.vie = vie;
		this.attaque = attaque;
		this.energie = energie;
		this.distancePerception = distancePerception;
	}

	private Beings beings;

	public Monstre(int x, int y, int vie, int attaque, int energie, int distancePerception, Type _t, Beings b) {
		super(x, y, vie, attaque, energie, distancePerception);
		beings = b;
		if (_t == Type.KRAKEN) {
			t = Type.KRAKEN;
			attaque = ATTAQUE;
			vie = VIE;
			distancePerception = DIST_PERCEPTION * 2;
			maxDeplacement = MAX_DEP;
		} else {
			t = Type.TAUREAU;
			attaque = ATTAQUE / 2;
			vie = (VIE * 3) / 5;
			distancePerception = DIST_PERCEPTION;
			maxDeplacement = MAX_DEP * 2;
		}
		this.vie = vie;
		this.attaque = attaque;
		this.energie = MAX_ENERGIE;
		this.distancePerception = distancePerception;
		this.ANTRE_X = x;
		this.ANTRE_Y = y;
	}

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		boolean roundDone = false;
		if (vie <= 0) {
			meurt(beings);
		} else {
			System.out.println("\n\n Monster begins step : vie=" + vie + " ; energie =" + energie);
			System.out.println("Currently at (" + x + "," + y + ")");

			if (beings.livingContenders > 1) {
				/**************** Traitement ennemi *****************/
				Personnage closestEnemy = getClosestEnemy(beings);
				if ((closestEnemy != null) && (closestEnemy.getClass() != Monstre.class)) {
					if (!isAtRange(closestEnemy, 1)) {
						MoveTowards(closestEnemy.x, closestEnemy.y, 1, beings, 0);
						System.out.println(t+" a trouv� une cible en "+closestEnemy.x+","+closestEnemy.y);
						roundDone = true;
					} else {
						System.out.println("ennemi a portee");
						// Un monstre n'attaque que les contenders
						if (closestEnemy.getClass() == Contender.class)
							attack(closestEnemy, 0);
						else{
							System.out.println(t+" retourne � son antre en "+ANTRE_X+","+ANTRE_Y);
							MoveTowards(ANTRE_X,ANTRE_Y, maxDeplacement, beings, 0);
						}

					}
				} else {
					System.out.println(t+" retourne � son antre en "+ANTRE_X+","+ANTRE_Y);
					MoveTowards(ANTRE_X, ANTRE_Y, maxDeplacement, beings, 0);
					roundDone = true;
				}
			}
		}
	}

	// retourne l'objet Map de la case
	public Map getMap(int x, int y) {
		Bag b = beings.yard.getObjectsAtLocation(x, y);
		for (Object o : b) {
			if (o instanceof Map) {
				Map m = (Map) o;
				return m;
			}
		}
		return null;
	}
}
