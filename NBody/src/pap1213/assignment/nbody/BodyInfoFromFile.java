package pap1213.assignment.nbody;

public class BodyInfoFromFile {

	P2d position;
	V2d velocity;
	double mass;
	int index;
	
	public BodyInfoFromFile(P2d pos, V2d vel, double mass, int i)
	{
		
		this.position = pos;
		this.velocity = vel;
		this.mass = mass;
		this.index = i;
	}

}
