package mb.projectmain.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainUI extends JFrame {
	
	private JPanel	contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				try {
					MainUI frame = new MainUI();
					frame.setVisible( true );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		} );
	}
	
	/**
	 * Create the frame.
	 */
	public MainUI () {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 1024, 768 );
		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout(new CardLayout(0, 0));
		
		BatchRunnerPanel batchRunnerPanel = new BatchRunnerPanel();
		contentPane.add(batchRunnerPanel, "name_5862644354402");
	}
	
	
	public void initialiseLogging() throws SecurityException, IOException {
		Logger LOG = Logger.getGlobal();
		LOG.setLevel( Level.ALL );
		
		FileHandler fh = new FileHandler();
		
		fh.setFormatter( new SimpleFormatter() );
		
		
		LOG.addHandler(fh );
	}
	
	
}
