package pap1213.assignment.nbody;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *  Class Context.
 *  <p>
 *  It represents the class that generate the bodies with different method,
 *  and receive the request from the GUI ControlPanel class 
 *  
 * 	@author Nompleggio Pietro Antonio, Buscarini Andrea
 */
public class Context {
	
	private ArrayList<Body> bodies;
	private Universe universe;
	private NBodyFrame mainFrame;
	private ControlPanel ctrl;
	
	/**
	 * Class Universe constructor.
	 * <p>
	 * Create the bodies array, the Control Panel class that handle the GUI
	 **/
	public Context ()
	{
		bodies = new ArrayList<Body>();
		ctrl = new ControlPanel(this);
		mainFrame = new NBodyFrame();
		mainFrame.setLayout(null);
		ctrl.setLocation(0, mainFrame.getSize().height-60);
		mainFrame.add(ctrl);
	}
	
	/**
	 * Create frame method.
	 * <p>
	 * Set visible the Control Panel
	 **/
	public void createFrame()
	{
		mainFrame.setVisible(true);
	}
	
	/**
	 * Random Body Generator method.
	 * <p>
	 * This body generate a nbody random number of body.
	 * 
	 * @param nbody the number of the bodies 
	 **/
	public void generateRandomBodyWithNumber(int nbody)
	{
		bodies.clear();
		universe = new Universe(nbody,mainFrame);
		
		Random ran_mass = new Random(System.nanoTime());
		
		double [ ] all_mass = new double [6];
		for (int m = 0; m<6;m++)
		{
			double val_mass = Math.random()*ran_mass.nextInt(10);
			if (val_mass == 0)
			{
				val_mass = Math.random()*ran_mass.nextInt(10)+1;
			}
			double final_val_mass = val_mass*Math.pow(10, 22+m);
			
			all_mass[m] = final_val_mass;
		}

		for (int i = 0; i<nbody; i++)
		{
			P2d pos = new P2d((Math.random()*(Utility.rect.width-10-1)+1),(Math.random()*(Utility.rect.height-90-1)+1));
			pos.x = pos.x * Math.pow(10, 4);
			pos.y = pos.y * Math.pow(10, 4);
			
			//Random velocita vogliamo un tetto massimo di 3000m/s (che puo essere positivo o negativo) quindi 
			//mettiamo un tetto massimo di 3000 m/s e togliamo ogni volta la meta del tetto al numero random es:
			//random con Math.random() genera un numero tra 0 e 1.0 * 3000 - 1500
	        double vx = ((Math.random()*Utility.max_velocity)-(Utility.max_velocity/2));
	        double vy = ((Math.random()*Utility.max_velocity)-(Utility.max_velocity/2));
	        V2d vel = new V2d(vx,vy);
	        
	        double mass = all_mass[(int)Math.round(Math.random()*5)];
	    	
	        Color bodyColour = selectColorForBodyMass(mass);

			Body agent = new Body(i,bodies,pos,vel,mass,bodyColour);
	        bodies.add(agent);
		}
		
		universe.setBodies(bodies);
		universe.printBody();
		universe.start();
	}
	
	/**
	 * Choose Body Colory method.
	 * <p>
	 * This method receive the mass value of the body and choose the right color.
	 * 
	 * @param mass the mass of the body 
	 * 
	 * @return bodyColour the color of the body
	 **/
	private Color selectColorForBodyMass(double mass)
	{
		Color bodyColour = Color.white;
		
        if (mass >= Math.pow(10, 26))
        {
        	bodyColour = Utility.color_27[(int)Math.round(Math.random()*1)];
        } else if (mass >= Math.pow(10, 25) && mass <= Math.pow(10, 26))
        {
        	bodyColour = Utility.color_26[(int)Math.round(Math.random()*1)];
        } else if (mass >= Math.pow(10, 24) && mass <= Math.pow(10, 25))
        {
        	bodyColour = Utility.color_25[(int)Math.round(Math.random()*3)];
        } else if (mass >= Math.pow(10, 23) && mass <= Math.pow(10, 24))
        {
        	bodyColour = Utility.color_24[(int)Math.round(Math.random()*1)];
        } else if (mass >= Math.pow(10, 22) && mass <= Math.pow(10, 23))
        {
        	bodyColour = Utility.color_23[(int)Math.round(Math.random()*3)];
        } else {
        	bodyColour = Utility.color_22[(int)Math.round(Math.random()*3)];
        }
        
		return bodyColour;
		
	}
	
