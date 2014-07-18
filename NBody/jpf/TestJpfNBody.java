package jpf;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import gov.nasa.jpf.jvm.Verify;
import gov.nasa.jpf.test.mc.basic.BreakTest.VerifyNextIntBreaker;
import gov.nasa.jpf.test.mc.basic.VerifyTest;

public class TestJpfNBody {
	
	static class P2d{

		public double x,y;

	    public P2d(double x,double y){
	    	Verify.beginAtomic();
	        this.x=x;
	        this.y=y;
	        Verify.endAtomic();
	    }

	    public P2d sum(V2d v){
	        return new P2d(x+v.x,y+v.y);
	    }

	    public V2d sub(P2d v){
	        return new V2d(x-v.x,y-v.y);
	    }

	    public String toString(){
	        return "P2d("+x+","+y+")";
	    }
	    
	    public void setPos(double a, double b){
	    	Verify.beginAtomic();
	    	
	    	this.x = a;
	    	this.y = b;
	    	
	    	Verify.endAtomic();
	    }

	}
	
	static class V2d {

		public double x,y;

	    public V2d(double x,double y){
	    	Verify.beginAtomic();
	        
	    	this.x=x;
	        this.y=y;
	        
	        Verify.endAtomic();
	    }

	    public V2d sum(V2d v){
	        return new V2d(x+v.x,y+v.y);
	    }

	    public double abs(){
	        return (double)Math.sqrt(x*x+y*y);
	    }

	    public V2d getNormalized(){
	        double module=(double)Math.sqrt(x*x+y*y);
	        return new V2d(x/module,y/module);
	    }

	    public V2d mul(double fact){
	        return new V2d(x*fact,y*fact);
	    }

	    public String toString(){
	        return "V2d("+x+","+y+")";
	    }
	}

	static class Body implements Callable<BodyInfo> {

		private final double G = 6.674*Math.pow(10,-11);   // gravitational constant
		private static final double dt = 20*0.001;

		private ArrayList<Body> bodies;
		public BodyInfo me;
		private BodyInfo future_me;

		public Body(int p, ArrayList<Body> bodies, P2d pos, V2d vel, double mass) {
			Verify.beginAtomic();
			
			me = new BodyInfo(pos, vel, 0, 0,mass,p);
            future_me = new BodyInfo(pos, vel, 0, 0,mass,p);
			this.bodies = bodies;
			
			Verify.endAtomic();
		}

		public void update(BodyInfo new_me) {
			Verify.beginAtomic();
			
			me.fx = new_me.fx;
			me.fy = new_me.fy;
			me.position.x = new_me.position.x;
			me.position.y = new_me.position.y;
			me.velocity.x = new_me.velocity.x;
			me.velocity.y = new_me.velocity.y;
			
			Verify.endAtomic();
		  }
	
		public BodyInfo call() throws Exception {
			Verify.beginAtomic();
			
			double future_fx = 0;
			double future_fy = 0;

			for (int j = 0; j< bodies.size(); j++)
	    	{	
	    		if (bodies.get(j).me.index != me.index)
	    		{
	    			double dx = (bodies.get(j).me.position.x-this.me.position.x);
	    			double dy = (bodies.get(j).me.position.y-this.me.position.y);
	    			double dist = Math.sqrt(dx*dx + dy*dy);
	    			double f = (G*bodies.get(j).me.mass * this.me.mass)/(dist*dist);

	    			future_fx = future_fx + ((f * dx)/dist);
	    			future_fy = future_fy + ((f * dy)/dist);
	    		}
	    	}
			
            future_me.fx = future_fx;
            future_me.fy = future_fy;
            future_me.index = me.index;
            future_me.mass = me.mass;
            future_me.velocity.x = me.velocity.x + (dt * future_fx) / me.mass;
            future_me.velocity.y = me.velocity.y + (dt * future_fy) / me.mass;
            future_me.position.x = me.position.x + (dt * future_me.velocity.x);
            future_me.position.y = me.position.y + (dt * future_me.velocity.y);
            
			return future_me;
            
		}
	}
	
	static class BodyInfo {
		
		P2d position;
		V2d velocity;
		double fx;
		double fy;
		double mass;
		int index;
		
		public BodyInfo (P2d position, V2d velocity, double fx, double fy, double mass,int index)
		{
			Verify.beginAtomic();
			
			this.position = position;
			this.velocity = velocity;
			this.fx = fx;
			this.fy = fy;
			this.mass = mass;
			this.index = index;
			
			Verify.endAtomic();
		}
	}

	static class Universe extends Thread {

		private ArrayList<Body> bodies;
		private int cores;
		private ExecutorService executor;
		private List<Future<BodyInfo>> list;
		
	    public Universe(){
	    	
	    	Verify.beginAtomic();
	        
	    	cores = Runtime.getRuntime().availableProcessors() + 1;
	        executor = Executors.newFixedThreadPool(cores);
	        
	        Verify.endAtomic();
	    }
	    
	    public void setBodies(ArrayList<TestJpfNBody.Body> bodies) {
	    	Verify.beginAtomic();
			this.bodies = bodies;
			Verify.endAtomic();
		}
	    
	    public void run(){
	        while (true) {
	        		Runtime rc = Runtime.getRuntime();
	        		
	                try {
	                	
	                	Verify.beginAtomic();
	                	list = executor.invokeAll(bodies);
	                	Verify.endAtomic();
	                	rc.gc();
	                	
	                	for (Future<BodyInfo> future : list){
	                		Verify.beginAtomic();
	                		BodyInfo temp = future.get();

	                		bodies.get(temp.index).update(temp);
	                		Verify.endAtomic();
	                		rc.gc();
	                	}
  
	                } catch (Exception ex){
	                }
	                
	        }
	    }
	}


	public static void main(String[] args) {
		
		Verify.beginAtomic();
		
		Runtime rc = Runtime.getRuntime();
		rc.gc();
		
		ArrayList<Body> bodies = new ArrayList<Body>();
		bodies.clear();
		
		int nbody = 2;
		
		double [ ] all_mass = new double [3];
		
		all_mass[0] = Math.pow(10, 22);
		all_mass[1] = Math.pow(10, 23);
		all_mass[2] = Math.pow(10, 24);
		rc.gc();
		
		for (int i = 0; i<nbody; i++){

			P2d pos = new P2d((Verify.getInt(1000, 1001)),Verify.getInt(500, 501));
			pos.x = pos.x * Math.pow(10, 4);
			pos.y = pos.y * Math.pow(10, 4);

			double vx = Verify.getInt(1500,1501);
			double vy = Verify.getInt(1500,1501);
	        V2d vel = new V2d(vx,vy);

			double mass = all_mass[Verify.getInt(1, 2)];
					
			Body agent = new Body(i,bodies,pos,vel,mass);
	        bodies.add(agent);
	        rc.gc();
		}
		
		Universe universe = new Universe();
		universe.setBodies(bodies);
		rc.gc();
		universe.start();
		
		Verify.endAtomic();

	}
}
