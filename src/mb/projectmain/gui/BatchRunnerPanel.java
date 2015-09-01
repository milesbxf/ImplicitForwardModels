package mb.projectmain.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.projectmain.experiment.ExperimentTask;
import mb.projectmain.experiment.params.Parameters;
import mb.projectmain.gui.TaskAddingPanel.AddTaskCallBack;
import mb.projectmain.tasks.TaskFuture;
import mb.projectmain.tasks.TaskRunner;



public class BatchRunnerPanel extends JPanel implements AddTaskCallBack {
	
	private TaskRunner taskRunner;
	private TasksListPanel tasksListPanel;
	
	
	/**
	 * Create the panel.
	 */
	public BatchRunnerPanel () {
		taskRunner = TaskRunner.withThreads( 8 );
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		TaskAddingPanel taskAddingPanel = new TaskAddingPanel();
		GridBagConstraints gbc_taskAddingPanel = new GridBagConstraints();
		gbc_taskAddingPanel.anchor = GridBagConstraints.WEST;
		gbc_taskAddingPanel.fill = GridBagConstraints.BOTH;
		gbc_taskAddingPanel.weightx = 0.1;
		gbc_taskAddingPanel.ipady = 1;
		gbc_taskAddingPanel.ipadx = 1;
		gbc_taskAddingPanel.insets = new Insets(15, 15, 1, 15);
		gbc_taskAddingPanel.gridx = 0;
		gbc_taskAddingPanel.gridy = 0;
		add(taskAddingPanel, gbc_taskAddingPanel);
		
		taskAddingPanel.registerAddTaskCallBack( this );
		
		
		tasksListPanel = new TasksListPanel();
		GridBagConstraints gbc_tasksListPanel = new GridBagConstraints();
		gbc_tasksListPanel.insets = new Insets(15, 15, 15, 15);
		gbc_tasksListPanel.anchor = GridBagConstraints.WEST;
		gbc_tasksListPanel.ipady = 1;
		gbc_tasksListPanel.ipadx = 1;
		gbc_tasksListPanel.fill = GridBagConstraints.BOTH;
		gbc_tasksListPanel.weightx = 1.0;
		gbc_tasksListPanel.gridx = 1;
		gbc_tasksListPanel.gridy = 0;
		add(tasksListPanel, gbc_tasksListPanel);
		
		
	}
	
	@Override
	public void addTask( Parameters parameters ) {
		try {
			TaskFuture task = taskRunner.addTask( new ExperimentTask(parameters) );
			tasksListPanel.addTask( task );			
		} catch ( Exception e ) {
			JOptionPane.showMessageDialog( this, "An error occurred. Details:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}		
	}
}
