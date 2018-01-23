package panels;

import java.awt.*;
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

public class OutputPanel extends JPanel {
	
	
	JPanel topPanel = new JPanel();
		JButton leftBtn = new JButton( "◄" );
		JTextField scheduleNumberField = new JTextField( 6 );
		JButton rightBtn = new JButton( "►" );
	JSplitPane centerPanel;
		JPanel leftPanel = new JPanel();
			JScrollPane scrollPane = new JScrollPane();
				JTextArea textArea = new JTextArea();
		SchedulePlot plot = new SchedulePlot();
	
		
		
		
	ArrayList<Schedule> schedules;	
	int scheduleIndex = -1;
	
	
	public OutputPanel() {
		
		build();
		
	}
	public void build() {
		
		buildTopPanel();
		buildCenterPanel();
		
		this.setLayout( new BorderLayout() );
		this.add( topPanel , BorderLayout.NORTH );
		this.add( centerPanel , BorderLayout.CENTER );
		
		
		
		
		centerPanel.setDividerLocation( 0.5 );
		
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
		
		centerPanel = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , plot );
		centerPanel.setResizeWeight( 0.5 );
		
		
		leftPanel.setLayout( new GridBagLayout() );
		GridBagConstraints c =  new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0; c.gridy = 0; leftPanel.add( scrollPane , c );
		
		
		
		scrollPane.setViewportView( textArea );
		scrollPane.getViewport().setOpaque( false );
		scrollPane.setOpaque( false );
		
		int bt = 20;
		Color color = new Color( 220,220,255 );
		//scrollPane.setBorder( BorderFactory.createMatteBorder( bt , bt , bt , bt , color ) );
		//scrollPane.setBorder( BorderFactory.createLoweredBevelBorder() );
		scrollPane.setBorder( BorderFactory.createEmptyBorder( bt , bt , bt , bt ) );
		
		textArea.setFont( new Font( "Tahoma" , Font.PLAIN , 12 ) );
		textArea.setEditable( false );
		
		
		
		
		
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
			setTextArea( null );
			
		}
		else
		{
			Schedule schedule = schedules.get( i );
			
			String fieldText = String.valueOf( i+1 ) + " / " + schedules.size();
			scheduleNumberField.setText( fieldText );
			leftBtn.setEnabled( i > 0 );
			rightBtn.setEnabled( i < schedules.size()-1 );
			
			plot.set( schedule );
			setTextArea( schedule );
		}
		
		
		
		
		
		
	
	}
	private void setTextArea( Schedule schedule ) {
		
		
		String text = "";
		for( int i=0; i<schedule.size(); i++ )
		{
			Section section = schedule.get( i );
			text += section.getPrefix() + " " + section.getCourseNumber() + " - " + section.getCourseName();
			text += "\n" + "\t" + "Section #: " + section.getSectionNumber() + "        " + "class #: " + section.getCallNumber();
			text += "\n" + "\t" + "Times: " + section.getDays() + ",  " + section.getStartTime() + " - " + section.getEndTime();
			text += "\n" + "\t" + "Location: " + section.getRoom();
			text += "\n" + "\t" + "Instructor: " + section.getInstructor();
			
			if( i != schedule.size()-1 )
			{
				text += "\n\n";
			}
			
			
		}
		
		textArea.setText( text );
		
	}
	
	
	private void leftBtnPushed() {
		
		displaySchedule( this.scheduleIndex - 1 );
		
	}
	private void rightBtnPushed() {
		
		displaySchedule( this.scheduleIndex + 1 );
		
		System.out.println( this.getParent().getParent().getParent().getParent().getSize() );
		
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
		OutputPanel outputPanel = new OutputPanel();
		outputPanel.setScheduleList( schedules );
		
		
		
		JFrame frame = new JFrame( "CriteriaPanel" );
		frame.setSize( 1000,600 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		
		frame.add( outputPanel );
		
		frame.setVisible( true );
		
		//outputPanel.centerPanel.setDividerLocation( 0.65 );
		
	}
	
	
	
	
	
	
	
	
	
}






















