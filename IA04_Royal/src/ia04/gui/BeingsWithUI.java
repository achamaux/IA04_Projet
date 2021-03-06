package ia04.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ia04.model.Arme;
import ia04.model.Beings;
import ia04.model.Contender;
import ia04.model.Map;
import ia04.model.Map.Zone;
import ia04.model.Monstre;
import ia04.model.Monstre.Type;
import ia04.model.Nourriture;
import ia04.model.Piege;
import ia04.model.Soin;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class BeingsWithUI extends GUIState {
	public static int FRAME_SIZE = 900;
	public Display2D display;
	public static JFrame displayFrame;
	Color eauColor = new Color(66, 124, 202);// new Color(66, 123, 202);
	Color jungleColor = new Color(69, 99, 40);
	Color plaineColor = new Color(122, 166, 71);
	Color desertColor = new Color(246, 226, 155);
	Color antreColor = new Color(70, 70, 70);

	File fgrass;
	File fsand;
	File fwater;
	File fjungle;

	Image igrass;
	Image isand;
	Image iwater;
	Image ijungle;
	SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();

	public BeingsWithUI(SimState state) {
		super(state);
		fgrass = new File("res/icon/grass.png");
		fsand = new File("res/icon/sand.png");
		fwater = new File("res/icon/eau.png");
		fjungle = new File("res/icon/foret_.png");
		try {
			igrass = ImageIO.read(fgrass);
			isand = ImageIO.read(fsand);
			iwater = ImageIO.read(fwater);
			ijungle = ImageIO.read(fjungle);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void start() {
		super.start();
		setupPortrayals();
	}

	public Object getSimulationInspectedObject() {
		return state;
	}

	public Inspector getInspector() {
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}

	public void load(SimState state) {

	}

	public void setupPortrayals() {
		Beings beings = (Beings) state;
		yardPortrayal.setField(beings.yard);
		yardPortrayal.setPortrayalForClass(Map.class, getMapPortrayal());
		yardPortrayal.setPortrayalForClass(Nourriture.class, getNourriturePortrayal());
		yardPortrayal.setPortrayalForClass(Piege.class, getPiegePortrayal());
		yardPortrayal.setPortrayalForClass(Arme.class, getArmePortrayal());
		yardPortrayal.setPortrayalForClass(Contender.class, getContenderPortrayalLabelled());
		yardPortrayal.setPortrayalForClass(Monstre.class, getMonstrePortrayalLabelled());
		yardPortrayal.setPortrayalForClass(Soin.class, getSoinPortrayal());
		display.reset();
		Color backgroundCol = new Color(13, 115, 13);
		display.setBackdrop(backgroundCol);
		display.repaint();
	}

	public void init(Controller c) {
		super.init(c);
		display = new Display2D(2 * FRAME_SIZE, FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Beings");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "Yard");
	}

	private RectanglePortrayal2D getMapPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D() {

			private static final long serialVersionUID = -2531930723151537502L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info) {
				Map i = (Map) o;
				switch (i.z) {
				case EAU:
					this.paint = eauColor;
					break;
				case JUNGLE:
					this.paint = jungleColor;
					break;
				case PLAINE:
					this.paint = plaineColor;
					break;
				case DESERT:
					this.paint = desertColor;
					break;
				case ANTRE:
					this.paint = antreColor;
					break;
				}

				super.draw(o, g, info);
			}
		};
		// r.paint=Color.PINK;
		r.filled = true;
		return r;
	}

	private ImagePortrayal2D getNourriturePortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/hamburger.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}

	private ImagePortrayal2D getMapImagePortrayal() {
		Image image = null;
		ImagePortrayal2D r = new ImagePortrayal2D(image) {
			private static final long serialVersionUID = -9018920390744116027L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info) {
				Map i = (Map) o;
				switch(i.z){
				case PLAINE:
					image = igrass;
					break;
				case DESERT:
					image = isand;
					break;
				case EAU:
					image = iwater;
					break;
				case JUNGLE:
					image = ijungle;
					break;
				}
				super.draw(o, g, info);
			}

		};
		return r;
	}

	private ImagePortrayal2D getPiegePortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/piege.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}

	private ImagePortrayal2D getArmePortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/glaive.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}

	private ImagePortrayal2D getMonstrePortrayal() {
		Image image = null;
		ImagePortrayal2D r = new ImagePortrayal2D(image) {
			private static final long serialVersionUID = -9018920390744116027L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info) {
				Monstre m = (Monstre) o;
				File f1 = new File("res/icon/kraken.png");
				File f2 = new File("res/icon/taureau.png");
				if (m.t == Type.KRAKEN)
					try {
						image = ImageIO.read(f1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (m.t == Type.TAUREAU)
					try {
						image = ImageIO.read(f2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				super.draw(o, g, info);
			}

		};
		return r;
	}

	private LabelledPortrayal2D getMonstrePortrayalLabelled() {
		ImagePortrayal2D child = getMonstrePortrayal();
		String l = "initialisation";
		LabelledPortrayal2D r = new LabelledPortrayal2D(child, l) {

			private static final long serialVersionUID = -5155353325577168909L;

			@Override
			public String getLabel(Object o, DrawInfo2D info) {
				Monstre i = (Monstre) o;
				String label = "( V = " + i.vie + " A= " + i.attaque + " )";
				return label;
			}
		};
		return r;

	}

	private OvalPortrayal2D getContenderPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(1.2) {

			private static final long serialVersionUID = -9018920390744116027L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info) {
				Contender i = (Contender) o;
				if (i.vie >= 10)
					this.paint = Color.GREEN;
				else if (i.vie >= 3)
					this.paint = Color.ORANGE;
				else
					this.paint = Color.RED;
				super.draw(o, g, info);
			}

		};

		r.paint = Color.YELLOW;
		r.filled = true;
		return r;
	}

	private LabelledPortrayal2D getContenderPortrayalLabelled() {
		OvalPortrayal2D child = getContenderPortrayal();
		String l = "initialisation";
		LabelledPortrayal2D r = new LabelledPortrayal2D(child, l) {

			private static final long serialVersionUID = -5155353521577168909L;

			@Override
			public String getLabel(Object o, DrawInfo2D info) {
				Contender i = (Contender) o;
				String label = "(  E = " + i.energie + " V = " + i.vie + " A= " + i.attaque + " )";
				return label;
			}
		};
		return r;

	}

	private ImagePortrayal2D getSoinPortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/heal.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}

}
