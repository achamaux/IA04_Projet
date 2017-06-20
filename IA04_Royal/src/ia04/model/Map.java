package ia04.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import ia04.gui.BeingsWithUI;

import java.io.BufferedReader;
import java.io.FileReader;


import sim.engine.SimState;

public class Map extends Element { 

	private static final long serialVersionUID = -3233964416056145588L;
	public enum Zone {EAU, JUNGLE, DESERT, PLAINE, ANTRE};
	public Zone z;

	public Map(int x, int y) {
		super(x,y);
		 	
		z = Zone.EAU;
		switch(Beings.mapMat[y][x])
		{
		case 0: z = Zone.EAU; break;
		case 1: z = Zone.PLAINE; break;
		case 2: z = Zone.JUNGLE; break;
		case 3: z = Zone.DESERT; break;
		}
		
		
	}
	

	@Override
	public void step(SimState state) {
		//
	}
}
