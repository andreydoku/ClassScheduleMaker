package basic;

import table.RowData;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import myLibrary.Methods;
import myLibrary.Time;

public class Section implements RowData, Serializable {


	private Course course;
	
	private String sectionNumber;
	private String instructor;
	
	private String days;
	private Time startTime;
	private Time endTime;
	private String room;
	
	private String classNumber;
	private boolean isOpen;




	
	public Section(){

		
		this.course = new Course( "NEW" , "0000" , "N/A" , "13f" );
		
		this.sectionNumber = "000";
		this.instructor = "N/A";
		this.days = "N/A";

		this.startTime = new Time( "8:30 am" );
		this.endTime = new Time( "9:20 am" );
		
		this.room = "N/A";

		this.classNumber = "N/A";
		this.isOpen = true;





	}
	public Section( Course course ){

		set( course );

		this.sectionNumber = "000";
		this.instructor = "N/A";
		this.days = "N/A";

		this.startTime = new Time( "8:30 am" );
		this.endTime = new Time( "9:20 am" );
		
		this.room = "N/A";

		this.classNumber = "N/A";
		this.isOpen = true;

	}
	public Section( Course course , String sectionNumber , String instructor , String days , Time start , Time end , String room , String callNumber , boolean isOpen ){
		
		set( course , sectionNumber , instructor , days , start , end , room , callNumber , isOpen );
		
	}
	
	
	public void set( Course c , String sectionNumber , String instructor , String days , Time start , Time end , String room , String callNumber , boolean isOpen ){

		this.course = c;
		
		this.sectionNumber = sectionNumber;
		this.instructor = instructor;
		this.days = days;

		this.startTime = start;
		this.endTime = end;

		this.room = room;
		this.classNumber = callNumber;
		this.isOpen = isOpen;


	}
	public void set( Course c ){
		
		this.course = c;
		

	}
	
	//set methods
	public void setSectionNumber( String sectionNumber ){

		this.sectionNumber = sectionNumber;

	}
	public void setInstructor( String instructor ){

		this.instructor = instructor;

	}
	public void setDays( String days ){

		this.days = days;

	}

	public void setStartTime( Time startTime ){
		
		
		
		if( startTime.isLaterThan( this.endTime ) )
		{
			String text = "trying to set startTime later than endTime. ";
			text += "new startTime: " + startTime + ", " + "endTime: " + this.endTime;

			throw new IllegalArgumentException( text );
		}
		
		this.startTime = startTime;

	}
	public void setEndTime( Time endTime ){
		
		
		
		if( endTime.isEarlierThan( this.startTime ) )
		{
			String text = "trying to set endTime earlier than startTime. ";
			text += "startTime: " + this.startTime + ", " + "new endTime: " + endTime;

			throw new IllegalArgumentException( text );
		}
		
		this.endTime = endTime;

	}
	public void setTimes( Time startTime , Time endTime ){
		
		if( startTime.isLaterThan( endTime ) )
		{
			String text = "trying to set endTime earlier than startTime. ";
			text += "new startTime: " + startTime + ", " + "new endTime: " + endTime;

			throw new IllegalArgumentException( text );
		}

		this.startTime = startTime;
		this.endTime = endTime;
		
	}

	public void setRoom( String room ){

		this.room = room;

	}
	public void setClassNumber( String classNumber ){

		this.classNumber = classNumber;

	}
	public void setOpen( boolean isOpen ){

		this.isOpen = isOpen;

	}
	
	
	// get methods
	public Course getCourse(){
		
		return this.course;
		
	}
	
	public String getPrefix(){

		return this.course.getPrefix();

	}
	public String getCourseNumber(){

		return this.course.getCourseNumber();

	}
	public String getCourseName(){

		return this.course.getCourseName();

	}
	public String getTerm(){
		
		return this.course.getTerm();
		
	}
	
	public String getSectionNumber(){

		return this.sectionNumber;

	}
	public String getInstructor(){

		return this.instructor;

	}
	public String getDays(){

		return this.days;

	}
	
	public Time getStartTime(){

		return this.startTime;

	}
	public Time getEndTime(){

		return this.endTime;

	}
	public int getDurationInMinutes() {
		
		return this.getEndTime().getMinutes() - this.getStartTime().getMinutes();
		
	}
	
	
	public String getRoom(){

		return this.room;

	}
	public String getCallNumber(){

		return this.classNumber;

	}
	public boolean getOpen(){

		return this.isOpen;

	}
	
