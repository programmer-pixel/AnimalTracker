import processing.core.PApplet;

public class ColorThreshold implements PixelFilter, Clickable {
    private static double THRESHOLD = 30;
    private static short TARGET_RED = 170;
    private static short TARGET_GREEN = 170;
    private static short TARGET_BLUE = 170;

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {
                double dist = getColorDist(TARGET_RED, TARGET_GREEN, TARGET_BLUE, red[r][c], green[r][c], blue[r][c]);
                if (dist > THRESHOLD) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }

        img.setColorChannels(red, green,blue);
        return img;
    }

    private double getColorDist(short targetRed, short targetGreen, short targetBlue, short r, short g, short b) {
        int dr = targetRed - r;
        int dg = targetGreen - g;
        int db = targetBlue - b;

        return Math.sqrt(dr*dr + dg*dg + db*db);
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        TARGET_BLUE = blue[mouseY][mouseX];
        TARGET_GREEN = green[mouseY][mouseX];
        TARGET_RED = red[mouseY][mouseX];
        System.out.println("red: " + TARGET_RED + " green: " + TARGET_GREEN + " blue: " + TARGET_BLUE);
    }

    @Override
    public void keyPressed(char key) {
        if (key == 'w') THRESHOLD += 10;
        if (key == 's') THRESHOLD -= 10;
        System.out.println("Threshold: " + THRESHOLD);
    }
}
