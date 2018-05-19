import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.lang.Math;
import java.awt.Color;

public class Graph extends JPanel {
	private ArrayList<Packet> currentData;
    private int largestXValue = 900;
    private int largestYValue = 0;
    private int intervalLength = 2;
    private ArrayList<Integer> dataSizes = new ArrayList<Integer>();
    private int startingXValue = 0;
    private Color primaryColor = new Color(0,0,0);
    private Color secondaryColor = new Color(100,100,100);

    public void setData(ArrayList<Packet> selectedData) {
    	currentData = selectedData;
        largestXValue = (int)(currentData.get(currentData.size()-1).time);
        largestXValue = largestXValue + largestXValue % intervalLength;
        getYValues();
    }

    public void clearData() {
    	currentData = new ArrayList<Packet>();
        dataSizes = new ArrayList<Integer>();
        largestXValue = 900;
        largestYValue = 0;
    }

    public void setIntervalSize(int size) {
        intervalLength = size;
        getYValues();
    }

    public int getIntervalSize(int size) {
        return intervalLength;
    }

    private void getYValues() {
        int thisIntervalsTotal = 0;
        int intervalNumber = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawLine(50, 25, 50, 275);
        g.drawLine(45, 275, 955, 275);

        int pixelsBetweenTicks = 900/14;
        int dataBetweenTicks = (int)Math.ceil(largestXValue/14.0);
        int charWidth = 3;

        for (int tick = 0; tick <= 14; tick += 1) {
        	g.drawLine(tick*pixelsBetweenTicks + 50, 275, tick*pixelsBetweenTicks + 50, 280);
            String tickLabel = Integer.toString(dataBetweenTicks*tick + startingXValue);
        	g.drawString(tickLabel, tick*pixelsBetweenTicks + 50 - tickLabel.length()*3, 295);
        }


        g.drawString("Time [s]", 500-35, 315);
        int labelDivisor = 1;
        if (largestYValue >= 10000) {

            if (largestYValue >= 10000000) {
                if (largestYValue >= 10000000) {
                    labelDivisor = 1000000;
                    g.drawString("Volume [gigabytes]", 25, 10);
                }
                else {
                    labelDivisor = 1000000;
                    g.drawString("Volume [megabytes]", 25, 10);
                }
            }
            else {
                labelDivisor = 1000;
                g.drawString("Volume [kilobytes]", 25, 10);
            }
        }
        else {
            g.drawString("Volume [bytes]", 25, 10);
        }
        g.drawString("0", 35, 280);

        
        
    }
}