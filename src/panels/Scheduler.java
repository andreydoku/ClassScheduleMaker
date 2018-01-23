package panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import basic.*;
import myLibrary.Time;


public class Scheduler extends JFrame{
	
	
	
	JPanel topPanel = new JPanel();
		JLabel titleLabel = new JLabel( "Title goes here" );
	JPanel centerPanel = new JPanel();
		TermPanel termPanel;
		CoursesPanel coursesPanel = new CoursesPanel();
		CriteriaPanel criteriaPanel = new CriteriaPanel();
		OutputPanel outputPanel = new OutputPanel();
	JPanel bottomPanel = new JPanel();
		JButton backBtn = new JButton( "Back" );
		JButton nextBtn = new JButton( "Next" );
	
	
	int page = TERM_PAGE;
		static int TERM_PAGE = 1;
		static int COURSES_PAGE = 2;
		static int PREFERENCES_PAGE = 3;
		static int OUTPUT_PAGE = 4;
		
	String term = "";
	ArrayList<Course> selectedCourses = new ArrayList<Course>();
	
	
	public Scheduler(){
		
		
		
		try
		{
			termPanel = new TermPanel();
		}
		catch ( IOException e )
		{
			
			e.printStackTrace();
		}
		
		build();
		
		
		
		titleLabel.setText( "Select term" );
		backBtn.setEnabled( false );
		
		
		
		
		
	}
	public void build(){
		
		
		//System.out.println( titleLabel.getFont() );
		titleLabel.setFont( new Font( "Tahoma" , Font.PLAIN , 16 ) );
		backBtn.setFont( new Font( "Tahoma" , Font.PLAIN , 16 ) );
		nextBtn.setFont( new Font( "Tahoma" , Font.PLAIN , 16 ) );
		
		int b = 20;
		titleLabel.setBorder( BorderFactory.createEmptyBorder( b , b , b , b ) );
		bottomPanel.setBorder( BorderFactory.createEmptyBorder( b , b , b , b ) );
		
		topPanel.add( titleLabel );
		bottomPanel.add( backBtn );
		bottomPanel.add( nextBtn );
		
		
		topPanel.setBackground( topPanel.getBackground().darker() );
		bottomPanel.setBackground( bottomPanel.getBackground().darker() );
		
		backBtn.setOpaque( false );
		nextBtn.setOpaque( false );
		
		backBtn.addActionListener( new BtnListener() );
		nextBtn.addActionListener( new BtnListener() );
		
		setLayout( new BorderLayout() );
		this.add( topPanel , BorderLayout.NORTH );
		this.add( termPanel , BorderLayout.CENTER );
		this.add( bottomPanel , BorderLayout.SOUTH );
		
		
		
		this.setTitle( "scheduler" );
		this.setSize( 900,700 );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLocationRelativeTo( null );
		
		
		//outputPanel.centerPanel.setDividerLocation( 0.75 );
		this.setVisible( true );
		
		
		
	}
	
	
	//back
	private void backPushed(){
		
		if( page == COURSES_PAGE )
		{
			backCoursesToTerm();
		}
		else if( page == PREFERENCES_PAGE )
		{
			backCriteriaToCourses();
		}
		else if( page == OUTPUT_PAGE )
		{
			backOutputToCriteria();
		}
		
		page--;
		
	}
	private void backCoursesToTerm() {
		
		this.remove( coursesPanel );
		this.add( termPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();
		
		
		titleLabel.setText( "Select term" );
		
		coursesPanel.coursesTable.removeAllCourses();
		this.selectedCourses.clear();
		coursesPanel.updateCreditHours();
		
		backBtn.setEnabled( false );
		
	}
	private void backCriteriaToCourses() {
		
		this.remove( criteriaPanel );
		this.add( coursesPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();
		
		titleLabel.setText( "Add all courses you wish to take" );
		
	}
	private void backOutputToCriteria() {
		
		this.remove( outputPanel );
		this.add( criteriaPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();
		
		titleLabel.setText( "Select any preferences for your weekly schedule" );
		
		nextBtn.setEnabled( true );
		
		
	}
	
	
	//next
	private void nextPushed(){
		
		if( page == TERM_PAGE )
		{
			nextTermToCourses();
		}
		else if( page == COURSES_PAGE )
		{
			nextCoursesToCriteria();
		}
		else if( page == PREFERENCES_PAGE )
		{
			nextCriteriaToOutput();
		}
		
		
		
	}
	public void nextTermToCourses() {
		
		this.term = termPanel.getSelectedTerm();
		//System.out.println( "next pushed... term: " + term );
		coursesPanel.term = this.term;
		
		this.remove( termPanel );
		this.add( coursesPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();	
		
		titleLabel.setText( "Add all courses you wish to take" );
		
		backBtn.setEnabled( true );
		
		page++;
		
	}
	public void nextCoursesToCriteria() {
		
		this.selectedCourses = coursesPanel.getAllCourses();
		
		if( selectedCourses.size() == 0 )
		{
			String message = "Please add at least one class";
			JOptionPane.showMessageDialog( null , message );
			return;
		}
		
		criteriaPanel.setCourses( selectedCourses );
		
		this.remove( coursesPanel );
		this.add( criteriaPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();
		
		titleLabel.setText( "Select any preferences for your weekly schedule" );
		
		page++;
		
	}
	public void nextCriteriaToOutput() {
		
		ArrayList<Schedule> schedules = Schedule.computeAllSchedules( selectedCourses );
		
		//remove closed sections
		schedules = Schedule.removeClosedSections( schedules );
		
		//--------------------------------------------------------------------------------------------
		
		//no classes before
		Time noClassBefore = criteriaPanel.getNoClassBefore();
		if( noClassBefore != null )
		{
			schedules = Schedule.removeClassesBefore( schedules , noClassBefore );
		}
		
		//--------------------------------------------------------------------------------------------
		
		//no classes after
		Time noClassAfter = criteriaPanel.getNoClassAfter();
		if( noClassAfter != null )
		{
			schedules = Schedule.removeClassesAfter( schedules , noClassAfter );
		}
		
		//--------------------------------------------------------------------------------------------
		
		int noClassesLongerThan = criteriaPanel.getNoclassesLongerThanInMins();
		if( noClassesLongerThan != -1 )
		{
			schedules = Schedule.removeClassesLongerThan( schedules , noClassesLongerThan );
		}
		
		//--------------------------------------------------------------------------------------------
		
		int noBreaksLongerThan = criteriaPanel.getNoBreaksLongerThanInMins();
		if( noBreaksLongerThan != -1 )
		{
			schedules = Schedule.removeBreaksLongerThan( schedules , noBreaksLongerThan );
		}
		
		//--------------------------------------------------------------------------------------------
		
		ArrayList<OmittedInstructor> omittedInstructors = criteriaPanel.getAllOmittedInstructors();
		schedules = Schedule.removeContainsOmittedInstructors( schedules , omittedInstructors );
		
		//--------------------------------------------------------------------------------------------
		
		
		
		
		//--------------------------------------------------------------------------------------------
		
		//System.out.println( "Found " + schedules.size() + " possible schedules" );
		
		
		if( schedules.size() == 0 )
		{
			String message = "Found 0 possible schedules with those classes and selected criteria";
			JOptionPane.showMessageDialog( this , message );
			return;
		}
		
		
		outputPanel.setScheduleList( schedules );
		
		this.remove( criteriaPanel );
		this.add( outputPanel , BorderLayout.CENTER );
		this.revalidate();
		this.repaint();
		
		titleLabel.setText( "Found " + schedules.size() + " possible schedules" );
		
		nextBtn.setEnabled( false );
		
		page++;
		
	}
	
	
	
	private class BtnListener implements ActionListener {

		
		public void actionPerformed( ActionEvent e ){
		
			if( e.getSource() == backBtn )
			{
				backPushed();
			}
			else if( e.getSource() == nextBtn )
			{
				nextPushed();
			}
			
		}
		
		
		
		
	}
	
		
	
	
	
	public static void main(String[] args) {
		
		try{ UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch ( Exception ex ){}
		
		Scheduler scheduler = new Scheduler();
		
	}
	
	
	
	
}



























