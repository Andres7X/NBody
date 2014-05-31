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

public class Context {
	
	private ArrayList<Body> bodies;
	private Universe universe;
	private NBodyFrame mainFrame;
	private ControlPanel ctrl;
	
	public Context ()
	{
		bodies = new ArrayList<Body>();
		ctrl = new ControlPanel(this);
		mainFrame = new NBodyFrame();
		mainFrame.setLayout(null);
		ctrl.setLocation(0, mainFrame.getSize().height-60);
		mainFrame.add(ctrl);
	}
	
	public void createFrame()
	{
		mainFrame.setVisible(true);
	}
	
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
			System.out.println(m+" - valore: "+final_val_mass);
			all_mass[m] = final_val_mass;
		}

		for (int i = 0; i<nbody; i++)
		{
			
			//Random velocità vogliamo un tetto massimo di 50.000m/s (che può essere positivo o negativo) quindi 
			//mettiamo un tetto massimo di 100.000 m/s e togliamo ogni volta la metà del tetto al numero random es:
			//random con Math.random() genera un numero tra 0 e 1.0 * 100.000 - 50.000
			P2d pos = new P2d((Math.random()*(1190-1)+1),(Math.random()*(670-1)+1));
			pos.x = pos.x * Math.pow(10, 4);
			pos.y = pos.y * Math.pow(10, 4);
			
			System.out.println("X: "+pos.x+" Y: "+pos.y);

	        double vx = ((Math.random()*3000)-1500);
	        double vy = ((Math.random()*3000)-1500);
	        V2d vel = new V2d(vx,vy);
	        //System.out.println("Vel x: "+vel.x+" Vel y: "+vel.y);
	        //metto abs perch�� voglio le masse sempre positive
	        System.out.println(Math.round(Math.random()*5));
	        double mass = all_mass[(int)Math.round(Math.random()*5)];
	    	
	        Color bodyColour = null;
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

			Body agent = new Body(i,bodies,pos,vel,mass,bodyColour);
	        bodies.add(agent);
	      
	        
		}
		
		universe.setBodies(bodies);

		universe.printBody();
		universe.start();
	}
	
	
	public boolean generateBodyFromFile(File file){
		//Ogni volta pulisco l'array con i corpi
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	public boolean analizeLine(String line, int n_body){
		
		StringTokenizer st = new StringTokenizer(line);
		
		//analyze the position vector
		String string = st.nextToken();
		
		try{
			String spls = string.substring(1, string.length()-1);
			String[] arg = spls.split(",");
			P2d position = new P2d(Double.parseDouble(arg[0])*Math.pow(10, 4), Double.parseDouble(arg[1])*Math.pow(10, 4));
			System.out.println("pos_x: "+position.x+" pos_y: "+position.y);
			
			//analyze the speed vector
			string = st.nextToken();
			
			spls = string.substring(1, string.length()-1);
			arg = spls.split(",");
			V2d velocity = new V2d(Double.parseDouble(arg[0]),Double.parseDouble(arg[1]));
			System.out.println("vel_x: "+velocity.x+" vel_y: "+velocity.y);
			
			//analyze the mass
			//put that block out from try and catch because in that token 
			//there is some special characters like * and ^
			String string_massa = st.nextToken();
			System.out.println("string: "+string_massa);
			spls = string_massa.substring(string_massa.length()-2);
			int exp = Integer.parseInt(spls);
			
			spls = string_massa.substring(0,string_massa.length()-6);
			double mass = Double.parseDouble(spls);
			
			mass = mass * Math.pow(10, exp);
		
			System.out.println("massa: "+mass);
			
			Color bodyColour = Color.WHITE;
			Body agent = new Body(n_body,bodies,position,velocity,mass,bodyColour);
			bodies.add(agent);
			
			return true;
			
		} catch(NumberFormatException e){
			
			return false;
		}		
	}
	
	public void start_pressed()
	{
		universe.start_pressed();
	}
	
	public void pause_pressed()
	{
		universe.pause_pressed();
	}
	
	public void singleStep_pressed()
	{
		universe.singleStep_pressed();
	}
	
	public void stop_pressed()
	{
		universe.stop_pressed();
	}
	
	public int bodyNumber ()
	{
		return bodies.size();
	}
	
}
