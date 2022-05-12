import processing.core.PApplet;

import javax.swing.*;
import java.util.ArrayList;

public class Tracker {
    private static final short WHITE = 255;
    private ArrayList<ClusterSet> clusterSets;
    private ArrayList<Point> pointsToCluster;

    public Tracker(int max) {
        pointsToCluster = new ArrayList<>();
        clusterSets = new ArrayList<>();

        clusterSets.add(new ClusterSet(max));
    }

    public void init(short[][] pixels) {
        pointsToCluster.clear();

        initializePoints(pixels);
        for (ClusterSet cluster : clusterSets) {
            cluster.clearClusters();
            cluster.initClusters(pointsToCluster);
        }
    }

    public void findClusters() {
        if (pointsToCluster.size() == 0) {
            System.out.println("No points yet!");
            return;
        }

        for (ClusterSet cluster : clusterSets) {
            System.out.println("Clustering with " + cluster.getK());
            cluster.findClusters(pointsToCluster);
        }
    }

    private void initializePoints(short[][] pixels) {
        pointsToCluster = new ArrayList<>();

        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[r].length; c++) {
                if (pixels[r][c] == WHITE) {
                    pointsToCluster.add(new Point(r,c));
                }
            }
        }
    }

    public ArrayList<ClusterSet> getAllClusterSets() {
        return clusterSets;
    }

    /*
    Remove outliers
    Merge sets that looks good for merging
    Eliminate tiny clusters
     */
    public ClusterSet refineCluster() {
        ClusterSet c = clusterSets.get(0);
       /// c.removeOutliers(1.2);
        c.mergeClusters();
        /*c.eliminateSmallClusters(5);*/

        return c;
    }

   /* public ClusterSet getBestClusterSet() {
        return getMinMSEClusterSet();
    }

    private ClusterSet getMinMSEClusterSet() {
        System.out.println("Getting best...");
        double min = Double.MAX_VALUE;
        ClusterSet minSet = null;

        for (ClusterSet clusterSet : clusterSets) {
            double s = clusterSet.getDistanceSums();
            System.out.println(clusterSet.getK() + ": " + s);
            if (s < min) {
                min = s;
                minSet = clusterSet;
            }
        }

        return minSet;
    }*/
}
