import processing.core.PApplet;

public class VerticalFlip implements PixelFilter {


    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        int lastRowIndex = pixels.length - 1;
        for (int r = 0; r < pixels.length/2; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                short tmp = pixels[r][c];
                pixels[r][c] = pixels[lastRowIndex - r][c];
                pixels[lastRowIndex-r][c] = tmp;
            }
        }

        img.setPixels(pixels);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }
}
