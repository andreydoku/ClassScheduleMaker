package basic;

import table.RowData;
import java.io.*;
import java.util.ArrayList;

import myLibrary.*;

public class Course implements RowData, Serializable {



    private String prefix;
    private String courseNumber;
    private String courseName;
    private String term;
    
    private ArrayList<Section> sections = new ArrayList<Section>();

    public Course( String prefix , String courseNumber , String courseName , String term ){

		set( prefix , courseNumber , courseName , term );

    }
    public Course(){

		set( "NEW" , "0000" , "N/A" , "13f" );

    }

    public void set( String prefix , String courseNumber , String courseName , String term ){

		this.prefix = prefix;
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.term = term;
		
		for ( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get( i );
			s.set( this );
		}

    }
    
    public void add( Section s ){

		s.set( this );
		sections.add( s );

    }
	public void remove( Section s ){

		sections.remove( s );

	}
	public void removeAllSections(){

		sections.clear();

	}
	
    public String getPrefix(){

		return this.prefix;

    }
    public String getCourseNumber(){

		return this.courseNumber;

    }
    public String getCourseName(){

		return courseName;

    }
    public String getTerm(){
    	
    	return this.term;
    	
    }
    
    public void setPrefix( String prefix ){

		this.prefix = prefix;

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			s.set( this );
		}

    }
    public void setCourseNumber( String courseNumber ){

		this.courseNumber = courseNumber;

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			s.set( this );
		}

    }
    public void setCourseName( String courseName ){

		this.courseName = courseName;

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			s.set( this );
		}

    }
    public void setTerm( String term ){
    	
    	this.term = term;
    	
    }
    
    public int getSectionCount(){

		return sections.size();

    }
    public Section getSection( int i ){

		return sections.get( i );

    }
	public void setSection( int i , Section s ){
		
		this.sections.set( i , s );
		
	}
	public int indexOf( String sectionNumber ){

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			if( s.getSectionNumber().equals(sectionNumber) )
			{
				return i;
			}

		}

		return -1;

	}
	
	
	public int getCreditHours(){
		
		char c = courseNumber.charAt( 1 );
		
		if( Character.isDigit( c ) )
		{
			return Integer.parseInt( String.valueOf( c ) );
		}
		else
		{
			return 0;
		}
		
		
		
		
		
	}
	public ArrayList<String> getAllInstructors(){
		
		ArrayList<String> allInstructors = new ArrayList<String>();
		for( int i=0;i<sections.size();i++ )
		{
			Section section = sections.get( i );
			String instructor = section.getInstructor();
			
			if( instructor != null && !instructor.isEmpty() && !instructor.equals( "TBA" ) )
			{
				if( !Methods.contains( allInstructors , instructor ) )
				{
					allInstructors.add( instructor );
				}
				
			}
			
		}
		
		return allInstructors;
		
	}
	
	
	
	public static Course read( File file ) throws Exception {

		return (Course) Methods.read( file );

	}
	public static ArrayList<Course> readAll( File dir , String courseExtension ) throws Exception {

		ArrayList<Course> courses = new ArrayList<Course>();

		File[] allCourseFiles = dir.listFiles( new FileExtensionFilter( courseExtension ) );
		for( int i=0;i<allCourseFiles.length;i++ )
		{
			Course c = Course.read( allCourseFiles[i] );
			courses.add( c );
		}

		return courses;




	}
	
	public void write( File file ) throws Exception {

		Methods.write( this , file );


	}
	
    public String toString(){
    	
    	
		String str = "";
		
		str += prefix + " " + courseNumber + " - " + courseName + " - " + term + "\n";
		for ( int i=0;i<getSectionCount();i++ )
		{
			str += "\t";
			str += getSection(i).toString();
			str += "\n";
		}
		str += "\n";
		
		
		return str;
		
    }
    
    
	public static Course getDupeCourses( ArrayList<Course> courses ){


		for( int i=0;i<courses.size()-1;i++ )
		{
			Course c1 = courses.get(i);
			for( int j=1;j<courses.size();j++ )
			{
				Course c2 = courses.get(j);
				if( i != j )
				{
					if(    c1.getPrefix().equals( c2.getPrefix() )   &&   c1.getCourseNumber().equals( c2.getCourseNumber() )   &&   c1.getTerm().equals( c2.getTerm() )    )
					{
						return c1;
					}
				}

			}
		}
		
		return null;
		
	}
	public static Section getDupeSections( Course c ){


		for( int i=0;i<c.getSectionCount()-1;i++ )
		{
			Section s1 = c.getSection(i);
			for( int j=1;j<c.getSectionCount();j++ )
			{
				Section s2 = c.getSection(j);
				if( i != j )
				{
					if(    s1.getPrefix().equals( s2.getPrefix() )   &&   s1.getCourseNumber().equals( s2.getCourseNumber() )   )
					{
						return s1;
					}
				}

			}
		}

		return null;

	}
	
	
	
	public static ArrayList<Course> getExampleCourseList(){
		
		ArrayList<Course> courseList = new ArrayList<Course>();
		
		Course course1 = new Course( "MATH" , "2413" , "Diff Calc" , "18S" );
		course1.add( new Section( course1 , "001" , "Hingle McCringleberry" , "Mon & Wed" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
		course1.add( new Section( course1 , "002" , "Devoin Showerhandle" , "Mon & Wed" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
		courseList.add( course1 );
		
		Course course2 = new Course( "EE" , "3301" , "ENA" , "18S" );
		course2.add( new Section( course2 , "001" , "Robert Helms" , "Tue & Thur" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
		course2.add( new Section( course2 , "002" , "Javarison Jamar" , "Tue & Thur" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
		courseList.add( course2 );
		
		Course course3 = new Course( "EE" , "3310" , "Elect. Devices" , "18S" );
		course3.add( new Section( course3 , "001" , "James Coleman" , "Mon & Wed" , new Time("8:30 am") , new Time("9:20 am") , "room" , "call#" , true ) );
		course3.add( new Section( course3 , "002" , "William Frensley" , "Mon & Wed" , new Time("9:30 am") , new Time("10:20 am") , "room" , "call#" , true ) );
		courseList.add( course3 );
		
		
		return courseList;
		
	}
	
	
	
	

   public Object getValueAt( int column ) {


		switch (column) 
		{
			case 0:	return prefix;
			case 1: return courseNumber;
			case 2: return courseName;
			default: throw new UnsupportedOperationException("getValueAt(int column): must be 0-2, not " + column);
		}



	}
	public void setValueAt(int column, Object object) {
		
		switch (column)
		{
			case 0:	this.setPrefix( (String) object );			break;
			case 1: this.setCourseNumber( (String) object );	break;
			case 2: this.setCourseName( (String) object );		break;
			default: throw new UnsupportedOperationException("setValueAt(int column): must be 0-2, not " + column);
		}
		
	}
	
	public ArrayList<String> getColumnNames() {

		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add( "Prefix" );
		columnNames.add( "Course #" );
		columnNames.add( "Course Name" );

		return columnNames;

	}
	public Class<?> getColumnClass(int column) {

		switch (column)
		{
			case 0:	return prefix.getClass();
			case 1: return courseNumber.getClass();
			case 2: return courseName.getClass();
			default: throw new UnsupportedOperationException("getColumnClass(int column): must be 0-2, not " + column);
		}

	}
	public int getColumnCount() {
		
		return 3;
		
	}
	public boolean isCellEditable( int actualC ){
	
		return false;
		
	}
	
	
	
	
	
	
	
}







