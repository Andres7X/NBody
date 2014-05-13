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
	
    private UniverseFrame frame;
	private ArrayList<Body> bodies;
	private P2d[] pos;
	private int cores;
	private ExecutorService executor;
	private long lastTime;
	private double fps;
	
    public Universe(){
        stop = new AtomicBoolean(false);
        pause = new AtomicBoolean(true);
        singleStep = new AtomicBoolean(false);
        
        frame = new UniverseFrame();
        frame.setVisible(true);
        cores = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println("Numero dei cores: "+cores);
        executor = Executors.newFixedThreadPool(cores);
        
   }
    
    public void setBodies(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
    
    public P2d[] getPositions(){
    	for(int i = 0; i<bodies.size(); i++){
    		pos[i] = bodies.get(i).getPos();
    		//System.out.println("Il corpo "+i+" ha posizione: ("+pos[i].x+", "+pos[i].y+")");
    	}
    	//System.out.println("Ciclo finito");
    	return pos;
    }
    
    public synchronized void setPos(P2d[] p){
    	this.pos = p;
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
    	frame.setVisible(false);
    	frame.dispose();
    }
    
    public void printBody()
    {
    	frame.updatePosition(getPositions());
    }
    
    public void run(){
        while (!stop.get()) {
        	//System.out.println("");
        	if (!pause.get() || singleStep.get())
        	{
        		lastTime = System.nanoTime();
                try {
                	List<Future<BodyInfo>> list = executor.invokeAll(bodies);
                	for (Future<BodyInfo> future : list){
                		BodyInfo temp = future.get();
                		bodies.get(temp.index).update(temp.position, temp.velocity, temp.fx, temp.fy);
                	}

                	frame.updatePosition(getPositions());
                    Thread.sleep(20);     
                } catch (Exception ex){
                }
                
                singleStep.set(false);
                
                fps = 1000000000.0 / (System.nanoTime() - lastTime);
                System.out.println(fps);
        	}
        }
    }
}
