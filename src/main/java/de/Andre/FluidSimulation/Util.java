package de.Andre.FluidSimulation;//h

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Controller.RenderController;
import de.Andre.FluidSimulation.Extentions.Point3D;

import java.awt.*;

public class Util {
    private static double SCALE = 1;
    private static double ZOOMFACTOR = 1.2;
    public static ConfigController controller = null;

    public static Point Point3DtoPoint(Point3D p) {
        double x3d = p.getAdjustedY() * SCALE;
        double y3d = p.getAdjustedZ() * SCALE;
        double depth = p.getAdjustedX() * SCALE;

        double[] newVal = scale(x3d, y3d, depth);


        int x = (int) (RenderController.WIDTH / 2 + newVal[0]);
        int y = (int) (RenderController.HEIGHT / 2 - newVal[1]);
        return new Point(x, y);
    }

    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt(x3d * x3d + y3d * y3d);
        double theta = Math.atan2(y3d, x3d);
        double depth2 = 15 - depth;
        double localScale = Math.abs((1400 / (depth2 + 1400)));
        dist *= localScale;
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(theta);
        newVal[1] = dist * Math.sin(theta);
        return newVal;
    }

    public static void rotateX(Point3D p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y * p.y + p.z * p.z);
        double theta = Math.atan2(p.z,p.y);
        theta += Math.toRadians(degrees)*(CW ? -1 : 1);
        p.y = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }

    public static void rotateY(Point3D p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.x * p.x + p.z * p.z);
        double theta = Math.atan2(p.z, p.x);
        theta += Math.toRadians(degrees)*(CW ? -1 : 1);
        p.x = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }

    public static void rotateZ(Point3D p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y * p.y + p.x * p.x);
        double theta = Math.atan2(p.y,p.x);
        theta += Math.toRadians(degrees)*(CW ? -1 : 1);
        p.y = radius * Math.sin(theta);
        p.x = radius * Math.cos(theta);
    }

    public static void zoomOut() {
        SCALE /= ZOOMFACTOR;
    }

    public static void zoomIn(){
        SCALE *= ZOOMFACTOR;
    }
}