package pap1213.assignment.nbody;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Context {
	
	private ArrayList<Body> bodies;
	private Universe universe;
	
	public Context ()
	{
		bodies = new ArrayList<Body>();		
	}
	
	public void generateRandomBodyWithNumber(int nbody)
	{
		//Ogni volta pulisco l'array con i corpi
		bodies.clear();
		universe = new Universe(nbody);
		
		//System.out.println("creo corpi random...");
		for (int i = 0; i<nbody; i++)
		{
			//System.out.println("Creo corpo: "+i);
			//Random Position
			Random rand = new Random(System.currentTimeMillis()+i);
			//perch�� tra 590 e 570??
			P2d pos = new P2d(rand.nextInt(1190),rand.nextInt(670));
			//System.out.println("X: "+pos.x+" Y: "+pos.y);
			//creo dx da un generatore random e lo moltiplico per il segno di un intero che mi restituisce
			//il generatore perch�� sembra che con i double non ne dia mai con segno negativo
	        double dx = rand.nextDouble()*Math.signum(rand.nextInt());
	        V2d vel = new V2d(dx,Math.sqrt(1-dx*dx));
	        System.out.println("Vel x: "+vel.x+" Vel y: "+vel.y);
	        //metto abs perch�� voglio le masse sempre positive
	        int mass = Math.abs(rand.nextInt());
	        //bounds = ctx.getBounds();
	        Random randomGenerator = new Random();
	        int red = randomGenerator.nextInt(255);
	        int green = randomGenerator.nextInt(255);
	        int blue = randomGenerator.nextInt(255);
	        Color randomColour = new Color(red,green,blue);
	        Color bodyColour = Color.WHITE;
			Body agent = new Body(i,bodies,pos,vel,mass,bodyColour);
	        bodies.add(agent);
	      
	        
		}
		
		//una volta creati i corpi faccio partire l'universo, ***la variabile stop di universe dobbiamo usarla?
		//*** e' meglio mettere la variabile di default a true e quando si crea l'universo fare partire start direttamente
		//e poi usare la variabile stop per fermare e fare partire il thread
		universe.setBodies(bodies);

		universe.printBody();
		universe.start();
	}
	
	public void start_pressed()
	{
		universe.start_pressed();
	}
	
	public void pause_pressed()
	{
		universe.pause_pressed();
	}
	
	public void singleStep_pressed()
	{
		universe.singleStep_pressed();
	}
	
	public void stop_pressed()
	{
		universe.stop_pressed();
	}
	
	public int bodyNumber ()
	{
		return bodies.size();
	}
	
}
