package pap1213.assignment.nbody;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Body extends Thread {

	private static final double G = 6.673e-11;   // gravitational constant
	private static final double dt = 20;
	//private static final double solarmass=1.98892e30;
	
	private P2d pos; 		  // cartesian positions
	private V2d vel;       	 // velocity 
	public double fx, fy; 	// force
	public double mass;    // mass
	public Color color;   // color
	private boolean stop;
	private int p;
	private ArrayList<Body> bodies;
	private P2d o;
	
	private Semaphore writeLock;
	private Semaphore readLock;
	private CountDownLatch writeLatch;
	private CountDownLatch doneLatch;
	
	// create and initialize a new Body
	public Body(int p, ArrayList<Body> bodies, P2d pos, V2d vel, int mass, Color randomColour,Semaphore writeLock,Semaphore readLock,CountDownLatch writeLatch,CountDownLatch doneLatch) {
		this.p = p;
		this.bodies = bodies;
		this.pos = new P2d(pos.x,pos.y);
		this.vel = new V2d(vel.x,vel.y);
	    this.mass  = mass;
	    this.color = randomColour;
	    this.stop = false;
	    this.writeLock = writeLock;
	    this.readLock = readLock;
	    this.writeLatch = writeLatch;
	    this.doneLatch = doneLatch;
	    this.o = new P2d(0,0);
	}

	// update the velocity and position using a timestep dt
	public void update(double tempfx,double tempfy) {
		fx = tempfx;
		fy = tempfy;
		vel.x += dt * fx / mass;
		vel.y += dt * fy / mass;
		pos.x += (vel.x + ((dt * fx / mass) /2)) * dt;
		pos.y += (vel.y + ((dt * fy / mass) /2)) * dt;
	  }
	
    public P2d getPos(){
        //return new P2d(pos.x,pos.y);
    	o.setPos(pos.x, pos.y);
    	return o;
    }
    
    public void run(){
    	
    	while (!stop) {
        
    		try {
    			
    			//Semaforo che aspetta la lettura, e viene sbloccato dall'universe con un signalAll(ndbody)
				readLock.acquire();
				
				double tempfx = 0;
				double tempfy = 0;
				
	    		for (int j = 0; j< bodies.size(); j++)
	        	{
	        		if (j != p)
	        		{
	        			tempfx = tempfx + ((G * bodies.get(j).mass * this.mass)/(Math.pow((bodies.get(j).pos.x+this.pos.x), 2)));
	        			tempfy = tempfy + ((G * bodies.get(j).mass * this.mass)/(Math.pow((bodies.get(j).pos.y+this.pos.y), 2)));
	        		}
	        	}
	    		//Questo latch viene decrementato per ogni body, perchè quando il valore sarà 0 allora l'universe sbloccherà il semaforo writeLock per
	    		//fare partire la scrittura concorrente
	    		writeLatch.countDown();
	    		System.out.println(p+" Ho fatto la lettura aspetto per scrivere");
	    		writeLock.acquire();
	    		System.out.println(p+" Sto scrivendo");
	        	update(tempfx,tempfy);
	        	doneLatch.countDown();
	        	System.out.println(p+" Ho scritto");
	        	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
