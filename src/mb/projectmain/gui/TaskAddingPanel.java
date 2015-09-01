package mb.projectmain.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import mb.projectmain.experiment.inputs.InputProviderTypes;
import mb.projectmain.experiment.params.Parameters;



public class TaskAddingPanel extends JPanel {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yy-MM-dd HHmmss");
	
	private void loadNetworkFileTo(JTextField textField) {
		fileChooser.setFileFilter( new FileNameExtensionFilter( "JSON network layout (*.json)", "json" ) );			
		int result = fileChooser.showOpenDialog( this );
		if(result == JFileChooser.APPROVE_OPTION) {
			textField.setText( fileChooser.getSelectedFile().getAbsolutePath() );
		}
	}
	
	
	private void generateOutputDir() {
		String date = SDF.format( new Date() );
		StringBuilder outputDir = new StringBuilder(date)
		.append( "_" )
		.append( txtName.getText() );
		txtOutputDir.setText( new File(outputDir.toString()).toPath().toString() );		
	}
	
	
	private Parameters createParameters() {
		Parameters params = new Parameters();
		
		params.name = txtName.getText();
		params.output_path = System.getProperty( "user.dir" ) + "/experiments/" + txtOutputDir.getText();
		
		params.gaLengthParams.total_runs = (Integer)spnTotalRuns.getValue();
		params.gaLengthParams.input_generations_per_run = (Integer)spnInputPerRun.getValue();
		params.gaLengthParams.model_generations_per_run = (Integer)spnModelPerRun.getValue();
		
		params.gaPropertiesParams.population = (Integer)spnPop.getValue();
		params.gaPropertiesParams.mutation_rate = (Float)spnMutRate.getValue();
		params.gaPropertiesParams.crossover_probability = (Float)spnPCross.getValue();
		params.gaPropertiesParams.deme_size = (Integer)spnDemeSize.getValue();
		
		params.netPropertiesParams.target_network_file = txtTargetFile.getText();
		params.netPropertiesParams.evolved_network_file = txtEvolveFile.getText();
		params.netPropertiesParams.time_step = (Float) spnTimestep.getValue();
		params.netPropertiesParams.run_length_steps = (Integer) spnRunLength.getValue();
		params.netPropertiesParams.network_input_index = (Integer) spnNetInIndex.getValue();
		params.netPropertiesParams.network_output_index = (Integer) spnNetOutIndex.getValue();
		
		params.inputProperties.input_provider = (InputProviderTypes)cmbInputSelect.getSelectedItem();
		params.inputProperties.parameters = new Object[]{(Float)spnInputParam1.getValue(),(Float)spnInputParam2.getValue()};
		
		params.modelProperties.derivative_depth = (Integer)spnModelDeriv.getValue();
		params.modelProperties.exponential_penalty = (Integer) spnExpPen.getValue();
		

		
		
		
		return params;
	}
	
	private void populateComboBox() {
		for(InputProviderTypes type : InputProviderTypes.values()) {
			cmbInputSelect.addItem( type );
		}
	}
	
	private void loadComboParams(InputProviderTypes selectedItem) {
		lblInputParam.setText( selectedItem.getParameters().get( 0 ) );
		lblInputParam_1.setText( selectedItem.getParameters().get( 1 ) );
	}
	
	public void registerAddTaskCallBack(AddTaskCallBack addTaskCallBack) {
		this.addTaskCallBack = addTaskCallBack;
	}
	
	private AddTaskCallBack addTaskCallBack;
	
