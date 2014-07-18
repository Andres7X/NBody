package pap1213.assignment.nbody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class Universe extends Thread {

	private AtomicBoolean stop;
	private AtomicBoolean pause;
	private AtomicBoolean singleStep;
	
    private NBodyFrame frame;
	private ArrayList<Body> bodies;
	private int cores;
	private ExecutorService executor;
	
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
    
    public void setBodies(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
    
    public void start_pressed()
    {
    	System.out.println("start premuto");
    	pause.set(false);
    	singleStep.set(false);
    }
    
    public void pause_pressed()
    {
    	pause.set(true);
    	singleStep.set(false);
    }
    
    public void singleStep_pressed()
    {
    	pause.set(true);
    	singleStep.set(true);
    }
    
    public void stop_pressed()
    {
    	stop.set(true);
    	pause.set(true);
    	singleStep.set(false);
    	frame.removeUniversePanel();
    }
    
    public void printBody()
    {
    	frame.updateBodies(bodies);
    }
    
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