	public boolean isClosed(){

		return !this.isOpen;

	}
	
	
	public boolean hasDay( int day ){

		switch ( day )
		{
			case 0: return hasMondays();
			case 1: return hasTuesdays();
			case 2: return hasWednesdays();
			case 3: return hasThursdays();
			case 4: return hasFridays();
			case 5: return hasSaturdays();
			default: throw new IllegalArgumentException( "section.hasDay(day):  day has to be 0 thru 5, not " + day );

		}

	}
	public boolean hasMondays(){

		//return days.equals("M") || days.equals("MW") || days.equals("MWF");
		return days.contains( "Mon" );

	}
	public boolean hasTuesdays(){

		//return days.equals("T") || days.equals("TR") ;
		return days.contains( "Tue" );
		
	}
	public boolean hasWednesdays(){

		//return days.equals("W") || days.equals("MW") || days.equals("MWF") ;
		return days.contains( "Wed" );
		
	}
	public boolean hasThursdays(){

		//return days.equals("R") || days.equals("TR") ;
		return days.contains( "Thur" );
		
	}
	public boolean hasFridays(){

		//return days.equals("F") || days.equals("MWF") ;
		return days.contains( "Fri" );
		
	}
	public boolean hasSaturdays(){

		//return days.equals("S") ;
		return days.contains( "Sat" );
		
	}
	
	public String toString(){

		String text = "";

//		text += this.getPrefix() + " - " + this.getCourseNumber() + " - " + this.getSectionNumber() + " - " + this.getTerm() + " - " + this.getCourseName();
//		text += "\n";
//		
//		text += "Instructor: " + this.instructor + "\n";
//		text += "Days: " + this.days + "\n";
//		text += "Time: " + this.startTime + " - " + this.endTime + "\n";
//		text += "Room: " + this.room + "\n";
//		text += "Class #: " + this.classNumber + "\n";
//		text += "Open: " + this.isOpen;
		
		String a = " | ";
		
		text += this.getSectionNumber() + ": " + instructor + " | " + days + a + this.startTime + " - " + this.endTime + a + this.room + a + classNumber + a + this.isOpen;
		
		
		
		
		
		return text;
	}
	
	
	//RowData
	public Object getValueAt( int column ) {
		
		
		/*
		0	private String prefix;
		1	private String courseNumber;
		2	private String courseName;
			
		3	private String sectionNumber;
		4	private String instructor;
		5	private String days;

		6	private MyTime start;
		7	private MyTime end;

		8	private String room;
		9	private String callNumber;
		10	private boolean isOpen;
		*/
		switch ( column )
		{
			case 0: return getPrefix();
			case 1: return getCourseNumber();
			case 2: return getCourseName();
			
			case 3: return sectionNumber;
			case 4: return instructor;
			case 5: return days;
			
			case 6: return startTime;
			case 7: return endTime;
			
			case 8: return room;
			case 9: return classNumber;
			case 10: return isOpen;
			
			default: throw new UnsupportedOperationException( "Section.getValueAt( column ): column has to be 0-10, not " + column );
			
		}
		
		
		
	}
	public void setValueAt( int column , Object object ) {
		
		switch ( column )
		{
			case 0: 												break;
			case 1: 												break;
			case 2: 												break;
			
			case 3: this.sectionNumber = (String) object;			break;
			case 4: this.instructor = (String) object;				break;
			case 5: this.days = (String) object;					break;
			
			case 6: this.startTime = (Time) object;					break;
			case 7: this.endTime = (Time) object;					break;
			
			case 8: this.room = (String) object;					break;
			case 9: this.classNumber = (String) object;				break;
			case 10: this.isOpen = (Boolean) object;				break;
			
			default: throw new UnsupportedOperationException( "Section.setValueAt( column ): column has to be 0-10, not " + column );
			
		}
	}
	public ArrayList<String> getColumnNames() {
		
		ArrayList<String> names = new ArrayList<String>();
		
		names.add( "Prefix" );
		names.add( "Course #" );
		names.add( "Course Name" );

		names.add( "Section #" );
		names.add( "Instructor" );
		names.add( "Days" );

		names.add( "Start" );
		names.add( "End" );

		names.add( "Room" );
		names.add( "Call #" );
		names.add( "Open" );
		
		return names;
	}
	public Class<?> getColumnClass( int column ) {
		
		switch ( column )
		{
			case 0: return getPrefix().getClass();
			case 1: return getCourseNumber().getClass();
			case 2: return getCourseName().getClass();
			
			case 3: return sectionNumber.getClass();
			case 4: return instructor.getClass();
			case 5: return days.getClass();
			
			case 6: return startTime.getClass();
			case 7: return endTime.getClass();
			
			case 8: return room.getClass();
			case 9: return classNumber.getClass();
			case 10: return Boolean.class;
			
			default: throw new UnsupportedOperationException( "Section.getColumnClass( column ): column has to be 0-10, not " + column );
			
		}
	}
	public int getColumnCount() {
		
		return 11;
		
	}
	public boolean isCellEditable( int actualC ){
	
		return !( actualC >= 0 && actualC <= 2 );
		
	}
	
	
	


}





















