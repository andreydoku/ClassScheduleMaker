package panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import basic.Course;
import basic.OmittedInstructor;
import myLibrary.Time;

public class CriteriaPanel extends JPanel{
	
	
	JPanel mainPanel = new JPanel();
	
		JCheckBox noClassesBefore_CheckBox = new JCheckBox( "No classes before " );
		JComboBox noClassesBefore_ComboBox;
	
		JCheckBox noClassesAfter_CheckBox = new JCheckBox( "No classes after   " );
		JComboBox noClassesAfter_ComboBox;
	
		JCheckBox noClassesLongerThan_CheckBox = new JCheckBox( "No classes longer than " );
		JComboBox noClassesLongerThan_ComboBox;
	
		JCheckBox noBreaksLongerThan_CheckBox = new JCheckBox( "No breaks longer than " );
		JComboBox noBreaksLongerThan_ComboBox;
	
		JCheckBox maxClassesPerDay_CheckBox = new JCheckBox( "Max classes per day" );
		JComboBox maxClassesPerDay_ComboBox;
	
	JPanel instructorPanel = new JPanel();	
		JLabel uncheckInstructorsLabel = new JLabel( "Uncheck any instructors you want to avoid" );
		ArrayList<InstructorCheckBox> instructorCheckBoxes = new ArrayList<InstructorCheckBox>();
	
	
	private ArrayList<Course> courses;
	
	
	public CriteriaPanel() {
		
		this( new ArrayList<Course>() );
		
	}
	public CriteriaPanel( ArrayList<Course> courses ) {
		
		this.courses = courses;
		build();
		
	}
	public void build() {
		
		
		buildMainPanel();
		buildInstructorPanel();
		
		////////////////////////////////////////////////
		
		this.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1; c.weighty = 1;
		
		c.gridx = 0; c.gridy = 0; this.add( mainPanel , c );
		c.gridx = 1; c.gridy = 0; this.add( instructorPanel , c );
		
	}
	public void buildMainPanel() {
		
		mainPanel.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		int bt = 2;
		c.insets.set( bt , bt , bt , bt );
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String[] options = { "9:00 am" , "9:30 am" , "10:00 am" , "10:30 am" , "11:00 am" , "11:30 am" , "12:00 pm" , "12:30pm" , "1:00 pm" , "1:30pm" , "2:00 pm" , "2:30pm" , "3:00pm" };
		noClassesBefore_ComboBox = new JComboBox( options );
		
		c.gridx = 0; c.gridy = 0; c.weightx = 1; mainPanel.add( noClassesBefore_CheckBox , c );
		c.gridx = 1; c.gridy = 0; c.weightx = 0; mainPanel.add( noClassesBefore_ComboBox , c );
		
		noClassesBefore_ComboBox.setEnabled( false );
		noClassesBefore_CheckBox.addActionListener( new MyActionListener() );
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
		String[] options2 = { "2:00 pm" , "2:30 pm" , "3:00 pm" , "3:30 pm" , "4:00 pm" , "4:30 pm" , "5:00 pm" , "5:30 pm" , "6:00 pm" , "6:30 pm" , "7:00 pm" , "7:30 pm" , "8:00 pm" };
		noClassesAfter_ComboBox = new JComboBox( options2 );
		
		c.gridx = 0; c.gridy = 1; c.weightx = 1; mainPanel.add( noClassesAfter_CheckBox , c );
		c.gridx = 1; c.gridy = 1; c.weightx = 0; mainPanel.add( noClassesAfter_ComboBox , c );
		
		noClassesAfter_ComboBox.setEnabled( false );
		noClassesAfter_ComboBox.setSelectedItem( "5:00 pm" );
		noClassesAfter_CheckBox.addActionListener( new MyActionListener() );

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String[] options3 = { "1 hour" , "1.5 hours" , "2 hours" , "2.5 hours" , "3 hours" };
		noClassesLongerThan_ComboBox = new JComboBox( options3 );
		
		c.gridx = 0; c.gridy = 2; mainPanel.add( noClassesLongerThan_CheckBox , c );
		c.gridx = 1; c.gridy = 2; mainPanel.add( noClassesLongerThan_ComboBox , c );
		
		noClassesLongerThan_ComboBox.setEnabled( false );
		noClassesLongerThan_ComboBox.setSelectedItem( "2 hours" );
		noClassesLongerThan_CheckBox.addActionListener( new MyActionListener() );
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String[] options4 = { "1 hour" , "1.5 hours" , "2 hours" , "2.5 hours" , "3 hours" , "3.5 hours" , "4 hours" , "4.5 hours" , "5 hours" };
		noBreaksLongerThan_ComboBox = new JComboBox( options4 );
		
		c.gridx = 0; c.gridy = 3; mainPanel.add( noBreaksLongerThan_CheckBox , c );
		c.gridx = 1; c.gridy = 3; mainPanel.add( noBreaksLongerThan_ComboBox , c );
		
		noBreaksLongerThan_ComboBox.setEnabled( false );
		noBreaksLongerThan_ComboBox.setSelectedItem( "2 hours" );
		noBreaksLongerThan_CheckBox.addActionListener( new MyActionListener() );
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String[] options5 = { "1" , "2" , "3" , "4" , "5" };
		maxClassesPerDay_ComboBox = new JComboBox( options5 );
		
		c.gridx = 0; c.gridy = 4; mainPanel.add( maxClassesPerDay_CheckBox , c );
		c.gridx = 1; c.gridy = 4; mainPanel.add( maxClassesPerDay_ComboBox , c );
		
		maxClassesPerDay_ComboBox.setEnabled( false );
		maxClassesPerDay_ComboBox.setSelectedItem( "3" );
		maxClassesPerDay_CheckBox.addActionListener( new MyActionListener() );
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
	}
	public void buildInstructorPanel() {
		
		instructorPanel.removeAll();
		instructorCheckBoxes.clear();
		
		instructorPanel.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0; c.gridy = 0; instructorPanel.add( uncheckInstructorsLabel , c ); c.gridy++;
		
		for( int i=0; i<courses.size(); i++ )
		{
			Course course = courses.get( i );
			JLabel courseLabel = new JLabel( "    " + course.getPrefix() + " - " + course.getCourseNumber() );
			
			c.insets.set( 10 , 0 , 0 , 0 );
			instructorPanel.add( courseLabel , c ); c.gridy++;
						
			ArrayList<String> instructors = course.getAllInstructors();
			for( int j=0; j<instructors.size(); j++ )
			{
				String instructor = instructors.get( j );
				InstructorCheckBox instructorCheckBox = new InstructorCheckBox( course.getPrefix() , course.getCourseNumber() , instructor );
				
				instructorCheckBox.setSelected( true );
				instructorCheckBox.setBorder( BorderFactory.createEmptyBorder( 0 , 30 , 0 , 0 ) );
				
				this.instructorCheckBoxes.add( instructorCheckBox );
				
				c.insets.set( 3 , 0 , 0 , 0 );
				instructorPanel.add( instructorCheckBox , c ); c.gridy++;
				
				
			}
			
			
			
		}
		
		instructorPanel.revalidate();
		instructorPanel.repaint();
		
		
		
		
	}
	
	
	public void setCourses( ArrayList<Course> courses ) {
		
		this.courses = courses;
		buildInstructorPanel();
		
	}
	
	
	
