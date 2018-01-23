package basic;

import java.util.*;

import myLibrary.ArrayToString;
import myLibrary.Time;

public class Schedule extends ArrayList<Section> {
	
	
	
	
	
	public boolean hasOverlaps() {
		
		for( int i=0; i<this.size()-1; i++ )
		{
			Section section1 = this.get( i );
			
			for( int j=i+1; j<this.size(); j++ )
			{
				//System.out.println( i + " " + j );
				
				Section section2 = this.get( j );
				
				if( hasOverlaps( section1 , section2 ) )
				{
					return true;
				}
				
				
				
			}
			
		}
		
		return false;
		
	}
	public boolean containsClosedSections() {
		
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			
			if( section.isClosed() )
			{
				return true;
			}
		}
		
		return false;
		
		
	}
	
	public boolean containsClassesBefore( Time time ) {
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			
			if( section.getStartTime().isEarlierThan( time ) )
			{
				return true;
			}
		}
		
		return false;
		
	}
	public boolean containsClassesAfter( Time time ) {
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			
			if( section.getEndTime().isLaterThan( time ) )
			{
				return true;
			}
		}
		
		return false;
		
	}
	public boolean containsClassesLongerThan( int mins ) {
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			
			if( section.getDurationInMinutes() > mins )
			{
				return true;
			}
		}
		
		return false;
		
	}
	public boolean containsBreaksLongerThan( int mins ) {
		
		
		
		for( int day=0; day<6; day++ )
		{
			ArrayList<Section> dailySections = this.getDailySections( day );
			if( dailySections.size() < 2 )
			{
				continue;
			}
			
			for( int i=0; i<dailySections.size()-1; i++ )
			{
				Section s1 = dailySections.get( i );
				Section s2 = dailySections.get( i+1 );
				
				int breakInMins = calculateBreakBetweenInMinutes( s1 , s2 );
				if( breakInMins > mins )
				{
					return true;
				}
				
			}
			
		}
		
		return false;
		
		
	}
	public boolean containsTooManyClassesInOneDay( int maxClasses ) {
		
		for( int day=0; day<6; day++ )
		{
			int classesPerDay = getDailySections( day ).size();
			if( classesPerDay > maxClasses )
			{
				return true;
			}
		}
		
		return false;
		
	}
	public boolean containsOmittedInstructor( OmittedInstructor omittedInstructor ) {
		
		Section section = getSection( omittedInstructor.coursePrefix , omittedInstructor.courseNumber );
		if( section == null )
		{
			String message = "Schedule.containsOmittedInstructor - " + "this schedule doesnt even cotain a section from the course taught by that instructor";
			message += "\n" + "omitted instructor: " + omittedInstructor.coursePrefix + " - " + omittedInstructor.courseNumber + "   " + omittedInstructor.instructor;
			message += "\n" + "schedule:";
			message += this.toString();
			throw new IllegalArgumentException( message );
		}
		
		return section.getInstructor().equals( omittedInstructor.instructor );
		
	}
	public boolean containsOmittedInstructor( ArrayList<OmittedInstructor> omittedInstructors ) {
		
		for( int i=0; i<omittedInstructors.size(); i++ )
		{
			OmittedInstructor omittedInstructor = omittedInstructors.get( i );
			if( containsOmittedInstructor( omittedInstructor ) )
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	
	
	public ArrayList<Section> getDailySections( int day ){
		
		//used in calculating breaks
		
		ArrayList<Section> output = new ArrayList<Section>();
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			if( section.hasDay( day ) )
			{
				output.add( section );
			}
			
		}
		
		Collections.sort( output , new Comparator<Section>() {
			
			public int compare( Section s1 , Section s2 ) {
				
				//-1 = less than
				//+1 = greater than
				
				Time start1 = s1.getStartTime();
				Time start2 = s2.getStartTime();
				
				if( start1.isEarlierThan( start2 ) )
				{
					return -1;
				}
				else if( start1.isLaterThan( start2 ) )
				{
					return +1;
				}
				else
				{
					return 0;
				}
				
				
				
			}
			
			
		} );
		
		
		return output;
		
	}

	public Section getSection( String coursePrefix , String courseNumber ) {
		
		for( int i=0; i<this.size(); i++ )
		{
			Section section = this.get( i );
			
			if(    section.getPrefix().equals( coursePrefix )   &&   section.getCourseNumber().equals( courseNumber )    )
			{
				return section;
			}
		}
		
		return null;
		
	}
	
	
	
	public static boolean hasOverlaps( Section s1 , Section s2 ) {
		
		
		Time start1 = s1.getStartTime();
		Time end1 = s1.getEndTime();
		
		Time start2 = s2.getStartTime();
		Time end2 = s2.getEndTime();
		
		
		
		for( int day=0; day<6; day++ )
		{
			if( s1.hasDay( day ) && s2.hasDay( day ) )
			{
				
				boolean noOverlap = start1.isLaterThan( end2 ) || start2.isLaterThan( end1 );
				boolean overlap = !noOverlap;
				
				if( overlap )
				{
					return true;
				}
				
			}
		}
		
		return false;
		
		
	}
	public static int calculateBreakBetweenInMinutes( Section s1 , Section s2 ) {
		
		Time start1 = s1.getStartTime();
		Time start2 = s2.getStartTime();
		Time end1 = s1.getEndTime();
		Time end2 = s2.getEndTime();
		
		if( start1.isLaterThan( start2 ) )
		{
			String message = "Schedule.getBreakBetween(): " + "s1 starts after s2! s1 should come first";
			message += "\n" + "s1: " + s1;
			message += "\n" + "s2: " + s2;
			throw new IllegalArgumentException( message );
		}
		
		if( hasOverlaps( s1 , s2 ) )
		{
			String message = "Schedule.getBreakBetween(): " + "s1 and s2 overlap!";
			message += "\n" + "s1: " + s1;
			message += "\n" + "s2: " + s2;
			throw new IllegalArgumentException( message );
		}
		
		
		return start2.getMinutes() - end1.getMinutes();
		
		
		
		
	}
	
	
	
	//used to calculate all schedule combinations
	public static ArrayList<Schedule> computeAllSchedules( ArrayList<Course> selectedCourses ) {
		
		
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		
		for( int i=0; i<selectedCourses.size(); i++ )
		{
			Course courseToAdd = selectedCourses.get( i );
			schedules = addCourse( schedules , courseToAdd );
		}
		
		schedules = removeOverlaps( schedules );
		return schedules;
		
	}
	private static ArrayList<Schedule> addCourse( ArrayList<Schedule> schedules , Course course ) {
		
		boolean debug = false;
		
		if( debug ) System.out.println( "addCourse()" );
		if( debug ) System.out.println( "schedules thus far: " + schedules.size() );
		if( debug ) System.out.println( ArrayToString.getString( schedules , "\n" + "=====" + "\n" ) );
		if( debug ) System.out.println( "\n" + "course to add: " + course );
		
		if( schedules.isEmpty() )
		{
			schedules.add( new Schedule() );
		}
		
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<schedules.size(); i++ )
		{
			Schedule schedule = schedules.get( i );
			
			for( int j=0; j<course.getSectionCount(); j++ )
			{
				Section section = course.getSection( j );
				
				Schedule newSchedule = new Schedule();
				newSchedule.addAll( schedule );
				
				newSchedule.add( section );
				output.add( newSchedule );
			}
		}
		
		if( debug ) System.out.println( "\n\n\n\n" + " v v v v v v v v " + "\n\n\n\n" );
		if( debug ) System.out.println( "   output   " );
		if( debug ) System.out.println( ArrayToString.getString( output , "\n" + "=====" + "\n" ) );
		
		if( debug ) System.out.println( "\n\n\n\n" + "============================================" + "\n\n\n\n" );
		
		return output;
		
	}
	
	
	
	//filter by criteria
	public static ArrayList<Schedule> removeOverlaps( ArrayList<Schedule> input ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.hasOverlaps() )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	public static ArrayList<Schedule> removeClosedSections( ArrayList<Schedule> input ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsClosedSections() )
			{
				output.add( schedule );
			}
		}
		
		
		return output;
		
	}
	
	public static ArrayList<Schedule> removeClassesBefore( ArrayList<Schedule> input , Time noClassesBefore ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsClassesBefore( noClassesBefore ) )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	public static ArrayList<Schedule> removeClassesAfter( ArrayList<Schedule> input , Time noClassesAfter ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsClassesAfter( noClassesAfter ) )
			{
				output.add( schedule );
			}
		}
		
		
		return output;
		
	}
	public static ArrayList<Schedule> removeClassesLongerThan( ArrayList<Schedule> input , int mins ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsClassesLongerThan( mins ) )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	public static ArrayList<Schedule> removeBreaksLongerThan( ArrayList<Schedule> input , int mins ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsBreaksLongerThan( mins ) )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	public static ArrayList<Schedule> removeTooManyClassesInOneDay( ArrayList<Schedule> input , int maxClasses ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsTooManyClassesInOneDay( maxClasses ) )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	public static ArrayList<Schedule> removeContainsOmittedInstructors( ArrayList<Schedule> input , ArrayList<OmittedInstructor> omittedInstructors ){
		
		ArrayList<Schedule> output = new ArrayList<Schedule>();
		for( int i=0; i<input.size(); i++ )
		{
			Schedule schedule = input.get( i );
			if( !schedule.containsOmittedInstructor( omittedInstructors ) )
			{
				output.add( schedule );
			}
		}
		
		return output;
		
	}
	
	
	public String toString() {
		
		return ArrayToString.getString( this , "\n" );
		
	}
	
	
	
	private static void computeSchedulesTest() {
		
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
		
		ArrayList<Schedule> schedules = computeAllSchedules( selectedCourses );
		ArrayToString.printArray( schedules , "\n" + "---------------------------------------" + "\n" );
		
	}
	private static void overlapTest1() {
		
		Section s1 = new Section( null , "001" , "teacher" , "Mon & Wed" , new Time("10:00 am") , new Time("12:00 pm") , "room" , "call#" , true );
		Section s2 = new Section( null , "001" , "teacher" , "Mon & Wed" , new Time("1:00 pm") , new Time("1:30 pm") , "room" , "call#" , true );
		
		System.out.println( hasOverlaps( s1 , s2 ) );
		
		
	}
	private static void overlapTest2() {
		
		
		
	}
	private static void dailySectionsTest() {
		
		Section s1 = new Section( null , "001" , "teacher" , "Mon & Wed" , new Time("1:00 pm") , new Time("1:30 pm") , "room" , "call#" , true );
		Section s2 = new Section( null , "001" , "teacher" , "Mon & Wed" , new Time("10:00 am") , new Time("10:30 am") , "room" , "call#" , true );
		Section s3 = new Section( null , "001" , "teacher" , "Mon & Tues" , new Time("11:00 am") , new Time("11:30 am") , "room" , "call#" , true );
		Schedule schedule = new Schedule();
		schedule.add( s1 );
		schedule.add( s2 );
		schedule.add( s3 );
		
		ArrayList<Section> dailySections = schedule.getDailySections( 2 );
		ArrayToString.printArray( dailySections );
		
	}
	
	
	public static void main(String[] args) {
		
		
		//computeSchedulesTest();
		//overlapTest1();
		dailySectionsTest();
		
	}
	
	
	
	
	
	
}



































