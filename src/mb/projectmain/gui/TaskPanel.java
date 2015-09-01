package mb.projectmain.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import mb.projectmain.experiment.ExperimentTask.ExperimentUserInfo;
import mb.projectmain.gui.TasksListPanel.SelectionCallback;
import mb.projectmain.tasks.TaskFuture;

public class TaskPanel extends JPanel {
	
	private final TaskFuture taskFuture;
	private JLabel lblName;
	private JProgressBar progressBar;
	private JButton btnStop;
	private SelectionCallback callback;
	
	private boolean selected = false;
	private Timer t;
	
	void registerSelectionCallback(SelectionCallback callback) {
		this.callback = callback;
	}
	
	/**
	 * Create the panel.
	 */
	public TaskPanel (final TaskFuture taskFuture) {
		final TaskPanel thisTask = this;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(callback != null) {
					callback.doSelection( thisTask );
				}
			}
		});
		
		this.taskFuture = taskFuture;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{40, 148, 67, 0};
		gridBagLayout.rowHeights = new int[]{25, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblName.weightx = 0.25;
		gbc_lblName.insets = new Insets(5, 5, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblName, gbc_lblName);
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.weightx = 0.5;
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(5, 5, 5, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 0;
		add(progressBar, gbc_progressBar);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				taskFuture.getTask().cancel();
				btnStop.setEnabled( false );
			}
		});
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStop.insets = new Insets(5, 5, 5, 5);
		gbc_btnStop.weightx = 0.25;
		gbc_btnStop.gridx = 2;
		gbc_btnStop.gridy = 0;
		add(btnStop, gbc_btnStop);

		
		TimerTask reval = new TimerTask(){

			@Override
			public void run() {
				repaint();
			}
			
		};
		Timer t = new Timer();
		t.schedule( reval, 1, 1);
		

		ExperimentUserInfo info = (ExperimentUserInfo) taskFuture.getTask().getUserInformation();
		
		lblName.setText( info.name );
		
	}
	
	@Override
	protected void paintComponent( Graphics g ) {
		if(taskFuture == null) {
			lblName.setText( "" );
			progressBar.setValue( 0 );
			btnStop.setEnabled( false );
			super.paintComponent( g );
			return;
		}
		if(selected) {
			setBackground(UIManager.getColor("EditorPane.selectionBackground"));
		} else {
			setBackground(UIManager.getColor("Desktop.background"));
		}
		
		ExperimentUserInfo info = (ExperimentUserInfo) taskFuture.getTask().getUserInformation();
		
		lblName.setText( info.name );
		progressBar.setValue( Math.round( info.progress*1000f ) );
				
		if(taskFuture.isDone()) {
			btnStop.setEnabled( false );
			if(t != null) {
				t.cancel();				
			}
		}
		
		super.paintComponent( g );
	}
	
	public void setSelected( boolean selected ) {
		this.selected = selected;
		this.repaint();
	}
	
	
	public boolean isSelected(){
		return selected;
	}
}
