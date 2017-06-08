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
import ia04.model.Nourriture;
import ia04.model.Contender;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class BeingsWithUI extends GUIState {
	public static int FRAME_SIZE = 900;
	public Display2D display;
	public JFrame displayFrame;
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
		yardPortrayal.setPortrayalForClass(Insecte.class, getInsectePortrayal());
		yardPortrayal.setPortrayalForClass(Nourriture.class, getNourriturePortrayal());
		yardPortrayal.setPortrayalForClass(Arme.class, getArmePortrayal());
		yardPortrayal.setPortrayalForClass(Contender.class, getContenderPortrayal());
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

	private OvalPortrayal2D getInsectePortrayal() {
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
				if(i.vie>=6)
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
	
	public Object getSimulationInspectedObject() { return state; }
	public Inspector getInspector() {
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}
	
}
