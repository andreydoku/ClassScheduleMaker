package tables;

import RowFilters.SearchFilter;
import basic.*;
import table.MyTable;
import table.MyTableModel;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import myLibrary.Time;



public class SectionsTable extends MyTable {

	
	public MyTableModel model = new MyTableModel( new Section() );

	

	public SectionsTable(){
		
		setModel( model );
		this.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		model.setRowFilter( new ClosedSectionFilter() );

	}

	public void addNewSection( Course c ){

		//model.addRow();
		Section newSection = new Section( c );
		addSection( newSection );
		

		
		int actualR = model.getActualRowCount()-1;
		int actualC = model.getActualC_fromName( "Section #" );
		
		int displayedR = model.getDisplayedR( actualR );
		int displayedC = model.getDisplayedC( actualC );

		if ( displayedR != -1  &&  displayedC != -1 )
		{
			this.changeSelection( displayedR , displayedC , false , false );
		}


		/*
		
		//start cell editing
		boolean success = this.editCellAt( displayedR , displayedC );
		if (success)
		{
			// Select cell
			boolean toggle = false;
			boolean extend = false;
			this.changeSelection( displayedR , displayedC , toggle , extend );


			DefaultCellEditor ed = (DefaultCellEditor) this.getCellEditor();
			JTextField jf = (JTextField)ed.getComponent();
			jf.selectAll();
			jf.requestFocusInWindow();
			

		}
		else
		{
			// Cell could not be edited
			
		}
		*/
	}
	
	public void addSection( Section s ){
		
		model.addRow( s );
		
	}
	
	public void removeActualSection( int actualR ){

		model.removeActualRow( actualR );
		//model.printOrderedRows();
		
	}
	public void removeDisplayedSection( int displayedR ){
		
		int actualR = model.getActualR( displayedR );
		removeActualSection( actualR );
		
	}
	public void removeSelectedSection(){


		int displayedR = getSelectedRow();
		removeDisplayedSection( displayedR );


		if ( this.getRowCount() > 0 )
		{

			if ( displayedR == this.getRowCount() )//last row was removed
			{
				this.getSelectionModel().setSelectionInterval( displayedR-1 , displayedR-1 );
			}
			else
			{
				this.getSelectionModel().setSelectionInterval( displayedR , displayedR );
			}


		}
		
	}
	public void removeAllSections(){

		model.removeAllRows();

	}

	public void setCourse( Course c ){

		removeAllSections();

		for ( int i=0;i<c.getSectionCount();i++ )
		{

			Section s = c.getSection( i );
			addSection( s );

		}

	}
	public void setSchedule( Schedule schedule ) {
		
		removeAllSections();
		
		if( schedule == null )
		{
			return;
		}
		
		
		for ( int i=0;i<schedule.size();i++ )
		{

			Section s = schedule.get( i );
			addSection( s );

		}
		
	}
	
	
	public Section getActualSection( int actualR ){

		return (Section) model.getActualRowData( actualR );

	}
	public Section getActualLastSection(){

		int actualR_last = model.getActualRowCount() - 1;
		return (Section) model.getActualRowData( actualR_last );

	}
	public Section getDisplayedSection( int displayedR ){
		
		int actualR = model.getActualR( displayedR );
		return getActualSection( actualR );
		
	}
	public Section getSelectedSection(){

		int displayedR = getSelectedRow();
		if( displayedR == -1 )
		{
			return null;
		}
		
		return getDisplayedSection( displayedR );

	}
	public ArrayList<Section> getAllSections(){

		ArrayList<Section> allSections = new ArrayList<Section>();

		for( int actualR=0;actualR<model.getActualRowCount();actualR++ )
		{
			Section s = getActualSection( actualR );
			allSections.add( s );
		}

		return allSections;
	}
	
	public TableCellRenderer getCellRenderer( int displayedR , int displayedC ) {

		return super.getCellRenderer( displayedR , displayedC );

		

	}
	public TableCellEditor getCellEditor( int displayedR , int displayedC ) {


		return super.getCellEditor( displayedR , displayedC );

	}


	public void setSearchText( String text ){

		SearchFilter filter = (SearchFilter) model.getRowFilter();
		filter.setSearchText( text );
		model.applyFilter();

	}


	public void buildStartEditor(){

		ArrayList<String> times = new ArrayList<String>();

		Time first = new Time( "8:30 am" );
		Time last = new Time( "10:00 pm" );

		Time t = first;

		times.add( t.toString() );

		while ( t.isEarlierThan( last ) )
		{

			t.add( 30 );
			times.add( t.toString() );

		}

		int size = times.size();
		String[] timesArray = new String[ size ];

		for ( int i=0;i<size;i++ )
		{
			timesArray[i] = times.get( i );
		}

		JComboBox startEditor = new JComboBox( timesArray );

	}
	public void buildEndEditor(){

		ArrayList<String> times = new ArrayList<String>();

		Time first = new Time( "9:00 am" );
		Time last = new Time( "10:00 pm" );

		Time t = first;

		

		while ( t.isEarlierThan( last ) )
		{
			
						
			t.add( 15 );//15
			if ( t.isEarlierThan( last ) )
			{
				times.add( t.toString() );
			}

			t.add( 15 );//30
			if ( t.isEarlierThan( last ) )
			{
				times.add( t.toString() );
			}

			t.add( 15 );//45
			if ( t.isEarlierThan( last ) )
			{
				times.add( t.toString() );
			}

			t.add( 5 );//50
			if ( t.isEarlierThan( last ) )
			{
				times.add( t.toString() );
			}
			
			
			t.add( 10 );
			

		}

		int size = times.size();
		String[] timesArray = new String[ size ];

		for ( int i=0;i<size;i++ )
		{
			timesArray[i] = times.get( i );
		}

		JComboBox endEditor = new JComboBox( timesArray );

	}
	public void buildDaysEditor(){

		String[] days = { "MWF" , "MW" , "TR" , "M" , "T" , "W" , "R" , "F" , "S" };
		JComboBox daysEditor = new JComboBox( days );

	}
	
	public void setFiltering( boolean filtering ){

		ClosedSectionFilter filter = (ClosedSectionFilter) model.getRowFilter();
		filter.filtering = filtering;
		model.applyFilter();

	}





}


















