

package plot;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.*;

import basic.*;
import myLibrary.Methods;
import myLibrary.Time;


public class SchedulePlot extends JPanel {


	int bx = 60;
	int by = 30;
	int days = 5;

	Time start = new Time( "8:00 am" );
	Time end = new Time( "11:00 pm" );
	
	private double pixelsPerMin = 0;
	private double pixelsPerDay = 0;
	
	int minuteGridlineInterval = 30;
	int minuteLabelInterval = 60;


	Color plotBackgroundColor = Color.white;
	Color borderBackgroundColor = new Color( 220,220,255 );
	Color borderLineColor = Color.black;
	Color gridlineColor = Color.gray;
	Color labelColor = Color.black;
	
	Color sectionColor = new Color( 100,100,255 );
	Color highlightedSectionColor = sectionColor.brighter();
	Color sectionBorderColor = Color.black;
	Color sectionFontColor = Color.white;
	
	String fontName = "Tahoma";
	int labelFontSize = 10;
	int sectionFontSize = 8;
	
	private int z = 10;

	private ArrayList<Section> sections = new ArrayList<Section>();
	private Section highlightedSection = null;





	public SchedulePlot(){
		
		start = new Time( "8:00 am" );
		end = new Time( "11:00 pm" );
		
	}
	public SchedulePlot( ArrayList<Section> sections ){
		
		this();
		set( sections );
		
	}

	public void add( Section s ){

		this.sections.add( s );
		repaint();

	}
	public void remove( Section s ){

		sections.remove( s );
		repaint();
		
	}
	public void removeAll(){

		sections.clear();
		repaint();

	}
	public void set( ArrayList<Section> sections ){

		this.sections = sections;
		repaint();
		
	}
	public void set( Schedule schedule ) {
		
		this.sections = schedule;
		repaint();
		
	}
	
	
	
	private void calcVars(){


		int minuteSpan = end.getMinutes() - start.getMinutes();

		double gridHeight = this.getHeight() - 2*by ;
		pixelsPerMin = gridHeight / minuteSpan ;

		double gridWidth = this.getWidth() - 2*bx ;
		pixelsPerDay = gridWidth / days;

		
		
		
		

	}
	
	private int getPixelX( int day ){
		
		return (int)( bx + day * pixelsPerDay );
		
	}
	private int getPixelY( int mins ){
		
		return (int)( by + ( mins - start.getMinutes() ) * pixelsPerMin );
		
	}
	
	private int getDayAt( int pixelX ){

		if( pixelX >= bx  &&  pixelX <= this.getWidth() - bx )
		{
			double day = ( pixelX - bx )/ pixelsPerDay ;
			return (int) day;
		}
		else
		{
			return -1;
		}

	}
	private int getMinsAt( int pixelY ){

		if( pixelY >= by  &&  pixelY <= this.getHeight() - by )
		{
			double mins = ( pixelY - by ) / pixelsPerMin + start.getMinutes();
			return (int) mins ;
		}
		else
		{
			return -1;
		}

	}
	
	private String getDayTextAt( int pixelX ){

		int day = getDayAt( pixelX );

		if( day != -1 )
		{
			return SchedulePlot.getDayText( day );
		}
		else
		{
			return null;
		}

	}
	private Time getTimeAt( int pixelY ){
		
		int mins = getMinsAt( pixelY );
		if( mins != -1 )
		{
			return new Time( mins );
		}
		else
		{
			return null;
		}
		
		
		
	}
	private Section getSectionAt( int pixelX , int pixelY ){
		
		int day = getDayAt( pixelX );
		Time time = getTimeAt( pixelY );

		if( day == -1 || time == null )
		{
			return null;
		}

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			if( s.hasDay(day) )
			{
				
				if(  ! time.isEarlierThan( s.getStartTime() )   &&   ! time.isLaterThan( s.getEndTime() )   )
				{
					return s;
				}
			}
		}

