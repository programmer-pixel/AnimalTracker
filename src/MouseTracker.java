import processing.core.PApplet;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 100;  // TODO:  Change this value to match video
    DataSet dataset;
    int frameCount = 0;

    public MouseTracker() {
        dataset = new DataSet();  // TODO:  feel free to change the constructor
    }

    @Override
    public DImage processImage(DImage img) {
        frameCount++;

        if (frameCount < MAX_FRAMES) {
        /*

        1).  Filter the image to isolate mouse
        2).  Extract information about the mouse
        3).  Load information into dataset.

         */

        } else {     // If last frame, output CSV data
            displayInfo(dataset);           // display info if you wish
            outputCSVData(dataset);         // output data to csv file, if you wish
        }

        return img;
    }

    private void displayInfo(DataSet dataset) {

    }

    private void outputCSVData(DataSet dataset) {

    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        // TODO:  If you want, draw the trail behind the mouse.
    }

}

