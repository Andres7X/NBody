package pap1213.assignment.nbody;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class UniverseFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UniversePanel panel;
	
	public UniverseFrame(){
        setTitle("N-Body");
        setSize(600,600);
        setResizable(false);
        panel = new UniversePanel();
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	public void updatePosition(P2d[] pos){
        panel.updatePositions(pos);
    }
	
	public static class UniversePanel extends JPanel {
		
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private P2d[] positions;
		
		public UniversePanel(){
            setSize(600,600);
        }
		
		public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.clearRect(0, 0, 600, 600);
            Color prevColor = g.getColor();

            g.setColor(Color.BLACK); // background color
            g.fillRect(0, 0, 600, 600); // fill a rectangle with background color
            g.setColor(prevColor);

            synchronized (this){
	            if (positions!=null){
	                for (int i=0; i<positions.length; i++){
		                P2d p = positions[i];
		                //int x0 = (int)(180+p.x*180);
		                //int y0 = (int)(180-p.y*180);
		                g.drawOval((int)p.x,(int)p.y,5,5);
		                g.setColor(Color.WHITE); // background color
		                g.fillOval((int)p.x,(int)p.y, 5, 5);
		            }
	            }
            }
        }
		
		/*
        public void paint(Graphics g){
        	super.paint(g);
            g.clearRect(0,0,600,600);
            
        }*/
        
        public void updatePositions(P2d[] pos){
            synchronized(this){
                positions = pos;
            }
            repaint();
        }
	}
	

}