	private JFileChooser fileChooser;
	
	
	private JTextField txtOutputDir;
	private JTextField txtName;
	private JTextField txtTargetFile;
	private JTextField txtEvolveFile;
	private JSpinner spnTotalRuns;
	private JSpinner spnInputPerRun;
	private JSpinner spnModelPerRun;
	private JSpinner spnPop;
	private JSpinner spnMutRate;
	private JSpinner spnPCross;
	private JSpinner spnDemeSize;
	private JSpinner spnTimestep;
	private JSpinner spnRunLength;
	private JSpinner spnNetInIndex;
	private JSpinner spnNetOutIndex;
	private JSpinner spnModelDeriv;
	private JSpinner spnExpPen;
	private JComboBox cmbInputSelect;
	private JSpinner spnInputParam1;
	private JSpinner spnInputParam2;
	private JLabel lblInputParam;
	private JLabel lblInputParam_1;
	
	/**
	 * Create the panel.
	 */
	public TaskAddingPanel () {
		setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(panel, "name_14308866361676");
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label = new JLabel("Name");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.weighty = 0.1;
		gbc_label.ipady = 1;
		gbc_label.ipadx = 1;
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(5, 5, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		fileChooser = new JFileChooser( System.getProperty( "user.dir" ) + "/experiments/");
		
		
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				generateOutputDir();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				generateOutputDir();
			}
		});
		txtName.setColumns(10);
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.gridwidth = 2;
		gbc_txtName.anchor = GridBagConstraints.WEST;
		gbc_txtName.weighty = 0.1;
		gbc_txtName.ipadx = 1;
		gbc_txtName.ipady = 1;
		gbc_txtName.weightx = 0.75;
		gbc_txtName.insets = new Insets(5, 5, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		panel.add(txtName, gbc_txtName);
		
		JLabel lblOutput = new JLabel("Output directory");
		GridBagConstraints gbc_lblOutput = new GridBagConstraints();
		gbc_lblOutput.fill = GridBagConstraints.BOTH;
		gbc_lblOutput.weighty = 0.1;
		gbc_lblOutput.anchor = GridBagConstraints.WEST;
		gbc_lblOutput.insets = new Insets(5, 5, 5, 5);
		gbc_lblOutput.gridx = 0;
		gbc_lblOutput.gridy = 1;
		panel.add(lblOutput, gbc_lblOutput);
		
		txtOutputDir = new JTextField();
		GridBagConstraints gbc_txtOutputDir = new GridBagConstraints();
		gbc_txtOutputDir.gridwidth = 2;
		gbc_txtOutputDir.insets = new Insets(5, 5, 5, 0);
		gbc_txtOutputDir.anchor = GridBagConstraints.WEST;
		gbc_txtOutputDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtOutputDir.gridx = 1;
		gbc_txtOutputDir.gridy = 1;
		panel.add(txtOutputDir, gbc_txtOutputDir);
		txtOutputDir.setColumns(10);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weighty = 0.1;
		gbc_separator.gridwidth = 3;
		gbc_separator.insets = new Insets(5, 5, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		panel.add(separator, gbc_separator);
		
		JLabel lblTotalRuns = new JLabel("Total runs");
		GridBagConstraints gbc_lblTotalRuns = new GridBagConstraints();
		gbc_lblTotalRuns.weighty = 0.1;
		gbc_lblTotalRuns.anchor = GridBagConstraints.WEST;
		gbc_lblTotalRuns.insets = new Insets(5, 5, 5, 5);
		gbc_lblTotalRuns.gridx = 0;
		gbc_lblTotalRuns.gridy = 3;
		panel.add(lblTotalRuns, gbc_lblTotalRuns);
		
		spnTotalRuns = new JSpinner();
		spnTotalRuns.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(10)));
		GridBagConstraints gbc_spnTotalRuns = new GridBagConstraints();
		gbc_spnTotalRuns.ipadx = 20;
		gbc_spnTotalRuns.anchor = GridBagConstraints.WEST;
		gbc_spnTotalRuns.gridwidth = 2;
		gbc_spnTotalRuns.insets = new Insets(5, 5, 5, 0);
		gbc_spnTotalRuns.gridx = 1;
		gbc_spnTotalRuns.gridy = 3;
		panel.add(spnTotalRuns, gbc_spnTotalRuns);
		
		JLabel lblInputGenPer = new JLabel("Input gen per run");
		GridBagConstraints gbc_lblInputGenPer = new GridBagConstraints();
		gbc_lblInputGenPer.weighty = 0.1;
		gbc_lblInputGenPer.anchor = GridBagConstraints.WEST;
		gbc_lblInputGenPer.insets = new Insets(5, 5, 5, 5);
		gbc_lblInputGenPer.gridx = 0;
		gbc_lblInputGenPer.gridy = 4;
		panel.add(lblInputGenPer, gbc_lblInputGenPer);
		
		spnInputPerRun = new JSpinner();
		spnInputPerRun.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spnInputPerRun = new GridBagConstraints();
		gbc_spnInputPerRun.ipadx = 20;
		gbc_spnInputPerRun.anchor = GridBagConstraints.WEST;
		gbc_spnInputPerRun.gridwidth = 2;
		gbc_spnInputPerRun.insets = new Insets(5, 5, 5, 0);
		gbc_spnInputPerRun.gridx = 1;
		gbc_spnInputPerRun.gridy = 4;
		panel.add(spnInputPerRun, gbc_spnInputPerRun);
		
		JLabel lblModelGenPer = new JLabel("Model gen per run");
		GridBagConstraints gbc_lblModelGenPer = new GridBagConstraints();
		gbc_lblModelGenPer.weighty = 0.1;
		gbc_lblModelGenPer.anchor = GridBagConstraints.WEST;
		gbc_lblModelGenPer.insets = new Insets(5, 5, 5, 5);
		gbc_lblModelGenPer.gridx = 0;
		gbc_lblModelGenPer.gridy = 5;
		panel.add(lblModelGenPer, gbc_lblModelGenPer);
		
		spnModelPerRun = new JSpinner();
		spnModelPerRun.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spnModelPerRun = new GridBagConstraints();
		gbc_spnModelPerRun.ipadx = 20;
		gbc_spnModelPerRun.anchor = GridBagConstraints.WEST;
		gbc_spnModelPerRun.gridwidth = 2;
		gbc_spnModelPerRun.insets = new Insets(5, 5, 5, 0);
		gbc_spnModelPerRun.gridx = 1;
		gbc_spnModelPerRun.gridy = 5;
		panel.add(spnModelPerRun, gbc_spnModelPerRun);
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.weighty = 0.1;
		gbc_separator_1.insets = new Insets(5, 5, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 6;
		panel.add(separator_1, gbc_separator_1);
		
		JLabel lblGaPopulation = new JLabel("GA Population");
		GridBagConstraints gbc_lblGaPopulation = new GridBagConstraints();
		gbc_lblGaPopulation.weighty = 0.1;
		gbc_lblGaPopulation.anchor = GridBagConstraints.WEST;
		gbc_lblGaPopulation.insets = new Insets(5, 5, 5, 5);
		gbc_lblGaPopulation.gridx = 0;
		gbc_lblGaPopulation.gridy = 7;
		panel.add(lblGaPopulation, gbc_lblGaPopulation);
		
		spnPop = new JSpinner();
		spnPop.setModel(new SpinnerNumberModel(new Integer(100), new Integer(2), null, new Integer(1)));
		GridBagConstraints gbc_spnPop = new GridBagConstraints();
		gbc_spnPop.ipadx = 20;
		gbc_spnPop.anchor = GridBagConstraints.WEST;
		gbc_spnPop.gridwidth = 2;
		gbc_spnPop.insets = new Insets(5, 5, 5, 0);
		gbc_spnPop.gridx = 1;
		gbc_spnPop.gridy = 7;
		panel.add(spnPop, gbc_spnPop);
		
		JLabel lblGaMutationRate = new JLabel("GA Mutation rate");
		GridBagConstraints gbc_lblGaMutationRate = new GridBagConstraints();
		gbc_lblGaMutationRate.weighty = 0.1;
		gbc_lblGaMutationRate.anchor = GridBagConstraints.WEST;
		gbc_lblGaMutationRate.insets = new Insets(5, 5, 5, 5);
		gbc_lblGaMutationRate.gridx = 0;
		gbc_lblGaMutationRate.gridy = 8;
		panel.add(lblGaMutationRate, gbc_lblGaMutationRate);
		
		spnMutRate = new JSpinner();
		spnMutRate.setModel(new SpinnerNumberModel(new Float(0.4), new Float(0), null, new Float(0.1)));
		GridBagConstraints gbc_spnMutRate = new GridBagConstraints();
		gbc_spnMutRate.ipadx = 20;
		gbc_spnMutRate.anchor = GridBagConstraints.WEST;
		gbc_spnMutRate.gridwidth = 2;
		gbc_spnMutRate.insets = new Insets(5, 5, 5, 0);
		gbc_spnMutRate.gridx = 1;
		gbc_spnMutRate.gridy = 8;
		panel.add(spnMutRate, gbc_spnMutRate);
		
		JLabel lblGaCrossoverProbability = new JLabel("GA Crossover prob");
		GridBagConstraints gbc_lblGaCrossoverProbability = new GridBagConstraints();
		gbc_lblGaCrossoverProbability.weighty = 0.1;
		gbc_lblGaCrossoverProbability.anchor = GridBagConstraints.WEST;
		gbc_lblGaCrossoverProbability.insets = new Insets(5, 5, 5, 5);
		gbc_lblGaCrossoverProbability.gridx = 0;
		gbc_lblGaCrossoverProbability.gridy = 9;
		panel.add(lblGaCrossoverProbability, gbc_lblGaCrossoverProbability);
		
		spnPCross = new JSpinner();
		spnPCross.setModel(new SpinnerNumberModel(new Float(0.05), new Float(0), new Float(1), new Float(0.05)));
		GridBagConstraints gbc_spnPCross = new GridBagConstraints();
		gbc_spnPCross.ipadx = 20;
		gbc_spnPCross.anchor = GridBagConstraints.WEST;
		gbc_spnPCross.gridwidth = 2;
		gbc_spnPCross.insets = new Insets(5, 5, 5, 0);
		gbc_spnPCross.gridx = 1;
		gbc_spnPCross.gridy = 9;
		panel.add(spnPCross, gbc_spnPCross);
		
		JLabel lblGaDemeSize = new JLabel("GA Deme Size");
		GridBagConstraints gbc_lblGaDemeSize = new GridBagConstraints();
		gbc_lblGaDemeSize.weighty = 0.1;
		gbc_lblGaDemeSize.anchor = GridBagConstraints.WEST;
		gbc_lblGaDemeSize.insets = new Insets(5, 5, 5, 5);
		gbc_lblGaDemeSize.gridx = 0;
		gbc_lblGaDemeSize.gridy = 10;
		panel.add(lblGaDemeSize, gbc_lblGaDemeSize);
		
		spnDemeSize = new JSpinner();
		spnDemeSize.setModel(new SpinnerNumberModel(new Integer(5), new Integer(2), null, new Integer(1)));
		GridBagConstraints gbc_spnDemeSize = new GridBagConstraints();
		gbc_spnDemeSize.ipadx = 20;
		gbc_spnDemeSize.anchor = GridBagConstraints.WEST;
		gbc_spnDemeSize.gridwidth = 2;
		gbc_spnDemeSize.insets = new Insets(5, 5, 5, 0);
		gbc_spnDemeSize.gridx = 1;
		gbc_spnDemeSize.gridy = 10;
		panel.add(spnDemeSize, gbc_spnDemeSize);
		
		JSeparator separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 3;
		gbc_separator_2.weighty = 0.1;
		gbc_separator_2.insets = new Insets(5, 5, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 11;
		panel.add(separator_2, gbc_separator_2);
		
		JLabel lblTargetNetworkFile = new JLabel("Target network file");
		GridBagConstraints gbc_lblTargetNetworkFile = new GridBagConstraints();
		gbc_lblTargetNetworkFile.weighty = 0.1;
		gbc_lblTargetNetworkFile.anchor = GridBagConstraints.WEST;
		gbc_lblTargetNetworkFile.insets = new Insets(5, 5, 5, 5);
		gbc_lblTargetNetworkFile.gridx = 0;
		gbc_lblTargetNetworkFile.gridy = 12;
		panel.add(lblTargetNetworkFile, gbc_lblTargetNetworkFile);
		
		txtTargetFile = new JTextField(System.getProperty( "user.dir" ) + "/fixednetwork.json");
		GridBagConstraints gbc_txtTargetFile = new GridBagConstraints();
		gbc_txtTargetFile.insets = new Insets(5, 5, 5, 5);
		gbc_txtTargetFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTargetFile.gridx = 1;
		gbc_txtTargetFile.gridy = 12;
		panel.add(txtTargetFile, gbc_txtTargetFile);
		txtTargetFile.setColumns(10);
		
		JButton btnOpenTargetNet = new JButton("...");
		btnOpenTargetNet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadNetworkFileTo( txtTargetFile );
			}
		});
		GridBagConstraints gbc_btnOpenTargetNet = new GridBagConstraints();
		gbc_btnOpenTargetNet.weighty = 0.1;
		gbc_btnOpenTargetNet.insets = new Insets(5, 5, 5, 0);
		gbc_btnOpenTargetNet.gridx = 2;
		gbc_btnOpenTargetNet.gridy = 12;
		panel.add(btnOpenTargetNet, gbc_btnOpenTargetNet);
		
		JLabel lblEvolvableNetworkFile = new JLabel("Evolvable network file");
		GridBagConstraints gbc_lblEvolvableNetworkFile = new GridBagConstraints();
		gbc_lblEvolvableNetworkFile.weighty = 0.1;
		gbc_lblEvolvableNetworkFile.anchor = GridBagConstraints.WEST;
		gbc_lblEvolvableNetworkFile.insets = new Insets(5, 5, 5, 5);
		gbc_lblEvolvableNetworkFile.gridx = 0;
		gbc_lblEvolvableNetworkFile.gridy = 13;
		panel.add(lblEvolvableNetworkFile, gbc_lblEvolvableNetworkFile);
		
		txtEvolveFile = new JTextField(System.getProperty( "user.dir" ) + "/evolvablenetwork.json");
		GridBagConstraints gbc_txtEvolveFile = new GridBagConstraints();
		gbc_txtEvolveFile.insets = new Insets(5, 5, 5, 5);
		gbc_txtEvolveFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEvolveFile.gridx = 1;
		gbc_txtEvolveFile.gridy = 13;
		panel.add(txtEvolveFile, gbc_txtEvolveFile);
		txtEvolveFile.setColumns(10);
		
		JButton btnOpenEvolveNet = new JButton("...");
		btnOpenEvolveNet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadNetworkFileTo( txtEvolveFile );
			}
		});
		GridBagConstraints gbc_btnOpenEvolveNet = new GridBagConstraints();
		gbc_btnOpenEvolveNet.insets = new Insets(5, 5, 5, 0);
		gbc_btnOpenEvolveNet.gridx = 2;
		gbc_btnOpenEvolveNet.gridy = 13;
		panel.add(btnOpenEvolveNet, gbc_btnOpenEvolveNet);
		
		JLabel lblTimestep = new JLabel("Timestep");
		GridBagConstraints gbc_lblTimestep = new GridBagConstraints();
		gbc_lblTimestep.weighty = 0.1;
		gbc_lblTimestep.anchor = GridBagConstraints.WEST;
		gbc_lblTimestep.insets = new Insets(5, 5, 5, 5);
		gbc_lblTimestep.gridx = 0;
		gbc_lblTimestep.gridy = 14;
		panel.add(lblTimestep, gbc_lblTimestep);
		
		spnTimestep = new JSpinner();
		spnTimestep.setModel(new SpinnerNumberModel(new Float(0.01), new Float(0), null, new Float(0.1)));
		GridBagConstraints gbc_spnTimestep = new GridBagConstraints();
		gbc_spnTimestep.ipadx = 20;
		gbc_spnTimestep.anchor = GridBagConstraints.WEST;
		gbc_spnTimestep.gridwidth = 2;
		gbc_spnTimestep.insets = new Insets(5, 5, 5, 0);
		gbc_spnTimestep.gridx = 1;
		gbc_spnTimestep.gridy = 14;
		panel.add(spnTimestep, gbc_spnTimestep);
		
		JLabel lblRunLength = new JLabel("Run length");
		GridBagConstraints gbc_lblRunLength = new GridBagConstraints();
		gbc_lblRunLength.weighty = 0.1;
		gbc_lblRunLength.anchor = GridBagConstraints.WEST;
		gbc_lblRunLength.insets = new Insets(5, 5, 5, 5);
		gbc_lblRunLength.gridx = 0;
		gbc_lblRunLength.gridy = 15;
		panel.add(lblRunLength, gbc_lblRunLength);
		
		spnRunLength = new JSpinner();
		spnRunLength.setModel(new SpinnerNumberModel(new Integer(500), new Integer(1), null, new Integer(100)));
		GridBagConstraints gbc_spnRunLength = new GridBagConstraints();
		gbc_spnRunLength.ipadx = 20;
		gbc_spnRunLength.anchor = GridBagConstraints.WEST;
		gbc_spnRunLength.gridwidth = 2;
		gbc_spnRunLength.insets = new Insets(5, 5, 5, 0);
		gbc_spnRunLength.gridx = 1;
		gbc_spnRunLength.gridy = 15;
		panel.add(spnRunLength, gbc_spnRunLength);
		
		JLabel lblNetworkInputIndex = new JLabel("Network input index");
		GridBagConstraints gbc_lblNetworkInputIndex = new GridBagConstraints();
		gbc_lblNetworkInputIndex.weighty = 0.1;
		gbc_lblNetworkInputIndex.anchor = GridBagConstraints.WEST;
		gbc_lblNetworkInputIndex.insets = new Insets(5, 5, 5, 5);
		gbc_lblNetworkInputIndex.gridx = 0;
		gbc_lblNetworkInputIndex.gridy = 16;
		panel.add(lblNetworkInputIndex, gbc_lblNetworkInputIndex);
		
		spnNetInIndex = new JSpinner();
		spnNetInIndex.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spnNetInIndex = new GridBagConstraints();
		gbc_spnNetInIndex.ipadx = 20;
		gbc_spnNetInIndex.anchor = GridBagConstraints.WEST;
		gbc_spnNetInIndex.gridwidth = 2;
		gbc_spnNetInIndex.insets = new Insets(5, 5, 5, 0);
		gbc_spnNetInIndex.gridx = 1;
		gbc_spnNetInIndex.gridy = 16;
		panel.add(spnNetInIndex, gbc_spnNetInIndex);
		
		JLabel lblNetworkOutputIndex = new JLabel("Network output index");
		GridBagConstraints gbc_lblNetworkOutputIndex = new GridBagConstraints();
		gbc_lblNetworkOutputIndex.weighty = 0.1;
		gbc_lblNetworkOutputIndex.anchor = GridBagConstraints.WEST;
		gbc_lblNetworkOutputIndex.insets = new Insets(5, 5, 5, 5);
		gbc_lblNetworkOutputIndex.gridx = 0;
		gbc_lblNetworkOutputIndex.gridy = 17;
		panel.add(lblNetworkOutputIndex, gbc_lblNetworkOutputIndex);
		
		spnNetOutIndex = new JSpinner();
		spnNetOutIndex.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spnNetOutIndex = new GridBagConstraints();
		gbc_spnNetOutIndex.ipadx = 20;
		gbc_spnNetOutIndex.anchor = GridBagConstraints.WEST;
		gbc_spnNetOutIndex.gridwidth = 2;
		gbc_spnNetOutIndex.insets = new Insets(5, 5, 5, 0);
		gbc_spnNetOutIndex.gridx = 1;
		gbc_spnNetOutIndex.gridy = 17;
		panel.add(spnNetOutIndex, gbc_spnNetOutIndex);
		
		JSeparator separator_3 = new JSeparator();
		GridBagConstraints gbc_separator_3 = new GridBagConstraints();
		gbc_separator_3.gridwidth = 3;
		gbc_separator_3.weighty = 0.1;
		gbc_separator_3.insets = new Insets(5, 5, 5, 0);
		gbc_separator_3.gridx = 0;
		gbc_separator_3.gridy = 18;
		panel.add(separator_3, gbc_separator_3);
		
		JLabel lblModelDerivativeDepth = new JLabel("Model derivative depth");
		GridBagConstraints gbc_lblModelDerivativeDepth = new GridBagConstraints();
		gbc_lblModelDerivativeDepth.weighty = 0.1;
		gbc_lblModelDerivativeDepth.anchor = GridBagConstraints.WEST;
		gbc_lblModelDerivativeDepth.insets = new Insets(5, 5, 5, 5);
		gbc_lblModelDerivativeDepth.gridx = 0;
		gbc_lblModelDerivativeDepth.gridy = 19;
		panel.add(lblModelDerivativeDepth, gbc_lblModelDerivativeDepth);
		
		spnModelDeriv = new JSpinner();
		spnModelDeriv.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spnModelDeriv = new GridBagConstraints();
		gbc_spnModelDeriv.ipadx = 20;
		gbc_spnModelDeriv.anchor = GridBagConstraints.WEST;
		gbc_spnModelDeriv.gridwidth = 2;
		gbc_spnModelDeriv.insets = new Insets(5, 5, 5, 0);
		gbc_spnModelDeriv.gridx = 1;
		gbc_spnModelDeriv.gridy = 19;
		panel.add(spnModelDeriv, gbc_spnModelDeriv);
		
		JLabel lblModelExponentialPenalty = new JLabel("Model exponential penalty");
		GridBagConstraints gbc_lblModelExponentialPenalty = new GridBagConstraints();
		gbc_lblModelExponentialPenalty.anchor = GridBagConstraints.WEST;
		gbc_lblModelExponentialPenalty.insets = new Insets(5, 5, 5, 5);
		gbc_lblModelExponentialPenalty.gridx = 0;
		gbc_lblModelExponentialPenalty.gridy = 20;
		panel.add(lblModelExponentialPenalty, gbc_lblModelExponentialPenalty);
		
		spnExpPen = new JSpinner();
		spnExpPen.setModel(new SpinnerNumberModel(new Integer(2), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spnExpPen = new GridBagConstraints();
		gbc_spnExpPen.ipadx = 20;
		gbc_spnExpPen.anchor = GridBagConstraints.WEST;
		gbc_spnExpPen.gridwidth = 2;
		gbc_spnExpPen.insets = new Insets(5, 5, 5, 0);
		gbc_spnExpPen.gridx = 1;
		gbc_spnExpPen.gridy = 20;
		panel.add(spnExpPen, gbc_spnExpPen);
		
		JSeparator separator_4 = new JSeparator();
		GridBagConstraints gbc_separator_4 = new GridBagConstraints();
		gbc_separator_4.weighty = 0.1;
		gbc_separator_4.insets = new Insets(5, 5, 5, 0);
		gbc_separator_4.gridwidth = 3;
		gbc_separator_4.gridx = 0;
		gbc_separator_4.gridy = 21;
		panel.add(separator_4, gbc_separator_4);
		
		JLabel lblInputType = new JLabel("Input type");
		GridBagConstraints gbc_lblInputType = new GridBagConstraints();
		gbc_lblInputType.weighty = 0.1;
		gbc_lblInputType.anchor = GridBagConstraints.WEST;
		gbc_lblInputType.insets = new Insets(5, 5, 5, 5);
		gbc_lblInputType.gridx = 0;
		gbc_lblInputType.gridy = 22;
		panel.add(lblInputType, gbc_lblInputType);
		
		cmbInputSelect = new JComboBox();
		cmbInputSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == ItemEvent.SELECTED){
					loadComboParams( (InputProviderTypes) arg0.getItem() );					
				}
			}
		});
		GridBagConstraints gbc_cmbInputSelect = new GridBagConstraints();
		gbc_cmbInputSelect.gridwidth = 2;
		gbc_cmbInputSelect.insets = new Insets(5, 5, 5, 0);
		gbc_cmbInputSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbInputSelect.gridx = 1;
		gbc_cmbInputSelect.gridy = 22;
		panel.add(cmbInputSelect, gbc_cmbInputSelect);
		
		lblInputParam = new JLabel("Input param 1");
		GridBagConstraints gbc_lblInputParam = new GridBagConstraints();
		gbc_lblInputParam.weighty = 0.1;
		gbc_lblInputParam.anchor = GridBagConstraints.WEST;
		gbc_lblInputParam.insets = new Insets(5, 5, 5, 5);
		gbc_lblInputParam.gridx = 0;
		gbc_lblInputParam.gridy = 23;
		panel.add(lblInputParam, gbc_lblInputParam);
		
		spnInputParam1 = new JSpinner();
		spnInputParam1.setModel(new SpinnerNumberModel(new Float(0.25f), new Float(0), null, new Float(1)));
		GridBagConstraints gbc_spnInputParam1 = new GridBagConstraints();
		gbc_spnInputParam1.ipadx = 20;
		gbc_spnInputParam1.anchor = GridBagConstraints.WEST;
		gbc_spnInputParam1.insets = new Insets(5, 5, 5, 5);
		gbc_spnInputParam1.gridx = 1;
		gbc_spnInputParam1.gridy = 23;
		panel.add(spnInputParam1, gbc_spnInputParam1);
		
		spnInputParam2 = new JSpinner();
		spnInputParam2.setModel(new SpinnerNumberModel(new Float(2), new Float(0), null, new Float(1)));
		GridBagConstraints gbc_spnInputParam2 = new GridBagConstraints();
		gbc_spnInputParam2.ipadx = 20;
		gbc_spnInputParam2.anchor = GridBagConstraints.WEST;
		gbc_spnInputParam2.insets = new Insets(5, 5, 5, 5);
		gbc_spnInputParam2.gridx = 1;
		gbc_spnInputParam2.gridy = 24;
		panel.add(spnInputParam2, gbc_spnInputParam2);
		
		lblInputParam_1 = new JLabel("Input param 2");
		GridBagConstraints gbc_lblInputParam_1 = new GridBagConstraints();
		gbc_lblInputParam_1.weighty = 0.1;
		gbc_lblInputParam_1.anchor = GridBagConstraints.WEST;
		gbc_lblInputParam_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblInputParam_1.gridx = 0;
		gbc_lblInputParam_1.gridy = 24;
		panel.add(lblInputParam_1, gbc_lblInputParam_1);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(addTaskCallBack != null) {
					addTaskCallBack.addTask( createParameters() );
				}
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.anchor = GridBagConstraints.EAST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 25;
		panel.add(btnAdd, gbc_btnAdd);
		
		generateOutputDir();
		
		populateComboBox() ;
		cmbInputSelect.setSelectedItem( InputProviderTypes.SINUSOIDAL );
				
		btnAdd.requestFocusInWindow();
		
	}
	
	public static interface AddTaskCallBack {
		public void addTask(Parameters parameters);
	}
	
}
