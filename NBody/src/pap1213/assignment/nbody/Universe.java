package pap1213.assignment.nbody;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import javax.rmi.CORBA.Util;

public class Universe extends Thread {

	private boolean stop;
	private boolean isFirstTime;
    private UniverseFrame frame;
    private Context context;
    private int nBody;
    private Semaphore bodyStop;
	private CyclicBarrier writeBarrier;
	private CyclicBarrier readBarrier;
	private ArrayList<Body> bodies;
	private P2d[] pos;
	private int cores;
	
	//cacca
	
	
    public Universe(){
        stop = false;
        isFirstTime = true;
        frame = new UniverseFrame();
        frame.setVisible(true);
        cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Numero dei cores: "+cores);
   }
   
    public void setSemaphore (Semaphore bodyStop, CyclicBarrier readBarrier, CyclicBarrier writeBarrier)
    {
	    this.bodyStop = bodyStop;
	    this.readBarrier = readBarrier;
	    this.writeBarrier = writeBarrier;
    }
    
    public void setNBody (int nBody)
    {
    	this.nBody = nBody;
    }
    
    public void setBodies(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
    
    public P2d[] getPositions(){
    	for(int i = 0; i<nBody; i++){
    		pos[i] = bodies.get(i).getPos();
    		System.out.println("Il corpo "+i+" ha posizione: ("+pos[i].x+", "+pos[i].y+")");
    	}
    	System.out.println("Ciclo finito");
    	return pos;
    }
    
    public synchronized void setPos(P2d[] p){
    	this.pos = p;
    }
    
    
    public void run(){
        while (!stop) {
        	
        	//*** Faccio partire tutti i corpi, o devono partire prima dell'universe? forsee ha piu' senso farli partire direttamente nel ciclo sopra dopo averli aggiunti all'array
    		if(isFirstTime){
    			isFirstTime=false;
    			for (int i=0; i<nBody; i++){
    				bodies.get(i).start();
    			}
    		}
        	System.out.println("Fine Start corpi");
            try {
            	readBarrier.reset();
            	writeBarrier.reset();
            	
            	frame.updatePosition(getPositions());
                
            	bodyStop.release(nBody);
            
				writeBarrier.await();
			
			//Qui bisogna risettare i latch, oppure usare una cosa diversa tipo una CyclicBarrier
			} catch (InterruptedException | BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            //Faccio leggere le nuove posizioni
            
            try {
                Thread.sleep(20);     
            } catch (Exception ex){
            }
        }
    }
}
