import processing.core.PApplet;

import java.awt.image.ImageFilter;

public class GammaCorrection implements PixelFilter, Clickable {
    private double gamma, k;
    private int[] newValues;

    public GammaCorrection() {
        gamma = 2.2;
        k = calculateK(gamma);

        newValues = calculateValues(gamma, k);
    }

    private int[] calculateValues(double gamma, double k) {
        int[] newValues = new int[256];
        for (int i = 0; i <=255; i++) {
            newValues[i] = computeGammaCorrection(i);
        }

        return  newValues;
    }

    private double calculateK(double gamma) {
        return 255.0/Math.pow(255,gamma);
    }

    private int computeGammaCorrection(int val) {
        return (int)(k*Math.pow(val,gamma));
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                pixels[r][c] = (short)(newValues[ pixels[r][c] ]);
            }
        }

        img.setPixels(pixels);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.fill(255);
        window.textSize(32);
        window.text("" + gamma, 10, 10);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            gamma += 0.1;
            k = calculateK(gamma);
            newValues = calculateValues(gamma, k);
        }

        if (key == '-') {
            gamma -= 0.1;
            k = calculateK(gamma);
            newValues = calculateValues(gamma, k);
        }
    }
}