	public Time getNoClassBefore() {
		
		if( !noClassesBefore_CheckBox.isSelected() )
		{
			return null;
		}
		
		String timeString = (String) noClassesBefore_ComboBox.getSelectedItem();
		return new Time( timeString );
		
	}
	public Time getNoClassAfter() {
		
		if( !noClassesAfter_CheckBox.isSelected() )
		{
			return null;
		}
		
		String timeString = (String) noClassesAfter_ComboBox.getSelectedItem();
		return new Time( timeString );
		
	}
	public int getNoclassesLongerThanInMins() {
		
		if( !noClassesLongerThan_CheckBox.isSelected() )
		{
			return -1;
		}
		
		String selected = (String) noClassesLongerThan_ComboBox.getSelectedItem();
		String numberString = selected.substring( 0 , selected.indexOf( " " ) );
		double hours = Double.parseDouble( numberString );
		int mins = (int)(hours * 60);
		
		System.out.println( "CriteriaPanel.getNoClassesLongerThan(): " + mins + "mins" );
		return mins;
		
	}
	public int getNoBreaksLongerThanInMins() {
		
		if( !noBreaksLongerThan_CheckBox.isSelected() )
		{
			return -1;
		}
		
		String selected = (String) noBreaksLongerThan_ComboBox.getSelectedItem();
		String numberString = selected.substring( 0 , selected.indexOf( " " ) );
		double hours = Double.parseDouble( numberString );
		int mins = (int)(hours * 60);
		
		return mins;
		
	}
	public int getMaxClassesPerDay() {
		
		if( !maxClassesPerDay_CheckBox.isSelected() )
		{
			return -1;
		}
		
		String selected = (String) maxClassesPerDay_ComboBox.getSelectedItem();
		return Integer.parseInt( selected );
		
	}
	public ArrayList<OmittedInstructor> getAllOmittedInstructors(){
		
		ArrayList<OmittedInstructor> output = new ArrayList<OmittedInstructor>();
		for( int i=0; i<instructorCheckBoxes.size(); i++ )
		{
			InstructorCheckBox checkBox = instructorCheckBoxes.get( i );
			if( !checkBox.isSelected() )
			{
				OmittedInstructor omittedInstructor = new OmittedInstructor( checkBox.coursePrefix , checkBox.courseNumber , checkBox.instructor );
				output.add( omittedInstructor );
			}
		}
		
		
		
		return output;
		
	}
	
	
	
