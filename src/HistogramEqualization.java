import processing.core.PApplet;

public class HistogramEqualization implements PixelFilter {
    private short[] histogram, equalizedHist;
    private int[] cumulativeFreqDist, equalizedCFD;
    private short[] mapping;

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        histogram = calculateHistogramFor(pixels);
        cumulativeFreqDist = calculateCumulativeFreqDistribution(histogram);
        equalizedHist = calculateEqualizedHist(histogram, cumulativeFreqDist);
        equalizedCFD = calculateCumulativeFreqDistribution(equalizedHist);

        mapping = computeMapping(cumulativeFreqDist, equalizedCFD);

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                int val = pixels[r][c];
                pixels[r][c] = mapping[val];
            }
        }

        img.setPixels(pixels);
        return img;
    }

    private short[] computeMapping(int[] cumulativeFreqDist, int[] equalizedCFD) {
        short[] map = new short[256];

        for (int i = 0; i < map.length; i++) {
            int val = cumulativeFreqDist[i];
            int index = findClosestValTo(val, equalizedCFD);
            map[i] = (short)index;
        }

        return map;
    }

    private int findClosestValTo(int val, int[] equalizedCFD) {
        int minDistSoFar = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < equalizedCFD.length ; i++) {
            int dist = Math.abs(val-equalizedCFD[i]);
            if (dist < minDistSoFar) {
                minDistSoFar = dist;
                minIndex = i;
            }
        }

        return minIndex;
    }

    private short[] calculateEqualizedHist(short[] histogram, int[] cumulativeFreqDist) {
        short[] eqHist = new short[256];
        short val = (short) (cumulativeFreqDist[cumulativeFreqDist.length-1]/256);
        for (int i = 0; i < eqHist.length; i++) {
            eqHist[i] = val;
        }
        return eqHist;
    }

    private int[] calculateCumulativeFreqDistribution(short[] histogram) {
        int[] cfd = new int[256];

        int sum = 0;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            cfd[i] = sum;
        }

        return cfd;
    }

    private short[] calculateHistogramFor(short[][] pixels) {
        short[] hist = new short[256];

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                short val = pixels[r][c];
                hist[val]++;
            }
        }
        return hist;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }
}
