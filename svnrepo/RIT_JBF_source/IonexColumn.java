/*
 * IonexColumn.java
 */

import java.util.*;
import java.awt.*;

/**
 * Represents the column in ionex.
 * The column shows what proteins are inside of it, along with the
 * [NaCl] that is at the top and the bottom of the column
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class IonexColumn extends Component {
    // begin constants
    public static final Color DEFAULT_COLUMN_COLOR = Color.WHITE;
    // end constants

    // begin instance variables
    private Component parent;
    private int columnStartX; // starting X position of the column
    private int columnEndX; // ending X position of the column
    private int columnStartY; // starting Y position of the column
    private int columnEndY; // ending Y position of the column
    private Color color; // the background color of the column
    // end instance variables

    /**
     * Creates a new column that will be drawn on the given graphics 
     * object.  The column will be of the given size.
     */
    public IonexColumn( Component parent,
			Color color,
			int columnStartX,
			int columnStartY,
			int columnEndX,
			int columnEndY ) {
	this.parent = parent;
	this.color = color;
	this.columnStartX = columnStartX;
	this.columnEndX = columnEndX;
	this.columnStartY = columnStartY;
	this.columnEndY = columnEndY;
    }
    
    /**
     * Creates a column with the default background color
     */
    public IonexColumn( Component parent,
			int columnStartX,
			int columnStartY,
			int columnEndX,
			int columnEndY ) {
	this( parent,
	      DEFAULT_COLUMN_COLOR,
	      columnStartX,
	      columnStartY,
	      columnEndX,
	      columnEndY );
    }

    /**
     * Draws the given protiein on the column
     * @pre The previous location is clear
     */
    protected void drawProtein( IonexProteinBand protein,
				Graphics g ) {
	Color originalColor = g.getColor();
	int position = IonexModel.proteinPosition( protein.getPosition() );
	int bandWidth = IonexModel.proteinBandWidth( position,
						     protein.getBandWidth() );
	
	if ( position != IonexModel.BEYOND_COLUMN ) {
	    g.setColor( protein.getColor() );
	    g.fillRect( columnStartX,
			position,
			columnEndX - columnStartX,
			bandWidth );
	    g.setColor( originalColor );
	}
    }

    public void updateColumn( IonexProteinBand protein,
			      Graphics g ) {
	blankColumn( protein, g );
	drawProtein( protein, g );
    }

    /**
     * Updates the column with the given protein information.
     * The column is cleared before updating.  Note that proteins
     * will be updated in order or decreasing position (greatest
     * position first).
     */
    public void updateColumn( IonexProteinBand[] proteins,
			      Graphics g ) {
	proteins = sortByPositionDesc( proteins );

	for ( IonexProteinBand current : proteins ) {
	    updateColumn( current, g );
	}
    }

    /**
     * Clears out the area of the column previously occupied
     * by the given protein
     */
    protected void blankColumn( IonexProteinBand protein,
				Graphics g ) {
	Color originalColor = g.getColor();
	int position = IonexModel.proteinPosition( protein.getOldPosition() );
	int bandWidth = IonexModel.proteinBandWidth( position,
						     protein.getOldBandWidth() );
	if ( position != IonexModel.BEYOND_COLUMN ) {
	    g.setColor( color );
	    g.fillRect( columnStartX,
			position,
			columnEndX - columnStartX,
			bandWidth );
	    g.setColor( originalColor );
	}
    }

    /**
     * Blanks out the entire column visually.
     * Doesn't update the graphics object.
     */
    public void blankColumn( Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( color );
	g.fillRect( columnStartX,
		    columnStartY,
		    columnEndX - columnStartX,
		    columnEndY - columnEndY );
	g.setColor( originalColor );
    }

    public void paintComponent( IonexProteinBand[] proteins,
				Graphics g ) {
	updateColumn( proteins,
		      g );
    }

    /**
     * Converts the given array to a list
     */
    public static java.util.List< IonexProteinBand > arrayToList( IonexProteinBand[] array ) {
	java.util.List< IonexProteinBand > retval = new ArrayList< IonexProteinBand >();
	for( IonexProteinBand current : array ) {
	    retval.add( current );
	}

	return retval;
    }

    /**
     * Sorts the given proteins according to position
     * The protein with the greatest position is put first
     * Note that this is nondestructive.
     */
    public static IonexProteinBand[] sortByPositionDesc( IonexProteinBand[] proteins ) {
	java.util.List< IonexProteinBand > asList = arrayToList( proteins );
	Collections.sort( asList,
			  new Comparator< IonexProteinBand >() {
			      public int compare( IonexProteinBand first,
						  IonexProteinBand second ) {
				  if ( second.getPosition() > first.getPosition() ) {
				      return -1;
				  } else if ( second.getPosition() < first.getPosition() ) {
				      return 1;
				  } else {
				      return 0;
				  }
			      }
			  } );
	return asList.toArray( new IonexProteinBand[ proteins.length ] );
    }
}