	private class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed( ActionEvent e ){
			
			if( e.getSource() == noClassesBefore_CheckBox     )		noClassesBefore_ComboBox	.setEnabled( noClassesBefore_CheckBox	 .isSelected()  );
			if( e.getSource() == noClassesAfter_CheckBox      )		noClassesAfter_ComboBox		.setEnabled( noClassesAfter_CheckBox	 .isSelected()  );
			if( e.getSource() == noClassesLongerThan_CheckBox )		noClassesLongerThan_ComboBox.setEnabled( noClassesLongerThan_CheckBox.isSelected()  );
			if( e.getSource() == noBreaksLongerThan_CheckBox  )		noBreaksLongerThan_ComboBox	.setEnabled( noBreaksLongerThan_CheckBox .isSelected()  );
			if( e.getSource() == maxClassesPerDay_CheckBox    )		maxClassesPerDay_ComboBox	.setEnabled( maxClassesPerDay_CheckBox   .isSelected()  );
			
			
		}
		
		
		
	}
	
	private class InstructorCheckBox extends JCheckBox{
		
		String coursePrefix;
		String courseNumber;
		
		String instructor;
		
		public InstructorCheckBox( String coursePrefix , String courseNumber , String instructor ) {
			
			super( instructor );
			
			this.coursePrefix = coursePrefix;
			this.courseNumber = courseNumber;
			this.instructor = instructor;
			
		}
		
	}
	
	
	
	public static void main(String[] args) {
		
		try{ UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch ( Exception ex ){}
		
		
		
		JFrame frame = new JFrame( "CriteriaPanel" );
		frame.setSize( 500,400 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLocationRelativeTo( null );
		
		
		ArrayList<Course> courseList = Course.getExampleCourseList();
		frame.add( new CriteriaPanel( courseList ) );
		
		frame.setVisible( true );
		
		
	}
	
	
	
	
	
	
	
	
	
}


























