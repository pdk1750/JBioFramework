/*
 * This class extends JPanel to override the paintComponent method in order to
 * represent the output of the spectrometer on a graph. This OutputGraphGUI will
 * be added to the MainPanelGUI. OutputGraphGUI also sets each Ion's xCoordinate
 * instance variable.
 *
 * version 2
 */

/**
 *
 * @author Amanda Fisher
 */
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class OutputGraphGUI extends JPanel {

//private ArrayList<double[]> peakLines;
    private ArrayList<Ion> peakLines;
    int mostHits;
    int width;
    int height;
    int xAxisWidth;
    int yAxisHeight;
    int xAxisStartingPoint;
    int yAxisStartingPoint;
    int halfHashMarkLength;
    
    /**
     * setPeaks is called by the Spectrometer class to tell OutputGraphGUI where
     * to draw the lines that represent ion peaks.
     *
     * @param pL ArrayList of two element double arrays where each array's first
     * entry is the peak's mass charge ratio, and the second entry is the peak's
     * intensity.
     * @param mH Spectrometer gives the int number of the most hits for a specific
     * ion occuring so intensity of each peak can be calculated.
     */
    public void setPeaks(ArrayList<Ion> pL, int mH) {
        peakLines = pL;
        mostHits = mH;
        repaint();
    }

    /**
     * paintComponent is overridden from the JComponent class to allow
     * OutputGraphGUI to paint the graph the appropriate size whenever the user
     * resizes the window. All positions in it are relative to its size.
     * (paintComponent is only called by the JVM itself; never the coder- we
     * can just call repaint)
     *
     * @param g The graphics object, supplied by the internal workings of Java.
     */
    protected void paintComponent(Graphics g) {

        width = getWidth();
        height = getHeight();
        xAxisWidth = width - width*3/20;
        yAxisHeight = height - height*1/5;
        xAxisStartingPoint = width/10;
        yAxisStartingPoint = height/20;
        halfHashMarkLength = 10;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        //draw horizontal axis
        g.drawLine(xAxisStartingPoint, yAxisStartingPoint+yAxisHeight,
                   xAxisStartingPoint+xAxisWidth, yAxisStartingPoint+yAxisHeight);
        //draw verticle axis
        g.drawLine(xAxisStartingPoint, yAxisStartingPoint,
                   xAxisStartingPoint, yAxisStartingPoint+yAxisHeight);

        //draw labels
        g.drawString("I", width/40, height/2);
        g.drawString("m/e", width/2, height*49/50);

        //draw horizontal axis hash marks and numbers
        int yStart = yAxisStartingPoint + yAxisHeight - halfHashMarkLength;
        int yEnd = yAxisStartingPoint + yAxisHeight + halfHashMarkLength;
        for(int i = 0; i < 18; i++) {
            int xPos = xAxisStartingPoint + i*xAxisWidth/17;
            g.drawLine(xPos, yStart, xPos, yEnd);
            String markNumber = String.valueOf(i*100);
            g.drawString(markNumber, xPos-10, yEnd + 15);
        }

        //draw verticle axis hash marks and numbers
        int xStart = xAxisStartingPoint - halfHashMarkLength;
        int xEnd = xAxisStartingPoint + halfHashMarkLength;
        for(int i = 0; i < 11; i++) {
            int yPos = yAxisStartingPoint + i*yAxisHeight/10;
            g.drawLine(xStart, yPos, xEnd, yPos);
            String markNumber = String.valueOf(100-i*10);
            g.drawString(markNumber, xStart-25, yPos+5);
        }

        //call drawPeaks so it will take care of any mass spec lines
        drawPeaks(g);
    }

    /**
     * drawPeaks will draw the lines that represent output from the Spectrometer.
     *
     * @param g Used like a paintbrush to draw the lines.
     */
    public void drawPeaks(Graphics g) {
        g.setColor(Color.BLACK);
        if (peakLines != null) {
            for(Ion ion : peakLines) {
                int xPos = xAxisStartingPoint + xAxisWidth *
                        (int)ion.getMassChargeRatio()/1700;
                int yPos = yAxisStartingPoint + yAxisHeight -
                          (yAxisHeight * (ion.getHits()/mostHits));
                ion.setXCoordinate(xPos);
                g.drawLine(xPos, yAxisStartingPoint + yAxisHeight, xPos, yPos);
            }
        }
    }
}
