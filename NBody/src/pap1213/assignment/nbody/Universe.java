package pap1213.assignment.nbody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Class Universe extends the Thread Java class.
 *  <p>
 *  It represents the thread which controls the behavior of the simulation calling the executor
 *  to calculate all body future values, and then call the NBodyFrame to draw the new body position
 *  
 * 	@author Nompleggio Pietro Antonio, Buscarini Andrea
 */
public class Universe extends Thread {

	private AtomicBoolean stop;
	private AtomicBoolean pause;
	private AtomicBoolean singleStep;
	
    private NBodyFrame frame;
	private ArrayList<Body> bodies;
	private int cores;
	private ExecutorService executor;
	
	/**
	 * Class Universe constructor.
	 * 
	 * @param nbody the number of the bodies 
	 * @param mainFrame the reference of the JFrame that contains all the graphics information and draw the bodies 
	 **/
    public Universe(int nbody, NBodyFrame mainFrame){
        stop = new AtomicBoolean(false);
        pause = new AtomicBoolean(true);
        singleStep = new AtomicBoolean(false);
        
        frame = mainFrame;
        frame.createUniversePanel();
        cores = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println("Numero dei cores: "+cores);
        executor = Executors.newFixedThreadPool(cores);
   }
    
	/**
	 * Setter method
	 * <p>
	 * Set the bodies array with the value passed
	 * 
	 * @param bodies the reference of the array that contains all the bodies
	 **/
    public void setBodies(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
    
	/**
	 * Start Simulation
	 * <p>
	 * This method start the simulation, it's connected with the singleStep and pause AtomicBoolean values
	 **/
    public void start_pressed()
    {
    	pause.set(false);
    	singleStep.set(false);
    }
    
	/**
	 * Pause Simulation
	 * <p>
	 * This method pause the simulation, it's connected with the singleStep and pause AtomicBoolean values
	 **/
    public void pause_pressed()
    {
    	pause.set(true);
    	singleStep.set(false);
    }
    
	/**
	 * Single Step Simulation
	 * <p>
	 * This method pause the simulation, and port it forward one step at a time it's connected with the singleStep and pause AtomicBoolean values
	 **/
    public void singleStep_pressed()
    {
    	pause.set(true);
    	singleStep.set(true);
    }
    
	/**
	 * Pause Simulation
	 * <p>
	 * This method stop the simulation, it's connected with the singleStep, pause and stop AtomicBoolean values.
	 * It remove the frame of the simulation and prepare to start a new one.
	 * 
	 **/
    public void stop_pressed()
    {
    	stop.set(true);
    	pause.set(true);
    	singleStep.set(false);
    	frame.removeUniversePanel();
    }
    
	/**
	 * Print Body method
	 * <p>
	 * This method call the JFrame that draw the new body position.
	 * 
	 **/
    public void printBody()
    {
    	frame.updateBodies(bodies);
    }
    
	/**
	 * run method
	 * <p>
	 * This method is the default method of the Thread Java Class that, start
	 * when the start() reference method it's called.
	 * It create the Executors that invoke the all the call methods of each body, and wait until all 
	 * bodies return their future information, then iterate all this future BodyInfo Structure and update
	 * each body, eventually draw the new position in the frame.
	 * 
	 **/
    public void run(){
        while (!stop.get()) {
        	if (!pause.get() || singleStep.get())
        	{
                try {
                	List<Future<BodyInfo>> list = executor.invokeAll(bodies);
                	for (Future<BodyInfo> future : list){
                		BodyInfo temp = future.get();
                		bodies.get(temp.index).update(temp);
                	}

                	frame.updateBodies(bodies);
                	
                	} catch (Exception ex)
                	{
                		System.out.println("Exception: "+ex);
                	}
                
                singleStep.set(false);
        	}
        }
    }
}
