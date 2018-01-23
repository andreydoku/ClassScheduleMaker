package panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import basic.*;
import myLibrary.Time;
import plot.SchedulePlot;
import table.MyTable;
import table.MyTableSelectionListener;
import tables.SectionsTable;

public class OutputPanel2 extends JPanel {
	
	
	JPanel topPanel = new JPanel();
		JButton leftBtn = new JButton( "◄" );
		JTextField scheduleNumberField = new JTextField( 6 );
		JButton rightBtn = new JButton( "►" );
	JSplitPane centerPanel;
		JScrollPane tablePanel = new JScrollPane();
			SectionsTable table = new SectionsTable();
		SchedulePlot plot = new SchedulePlot();
	
		
		
		
	ArrayList<Schedule> schedules;	
	int scheduleIndex = -1;
	
	
	public OutputPanel2() {
		
		build();
		
	}
	public void build() {
		
		buildTopPanel();
		buildCenterPanel();
		
		this.setLayout( new BorderLayout() );
		this.add( topPanel , BorderLayout.NORTH );
		this.add( centerPanel , BorderLayout.CENTER );
		
		
		
		
		centerPanel.setDividerLocation( 0.75 );
		
	}
	public void buildTopPanel() {
		
		topPanel.add( leftBtn );
		topPanel.add( scheduleNumberField );
		topPanel.add( rightBtn );
		
		leftBtn.addActionListener( new BtnListener() );
		rightBtn.addActionListener( new BtnListener() );
		
		scheduleNumberField.setEditable( false );
		scheduleNumberField.setText( "5" );
		scheduleNumberField.setHorizontalAlignment( SwingConstants.CENTER );
		
	}
	public void buildCenterPanel() {
		
		centerPanel = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , tablePanel , plot );
		centerPanel.setResizeWeight( 0.5 );
		
		
		tablePanel.setBorder( BorderFactory.createEmptyBorder() );
		tablePanel.setCorner( JScrollPane.UPPER_RIGHT_CORNER , MyTable.createCornerComponent( table ) );
		tablePanel.setViewportView( table );
		tablePanel.getViewport().setOpaque( false );
		tablePanel.setOpaque( false );
		
		table.addMyTableSelectionListener( new TableSelectionListener() );
		//table.setDisplayedColumnWidth( 0 , 62 );
		//table.setDisplayedColumnWidth( 1 , 79 );
		//table.setDisplayedColumnWidth( 2 , 309 );
		
	}
	
	public void setScheduleList( ArrayList<Schedule> schedules ) {
		
		this.schedules = schedules;
		
		if( schedules.isEmpty() || schedules == null )
		{
			displaySchedule( -1 );
		}
		else
		{
			displaySchedule( 0 );
		}
		
		
	}
	private void displaySchedule( int i ) {
		
		this.scheduleIndex = i;
		
		if( i == -1 )
		{
			scheduleNumberField.setText( "" );
			leftBtn.setEnabled( false );
			rightBtn.setEnabled( false );
			
			plot.set( null );
			table.setSchedule( null );
			
		}
		else
		{
			Schedule schedule = schedules.get( i );
			
			String fieldText = String.valueOf( i+1 ) + " / " + schedules.size();
			scheduleNumberField.setText( fieldText );
			leftBtn.setEnabled( i > 0 );
			rightBtn.setEnabled( i < schedules.size()-1 );
			
			plot.set( schedule );
			table.setSchedule( schedule );
		}
		
		
		
		
		
		
	
	}
	
	private void leftBtnPushed() {
		
		displaySchedule( this.scheduleIndex - 1 );
		
	}
	private void rightBtnPushed() {
		
		displaySchedule( this.scheduleIndex + 1 );
		
		System.out.println( this.getParent().getParent().getParent().getParent().getSize() );
		
	}
	private void tableSelectionChanged() {
		
		Section section = table.getSelectedSection();
		plot.setHighlighted( section );
		
	}
	
	
	
	private class BtnListener implements ActionListener{

		
		public void actionPerformed( ActionEvent e ){
			
			if( e.getSource() == leftBtn )
			{
				leftBtnPushed();
			}
			if( e.getSource() == rightBtn )
			{
				rightBtnPushed();
			}
			
		}
		
		
		
		
		
	}
	private class TableSelectionListener extends MyTableSelectionListener {
		
		
		public void selectionChanged(){
			
			tableSelectionChanged();
			
						
			
		}



	}
	
	
	public static void main(String[] args) {
		
		try{ UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch ( Exception ex ){}
		
		
		
		ArrayList<Course> selectedCourses = new ArrayList<Course>();
		
		Course course1 = new Course( "AA" , "1000" , "a-1000" , "18S" );
		course1.add( new Section( course1 , "001" , "teacher" , "Mon & Wed" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
		course1.add( new Section( course1 , "002" , "teacher" , "Mon & Wed" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
		selectedCourses.add( course1 );
		
		Course course2 = new Course( "BB" , "1000" , "b-1000" , "18S" );
		course2.add( new Section( course2 , "001" , "teacher" , "Tue & Thur" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
		course2.add( new Section( course2 , "002" , "teacher" , "Tue & Thur" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
		selectedCourses.add( course2 );
		
//		Course course3 = new Course( "CC" , "1000" , "c-1000" , "18S" );
//		course3.add( new Section( course3 , "001" , "teacher" , "Mon & Wed" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
//		course3.add( new Section( course3 , "002" , "teacher" , "Mon & Wed" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
//		selectedCourses.add( course3 );
		
		
		
		ArrayList<Schedule> schedules = Schedule.computeAllSchedules( selectedCourses );
		OutputPanel2 outputPanel = new OutputPanel2();
		outputPanel.setScheduleList( schedules );
		
		
		
		JFrame frame = new JFrame( "CriteriaPanel" );
		frame.setSize( 1000,600 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		
		frame.add( outputPanel );
		
		frame.setVisible( true );
		
		outputPanel.centerPanel.setDividerLocation( 0.65 );
		
	}
	
	
	
	
	
	
	
	
	
}






















