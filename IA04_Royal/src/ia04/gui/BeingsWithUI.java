package ia04.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ia04.model.Arme;
import ia04.model.Beings;
import ia04.model.Insecte;
import ia04.model.Map;
import ia04.model.Map.Zone;
import ia04.model.Nourriture;
import ia04.model.Soin;
import ia04.model.Contender;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.Portrayal;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class BeingsWithUI extends GUIState {
	public static int FRAME_SIZE = 900;
	public Display2D display;
	public JFrame displayFrame;
	Color eauColor = new Color(66, 123, 202);
	Color jungleColor = new Color(69, 99, 40);
	Color plaineColor = new Color(122,166,71);
	Color desertColor = new Color(246, 226, 155);
	SparseGridPortrayal2D yardPortrayal =
			new SparseGridPortrayal2D();
	public BeingsWithUI(SimState state) {
		super(state);
	}
	public void start() {
		super.start(); setupPortrayals();
	}
	public void load(SimState state) {

	}
	public void setupPortrayals() {
		Beings beings = (Beings) state;
		yardPortrayal.setField(beings.yard );
		yardPortrayal.setPortrayalForClass(Map.class, getMapPortrayal());
		yardPortrayal.setPortrayalForClass(Nourriture.class, getNourriturePortrayal());
		yardPortrayal.setPortrayalForClass(Arme.class, getArmePortrayal());
		//yardPortrayal.setPortrayalForClass(Contender.class, getContenderPortrayal());
		yardPortrayal.setPortrayalForClass(Contender.class, getContenderPortrayalLabelled());
		yardPortrayal.setPortrayalForClass(Soin.class, getSoinPortrayal());
		display.reset();
		Color backgroundCol = new Color(13, 115, 13);
		display.setBackdrop(backgroundCol);
		display.repaint();
	}

	public void init(Controller c) {
		super.init(c);
		display = new Display2D(2*FRAME_SIZE,FRAME_SIZE,this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Beings");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach( yardPortrayal, "Yard" );
	}

	/*private OvalPortrayal2D getInsectePortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(1.2){

			private static final long serialVersionUID = -9018920390744116027L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info){
				Insecte i = (Insecte)o;
				if(i.distancePerception>=0.6*Insecte.NBRE_POINTS)
					this.paint=Color.RED;
				else if(i.chargeMax>=0.6*Insecte.NBRE_POINTS)
					this.paint=Color.BLUE;
				else if(i.distanceDeplacement>=0.6*Insecte.NBRE_POINTS)
					this.paint=Color.BLACK;
				else if(i.distancePerception==0.5*Insecte.NBRE_POINTS)
					this.paint=Color.white;
				else this.paint=Color.MAGENTA;
				super.draw(o,g,info);
			}
		};
		
		r.paint = Color.RED;
		r.filled = true;
		return r;
	}*/


	private RectanglePortrayal2D getMapPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D(){
			
			

			private static final long serialVersionUID = -2531930723151537502L;
			
			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info){
				Map i = (Map)o;
				if(i.z.equals(Zone.EAU))
					this.paint=eauColor;
				else if (i.z.equals(Zone.JUNGLE))
					this.paint=jungleColor;
				else
					this.paint=desertColor;
				super.draw(o,g,info);
			}
		};
		//r.paint=Color.PINK;
		r.filled = true;		
		return r;
	}
	
	private ImagePortrayal2D getNourriturePortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/hamburger.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}
	
	private ImagePortrayal2D getArmePortrayal() {
		ImageIcon icon = new ImageIcon("res/icon/glaive.png");
		ImagePortrayal2D r = new ImagePortrayal2D(icon);
		return r;
	}
	
	private OvalPortrayal2D getContenderPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(1.2){

			private static final long serialVersionUID = -9018920390744116027L;

			@Override
			public void draw(Object o, Graphics2D g, DrawInfo2D info){
				Contender i = (Contender)o;
				if(i.vie>=10)
					this.paint=Color.GREEN;
				else if (i.vie>=3)
					this.paint=Color.ORANGE;
				else
					this.paint=Color.RED;
				super.draw(o,g,info);
			}
			
		};
		
		r.paint = Color.YELLOW;
		r.filled = true;
		return r;
	}
	
	private LabelledPortrayal2D getContenderPortrayalLabelled(){
		OvalPortrayal2D child = getContenderPortrayal();
		String l = "initialisation" ;
		LabelledPortrayal2D r = new LabelledPortrayal2D(child,l){

			private static final long serialVersionUID = -5155353521577168909L;
			
			@Override
			public String getLabel(Object o, DrawInfo2D info){
				Contender i = (Contender)o;
				String label = "| Position : (" + i.x + "," + i.y + ")" + " Energie = " + i.energie + " Vie = " + i.vie + " Attaque = " + i.attaque + " |";				
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
	
	public Object getSimulationInspectedObject() { return state; }
	public Inspector getInspector() {
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}
	
}
