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
		
		Random ran_mass = new Random(System.nanoTime());
		
		double [ ] all_mass = new double [6];
		for (int m = 0; m<6;m++)
		{
			//double val = Math.pow(((Math.random()*ran_mass.nextInt(10))*10),22+m );
			double val_mass = Math.random()*ran_mass.nextInt(10);
			if (val_mass == 0)
			{
				val_mass = Math.random()*ran_mass.nextInt(10)+1;
			}
			double final_val_mass = val_mass*Math.pow(10, 22+m);
			System.out.println(m+" - valore: "+final_val_mass);
			all_mass[m] = final_val_mass;
		}
		//System.out.println("creo corpi random...");
		for (int i = 0; i<nbody; i++)
		{
			
			//Random velocità vogliamo un tetto massimo di 50.000m/s (che può essere positivo o negativo) quindi 
			//mettiamo un tetto massimo di 100.000 m/s e togliamo ogni volta la metà del tetto al numero random es:
			//random con Math.random() genera un numero tra 0 e 1.0 * 100.000 - 50.000
			P2d pos = new P2d((Math.random()*(1190-1)+1),(Math.random()*(670-1)+1));
			pos.x = pos.x * Math.pow(10, 4);
			pos.y = pos.y * Math.pow(10, 4);
			
			System.out.println("X: "+pos.x+" Y: "+pos.y);

	        double vx = ((Math.random()*3000)-1500);
	        double vy = ((Math.random()*3000)-1500);
	        V2d vel = new V2d(vx,vy);
	        //System.out.println("Vel x: "+vel.x+" Vel y: "+vel.y);
	        //metto abs perch�� voglio le masse sempre positive
	        System.out.println(Math.round(Math.random()*5));
	        double mass = all_mass[(int)Math.round(Math.random()*5)];

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
