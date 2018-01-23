package panels;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import myLibrary.Methods;

import parser.Parser;

import basic.Course;
import basic.Section;



public class AddCourseDialog extends JDialog{
	
	
	
	
	JPanel topPanel = new JPanel();
		JLabel prefixLabel = new JLabel( "Prefix:" );
		JTextField prefixField = new JTextField( 6 );
		
		JLabel numberLabel = new JLabel( "Course #:" );
		JTextField numberField = new JTextField( 6 );
		
		JButton searchBtn = new JButton( "Search" );
	
	JPanel centerPanel = new JPanel();
		JScrollPane scrollPanel = new JScrollPane();
			JTextArea area = new JTextArea();
	
	JPanel bottomPanel = new JPanel();
		JButton cancelBtn = new JButton( "Cancel" );
		JButton addBtn = new JButton( "Add" );	
	
	
	
	Course foundCourse = null;
	Course outputCourse = null;
	String term;
	
	Thread searchThread;
	
	
	public AddCourseDialog( String term ){
		
		this.term = term;
		
		build();
		
	}
	private void build(){
		
		area.setEditable( false );
		
		prefixField.setText( "MATH" );
		prefixField.setHorizontalAlignment( SwingConstants.CENTER );
		prefixField.addActionListener( new BtnListener() );
		prefixField.addFocusListener( new FieldFocusListener() );
		
		numberField.setText( "2413" );
		numberField.setHorizontalAlignment( SwingConstants.CENTER );
		numberField.addFocusListener( new FieldFocusListener() );
		numberField.addActionListener( new BtnListener() );
		
		scrollPanel.setViewportView( area );
		addBtn.setOpaque( false );
		searchBtn.setOpaque( false );
		
		cancelBtn.addActionListener( new BtnListener() );
		addBtn.addActionListener( new BtnListener() );
		searchBtn.addActionListener( new BtnListener() );
		
		addBtn.setEnabled( false );
		
		topPanel.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		int ob = 10;
		int ib = 10;
		
		c.fill = GridBagConstraints.NONE;
		
		
		
		c.gridx = 0;c.gridy = 0;
		c.insets = new Insets( ob,ob,0,0 );
		c.weightx = 0; c.weighty = 0;
		topPanel.add( prefixLabel , c );
		
		c.gridx = 1;c.gridy = 0;
		c.insets = new Insets( ob,ib,0,0 );
		c.weightx = 0; c.weighty = 0;
		topPanel.add( prefixField , c );
		
		c.gridx = 2;c.gridy = 0;
		c.insets = new Insets( ob,20,0,0 );
		c.weightx = 0; c.weighty = 0;
		topPanel.add( numberLabel , c );
		
		c.gridx = 3;c.gridy = 0;
		c.insets = new Insets( ob,ib,0,0 );
		c.weightx = 0; c.weighty = 0;
		topPanel.add( numberField , c );
		
		c.gridx = 4;c.gridy = 0;
		c.insets = new Insets( ob,20,0,ob );
		c.weightx = 0; c.weighty = 0;
		topPanel.add( searchBtn , c );
		
		////
		
		centerPanel.setLayout( new BorderLayout() );
		centerPanel.setBorder( BorderFactory.createEmptyBorder( ob , ob , ob , ob ) );
		centerPanel.add( scrollPanel , BorderLayout.CENTER );
		
		
		////
		
		bottomPanel.setBorder( BorderFactory.createEmptyBorder( 0 , ob , ob , ob ) );
		bottomPanel.add( cancelBtn );
		bottomPanel.add( addBtn );
		
		
		setLayout( new BorderLayout() );
		add( topPanel , BorderLayout.NORTH );
		add( centerPanel , BorderLayout.CENTER );
		add( bottomPanel , BorderLayout.SOUTH );
		
		
		
		setSize( 1000,450 );
		setLocationRelativeTo( null );
		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		
		
	}
	
	
	public void search(){
		
		this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
		prefixField.setEditable( false );
		numberField.setEditable( false );
		searchBtn.setEnabled( false );
		addBtn.setEnabled( false );
		
		area.setText( "" );
		
		String prefix = prefixField.getText();
		String courseNumber = numberField.getText();
		
		try
		{
			//Course course = Parser.extractCourse( term , prefix , courseNumber , new SearchListener() );
			Course course = Parser.extractCourse( term , prefix , courseNumber , new SearchListener() );
			if( course == null )
			{
				String message = "No course matched your criteria";
				JOptionPane.showMessageDialog( null , message );
			}
			else
			{
				this.foundCourse = course;
				addBtn.setEnabled( true );
			}
			
			
		}
		catch ( IOException e )
		{
			String address = "https://coursebook.utdallas.edu/" + prefix + "/" + courseNumber + "/term_" + term + "?";
			String message = "Unable to connect to: " + address;
			JOptionPane.showMessageDialog( null , message );
			e.printStackTrace();
		}
		
		prefixField.setEditable( true );
		numberField.setEditable( true );
		searchBtn.setEnabled( true );
		this.setCursor( Cursor.getDefaultCursor() );
		
	}
	
