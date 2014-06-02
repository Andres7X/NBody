package pap1213.assignment.nbody;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NBodyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UniversePanel panel;
	
	public NBodyFrame(){
        setTitle("N-Body");
        setSize(1200,760);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel top = new JPanel();
        top.setSize(1200,40);
        JLabel infoLabel = new JLabel("Choose one option, create and start!");
        infoLabel.setForeground(Color.BLACK);
        top.add(infoLabel);
        top.setLocation(0, this.getSize().height/2);
        add( top );
    }
	
	public void updateBodies(ArrayList<Body> bodies){
        panel.updateBodies(bodies);
    }
	
	public void createUniversePanel()
	{
        panel = new UniversePanel();
        getContentPane().add(panel);
	}
	
	public void removeUniversePanel()
	{
		remove(panel);
		revalidate();
		repaint();
	}
	
	public static class UniversePanel extends JPanel {
			
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Body> bodies;
		
		public UniversePanel(){
            setSize(1200,700);
        }
		
		public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //System.out.println("Disegno");
            g.clearRect(0, 0, 1200,700);
            Color prevColor = g.getColor();

            g.setColor(Color.BLACK); // background color
            g.fillRect(0, 0, 1200,700); // fill a rectangle with background color
            g.setColor(prevColor);

            synchronized (this){
	            if (bodies!=null){
	                for (int i=0; i<bodies.size(); i++){
		                P2d p = bodies.get(i).getPos();
		                g.setColor(bodies.get(i).me.color); // background color
		                g.drawOval((int)(p.x/Math.pow(10, 4)),(int)(p.y/Math.pow(10, 4)),5,5);
		                g.fillOval((int)(p.x/Math.pow(10, 4)),(int)(p.y/Math.pow(10, 4)), 5, 5);
		            }
	            }
            }
        }
		
		/*
        public void paint(Graphics g){
        	super.paint(g);
            g.clearRect(0,0,600,600);
            
        }*/
        
        public void updateBodies(ArrayList<Body> bodies){
            synchronized(this){
                this.bodies = bodies;
            }
            repaint();
        }
	}
}