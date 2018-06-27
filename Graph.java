import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.lang.Math;
import java.awt.Color;

public class Graph extends JPanel {
	private ArrayList<Packet> currentData;
    private int largestXValue = 900;
    private double largestYValue = 0;
    private int intervalLength = 2;
    private ArrayList<Integer> dataSizes = new ArrayList<Integer>();
    private int startingXValue = 0;
    private Color primaryColor = new Color(0,0,0);
    private Color secondaryColor = new Color(100,100,100);

    /**
    * Passes new data to the graph to be displayed
    *
    * @param  selectedData  An arraylist containing the packets that should be analysed
    */
    public void setData(ArrayList<Packet> selectedData) {
    	currentData = selectedData;
        largestXValue = (int)(currentData.get(currentData.size()-1).time);
        largestXValue = largestXValue + largestXValue % intervalLength;
        getYValues();
    }

    /**
    * Clears the graph
    */
    public void clearData() {
    	currentData = new ArrayList<Packet>();
        dataSizes = new ArrayList<Integer>();
        largestXValue = 900;
        largestYValue = 0;
    }

    /**
    * Computes the values that need to be displayed
    */
    private void getYValues() {
        int thisIntervalsTotal = 0;
        int intervalNumber = 0;

        dataSizes = new ArrayList<Integer>();
        largestYValue = 0;

        for (Packet currentPacket: currentData) {
            // If the  packet occured within the current interval
            if (currentPacket.time < (intervalNumber + 1) * intervalLength) {
                thisIntervalsTotal += currentPacket.size;
            }

            else {
                dataSizes.add(thisIntervalsTotal);
                intervalNumber += 1;
                if (thisIntervalsTotal > largestYValue) largestYValue = thisIntervalsTotal;

                // Skip any intervals between the current interval and the last interval
                while (currentPacket.time > (intervalNumber + 1) * intervalLength) {
                    dataSizes.add(0);
                    intervalNumber += 1;
                }

                thisIntervalsTotal = currentPacket.size;
            }
        }
        dataSizes.add(thisIntervalsTotal);
        if (thisIntervalsTotal > largestYValue) largestYValue = thisIntervalsTotal;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draw axis
        g.drawLine(50, 25, 50, 275);
        g.drawLine(45, 275, 955, 275);

        int pixelsBetweenTicks = 900/14;
        int dataBetweenTicks = (int)Math.ceil(largestXValue/14.0);
        int charWidth = 3;

        // Draw x ticks
        for (int tick = 0; tick <= 14; tick += 1) {
        	g.drawLine(tick*pixelsBetweenTicks + 50, 275, tick*pixelsBetweenTicks + 50, 280);
            String tickLabel = Integer.toString(dataBetweenTicks*tick + startingXValue);
        	g.drawString(tickLabel, tick*pixelsBetweenTicks + 50 - tickLabel.length()*3, 295);
        }

        // Draws axis labels
        g.drawString("Time [s]", 500-35, 315);
        g.drawString("Volume [bytes]", 25, 10);
        g.drawString("0", 35, 280);

        if (dataSizes.size() > 0) {
            int dataNumber = 0;
            int dataOccurences = 0; // Number of non-zero data points, used for selecting color
            double pixelsPerInterval = pixelsBetweenTicks / ((double)dataBetweenTicks / intervalLength);

            // Draws bars on plot
            for (int dataPoint: dataSizes) {
                if ((dataNumber * intervalLength >= startingXValue) && (dataNumber * intervalLength <= largestXValue)) {
                    if (dataPoint > 0) {
                        int width = (int)Math.floor(pixelsPerInterval*(dataNumber+1)) - (int)Math.floor(pixelsPerInterval*dataNumber);
                        int height = (int)Math.floor(250* ((double)dataPoint/largestYValue));
                        
                        g.setColor(dataOccurences%2 == 0 ? primaryColor : secondaryColor);
                        g.fillRect((int)Math.floor(pixelsPerInterval*dataNumber)+50, 275-height, width, height);
                        dataOccurences++;
                    }
                    dataNumber++;
                }
            }
            
            // Draws Y-ticks
            String[] labels = { "", "k", "M", "G", "T" };
            for (int tick = 1; tick <= 6; tick += 1) {
                g.drawLine(45, 275 - tick*50, 50, 275 - tick*50);

                int order = 0;
                double valueToDisplay = largestYValue/5*tick;
                while (valueToDisplay >= 1000) {
                    valueToDisplay /= 1000;
                    order += 1;
                }
                
                String tickLabel;
                if (valueToDisplay >= 100) tickLabel = String.format("%.0f%s", valueToDisplay, labels[order]);
                else tickLabel = String.format("%.1f%s", valueToDisplay, labels[order]);
                
                g.drawString(tickLabel, 40 - tickLabel.length()*6, 280 - tick*50);
            }
        }
    }
}