package pap1213.assignment.nbody;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Body implements Callable<BodyInfo> {

	private static final double G = 6.674*Math.pow(10,-11);   // gravitational constant
	private static final double dt = 20*0.001;
	//private static final double solarmass=1.98892e30;
	private double EPS = Math.pow(10, 8);      // softening parameter (just to avoid infinities)

	private ArrayList<Body> bodies;
	private P2d o;
	public BodyInfo me;
	
	// create and initialize a new Body
	public Body(int p, ArrayList<Body> bodies, P2d pos, V2d vel, double mass, Color color) {
		
		me = new BodyInfo(pos, vel, 0, 0,mass,p,color);
		this.bodies = bodies;
	    this.o = new P2d(0,0);
	}

	// update the velocity and position using a timestep dt
	public void update(BodyInfo new_me) {
		
		me.fx = new_me.fx;
		me.fy = new_me.fy;
		me.position.x = new_me.position.x;
		me.position.y = new_me.position.y;
		me.velocity.x = new_me.velocity.x;
		me.velocity.y = new_me.velocity.y;
	  }
	
    public P2d getPos(){
        //return new P2d(pos.x,pos.y);
    	o.setPos(me.position.x, me.position.y);
    	return o;
    }
    
	@Override
	public BodyInfo call() throws Exception {

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
		
		double future_vel_x = me.velocity.x + (dt * future_fx) / me.mass;
		double future_vel_y = me.velocity.y + (dt * future_fy) / me.mass;
		double future_pos_x = me.position.x + (dt * future_vel_x);
		double future_pos_y = me.position.y + (dt * future_vel_y);
		
		P2d future_pos = new P2d(future_pos_x, future_pos_y);
		V2d future_vel = new V2d(future_vel_x, future_vel_y);
		
		BodyInfo future_body = new BodyInfo(future_pos, future_vel, future_fx, future_fy,me.mass,me.index);
		return future_body;
	}
}
