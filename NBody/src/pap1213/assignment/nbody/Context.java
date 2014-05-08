package pap1213.assignment.nbody;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Context {
	
	private ArrayList<Body> bodies;
	private Universe universe;
	private Semaphore writeLock;
	private Semaphore readLock;
	private CountDownLatch writeLatch;
	private CountDownLatch doneLatch;
	private P2d[] pos;

	public Context ()
	{
		universe = new Universe();
		bodies = new ArrayList<Body>();		
	}
	
	public void generateRandomBodyWithNumber(int nbody)
	{
		//Ogni volta pulisco l'array con i corpi
		bodies.clear();
		readLock = new Semaphore(0);
		writeLock = new Semaphore(0);
		writeLatch = new CountDownLatch(nbody);
		doneLatch = new CountDownLatch(nbody);
		
		universe.setSemaphore(writeLock, readLock, writeLatch, doneLatch);
		universe.setNBody(nbody);
		for (int i = 0; i<nbody; i++)
		{
			System.out.println("Creo corpo: "+i);
			//Random Position
			Random rand = new Random(System.currentTimeMillis()+i);
			P2d pos = new P2d(rand.nextInt(590),rand.nextInt(570));
			System.out.println("X: "+pos.x+" Y: "+pos.y);
	        double dx = rand.nextDouble();
	        V2d vel = new V2d(dx,Math.sqrt(1-dx*dx));
	        System.out.println("Vel x: "+vel.x+" Vel y: "+vel.y);
	        int mass = rand.nextInt();
	        //bounds = ctx.getBounds();
	        Random randomGenerator = new Random();
	        int red = randomGenerator.nextInt(255);
	        int green = randomGenerator.nextInt(255);
	        int blue = randomGenerator.nextInt(255);
	        Color randomColour = new Color(red,green,blue);
	        Color bodyColour = Color.WHITE;
			Body agent = new Body(i,bodies,pos,vel,mass,bodyColour,writeLock,readLock,writeLatch,doneLatch);
	        bodies.add(agent);
	        
		}
		
		//una volta creati i corpi faccio partire l'universo, ***la variabile stop di universe dobbiamo usarla?
		//*** e' meglio mettere la variabile di default a true e quando si crea l'universo fare partire start direttamente
		//e poi usare la variabile stop per fermare e fare partire il thread
		universe.setBodies(bodies);
		pos = new P2d[nbody];
		pos = getPositions();
		universe.setPos(pos);
		
		universe.start();
		
		
	}
	
	public int bodyNumber ()
	{
		return bodies.size();
	}
	
	public P2d[] getPositions(){
        P2d[] array = new P2d[bodies.size()];
        for (int i=0; i<array.length; i++){
            array[i] = ((Body)bodies.get(i)).getPos();
        }
        return array;
    }
	
}
