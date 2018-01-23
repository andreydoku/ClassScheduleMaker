/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tables;

import RowFilters.MyRowFilter;
import basic.Section;
import table.RowData;

/**
 *
 * @author Andrey
 */
public class ClosedSectionFilter implements MyRowFilter {

	boolean filtering = false;;



	public boolean isRowHidden(RowData data) {

		Section s = (Section) data;

		if( filtering )
		{
			return ! s.getOpen();
		}
		else
		{
			return false;
		}


	}

}
