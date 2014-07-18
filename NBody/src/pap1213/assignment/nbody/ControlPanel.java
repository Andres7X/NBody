package pap1213.assignment.nbody;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
    private JButton buttonStart;
    private JButton buttonStop;
    private JButton buttonPause;
    private JButton buttonSingleStep;
    private JButton buttonCreateBody;
    private JButton buttonResetBody;
    private JRadioButton radioRandom;
    private JRadioButton radioFile;
    private JFormattedTextField bodyNumber;
    private JLabel amountLabel;
    private JButton buttonLoadFile;
    private Context context;
    private JFileChooser fc;
    private boolean txtSuccess;
    private int n_body;
    
	public ControlPanel (Context ctx){
        setSize(Utility.rect.width,40);
        setLayout(new GridBagLayout());

		this.context = ctx ;
		this.txtSuccess = false;
		
        buttonStart = new JButton("Start");
        buttonStop = new JButton("Stop");
        buttonPause = new JButton("Pause");
        buttonSingleStep = new JButton("Single Step");
        
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonPause.setEnabled(false);
        buttonSingleStep.setEnabled(false);
        
        buttonCreateBody = new JButton("Create");
        buttonResetBody = new JButton("Reset");
        
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        
        buttonStart.addActionListener(this);
        buttonStop.addActionListener(this);
        buttonPause.addActionListener(this);
        buttonSingleStep.addActionListener(this);
        buttonCreateBody.addActionListener(this);
        buttonResetBody.addActionListener(this);
        
        radioRandom = new JRadioButton("Random");
        radioFile = new JRadioButton("Select File");
        buttonLoadFile = new JButton("Load File...");
        buttonLoadFile.setEnabled(false);
        bodyNumber = new JFormattedTextField();
        bodyNumber.setColumns(10);
        amountLabel = new JLabel("N-Body:");
        amountLabel.setLabelFor(bodyNumber);
        
        ButtonGroup group = new ButtonGroup();
        group.add(radioRandom);
        group.add(radioFile);
        
        radioRandom.addActionListener(this);
        radioFile.addActionListener(this);
        buttonLoadFile.addActionListener(this);
        
        add(buttonStart);
        add(buttonPause);
        add(buttonSingleStep);
        add(buttonStop);
        
        add(radioRandom);
        add(radioFile);
        add(buttonLoadFile);
        
        add(amountLabel);
        add(bodyNumber);
        add(buttonCreateBody);
        add(buttonResetBody);
        
    }

	public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src==buttonStart){
        	if (!radioRandom.isSelected() && !radioFile.isSelected())
        	{
        		JOptionPane.showMessageDialog(null, "Select one option!");
        	} else {
        		startUniverse();
        		buttonStart.setEnabled(false);
        	}
        } else if (src==buttonStop){
        	stopUniverse();
        } else if (src == buttonPause)
        {
        	pauseUniverse();
        	buttonStart.setEnabled(true);
        	
        } else if (src == buttonSingleStep)
        {
        	singleStepUniverse();
        	buttonStart.setEnabled(true);
        } else if (src == buttonLoadFile)
        {
        	int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                txtSuccess = false;
                
                //Questo codice serve per trovare l'estensione del file
                //Riesce a gestire anche il caso in cui ci siano dei . nel file o nella directory
                //in modo da recuperare sempre l'ultimo punto e prenderne l'estensione
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                int p = Math.max(file.getName().lastIndexOf('/'), file.getName().lastIndexOf('\\'));

                if (i > p) {
                    extension = file.getName().substring(i+1);
                }
                
                //Se e' diversa da txt non andiamo oltre
                if (extension.equalsIgnoreCase("txt"))
                {
                	//Carichiamo il file
                	txtSuccess = context.generateBodyFromFile(file);
                	
                	if(txtSuccess){
                		fileloaded();
                		//System.out.println("Opening: " + file.getName() + " with extension: "+extension);
                	}else{
            			JOptionPane.showMessageDialog(this, "File loaded has error: numbers are written in a wrong format.");
                	}
  
                } else {
                	
                    JOptionPane.showMessageDialog(this, "No .txt file choosen.");
                }
                
            } else {
            	//System.out.println("Open command cancelled by user.");
            }
        } else if (src == radioRandom)
        {
        	randomPressed();
        } else if (src == radioFile)
        {
        	selectFileRadio();
        } else if (src == buttonCreateBody)
        {	
        	if (!radioRandom.isSelected() && !radioFile.isSelected())
        	{
        		JOptionPane.showMessageDialog(null, "Select one option first!");
        	} else {
        		
        		if (Integer.parseInt(bodyNumber.getText()) != 0)
        		{
        			createRandomBody();
        			
        		} else {
        			JOptionPane.showMessageDialog(null, "The body number must be different from 0!");
        		}
        	}
        	
        } else if (src == buttonResetBody)
        {
        	bodyNumber.setText("");
        	radioRandom.setSelected(true);
        	buttonLoadFile.setEnabled(false);
    		amountLabel.setEnabled(true);
    		bodyNumber.setEnabled(true);
        }
	}
	
	private void fileloaded() {
		
			buttonStart.setEnabled(true);
	        buttonStop.setEnabled(true);
	        buttonPause.setEnabled(true);
	        buttonSingleStep.setEnabled(true);
	        buttonCreateBody.setEnabled(false);
			buttonResetBody.setEnabled(false);
			buttonLoadFile.setEnabled(false);
	}

	public void createRandomBody()
	{
		if (radioRandom.isSelected())
		{
			context.generateRandomBodyWithNumber(Integer.parseInt(bodyNumber.getText()));
			buttonCreateBody.setEnabled(false);
			buttonResetBody.setEnabled(false);
			radioRandom.setEnabled(false);
			radioFile.setEnabled(false);
			bodyNumber.setEditable(false);
		}
		
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(true);
        buttonPause.setEnabled(true);
        buttonSingleStep.setEnabled(true);
	}
	
	public void startUniverse()
	{
		context.start_pressed();
		txtSuccess = false;
	}
	
	public void pauseUniverse()
	{
		context.pause_pressed();
	}
	
	public void singleStepUniverse()
	{
		context.singleStep_pressed();
	}
	
	public void stopUniverse()
	{
		context.stop_pressed();
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonPause.setEnabled(false);
        buttonSingleStep.setEnabled(false);
		buttonCreateBody.setEnabled(true);
		buttonResetBody.setEnabled(true);
		radioRandom.setEnabled(true);
		radioFile.setEnabled(true);
		bodyNumber.setEditable(true);
		txtSuccess = false;
	}
	
	public void randomPressed()
	{
		buttonLoadFile.setEnabled(false);
		amountLabel.setEnabled(true);
		bodyNumber.setEnabled(true);
		n_body = randInt(2,1000);
		bodyNumber.setText(Integer.toString(n_body));
	}
	
	public void selectFileRadio()
	{
		buttonLoadFile.setEnabled(true);
		amountLabel.setEnabled(false);
		bodyNumber.setEnabled(false);
		bodyNumber.setText("");
		txtSuccess = false;
	}
	
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
