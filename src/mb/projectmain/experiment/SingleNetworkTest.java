package mb.projectmain.experiment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import mb.ctrnn.core.LoggableCTRNN;
import mb.ctrnn.json.Network;
import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.ctrnn.layout.CTRNNLayout;
import mb.datalog.DataLogger;
import mb.evolution.Genotype;
import mb.projectmain.experiment.inputs.InputProvider;
import mb.projectmain.experiment.inputs.InputProviderTypes;

/**
 * Simulates a single network and saves the output and inputs to a CSV file for
 * further analysis. For testing new layouts.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class SingleNetworkTest {
	
	/**
	 * Asks for a file to run, loads it as a CTRNN and simulates it, saving
	 * output and input to a CSV file under the singleNets folder.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @throws FileNotFoundException If file given is not found.
	 * @throws JSONNetworkParseError If file given is not a valid JSON network file.
	 * @throws IOException If another IO error occurs.
	 */
	public static void main( String[] args ) throws FileNotFoundException, JSONNetworkParseError, IOException {
		String selectedFile = chooseFile();
		
		CTRNNLayout layout = Network.fromInputStream( new FileInputStream( selectedFile ) ).getLayout();
		layout.updateGenes( Genotype.withRandomGenome( layout.getNumberOfGenes() ).getGenes() );
		
		LoggableCTRNN net = new LoggableCTRNN( layout.createCTRNN() );
		
		DataLogger logger = new DataLogger( net );
		
		// can alter input type here
		
		InputProvider inputprovider = InputProviderTypes.SINUSOIDAL.createNewInstance( 0.25f, 1f );
		// set up input with random parameters.
		inputprovider.setParams( Genotype.withRandomGenome( inputprovider.getNumParams() ).getGenes() );
		
//		run CTRNN simulation
		for ( int t = 0; t < 500; t++ ) {
			
			float[] input = new float[5];
			input[0] = inputprovider.getInput( t );
			
			net.step( 0.01f, input );
			
			logger.appendData();
		}
		
		String date = new SimpleDateFormat( "yy-MM-dd HHmmss" ).format( new Date() );
		
		String expDir = System.getProperty( "user.dir" ) + "/singleNets/";
		new File( expDir ).mkdirs();
		
		
//		save all to CSV
		logger.writeToCSVFile( new File( expDir + date + " net.csv" ) );
	}
	
	private static String chooseFile() {
		JFileChooser chooser = new JFileChooser( System.getProperty( "user.dir" ) + "/experiments/" );
		
		chooser.setDialogTitle( "Select a .json with the evolved network" );
		chooser.setFileFilter( new FileNameExtensionFilter( "JSON files", "json" ) );
		
		int result = chooser.showOpenDialog( null );
		if ( result != JFileChooser.APPROVE_OPTION ) {
			System.exit( 0 );
		}
		return chooser.getSelectedFile().getAbsolutePath();
	}
	
}