	public void searchPushed(){
		
		searchThread = new Thread(){
			
			public void run(){
				
				search();
				
			}
			
		};
		searchThread.start();
		
	}
	public void addPushed(){
		
		//System.out.println( this.getSize() );
		
		outputCourse = foundCourse;
		dispose();
		
	}
	public void cancelPushed() {
		
		if( searchThread != null )
		{
			searchThread.stop();
		}
			
		outputCourse = null;
		dispose();
		
	}
	
	public static String formatSection( Section s ){
		
		
		String col1 = "5";//sectionNumber
		String col2 = "23";//instructor
		String col3 = "30";//days
		String col4 = "25";//times
		String col5 = "12";//room
		String col6 = "7";//classNumber
		String col7 = "7";//open
		
		String format = "%1$-" + col1 + "s";
		format += " " + "%2$-" + col2 + "s";
		format += " " + "%3$-" + col3 + "s";
		format += " " + "%4$-" + col4 + "s";
		format += " " + "%5$-" + col5 + "s";
		format += " " + "%6$-" + col6 + "s";
		format += " " + "%7$-" + col7 + "s";
		
		String open;
		if( s.getOpen() )
		{
			open = "OPEN";
		}
		else
		{
			open = "CLOSED";
		}
		
		String formatted = String.format( format , s.getSectionNumber() + ": " , s.getInstructor() , s.getDays() , s.getStartTime() + " - " + s.getEndTime() , s.getRoom() , s.getCallNumber() , open );
		
		
		return formatted;
		
	}
	
	private class BtnListener implements ActionListener {

		
		public void actionPerformed( ActionEvent e ){
		
			if( e.getSource() == searchBtn )			searchPushed();
			else if( e.getSource() == cancelBtn )		cancelPushed();
			else if( e.getSource() == addBtn )			addPushed();
			else if( e.getSource() == prefixField )		searchPushed();
			else if( e.getSource() == numberField )		searchPushed();	
			
			
		}
		
		
		
		
	}
	private class SearchListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ){
			
			if( e.getSource() instanceof Integer )
			{
				int numberOfSections = (int) e.getSource();
				
				String text = "Found " + numberOfSections + " sections";
				area.append( text + "\n\n" );
				
			}
			else if( e.getSource() instanceof String )
			{
				String text = (String) e.getSource();
				area.append( text + "\n" );
				
			}
			else if( e.getSource() instanceof Course )
			{
				Course course = (Course) e.getSource();
				area.append(  course.getTerm() + " " + course.getPrefix() + " " + course.getCourseNumber() + " - " + course.getCourseName() + "\n" );
				
			}
			else if( e.getSource() instanceof Section )
			{
				Section section = (Section) e.getSource();
				
				area.append(  formatSection( section ) + "\n" );
				area.setCaretPosition( area.getDocument().getLength() );
				
				//System.out.println( section );
			}
			
		}
		
	}
	private class FieldFocusListener implements FocusListener {

		
		public void focusGained( FocusEvent e ){
			
			if( e.getSource() == prefixField )
			{
				prefixField.selectAll();
			}
			else if( e.getSource() == numberField )
			{
				numberField.selectAll();
			}
			
		}
		public void focusLost( FocusEvent e ){}
		
		
		
	}
	
	public static Course showDialog( String term ){
		
		
		AddCourseDialog dialog = new AddCourseDialog( term );
		dialog.setModal( true );
		dialog.setVisible( true );
		
		
		
		while( dialog.isShowing() )
		{
			
		}
		
		return dialog.outputCourse;


	}
	
	public static void main(String[] args) {
		
		try{ UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch ( Exception ex ){}
		
		
		Course course = showDialog( "18s" );
		System.out.println( course );
		
		
		
	}
	
	
	
	
}















