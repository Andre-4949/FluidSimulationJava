package de.Andre.FluidSimulation.Extentions;//h

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygon3D implements IExtension {
    private final ConfigController controller;
    private Point3D[] points;
    private Color baseColor = Color.RED;
    private Color lightingColor;
    private boolean visible;
    private static final double AMBIENT_LIGHTING = 0.3;
    private double lightRatio;

    public Polygon3D(ConfigController controller, Point3D... points) {
        this.points = new Point3D[points.length];

        //Deep Copy of points
        for (int i = 0; i < points.length; i++) {
            Point3D p = points[i];
            this.points[i] = new Point3D(p.x, p.y, p.z);
        }

        this.controller = controller;
        this.updateVisibility();
    }

    public Polygon3D(Color c, ConfigController controller, Point3D... points) {
        this.points = new Point3D[points.length];

        //Deep Copy of points
        for (int i = 0; i < points.length; i++) {
            Point3D p = points[i];
            this.points[i] = new Point3D(p.x, p.y, p.z);
        }
        this.baseColor = this.lightingColor = c;
        this.controller = controller;
        this.updateVisibility();
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }

    @Override
    public void render(Graphics g) {
        if(!this.visible)return;
        g.setColor(this.lightingColor);
        g.fillPolygon(this.toPolygon());
    }

    public void translate(double x, double y, double z){
        for (Point3D point : points) {
            point.addXOffset(x).addYOffset(y).addZOffset(z);
        }
        this.updateVisibility();
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        if (!(xDegrees == 0 && yDegrees == 0 && zDegrees == 0)) {
            for (Point3D p : points) {
                Util.rotateX(p, CW, xDegrees);
                Util.rotateY(p, CW, yDegrees);
                Util.rotateZ(p, CW, zDegrees);
            }
        }
        this.updateLightingRatio(lightVector);
        this.updateVisibility();
    }


    @Override
    public String toString() {
        return "Polygon3D{" +
                "points=" + Arrays.toString(points) +
                ", color=" + baseColor +
                '}';
    }

    public Point3D[] getPoints() {
        return points;
    }

    public boolean isVisible() {
        return visible;
    }

    public double getAverageX() {
        double sum = 0;
        for (Point3D point : this.points) {
            sum += point.x + point.xOffset;
        }
        return sum / this.points.length;
    }

    private void updateVisibility(){
        this.visible = this.getAverageX()<10;
    }

    private Point3D getAveragePoint(){
        double x = 0;
        double y = 0;
        double z = 0;
        for (Point3D point : points) {
            x += point.x + point.xOffset;
            y += point.y + point.yOffset;
            z += point.z + point.zOffset;
        }
        x /= points.length;
        y /= points.length;
        z /= points.length;
        return new Point3D(x,y,z);
    }

    public static Polygon3D[] sortPolygons(Polygon3D[] polygons) {
        ArrayList<Polygon3D> polygon3DS = sortPolygons(new ArrayList<>(Arrays.asList(polygons)));
        for (int i = 0; i < polygon3DS.size(); i++) {
            polygons[i] = polygon3DS.get(i);
        }
        return polygons;
    }

    public static ArrayList<Polygon3D> sortPolygons(ArrayList<Polygon3D> polygon3DS) {
        polygon3DS.sort((o1, o2) -> {
            Point3D p1Average = o1.getAveragePoint();
            Point3D p2Average = o2.getAveragePoint();

            double p1Dist = Point3D.dist(p1Average, Point3D.origin);
            double p2Dist = Point3D.dist(p2Average, Point3D.origin);
            double diff = p1Dist - p2Dist;
            if (diff == 0) return 0;
            return diff < 0 ? 1 : -1;
        });
        return polygon3DS;
    }

    public Polygon3D clone() {
        return new Polygon3D(this.baseColor, controller, points);
    }

    public Polygon toPolygon() {
        Polygon p = new Polygon();
        for (Point3D point : this.points) {
            Point pt = Util.Point3DtoPoint(point);
            p.addPoint(pt.x, pt.y);
        }
        return p;
    }

    public ConfigController getController() {
        return controller;
    }

    private void updateLightingRatio(MyVector lightVector) {
        if (this.points.length < 3) {
            return;
        }
        MyVector v1 = new MyVector(this.points[0], this.points[1]);
        MyVector v2 = new MyVector(this.points[1], this.points[2]);
        MyVector normal = MyVector.normalize(MyVector.cross(v2, v1)); // vertical vector on the surface of the Polygon
        double dot = MyVector.dot(normal, lightVector); // dot-product
        double sign = dot < 0 ? -1 : 1; //is this surface facing the light-source or not
        dot *= dot; //square it
        dot *= sign; //add the sign (- / +)
        dot = (dot + 1) / 2 * (1-AMBIENT_LIGHTING); //magic
        double lightRatio = Math.min(1, Math.max(0, AMBIENT_LIGHTING + dot));//limits the lightRatio to a number between 0 and 1
        this.updateLightingColor(lightRatio);
    }

    private void updateLightingColor(double lightRatio) {
        int red = (int) (this.baseColor.getRed() * lightRatio);
        int green = (int) (this.baseColor.getGreen() * lightRatio);
        int blue = (int) (this.baseColor.getBlue() * lightRatio);
//        this.lightingColor = Color.red;
        this.lightingColor = new Color(red, green, blue, this.baseColor.getAlpha()) ;
    }
}
