package pap1213.assignment.nbody;

import java.awt.Color;

/**
 *  Class BodyInfo.
 *  <p>
 *  This class holds the strucutre of the Body.
 *  
 * 	@author Nompleggio Pietro Antonio, Buscarini Andrea
 */
public class BodyInfo {
	
	P2d position;
	V2d velocity;
	double fx;
	double fy;
	double mass;
	int index;
	Color color;
	
	/**
	 * Class BodyInfo constructor.
	 * <p>
	 * Used for the random body generation.
	 * 
	 * @param position the position structure
	 * @param velocity the velocity structure
	 * @param fx the x value of the force
	 * @param fy the y value of the force 
	 * @param mass the mass value
	 * @param index the index of the body
	 * @param color the color of the body
	 **/
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
	
	/**
	 * Class BodyInfo constructor.
	 * <p>
	 * Used for the load file body generation.
	 * 
	 * @param position the position structure
	 * @param velocity the velocity structure
	 * @param fx the x value of the force
	 * @param fy the y value of the force 
	 * @param mass the mass value
	 * @param index the index of the body
	 **/
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
