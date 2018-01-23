package tables;

import RowFilters.SearchFilter;
import basic.Course;
import basic.Section;
import table.MyTable;
import table.MyTableModel;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;



public class CoursesTable extends MyTable {

    
    public MyTableModel model = new MyTableModel( new Course() );



    public CoursesTable(){


		setModel( model );
		this.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		model.setRowFilter( new SearchFilter() );
		
		
		this.setHeaderHeight( 26 );
		this.setRowHeight( 20 );
		this.getTableHeader().setOpaque( false );
		
		
		
		
    }
    
    public void addNewCourse( boolean select ){

		Course c = new Course();
		model.addRow( c );



		int actualR = model.getActualRowCount()-1;
		int actualC = model.getActualC_fromName( "Prefix" );

		int displayedR = model.getDisplayedR( actualR );
		int displayedC = model.getDisplayedC( actualC );

		if ( displayedR != -1  &&  displayedC != -1  &&  select )
		{
			this.changeSelection( displayedR , displayedC , false , false );
		}

		/*
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
	public void addCourse( Course c ){

		model.addRow( c );

	}
	public void addCourses( ArrayList<Course> courses ){

		for( int i = 0;i<courses.size();i++ )
		{
			Course c = courses.get(i);
			addCourse( c );
		}

	}
	public void setCourses( ArrayList<Course> courses ){

		model.removeAllRows();
		addCourses( courses );

	}

	public void addSection( Section s , boolean overwrite ){

		String prefix = s.getPrefix();
		String courseNumber = s.getCourseNumber();
		String courseName = s.getCourseName();
		String term = s.getTerm();
		
		String sectionNumber = s.getSectionNumber();
		
		int courseIndex = getActualIndexOf( prefix , courseNumber );
		if( courseIndex == -1 ) //table doesnt have that course, so add a new course, and add the section to it
		{
			Course c = new Course( prefix , courseNumber , courseName , term );
			int sectionIndex = c.indexOf( sectionNumber );

			if( sectionIndex == -1 ) // course doesnt have section with that section number, so just add
			{
				c.add( s );
				addCourse( c );
			}
			else // course already has a section with that section number, so overwrite
			{
				c.setSection( sectionIndex , s );
				addCourse( c );
			}
			
			
		}
		else // table already has that course, so add the section to it
		{
			Course c = getActualCourse( courseIndex );
			c.add( s );
		}

	}
	public void addSections( ArrayList<Section> sections , boolean overwrite ){

		for( int i=0;i<sections.size();i++ )
		{
			Section s = sections.get(i);
			this.addSection( s , overwrite );
		}

	}
	public int getActualIndexOf( String prefix , String courseNumber ){


		for( int actualR=0;actualR<this.getActualRowCount();actualR++ )
		{
			Course c = this.getActualCourse( actualR );
			if(  c.getPrefix().equals(prefix)  &&  c.getCourseNumber().equals(courseNumber) )
			{
				return actualR;
			}
		}

		return -1;

	}



    public void removeActualCourse( int actualR ){

		model.removeActualRow( actualR );
		//model.printOrderedRows();

    }
    public void removeDisplayedCourse( int displayedR ){

		int actualR = model.getActualR( displayedR );
		removeActualCourse( actualR );

    }
    public void removeSelectedCourse(){


		int displayedR = getSelectedRow();
		removeDisplayedCourse( displayedR );


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
    public void removeAllCourses(){

		model.removeAllRows();

    }


    public Course getActualCourse( int actualR ){

            return (Course) model.getActualRowData( actualR );

    }
    public Course getDisplayedCourse( int displayedR ){

            if ( displayedR != -1 )
            {
                    int actualR = model.getActualR( displayedR );
                    if ( actualR != -1 )
                    {
                            return (Course) model.getActualRowData( actualR );
                    }
                    else
                    {
                            return null;
                    }
            }
            else
            {
                    return null;
            }


    }
    public Course getSelectedCourse(){

            int displayedR = this.getSelectedRow();
            //System.out.println( "selectedDisplayedRow: " + displayedR );

            if ( displayedR != -1 )
            {
                    int actualR = model.getActualR( displayedR );
                    return getActualCourse( actualR );
            }
            else
            {
                    return null;
            }

    }
    public ArrayList<Course> getAllCourses(){

		ArrayList<Course> allCourses = new ArrayList<Course>();

		for( int actualR=0;actualR<model.getActualRowCount();actualR++ )
		{
			Course c = getActualCourse( actualR );
			allCourses.add( c );
		}

		return allCourses;
	}
    
    public int getCreditHours(){
    	
    	int hours = 0;
    	
    	for( int actualR=0;actualR<model.getActualRowCount();actualR++ )
		{
			Course c = getActualCourse( actualR );
			hours += c.getCreditHours();
		}
    	
    	return hours;
    	
    }
    
    
    
    public void setSearchText( String text ){

		SearchFilter filter = (SearchFilter) model.getRowFilter();
		filter.setSearchText( text );
		model.applyFilter();


    }

    









}


















