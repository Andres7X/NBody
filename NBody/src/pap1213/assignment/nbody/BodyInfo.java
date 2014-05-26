package pap1213.assignment.nbody;

import java.awt.Color;

public class BodyInfo {
	
	P2d position;
	V2d velocity;
	double fx;
	double fy;
	double mass;
	int index;
	Color color;
	
	public BodyInfo (P2d position, V2d velocity, double fx, double fy, double mass,int index, Color color)
	{
		this.position = position;
		this.velocity = velocity;
		this.fx = fx;
		this.fy = fy;
		this.mass = mass;
		this.index = index;
		this.color = color;
	}
	
	public BodyInfo (P2d position, V2d velocity, double fx, double fy, double mass,int index)
	{
		this.position = position;
		this.velocity = velocity;
		this.fx = fx;
		this.fy = fy;
		this.mass = mass;
		this.index = index;
	}
}
