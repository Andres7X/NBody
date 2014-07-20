package pap1213.assignment.nbody;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

/**
 *  Class Utility.
 *  <p>
 *  Utility class that stores some of the data of the simulation
 *  
 * 	@author Nompleggio Pietro Antonio, Buscarini Andrea
 */
public class Utility {
	
	//Corpo piu' leggero, scala di grigi
	public static final Color[] color_22 = {new Color(236, 240, 241),new Color(189, 195, 199),new Color(149, 165, 166),new Color(127, 140, 141)};
	//Scala verde
	public static final Color[] color_23 = {new Color(26, 188, 156),new Color(22, 160, 133),new Color(46, 204, 113),new Color(39, 174, 96)};
	//Scala azzurro
	public static final Color[] color_24 = {new Color(52, 152, 219),new Color(41, 128, 185)};
	//Scala arancione
	public static final Color[] color_25 = {new Color(241, 196, 15),new Color(243, 156, 18),new Color(230, 126, 34),new Color(211, 84, 0)};
	//Scala rosso
	public static final Color[] color_26 = {new Color(231, 76, 60),new Color(192, 57, 43)};
	//Corpo piu' pesante, scala viola
	public static final Color[] color_27 = {new Color(155, 89, 182),new Color(142, 68, 173)};
	
	//Screen size
	public static final Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	
	//Velocita' massima
	public static double max_velocity = 1500;
}
