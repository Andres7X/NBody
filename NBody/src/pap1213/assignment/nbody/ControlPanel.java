package pap1213.assignment.nbody;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

public class ControlPanel extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 7526472295622776147L;
	
    private JButton buttonStart;
    private JButton buttonStop;
    private JButton buttonPause;
    private JButton buttonSingleStep;
    private JRadioButton radioRandom;
    private JRadioButton radioFile;
    private JFormattedTextField bodyNumber;
    private JLabel amountLabel;
    private JButton buttonLoadFile;
    private Context context;
    private JFileChooser fc;
    
	//Usare JPanel listPane = new JPanel();
    
	public ControlPanel (Context ctx)
	{
		this.context = ctx ;
        setTitle("Control Panel");
        setSize(400,200);
        setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});
		
        buttonStart = new JButton("Start");
        buttonStop = new JButton("Stop");
        buttonPause = new JButton("Pause");
        buttonSingleStep = new JButton("Single Step");
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        
        JPanel p = new JPanel();
        //p.setLayout(new GridBagLayout());
        p.add(buttonStart);
        p.add(buttonStop);
        p.add(buttonPause);
        p.add(buttonSingleStep);
        buttonStart.addActionListener(this);
        buttonStop.addActionListener(this);
        buttonPause.addActionListener(this);
        buttonSingleStep.addActionListener(this);
        
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
        
        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        p2.add(radioRandom);
        p2.add(radioFile);
        p2.add(buttonLoadFile);
        radioRandom.addActionListener(this);
        radioFile.addActionListener(this);
        buttonLoadFile.addActionListener(this);
        
        JPanel p3 = new JPanel();
        p3.setLayout(new GridBagLayout());
        p3.add(amountLabel);
        p3.add(bodyNumber);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(p);
        panel.add(new JSeparator(SwingConstants.HORIZONTAL));
        panel.add(p2);
        panel.add(p3);
        
        getContentPane().add(panel);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
	
	public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src==buttonStart){
        	System.out.println("Start button");
        	if (!radioRandom.isSelected() && !radioFile.isSelected())
        	{
        		JOptionPane.showMessageDialog(null, "Select one option!");
        	} else {
        		startUniverse();
        	}
        } else if (src==buttonStop){
        	System.out.println("Stop button");
        } else if (src == buttonPause)
        {
        	System.out.println("Pause button");
        } else if (src == buttonSingleStep)
        {
        	System.out.println("Single Step button");
        } else if (src == buttonLoadFile)
        {
        	System.out.println("Load File button");
        	int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
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
                    System.out.println("Opening: " + file.getName() + " with extension: "+extension);
                } else {
                	
                    JOptionPane.showMessageDialog(this, "No .txt file choosen.");
                }
                
            } else {
            	System.out.println("Open command cancelled by user.");
            }
        } else if (src == radioRandom)
        {
        	System.out.println("Random Radio Pressed");
        	randomPressed();
        } else if (src == radioFile)
        {
        	System.out.println("File Radio Pressed");
        	selectFileRadio();
        }
	}
	
	public void startUniverse()
	{
		if (radioRandom.isSelected())
		{
			context.generateRandomBodyWithNumber(Integer.parseInt(bodyNumber.getText()));
			
		}
	}
	
	public void randomPressed()
	{
		buttonLoadFile.setEnabled(false);
		amountLabel.setEnabled(true);
		bodyNumber.setEnabled(true);
		int n_body = randInt(2,100);
		bodyNumber.setText(Integer.toString(n_body));
	}
	
	public void selectFileRadio()
	{
		buttonLoadFile.setEnabled(true);
		amountLabel.setEnabled(false);
		bodyNumber.setEnabled(false);
		bodyNumber.setText("");
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
