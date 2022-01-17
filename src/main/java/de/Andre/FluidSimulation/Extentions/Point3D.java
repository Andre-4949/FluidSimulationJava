package de.Andre.FluidSimulation.Extentions;

import de.Andre.FluidSimulation.Util;

import java.awt.*;

public class Point3D implements IExtension {
    public static Point3D origin = new Point3D(0,0,0);
    public double x, y, z;
    public double xOffset, yOffset, zOffset;
    private int THICKNESS = 4;
    private Color color;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xOffset = this.yOffset = this.zOffset = 0;
        this.color = Color.BLACK;
    }

    public static double dist(Point3D p1, Point3D p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
    }

    @Override
    public void render(Graphics g) {
        Point p = Util.Point3DtoPoint(this);
        g.setColor(this.color);
        g.fillOval(p.x - (THICKNESS / 2), p.y - (THICKNESS / 2), THICKNESS, THICKNESS);
    }


    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", THICKNESS=" + THICKNESS +
                '}';
    }

    public Point3D clone() {
        return new Point3D(this.x, this.y, this.z);
    }

    public int getTHICKNESS() {
        return THICKNESS;
    }

    public double getAdjustedX() {
        return x + xOffset;
    }

    public double getAdjustedY() {
        return y + yOffset;
    }

    public double getAdjustedZ() {
        return z + zOffset;
    }

    public void setTHICKNESS(int THICKNESS) {
        this.THICKNESS = THICKNESS;
    }

    public Point3D addX(int x) {
        this.x += x;
        return this;
    }

    public Point3D addY(int y) {
        this.y += y;
        return this;
    }

    public Point3D addZ(int z) {
        this.z += z;
        return this;
    }

    public Point3D addX(double x) {
        this.x += x;
        return this;
    }

    public Point3D addY(double y) {
        this.y += y;
        return this;
    }

    public Point3D addZ(double z) {
        this.z += z;
        return this;
    }

    public Point3D addXOffset(double x){
        this.xOffset += x;
        return this;
    }
    public Point3D addYOffset(double y){
        this.yOffset += y;
        return this;
    }
    public Point3D addZOffset(double z){
        this.zOffset += z;
        return this;
    }
    public Point3D addXOffset(int x){
        this.xOffset += x;
        return this;
    }
    public Point3D addYOffset(int y){
        this.yOffset += y;
        return this;
    }
    public Point3D addZOffset(int z){
        this.zOffset += z;
        return this;
    }

    public Point3D addPoint(Point3D p){
        return new Point3D(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    public Point3D subtractPoint(Point3D p){
        return new Point3D(this.x - p.x, this.y - p.y, this.z - p.z);
    }

}
