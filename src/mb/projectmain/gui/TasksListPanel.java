package mb.projectmain.gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mb.projectmain.tasks.TaskFuture;



public class TasksListPanel extends JPanel {
	private JPanel tasksPanel;
	
	private final List<TaskPanel> tasks;
	
	/**
	 * Create the panel.
	 */
	public TasksListPanel () {
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_5705042189330");
		
		tasksPanel = new JPanel();
		scrollPane.setViewportView(tasksPanel);
		tasksPanel.setLayout( new BoxLayout( tasksPanel, BoxLayout.Y_AXIS )  );
		
		tasks = new ArrayList<>();
		
	}
	

	public void addTask(TaskFuture taskFuture) {
		TaskPanel newTask = new TaskPanel(taskFuture);
		newTask.registerSelectionCallback( new SelectionCallback() {
			
			@Override
			public void doSelection( TaskPanel selected ) {
				for(TaskPanel taskPanel: tasks) {
					taskPanel.setSelected( false );
				}
				selected.setSelected( true );
			}
		} );
		tasks.add( newTask );
		
		tasksPanel.add( newTask );
		revalidate();
	}
	
	public static interface SelectionCallback {
		public void doSelection(TaskPanel task);
	}
}
