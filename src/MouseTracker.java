import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 4500;  // TODO:  Change this value to match video
    DataSet dataset;
    ArrayList<Point> mouseCenters = new ArrayList<Point>();
    ArrayList<Point> mouseHead = new ArrayList<Point>();
    ArrayList<Point> mouseTail = new ArrayList<Point>();
    int frameCount = 0;

    public MouseTracker() {
        dataset = new DataSet(2.54, 0.06);  // TODO:  feel free to change the constructor
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] bwChannel = img.getBWPixelGrid();
        short[][] rChannel = img.getRedChannel();
        short[][] rOutput = new short[bwChannel.length][bwChannel[0].length];
        short[][] gChannel = img.getGreenChannel();
        short[][] gOutput = new short[bwChannel.length][bwChannel[0].length];
        short[][] bChannel = img.getBlueChannel();
        short[][] bOutput = new short[bwChannel.length][bwChannel[0].length];
        if (frameCount < MAX_FRAMES) {
     /*
     1).  Filter the image to isolate mouse
     2).  Extract information about the mouse
     3).  Load information into dataset.
      */
            ArrayList<Point> mouse = new ArrayList<Point>();
            for (int row = 0; row < bwChannel.length; row++) {
                for (int col = 0; col < bwChannel[0].length; col++) {
                    if(getColorDistance(rChannel[row][col], gChannel[row][col], bChannel[row][col], 47, 47, 47) < 5 || getColorDistance(rChannel[row][col], gChannel[row][col], bChannel[row][col], 54, 54, 54) < 5 || getColorDistance(rChannel[row][col], gChannel[row][col], bChannel[row][col], 61, 61, 61) < 2){
                        rOutput[row][col] = 255;
                        gOutput[row][col] = 255;
                        bOutput[row][col] = 255;
                    }
                    else if(!inCircle(row, col)){
                        rOutput[row][col] = 255;
                        gOutput[row][col] = 255;
                        bOutput[row][col] = 255;
                    }
                    else if (getColorDistance(rChannel[row][col], gChannel[row][col], bChannel[row][col], 30, 30, 30) < 140) {
                        rOutput[row][col] = 0;
                        gOutput[row][col] = 0;
                        bOutput[row][col] = 255;
                        Point mousePoint = new Point(row, col);
                        mouse.add(mousePoint);
                    }
                    else {
                        rOutput[row][col] = 0;
                        gOutput[row][col] = 0;
                        bOutput[row][col] = 0;
                    }

                }
            }
            Point mouseCenter = calcMouseCenter(mouse);
            Point tailPoint = calcMouseTail(mouse);
            Point headPoint = calcMouseHead(mouseCenter, mouse);
            mouseCenters.add(mouseCenter);
            mouseHead.add(headPoint);
            mouseTail.add(tailPoint);
            frameCount++;

        } else {     // If last frame, output CSV data
            displayInfo(dataset);           // display info if you wish
            outputCSVData(dataset);         // output data to csv file, if you wish
        }
        img.setColorChannels(rOutput, gOutput, bOutput);
        return img;
    }
    public Point calcMouseCenter(ArrayList<Point> mouse){
        int x = 0;
        int y = 0;
        for (int i = 0; i < mouse.size(); i++) {
            y += mouse.get(i).getRow();
            x += mouse.get(i).getCol();
        }
        Point p = new Point(x/(mouse.size()), y/(mouse.size()));
        return p;
    }
    public Point calcMouseTail(ArrayList<Point> mouse){
        int x = 0;
        int y = 0;
        for (int i = 0; i < mouse.size()/8; i++) {
            y += mouse.get(i).getRow();
            x += mouse.get(i).getCol();
        }
        Point p = new Point(x/(mouse.size()/8), y/(mouse.size()/8));
        return p;
    }
    public Point calcMouseHead(Point mouseCenter, ArrayList<Point> mouse) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < mouse.size()/8; i++) {
            y += mouse.get(i).getRow();
            x += mouse.get(i).getCol();
        }
        Point p = new Point(x/(mouse.size()/8), (y/(mouse.size()/8)));
        return p;
    }

    private boolean inCircle(int row, int col) {
        Point center = new Point(235, 310);
        Point p = new Point(row, col);
        int radius = 203;
        double distance = distanceFrom(p, center);
        if(distance < radius){
            return true;
        }
        else{
            return false;
        }
    }

    private void displayInfo(DataSet dataset) {
        //System.out.println(dataset.turnAngleAt(600));
    }

    private void outputCSVData(DataSet dataset) {
        dataset.writeDataToFile("WAGHELA_TANVI.csv", mouseCenters);
    }
    public double distanceFrom(Point one, Point two){
        double changeInx = Math.pow(two.getRow()-one.getRow(), 2);
        double changeIny = Math.pow(two.getCol()-one.getCol(), 2);
        double distance = Math.sqrt(changeInx+changeIny);
        return distance;
    }
    public double getColorDistance(short r1, short g1, short b1, int r2, int g2, int b2) {
        int red = Math.abs((r1 - r2));
        red = red * red;
        int green = Math.abs((g1 - g2));
        green = green * green;
        int blue = Math.abs((b1 - b2));
        blue = blue * blue;
        return Math.sqrt(red + green + blue);
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        // TODO:  If you want, draw the trail behind the mouse.
        if(mouseCenters.size() > 0){
            window.ellipse(mouseCenters.get(frameCount-1).getRow(), mouseCenters.get(frameCount-1).getCol(), 5, 5);
        }
        if(mouseHead.size() > 0){
            window.ellipse(mouseHead.get(frameCount-1).getRow(), mouseHead.get(frameCount-1).getCol(), 5, 5);
        }
        if(mouseTail.size() > 0){
            window.ellipse(mouseTail.get(frameCount-1).getRow(), mouseTail.get(frameCount-1).getCol(), 5, 5);
        }
    }

}
