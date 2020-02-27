public class Point {
    private int row, col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double getSquaredDistanceTo(Point center) {
        double dr = (this.getRow()-center.getRow());
        double dc = (this.getCol()-center.getCol());
        return dr*dr+dc*dc;
    }

    public boolean equals(Point other) {
        return this.getCol() == other.getCol() && this.getRow() == other.getRow();
    }

    public double getDistanceTo(Point center) {
        return Math.sqrt(this.getSquaredDistanceTo(center));
    }
}
