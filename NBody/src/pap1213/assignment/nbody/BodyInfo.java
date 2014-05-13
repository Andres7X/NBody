package pap1213.assignment.nbody;

public class BodyInfo {
	
	P2d position;
	V2d velocity;
	double fx;
	double fy;
	int index;
	
	public BodyInfo (P2d position, V2d velocity, double fx, double fy,int index)
	{
		this.position = position;
		this.velocity = velocity;
		this.fx = fx;
		this.fy = fy;
		this.index = index;
	}
}
