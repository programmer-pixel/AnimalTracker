import processing.core.PApplet;

/***
 * @author Sam Bobick
 */
public class ConvolutionFilter implements PixelFilter {
    //private static final int[][] BOX_BLUR_3X3 = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private static final int[][] BOX_BLUR_GENERAL = NxNboxBlur(3);
    //private static final int[][] SHARPEN_3X3 = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    //private static final int[][] PREWITT_EDGE_DETECTION = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};

    private int[][] kernel;
    private int kernelWeight;

    public ConvolutionFilter() {
        kernel = BOX_BLUR_GENERAL;
        kernelWeight = getKernelWeight(kernel);
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] output = new short[pixels.length][pixels[0].length];

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                if (isInBounds(r + 1, c + 1, output)) output[r + 1][c + 1] = convolute(r, c, pixels);
            }
        }
        img.setPixels(output);
        return img;
    }

    private short convolute(int topmostIndex, int leftmostIndex, short[][] arr) {
        int output = 0;
        if (!isInBounds(topmostIndex + this.kernel.length - 1, leftmostIndex + this.kernel[0].length - 1, arr))
            return 0;

        for (int rOffset = 0; rOffset < this.kernel.length; rOffset++) {
            for (int cOffset = 0; cOffset < this.kernel[0].length; cOffset++) {
                int kernelVal = this.kernel[rOffset][cOffset];
                int pixelVal = arr[topmostIndex + rOffset][leftmostIndex + cOffset];
                output += kernelVal * pixelVal;
            }
        }

        output /= this.kernelWeight;
        if (output < 0) output = 0;
        if (output > 255) output = 255;
        return (short) (output);
    }

    private static boolean isInBounds(int r, int c, short[][] arr) {
        if (r >= arr.length || r < 0) return false;
        if (c >= arr[0].length || r < 0) return false;
        return true;
    }

    private static int getKernelWeight(int[][] kernel) {
        int sum = 0;

        for (int[] r : kernel) {
            for (int num : r) {
                sum += num;
            }
        }
        if (sum == 0) sum = 1; //prevents div by 0
        return sum;
    }

    private static int[][] NxNboxBlur(int n) {
        int[][] boxBlur = new int[n][n];
        for (int r = 0; r < boxBlur.length; r++) {
            for (int c = 0; c < boxBlur[0].length; c++) {
                boxBlur[r][c] = 1;
            }
        }
        return boxBlur;
    }


    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }
}