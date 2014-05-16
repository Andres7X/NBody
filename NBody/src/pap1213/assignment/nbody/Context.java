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
			
			//Random velocità vogliamo un tetto massimo di 50.000m/s (che può essere positivo o negativo) quindi 
			//mettiamo un tetto massimo di 100.000 m/s e togliamo ogni volta la metà del tetto al numero random es:
			//random con Math.random() genera un numero tra 0 e 1.0 * 100.000 - 50.000
			Random rand = new Random(System.nanoTime()+i);
			//perch�� tra 590 e 570??
			P2d pos = new P2d(rand.nextInt(1190),rand.nextInt(670));
			//System.out.println("X: "+pos.x+" Y: "+pos.y);
			//creo dx da un generatore random e lo moltiplico per il segno di un intero che mi restituisce
			//il generatore perch�� sembra che con i double non ne dia mai con segno negativo
	        double vx = ((Math.random()*20)-10);
	        double vy = ((Math.random()*20)-10);
	        V2d vel = new V2d(vx,vy);
	        System.out.println("Vel x: "+vel.x+" Vel y: "+vel.y);
	        //metto abs perch�� voglio le masse sempre positive
	        double mass = Math.random()*10e9;
	        //bounds = ctx.getBounds();
//	        Random randomGenerator = new Random();
//	        int red = randomGenerator.nextInt(255);
//	        int green = randomGenerator.nextInt(255);
//	        int blue = randomGenerator.nextInt(255);
//	        Color randomColour = new Color(red,green,blue);
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
	
	public void generateBodyFromFile(int nbody, ArrayList<BodyInfoFromFile> bodiesFromFile){
		//Ogni volta pulisco l'array con i corpi
		bodies.clear();
		universe = new Universe(nbody);
		
		int i = 0;
		//creo l'arraylist con i corpi letti dal file
		for(BodyInfoFromFile agent:bodiesFromFile){
			
			Color bodyColour = null;
			switch (i) {
			case 0:
				bodyColour = Color.WHITE;
				break;
			case 1:
				bodyColour = Color.YELLOW;
				break;
			case 2:
				bodyColour = Color.RED;
				break;
			}
			 
			Body body = new Body(agent.index, bodies, agent.position, agent.velocity, agent.mass, bodyColour);
			bodies.add(body);
			i++;
		}
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
