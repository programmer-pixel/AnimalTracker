import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
public class DataSet{
    private double cmPerPixel;
    private double secondPerFrame;
    public ArrayList<Point> mouseCenters;
    public ArrayList<Point> mouseTail;
    public ArrayList<Point> mouseHead;
    public final int centerX = 200;
    public final int centerY = 100;
    public final int upDiameter = 0;
    public final int sideDiameter = 0;
    public final int circleCenterX = 240;
    public final int circleCenterY = 320;
    public final int radius = 100;
    public static final int maxFrames = 4500;


    public DataSet(double cmPerPixel, double secondPerFrame){
        this.cmPerPixel = cmPerPixel;
        this.secondPerFrame = secondPerFrame;
        mouseCenters = new ArrayList<Point>();
        mouseTail = new ArrayList<Point>();
        mouseHead = new ArrayList<Point>();
    }
    public void setMaxFrames(int maxFrames){

    }
    public void addMouseCenter(Point center){
        mouseCenters.add(center);
    }
    public void addMouseHead(Point head){
        mouseCenters.add(head);
    }
    public void addMouseTail(Point tail){
        mouseCenters.add(tail);
    }
    public Point getMouseLocationAt(double time) {
        if(time < 0){
            return null;
        }
        double frame = (double)(time/secondPerFrame);
        return mouseCenters.get((int)(frame));
    }
    public double distanceTraveledFrom(int startTime, int endTime){
        if(startTime < 0 || endTime < 0){
            return -1;
        }
        Point start = getMouseLocationAt(startTime);
        Point end = getMouseLocationAt(endTime);
        double changeInX = Math.pow((end.getRow() - start.getRow()), 2);
        double changeInY = Math.pow((end.getCol() - start.getCol()), 2);
        double distance = Math.sqrt(changeInX+changeInY);
        distance = distance*cmPerPixel;
        return distance;
    }
    public double distanceFrom(Point one, Point two){
        double changeInx = Math.pow(two.getRow()- one.getRow(), 2);
        double changeIny = Math.pow(two.getCol()- one.getCol(), 2);
        double distance = Math.sqrt(changeInx+changeIny);
        distance = distance*cmPerPixel;
        return distance;
    }
    public double speedAt(double time){
        if(time < 0){
            return -1;
        }
        double frame = time/secondPerFrame;
        double changeInDistance = distanceTraveledFrom((int)(frame-1),(int)(frame+1));
        changeInDistance = changeInDistance*cmPerPixel;
        return changeInDistance/((frame-1)-(frame+1));
    }
    public double averageSpeedFrom(int startTime, int endTime){
        if(startTime < 0 || endTime < 0){
            return -1;
        }
        double speeds = 0;
        int differenceInTime = endTime-startTime;
        for (int i = startTime; i < endTime; i++) {
            speeds += speedAt(i);
        }
        return (double) (speeds/differenceInTime);
    }
    public int turningLeftCount(int startTime, int endTime){
        if(startTime < 0 || endTime < 0){
            return -1;
        }
        int start = (int) (startTime/secondPerFrame);
        int end = (int) (endTime/secondPerFrame);
        int count = 0;
        for (int i = start; i < end-1; i++) {
            Point head = mouseHead.get(i);
            Point tail = mouseTail.get(i);
            Point headEnd = mouseHead.get(i+1);
            Point tailEnd = mouseTail.get(i+1);
            if((headEnd.getRow() < head.getRow()) && (tailEnd.getRow() > tail.getRow())){
                count++;
            }
        }
        return count;
    }
    public int turningRightCount(int startTime, int endTime){
        if(startTime < 0 || endTime < 0){
            return -1;
        }
        int start = (int) (startTime/secondPerFrame);
        int end = (int) (endTime/secondPerFrame);
        int count = 0;
        for (int i = start; i < end-1; i++) {
            Point head = mouseHead.get(i);
            Point tail = mouseTail.get(i);
            Point headEnd = mouseHead.get(i+1);
            Point tailEnd = mouseTail.get(i+1);
            if((headEnd.getRow() > head.getRow()) && (tailEnd.getRow() < tail.getRow())){
                count++;
            }
        }
        return count;
    }
    public double leftToRightTurnRatio(int startTime, int endtime){
        if(startTime < 0 || endtime < 0){
            return -1;
        }
        int rightCount = turningRightCount(startTime, endtime);
        int leftCount = turningLeftCount(startTime, endtime);
        return (double)(leftCount/rightCount);
    }
    public int stopCount(int startTime, int endTime){
        if(startTime < 0 || endTime < 0){
            return -1;
        }
        int counter = 0;
        for (int i = startTime; i < endTime; i++) {
            double distance = distanceTraveledFrom(i,i++);
            if(distance == 0){
                counter++;
            }
        }
        return counter;
    }
    public boolean isInSquareROI(int circleCenterX, int circleCenterY, int upDiameter, int sideDiameter, int atX, int atY){
        if((atX >= circleCenterX -(sideDiameter/2) && atX <= circleCenterX+(sideDiameter/2)) && (atY >= circleCenterY-(upDiameter/2) && atY <= circleCenterY+(upDiameter/2))){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isInCircleROI(int circleCenterX, int circleCenterY, int radius, int atX, int atY){
        Point center = new Point(circleCenterX, circleCenterY);
        Point p = new Point(atX, atY);
        double distance = distanceFrom(p, center);
        if(distance < radius){
            return true;
        }
        else{
            return false;
        }
    }
    public ArrayList<TimeInterval> mouseInSquareROI(){
        int start = 0;
        ArrayList<TimeInterval> times = new ArrayList<TimeInterval>();
        for (int i = 1; i < maxFrames-1; i++) {
            boolean checkbefore = isInSquareROI(centerX, centerY, upDiameter, sideDiameter, mouseCenters.get(i-1).getCol(), mouseCenters.get(i-1).getRow());
            boolean check = isInSquareROI(centerX, centerY, upDiameter, sideDiameter, mouseCenters.get(i).getCol(), mouseCenters.get(i).getRow());
            boolean checkafter = isInSquareROI(centerX, centerY, upDiameter, sideDiameter, mouseCenters.get(i+1).getCol(), mouseCenters.get(i+1).getRow());
            if(check == true && checkbefore == false){
                start = i;
            }
            else if(check == true && checkbefore == true && checkafter == false){
                int end = i+1;
                TimeInterval time = new TimeInterval(start, end);
                times.add(time);
            }
        }
        return times;
    }
    public ArrayList<TimeInterval> mouseInCircleROI(){
        int start = 0;
        ArrayList<TimeInterval> times = new ArrayList<TimeInterval>();
        for (int i = 1; i < maxFrames-1; i++) {
            boolean checkbefore = isInCircleROI(circleCenterX, circleCenterY, radius, mouseCenters.get(i-1).getCol(), mouseCenters.get(i-1).getRow());
            boolean check = isInCircleROI(circleCenterX, circleCenterY, radius, mouseCenters.get(i).getCol(), mouseCenters.get(i).getRow());
            boolean checkafter = isInCircleROI(circleCenterX, circleCenterY, radius, mouseCenters.get(i+1).getCol(), mouseCenters.get(i+1).getRow());
            if(check == true && checkbefore == false){
                start = i;
            }
            else if(check == true && checkbefore == true && checkafter == false){
                int end = i+1;
                TimeInterval time = new TimeInterval(start, end);
                times.add(time);
            }
        }
        return times;
    }
    public double turnAngleAt(double time){
        if(time < 0){
            return -1;
        }
        double newtime = time/secondPerFrame;
        Point head = mouseHead.get((int) newtime);
        Point tail = mouseTail.get((int) newtime);
        Point center = mouseCenters.get((int) newtime);
        if((head.getRow() == tail.getRow() && tail.getRow() == center.getRow()) || (head.getCol() == tail.getCol() && tail.getCol() == center.getCol())){
            return 0;
        }
        else{
            double hypothenus = distanceFrom(head, tail);
            double length1 = distanceFrom(tail, center);
            double length2 = distanceFrom(center, head);
            hypothenus = hypothenus*hypothenus;
            length1 = length1*length1;
            length2 = length2*length2;
            double expression = (hypothenus-length1-length2)/(-2*length1*length2);
            double angle = Math.acos(expression);
            return angle;
        }
    }
    public static void writeDataToFile(String filePath, ArrayList<Point> data){
        try(FileWriter f = new FileWriter(filePath);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b);) {
            for (int i = 0; i < maxFrames; i++) {
                p.println(data.get(i).getRow() + " , " + data.get(i).getCol());
            }
        }
        catch (IOException e) {
            System.err.println("There is a problem writing to the file: " + filePath);
            e.printStackTrace();
        }
    }
    private String readFileAsString(String filepath){
        StringBuilder output = new StringBuilder();
        try(Scanner scan = new Scanner(new File(filepath))){
            while(scan.hasNext()){
                String line = scan.nextLine();
                output.append(line + System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
