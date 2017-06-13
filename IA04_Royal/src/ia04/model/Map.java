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

public class Map extends MySteppable { 

	private static final long serialVersionUID = -3233964416056145588L;
	public enum Zone {EAU, JUNGLE, DESERT, PLAINE};
	public Zone z;

	public Map(int x, int y) {
		super(x,y);
		 	
		z = Zone.EAU;
//		try{
//		if(x==150)
//			System.out.println("x=="+x);
//		if(y==150)
//			System.out.println("y=="+y);
		try{
			System.out.println("before");
			if(Beings.mapMat[y][x] == 1)
			{
				System.out.println("after");
				z = Zone.PLAINE;
			}	
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("x : " +x+ "y : "+y);
		}
		
		
		
	}
	

	@Override
	public void step(SimState state) {
		//
	}
}
