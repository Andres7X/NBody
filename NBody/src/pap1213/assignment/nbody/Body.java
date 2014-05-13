package pap1213.assignment.nbody;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Body implements Callable<BodyInfo> {

	private static final double G = 6.673e-11;   // gravitational constant
	private static final double dt = 20;
	//private static final double solarmass=1.98892e30;

	
	private P2d pos; 		  // cartesian positions
	private V2d vel;       	 // velocity 
	public double fx, fy; 	// force
	public double mass;    // mass
	public Color color;   // color
	private int p;
	private ArrayList<Body> bodies;
	private P2d o;
	
	// create and initialize a new Body
	public Body(int p, ArrayList<Body> bodies, P2d pos, V2d vel, int mass, Color randomColour) {
		this.p = p;
		this.bodies = bodies;
		this.pos = new P2d(pos.x,pos.y);
		this.vel = new V2d(vel.x,vel.y);
	    this.mass  = mass;
	    this.color = randomColour;
	    this.o = new P2d(0,0);
	}

	// update the velocity and position using a timestep dt
	public void update(P2d new_pos, V2d new_vel,double new_fx,double new_fy) {
		fx = new_fx;
		fy = new_fy;
		pos.x = new_pos.x;
		pos.y = new_pos.y;
		vel.x = new_vel.x;
		vel.y = new_vel.y;
		/*vel.x += dt * fx / mass;
		vel.y += dt * fy / mass;
		pos.x += (vel.x + ((dt * fx / mass) /2)) * dt;
		pos.y += (vel.y + ((dt * fy / mass) /2)) * dt;
		System.out.println(p+" - Vel("+vel.x+", "+vel.y+"); Pos("+pos.x+", "+pos.y+") - Massa: "+mass);
		*/
	  }
	
    public P2d getPos(){
        //return new P2d(pos.x,pos.y);
    	o.setPos(pos.x, pos.y);
    	return o;
    }
    
	@Override
	public BodyInfo call() throws Exception {
		//System.out.println("Calcolo il nuovo valore del body: "+p);
		double future_fx = 0;
		double future_fy = 0;
		
		for (int j = 0; j< bodies.size(); j++)
    	{
    		if (j != p)
    		{
    			future_fx = future_fx + ((G * bodies.get(j).mass * this.mass)/(Math.pow((bodies.get(j).pos.x+this.pos.x), 2)));
    			future_fy = future_fy + ((G * bodies.get(j).mass * this.mass)/(Math.pow((bodies.get(j).pos.y+this.pos.y), 2)));
    		}
    	}
		
		//update(tempfx,tempfy);
		double future_vel_x = vel.x + dt * future_fx / mass;
		double future_vel_y = vel.y + dt * future_fy / mass;
		double future_pos_x = pos.x + (future_vel_x + ((dt * future_fx / mass) /2)) * dt;
		double future_pos_y = pos.y + (future_vel_y + ((dt * future_fy / mass) /2)) * dt;
		
		P2d future_pos = new P2d(future_pos_x, future_pos_y);
		V2d future_vel = new V2d(future_vel_x, future_vel_y);
		
		BodyInfo future_body = new BodyInfo(future_pos, future_vel, future_fx, future_fy,p);
		return future_body;
	}
}
