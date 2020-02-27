import processing.core.PApplet;

public class ThesholdFilter implements PixelFilter, Clickable {
    private int threshold = 127;

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                if (pixels[r][c] > threshold) {
                    pixels[r][c] = 255;
                } else {
                    pixels[r][c] = 0;
                }
            }
        }

        img.setPixels(pixels);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

    }

    @Override
    public void keyPressed(char key) {
        if (key == '-') {
            threshold -= 5;
        }

        if (key == '+') {
            threshold += 5;
        }
    }
}