		return null;
		
	}
	
	public void setHighlighted( Section s ){

		this.highlightedSection = s;
		repaint();
		
	}
	
	
	private static String getDayText( int day ){

//		switch ( day )
//		{
//			case 0: return "M";
//			case 1: return "T";
//			case 2: return "W";
//			case 3: return "R";
//			case 4: return "F";
//			case 5: return "S";
//			default: throw new IllegalArgumentException( "day has to be 0 thru 5, not " + day );
//
//		}
		
		switch ( day )
		{
			case 0: return "Mon";
			case 1: return "Tue";
			case 2: return "Wed";
			case 3: return "Thu";
			case 4: return "Fri";
			case 5: return "Sat";
			default: throw new IllegalArgumentException( "day has to be 0 thru 5, not " + day );

		}
		
		

	}
	
	
	protected void paintComponent( Graphics gfx ) {

		super.paintComponent( gfx );
		Graphics2D g = (Graphics2D) gfx;
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON ); 		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON ); 		g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING , RenderingHints.VALUE_COLOR_RENDER_QUALITY );

		calcVars();

		paintBorderBackground( g, borderBackgroundColor );
		paintPlotBackground( g , plotBackgroundColor );

		paintAllMinuteGridlines( g, gridlineColor );
		paintAllMinuteLabels( g , labelColor );

		paintAllDayGridlines( g, gridlineColor );
		paintAllDayLabels( g, labelColor );
		
		paintBorderLine( g , borderLineColor );
		
		paintAllSections( g , this.sections , sectionColor , highlightedSectionColor , sectionBorderColor );


		
	}

	private void paintBorderBackground( Graphics2D g , Color color ){

		g.setColor( color );
		g.fillRect( 0 , 0 , this.getWidth() , this.getHeight() );

	}
	private void paintPlotBackground( Graphics2D g , Color color ){
		
		
		g.setColor( color );		
		g.fillRect( getPixelX(0) , getPixelY( start.getMinutes() ) , this.getWidth() - 2*bx , this.getHeight() - 2*by );
		

	}
	private void paintBorderLine( Graphics2D g , Color color ){

		g.drawRect( bx , by , this.getWidth() - 2*bx , this.getHeight() - 2*by );

	}

	private void paintMinuteGridline( Graphics2D g , int mins , Color color ){

		
		int y = getPixelY( mins );

		int xStart = bx;
		int xEnd = this.getWidth() - bx;

		g.setColor( color );
		g.drawLine( xStart , y , xEnd , y );


	}
	private void paintAllMinuteGridlines( Graphics2D g , Color color ){

		for( int mins = start.getMinutes() ; mins <= end.getMinutes() ; mins += minuteGridlineInterval )
		{
			paintMinuteGridline( g , mins , color );
		}

	}

	private void paintMinuteLabel( Graphics2D g , int mins , Color color ){

		int y = getPixelY( mins );

		Time time = new Time( mins );
		String text = time.toString();

		g.setColor( color );
		Font font = new Font( this.fontName , Font.PLAIN , labelFontSize );
		g.setFont( font );
		g.drawString( text , z , y+5 );
		g.drawString( text , this.getWidth() - bx + z , y+5 );

	}
	private void paintAllMinuteLabels( Graphics2D g , Color color ){

		for( int mins = start.getMinutes() ; mins <= end.getMinutes() ; mins += minuteLabelInterval )
		{
			paintMinuteLabel( g , mins , color );
		}

	}

	private void paintDayGridline( Graphics2D g , int day , Color color ){

		int x = (int) (bx + pixelsPerDay * day);
		int yStart = by;
		int yEnd = this.getHeight() - by;

		g.setColor( color );
		g.drawLine( x , yStart , x , yEnd );


	}
	private void paintAllDayGridlines( Graphics2D g , Color color ){

		for( int day = 0 ; day <= days ; day ++ )
		{
			paintDayGridline( g , day , color );
		}

	}

	private void paintDayLabel( Graphics2D g , int day , Color color ){

		int x = (int) (bx + pixelsPerDay * day);

		String text = getDayText( day );

		g.setColor( color );
		//g.drawString( text , x + (int)(pixelsPerDay/2) - 2 , by - z );
		//g.drawString( text , x + (int)(pixelsPerDay/2) - 2 , this.getWidth() - by - 10 + z );
		
		
		Rectangle boundsTop = new Rectangle( getPixelX(day) , 0                             , (int)this.pixelsPerDay , by );
		Rectangle boundsBot = new Rectangle( getPixelX(day) , getPixelY( end.getMinutes() ) , (int)this.pixelsPerDay , by );
		
		
		
		
		//g.drawRect( bounds.x , bounds.y , bounds.width , bounds.height );
		
		Font font = new Font( this.fontName , Font.PLAIN , labelFontSize );
		
		Methods.drawStringCenteredOn( g , text , font , boundsTop );
		Methods.drawStringCenteredOn( g , text , font , boundsBot );
		
		
	}
	private void paintAllDayLabels( Graphics2D g , Color color ){

		for( int day = 0 ; day < days ; day ++ )
		{
			paintDayLabel( g , day , color );
		}

	}
	
	private void paintSection( Graphics2D g , Section s , Color color , Color highlightedColor , Color borderColor ){




		int yStart = getPixelY( s.getStartTime().getMinutes() );
		int yEnd = getPixelY( s.getEndTime().getMinutes() );
		int ySpan = yEnd - yStart;
		
		for( int day=0 ; day<days ; day++ )
		{
			if( s.hasDay(day) )
			{
								
				int xStart = getPixelX( day );
				int xEnd = getPixelX( day+1 );
				int xSpan = xEnd - xStart;

				g.setColor( color );
				if( highlightedSection != null )
				{
					if( s.equals( highlightedSection ) )
					{
						g.setColor( highlightedColor );
					}
				}


				g.fillRect( xStart , yStart , xSpan , ySpan );

				g.setColor( borderColor );
				g.drawRect( xStart , yStart , xSpan , ySpan );
				
				g.setColor( sectionFontColor );
				Font font = new Font( this.fontName , Font.PLAIN , sectionFontSize );
				Methods.drawStringCenteredOn( g , s.getPrefix() + " - " + s.getCourseNumber() , font , new Rectangle( xStart , yStart , xSpan , ySpan ) );
				
			}
		}
		
		
		
	}
	private void paintAllSections( Graphics2D g , ArrayList<Section> sections , Color color , Color highlightedColor , Color borderColor ){
		
		if( sections == null )
		{
			return;
		}
		
		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			paintSection( g , s , color , highlightedColor , borderColor );
		}

	}
	
	
	
	
	
	public static void main( String[] args ) {


		final SchedulePlot plot = new SchedulePlot();
		
		
		
		Section s1 = new Section( new Course( "EE" , "3110" , "Devices Lab" , "13f" ) );
		s1.setDays( "Tues & Thurs" );
		s1.setTimes( new Time("5:00pm") , new Time("7:00pm") );

		plot.add( s1 );


		Section s2 = new Section( new Course( "EE" , "3310" , "Devices" , "13f" ) );
		s2.setDays( "Mon & Wed & Fri" );
		s2.setTimes( new Time("11:00am") , new Time("2:00pm") );

		plot.add( s2 );





		plot.addMouseMotionListener( new MouseMotionListener() {

			public void mouseDragged( MouseEvent e ) {}
			public void mouseMoved( MouseEvent e ) {

				int pixelX = e.getX();
				int pixelY = e.getY();
				
				String dayString = plot.getDayTextAt( pixelX );
				Time time = plot.getTimeAt( pixelY );
				
				if( dayString != null && time != null )
				{
					//System.out.println( "moused: " + dayString + " , " + time );
				}
				else
				{
					//System.out.println( "moused: out of bounds" );
				}


				Section s = plot.getSectionAt( pixelX , pixelY );
				plot.setHighlighted( s );
				
				//System.out.println( "moused: " + s );
				
				
				
				

			}

			
		} );


		JFrame	frame = new JFrame();
        frame.add( plot );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 800,800 );
		frame.setLocationRelativeTo( null );
		//frame.setExtendedState( frame.getExtendedState() | frame.MAXIMIZED_BOTH );
        frame.setVisible(true);


	}
	
	
	
	
	


}










