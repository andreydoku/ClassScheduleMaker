package parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import basic.Course;
import basic.Section;

import myLibrary.*;


public class Parser{
	
	
	final static String COURSEBOOK_ADDRESS = "https://coursebook.utdallas.edu/";
	
	
	public static ArrayList<String> extractTerms() throws IOException{
		
		
		String address = COURSEBOOK_ADDRESS + "guidedsearch";
		ArrayList<String> html = UrlReader.getHtmlArray( address , "</select></div></td>" , true );
		//ArrayList<String> html = UrlReader.getHtmlArray( address);
		
		//ArrayToString.printArray( html );
		
		String lastLine = html.get( html.size()-1 );
		//System.out.println( lastLine );
		
		ArrayList<String> terms = Methods.getAllSubstrings( lastLine , "<option value=" , "</option>" );	
		//Methods.printArray( substrings );
		
		for( int i=0; i<terms.size(); i++ )
		{
			String term = terms.get( i );
			int index = term.indexOf( ">" );
			term = term.substring( index+1 );
			terms.set( i , term );
			//System.out.println( term );
		}
		
		terms.remove( 0 );
				
		return terms;
		
	}
	
	
	public static Course extractCourse_old( String term , String prefix , String courseNumber , ActionListener listener ) throws IOException {
		
		
		String address = COURSEBOOK_ADDRESS + prefix + courseNumber + "/term_" + term + "?";
		//System.out.println( "address: " + address );
		
		String message = "Downloading course info from: " + address;
		if( listener != null ) listener.actionPerformed( new ActionEvent( message , ActionEvent.ACTION_PERFORMED , null ) );
		
		
		
		ArrayList<String> htmlArray = UrlReader.getHtmlArray( address );
		//ArrayToString.printArray( htmlArray );
		
		
		String noneFound = "No course sections matched your search criteria.";
		if( Methods.contains( htmlArray , noneFound ) )
		{
			System.out.println( noneFound );
			return null;
		}
		
		String start = "<div class=\"search-panel-form-div\" align=\"center\">";
		String end = "The direct link to this search is";
		String delimiter = "<tr id=\"r";
		
		
		htmlArray = Methods.getSubstringArray( htmlArray , start , end );
		//ArrayToString.printArray( htmlArray , "\n" , false );
		
		
		Course output = null;
		
		ArrayList<ArrayList<String>> htmlArrayOfArrays = Methods.split( htmlArray , delimiter );
		htmlArrayOfArrays.remove( 0 );
		for( int i=0; i<htmlArrayOfArrays.size(); i++ )
		{
			ArrayList<String> sectionHtml = htmlArrayOfArrays.get( i );
			//ArrayToString.printArray( sectionHtml );
			
			Section section = parseSection_old( sectionHtml );
			
			
			if( i == 0 )
			{
				output = section.getCourse();
				if( listener != null ) listener.actionPerformed( new ActionEvent( output , ActionEvent.ACTION_PERFORMED , null ) );
			}
			
			output.add( section );
			if( listener != null ) listener.actionPerformed( new ActionEvent( section , ActionEvent.ACTION_PERFORMED , null ) );
			
			
			
			
			//System.out.println(  );
			
			//System.out.println( "=============================================================" );
		}
		
		
		if( listener != null ) listener.actionPerformed( new ActionEvent( "\n" + "Done" , ActionEvent.ACTION_PERFORMED , null ) );
		return output;
		
		
	}
	private static Section parseSection_old( ArrayList<String> sectionHtml ) {
		
		
		boolean debug = true;
		
		sectionHtml.set( 0 , "<" + sectionHtml.get( 0 ) );
		ArrayList<String> data = Methods.splitHtml( sectionHtml , false );
		
		if( data.size() == 16 )
		{
			data.remove( 5 );
			data.remove( 5 );
		}
		if( debug ) System.out.println( "parseSection2" );
		if( debug ) ArrayToString.printArray( sectionHtml );
		if( debug ) ArrayToString.printArray( data , "\n" , true );
		if( debug ) System.out.println( "==============================================================" );
		
		String term = data.get( 0 );
		boolean isOpen = data.get( 1 ).toLowerCase().equals( "open" );
		
		String prefixCoursenumberSectionNumber = data.get( 2 );
		String prefix = prefixCoursenumberSectionNumber.substring( 0 , prefixCoursenumberSectionNumber.indexOf( " " ) );
		String courseNumber = prefixCoursenumberSectionNumber.substring( prefixCoursenumberSectionNumber.indexOf( " " ) + 1 , prefixCoursenumberSectionNumber.indexOf( "." ) );
		String sectionNumber = Methods.getSubstring( prefixCoursenumberSectionNumber , "." );
		
		String classNumber = data.get( 3 );
		String classTitle = data.get( 4 ).replace( " -" , "" );
		
		
		String instructor = data.get( 5 );
		if( !Character.isUpperCase( instructor.charAt( 0 ) ) )
		{
			instructor = "TBA";
		}
		instructor = instructor.trim();
		
		
			
			
		String days = data.get( 6 );
		
		String times = data.get( 7 );
		String[] timesArray = times.split( " - " );
		Time startTime = new Time( timesArray[0] );
		Time endTime = new Time( timesArray[1] );
		
		String room = data.get( 8 );
		
		Course course = new Course( prefix , courseNumber , classTitle , term );
		Section section = new Section( course , sectionNumber , instructor , days , startTime , endTime , room , classNumber , isOpen );
		
		//System.out.println( section );
		return section;
		
	}
	
	
	
	
	public static Course extractCourse( String term , String prefix , String courseNumber , ActionListener listener ) throws IOException {
		
		String address = COURSEBOOK_ADDRESS + prefix + courseNumber + "/term_" + term + "?";
		//System.out.println( "address: " + address );
		
		String message = "Downloading course info from: " + address;
		if( listener != null ) listener.actionPerformed( new ActionEvent( message , ActionEvent.ACTION_PERFORMED , null ) );
		
		
		
		ArrayList<String> htmlArray = UrlReader.getHtmlArray( address );
		//ArrayToString.printArray( htmlArray );
		
		
		String noneFound = "No course sections matched your search criteria.";
		if( Methods.contains( htmlArray , noneFound ) )
		{
			System.out.println( noneFound );
			return null;
		}
		
		String start = "<div class=\"search-panel-form-div\" align=\"center\">";
		String end = "The direct link to this search is";
		
		
		htmlArray = Methods.getSubstringArray( htmlArray , start , end );
		//ArrayToString.printArray( htmlArray , "\n" , false );
		
		
		ArrayList<ArrayList<String>> table = UrlReader.parseHtmlTable( htmlArray );
		//ArrayToString.printArrayOfArrays_oneLinePerRow( table );
		
		table.remove( 0 );
		table.remove( 0 );
		
		
		Course output = null;
		for( int i=0; i<table.size(); i++ )
		{
			ArrayList<String> row = table.get( i );
			Section section = parseSection( row );
			
			if( i == 0 )
			{
				output = section.getCourse();
				if( listener != null ) listener.actionPerformed( new ActionEvent( output , ActionEvent.ACTION_PERFORMED , null ) );
			}
			
			output.add( section );
			if( listener != null ) listener.actionPerformed( new ActionEvent( section , ActionEvent.ACTION_PERFORMED , null ) );
			
		}
		
		if( listener != null ) listener.actionPerformed( new ActionEvent( "\n" + "Done" , ActionEvent.ACTION_PERFORMED , null ) );
		return output;
		
	}
	private static Section parseSection( ArrayList<String> row ) {
		
		//----- cell 0 -----
		
		String[] cell0_pieces = row.get( 0 ).split( "\n" );
		String term = cell0_pieces[0];
		boolean isOpen = cell0_pieces[1].toLowerCase().contains( "open" );
		
		//----- cell 1 -----
		
		String[] cell1_pieces = row.get( 1 ).split( "\n" );
		
		String prefixCoursenumberSectionNumber = cell1_pieces[0];
		String prefix = prefixCoursenumberSectionNumber.substring( 0 , prefixCoursenumberSectionNumber.indexOf( " " ) );
		String courseNumber = prefixCoursenumberSectionNumber.substring( prefixCoursenumberSectionNumber.indexOf( " " ) + 1 , prefixCoursenumberSectionNumber.indexOf( "." ) );
		String sectionNumber = Methods.getSubstring( prefixCoursenumberSectionNumber , "." );
		
		String classNumber = cell1_pieces[1];
		
		//----- cell 2 -----
		
		String classTitle = row.get( 2 );
		
		int indexOf_spaceDashspace = classTitle.indexOf( " - " );
		if( indexOf_spaceDashspace != -1 )
		{
			classTitle = classTitle.substring( 0 , indexOf_spaceDashspace );
		}
		
		int indexOf_parentheses = classTitle.indexOf( "(" );
		if( indexOf_parentheses != -1 )
		{
			classTitle = classTitle.substring( 0 , indexOf_parentheses );
		}
		classTitle = classTitle.trim();
		
		
		//----- cell 3 -----
		
		String instructor = row.get( 3 );
		if( !Character.isUpperCase( instructor.charAt( 0 ) ) )
		{
			instructor = "TBA";
		}
		instructor = instructor.trim();
		
		//----- cell 4 -----
		
		String[] cell4_pieces = row.get( 4 ).split( "\n" );
			
		String days = cell4_pieces[0];
				
		String bothTimesString = cell4_pieces[1];
		String[] timesArray = bothTimesString.split( " - " );
		Time startTime = new Time( timesArray[0] );
		Time endTime = new Time( timesArray[1] );
		
		String room = cell4_pieces[2];
		
		//---- done pulling data from table
		
		
		
		
		Course course = new Course( prefix , courseNumber , classTitle , term );
		Section section = new Section( course , sectionNumber , instructor , days , startTime , endTime , room , classNumber , isOpen );
		
		//System.out.println( section );
		return section;
		
		
		
	}
	
	
	
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		
		//extractTerms();
		
		Course course = extractCourse( "18s" , "MATH" , "2417" , null );
		System.out.println( course );
		
		//extractCourse( "18s" , "RHET" , "1302" , null );
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}














































