import processing.core.PApplet;

import java.util.ArrayList;

public class ObjectTracker implements PixelFilter, Clickable {
    private static double RATIO_THRESHOLD = 0.3;
    private static double THRESHOLD = 120;
    private ArrayList<ColorPoint> targetColors = new ArrayList<ColorPoint>();
    private Tracker tracker;
    private ArrayList<Cluster> clustersToDisplay;

    public ObjectTracker() {
        tracker = new Tracker(8);
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        filterImage(red, green, blue);
        tracker = new Tracker(4);

        tracker.init(red);           // is now same as green and blue
        tracker.findClusters();
        ClusterSet bestSet = tracker.refineCluster();
        clustersToDisplay = bestSet.getClusters();

        System.out.println("-------------------DONE--------------------");
        img.setColorChannels(red, green,blue);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        System.out.println("Drawing overlay");

        if (clustersToDisplay == null) {
            System.out.println("No clusters");
            return;
        }

        for (Cluster cluster : clustersToDisplay) {
            window.fill(0, 255, 0);
            window.ellipse(cluster.getCenter().getCol(), cluster.getCenter().getRow(), 10, 10);
            window.fill(0, 0, 0, 0);
            window.stroke(255, 0, 0);
            window.strokeWeight(2);
            window.ellipse(cluster.getCenter().getCol(),
                    cluster.getCenter().getRow(),
                    2*3*(float)cluster.getMeanDist(),
                    2*3*(float)cluster.getMeanDist());

        }
    }

    private void filterImage(short[][] red, short[][] green, short[][] blue) {
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                if ( !pixelMatchesATarget(red[r][c], green[r][c], blue[r][c], targetColors) ) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                } else {
                    red[r][c] = 255;
                    green[r][c] = 255;
                    blue[r][c] = 255;
                }
            }
        }
    }

    private boolean pixelMatchesATarget(short r, short g, short b, ArrayList<ColorPoint> targetColors) {
        for (ColorPoint p : targetColors) {
            if (getColorDist(p.getR(), p.getG(), p.getB(), r, g, b) < THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    private boolean similarColorRatios(int r, int g, int b, short r1, short g1, short b1) {
        double rg = (double)r/g;
        double gb = (double)g/b;

        double rg1 = (double)r1/g1;
        double gb1 = (double)g1/b1;

        return ( Math.abs(rg - rg1) < RATIO_THRESHOLD && Math.abs(gb - gb1) < RATIO_THRESHOLD);


    }

    private double getColorDist(int targetRed, int targetGreen, int targetBlue, short r, short g, short b) {
        int dr = targetRed - r;
        int dg = targetGreen - g;
        int db = targetBlue - b;

        return Math.sqrt(dr*dr + dg*dg + db*db);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        targetColors.add(new ColorPoint(red[mouseY][mouseX],green[mouseY][mouseX],blue[mouseY][mouseX] ));
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            THRESHOLD += 10;
            RATIO_THRESHOLD += 0.05;
        }
        if (key == '-') {
            THRESHOLD -= 10;
            RATIO_THRESHOLD -= 0.05;
        }
        System.out.println("Threshold: " + THRESHOLD);
    }
}
