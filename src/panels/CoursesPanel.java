package panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import basic.Course;
import table.*;
import tables.CoursesTable;


public class CoursesPanel extends JPanel{
	
	
	JPanel buttonPanel = new JPanel();
		JButton addBtn = new JButton( "Add" );
		JButton removeBtn = new JButton( "Remove" );
		JLabel hoursLabel = new JLabel( "0 credit hours" );
	JScrollPane coursesTablePanel = new JScrollPane();
		CoursesTable coursesTable = new CoursesTable();
	
		
	String term = "";
	
	
	public CoursesPanel(){
		
		build();
		
	}
	public void build(){
		
		buttonPanel.add( addBtn );
		buttonPanel.add( removeBtn );
		buttonPanel.add( hoursLabel );
		
		hoursLabel.setBorder( BorderFactory.createEmptyBorder( 0 , 20 , 0 , 0 ) );
		
		addBtn.addActionListener( new BtnListener() );
		removeBtn.addActionListener( new BtnListener() );
		
		removeBtn.setEnabled( false );
		
		
		coursesTablePanel.setBorder( BorderFactory.createEmptyBorder() );
		coursesTablePanel.setCorner( JScrollPane.UPPER_RIGHT_CORNER , MyTable.createCornerComponent( coursesTable ) );
		coursesTablePanel.setViewportView( coursesTable );
		coursesTablePanel.getViewport().setOpaque( false );
		coursesTablePanel.setOpaque( false );
		
		coursesTable.addMyTableSelectionListener( new TableSelectionListener() );
		coursesTable.setDisplayedColumnWidth( 0 , 62 );
		coursesTable.setDisplayedColumnWidth( 1 , 79 );
		coursesTable.setDisplayedColumnWidth( 2 , 309 );
		
		
//		setLayout( new BorderLayout() );
//		add( buttonPanel , BorderLayout.NORTH );
//		add( coursesTablePanel , BorderLayout.CENTER );
		
		this.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; c.gridy = 0; this.add( buttonPanel , c );
		c.gridx = 0; c.gridy = 1; this.add( coursesTablePanel , c );
		
		
	}
	
	public void updateCreditHours() {
		
		int creditHours = coursesTable.getCreditHours();
		hoursLabel.setText( creditHours + " credit hours" );
		
	}
	
	public void addPushed(){
		
		Course course = AddCourseDialog.showDialog( term );
		if( course != null )
		{
			coursesTable.addCourse( course );
		}
		
		updateCreditHours();
		
	}
	public void removePushed(){
		
		coursesTable.removeSelectedCourse();
		
		updateCreditHours();
		
	}
	
	public void tableSelectionChanged(){
		
		int newSelectedDisplayedCourse = coursesTable.getSelectedRow();
		removeBtn.setEnabled( newSelectedDisplayedCourse != -1 );
		
	}
	
	
	public ArrayList<Course> getAllCourses(){
		
		return coursesTable.getAllCourses();
		
	}
	
	
	private class TableSelectionListener extends MyTableSelectionListener {
		
		
		public void selectionChanged(){
			
			tableSelectionChanged();
			
						
			
		}



	}
	private class BtnListener implements ActionListener {

		
		public void actionPerformed( ActionEvent e ){
		
			if( e.getSource() == addBtn )
			{
				
				addPushed();
				
			}
			else if( e.getSource() == removeBtn )
			{
				
				removePushed();
				
			}
			
		}
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		try{ UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch ( Exception ex ){}
		
		
		
		JFrame frame = new JFrame( "CoursesPanel" );
		frame.setSize( 500,400 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		
		frame.add( new CoursesPanel() );
		
		frame.setVisible( true );
		
		
	}
	
	
	
	
	
	
	
}