	/**
	 * Load File Generate Body method.
	 * <p>
	 * This method receive a file to read and generate bodies.
	 * 
	 * @param file the txt file to reade to get all bodies to create
	 * 
	 * @return boolean return true if success the file loading, otherwise return false
	 **/
	public boolean generateBodyFromFile(File file){

		bodies.clear();
		
		boolean generate_body = splitFile(file);
		
		if (!generate_body)
		{
			return false;
		}
		
		universe = new Universe(bodies.size(),mainFrame);

		universe.setBodies(bodies);
		universe.printBody();
		universe.start();
		
		return true;
	}
	
	/**
	 * Split File method.
	 * <p>
	 * This method read the file line by line and pass the line to the analizeLine mehotd.
	 * 
	 * @param file the txt file to read to get all bodies to create
	 * 
	 * @return boolean return true if success the file reading, otherwise return false
	 **/
	private boolean splitFile(File file){
		try {
			
			//apro il file
			FileInputStream fstream = new FileInputStream(file);
			
			//metto il FileInputStream
			DataInputStream datain = new DataInputStream(fstream);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(datain));
			
			String line;
			int n_body = 0;
			boolean success = false;
			
			//leggo il file riga per riga
			while((line = buffer.readLine()) != null){
				success = analizeLine(line,n_body);
				n_body++;
				
				if (!success)
				{
					break;
				}
			}
			
			datain.close();
			
			if (!success)
			{
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	/**
	 * Analize Line method.
	 * <p>
	 * This method read the line and extract the body information, and create the Body.
	 * 
	 * @param line the text line to parse
	 * @param n_body the number of bodies
	 * 
	 * @return boolean return true if success the file reading, otherwise return false
	 **/
	public boolean analizeLine(String line, int n_body){
		
		StringTokenizer st = new StringTokenizer(line);
		
		//analyze the position vector
		String string = st.nextToken();
		
		try{
			String spls = string.substring(1, string.length()-1);
			String[] arg = spls.split(",");
			P2d position = new P2d(Double.parseDouble(arg[0])*Math.pow(10, 4), Double.parseDouble(arg[1])*Math.pow(10, 4));
			
			//analyze the speed vector
			string = st.nextToken();
			
			spls = string.substring(1, string.length()-1);
			arg = spls.split(",");
			V2d velocity = new V2d(Double.parseDouble(arg[0]),Double.parseDouble(arg[1]));
			
			//analyze the mass
			//put that block out from try and catch because in that token 
			//there is some special characters like * and ^
			String string_massa = st.nextToken();

			spls = string_massa.substring(string_massa.length()-2);
			int exp = Integer.parseInt(spls);
			
			spls = string_massa.substring(0,string_massa.length()-6);
			double mass = Double.parseDouble(spls);
			
			mass = mass * Math.pow(10, exp);
			
			Color bodyColour = selectColorForBodyMass(mass);
	        
			Body agent = new Body(n_body,bodies,position,velocity,mass,bodyColour);
			bodies.add(agent);
			
			return true;
			
		} catch(NumberFormatException e){
			
			return false;
		}		
	}
	
	/**
	 * Start method.
	 * <p>
	 * Call the start method in Universe Class
	 **/
	public void start_pressed()
	{
		universe.start_pressed();
	}
	
	/**
	 * Pause method.
	 * <p>
	 * Call the pause method in Universe Class
	 **/
	public void pause_pressed()
	{
		universe.pause_pressed();
	}
	
	/**
	 * Single Step method.
	 * <p>
	 * Call the single step method in Universe Class
	 **/
	public void singleStep_pressed()
	{
		universe.singleStep_pressed();
	}
	
	/**
	 * Stop method.
	 * <p>
	 * Call the stop method in Universe Class
	 **/
	public void stop_pressed()
	{
		universe.stop_pressed();
	}
}
