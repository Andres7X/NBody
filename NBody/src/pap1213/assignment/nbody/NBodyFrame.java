package pap1213.assignment.nbody;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  Class NBodyFrame extends the JFrame Java Swing class.
 *  <p>
 *  It contains the JFrame of the project and the UniversePanel class that
 *  contains the method to draw the body
 *  
 * 	@author Nompleggio Pietro Antonio, Buscarini Andrea
 */
public class NBodyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UniversePanel panel;
	
	/**
	 * Class NBodyFrame constructor.
	 * 
	 **/
	public NBodyFrame(){
        setTitle("N-Body");
        setSize(Utility.rect.width,Utility.rect.height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel top = new JPanel();
        top.setSize(Utility.rect.width,40);
        JLabel infoLabel = new JLabel("Choose one option, create and start!");
        infoLabel.setForeground(Color.BLACK);
        top.add(infoLabel);
        top.setLocation(0, this.getSize().height/2);
        add( top );
    }
	
	/**
	 * Update Bodies method
	 * <p>
	 * Call the method of the JPanel that set the new bodies reference
	 * 
	 * @param bodies the reference of the array that contains all the bodies
	 **/
	public void updateBodies(ArrayList<Body> bodies){
        panel.updateBodies(bodies);
    }
	
	/**
	 * Create Panel method
	 * <p>
	 * This method create the panel to start a new simulation
	 * 
	 **/
	public void createUniversePanel()
	{
        panel = new UniversePanel();
        getContentPane().add(panel);
	}
	
	/**
	 * Create Panel method
	 * <p>
	 * This method remove the panel to finish a simulation, it's called by the stop button of the GUI.
	 * 
	 **/
	public void removeUniversePanel()
	{
		remove(panel);
		revalidate();
		repaint();
	}
	
	/**
	 *  Class UniversePanel extends the UniversePanel Java Swing class.
	 *  <p>
	 *  It contains the JPanel of the project and the JPanel that
	 *  contains the method to draw the body
	 *  
	 */
	public static class UniversePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private ArrayList<Body> bodies;
		
		/**
		 * Class UniversePanel constructor.
		 * 
		 **/
		public UniversePanel(){
            setSize(Utility.rect.width,Utility.rect.height-60);
        }
		
		/**
		 * Paint method
		 * <p>
		 * This method paint the new body position in the panel
		 * 
		 * @param g
		 **/
		public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.clearRect(0, 0, Utility.rect.width,Utility.rect.height-60);
            Color prevColor = g.getColor();
            
            // background color
            g.setColor(Color.BLACK);
            // fill a rectangle with background color
            g.fillRect(0, 0, Utility.rect.width,Utility.rect.height-60);
            g.setColor(prevColor);

            synchronized (this){
	            if (bodies!=null){
	                for (int i=0; i<bodies.size(); i++){
		                P2d p = bodies.get(i).getPos();
		                g.setColor(bodies.get(i).me.color);
		                g.drawOval((int)(p.x/Math.pow(10, 4)),(int)(p.y/Math.pow(10, 4)),5,5);
		                g.fillOval((int)(p.x/Math.pow(10, 4)),(int)(p.y/Math.pow(10, 4)), 5, 5);
		            }
	            }
            }
        }
        
		/**
		 * Update Bodies method
		 * <p>
		 * This method set the new bodies reference and the call the repaint method to draw the new position
		 * 
		 * @param bodies the reference of the array that contains all the bodies
		 **/
        public void updateBodies(ArrayList<Body> bodies){
        	this.bodies = bodies;
            repaint();
        }
	}
}
