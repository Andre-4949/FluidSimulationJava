package de.Andre.FluidSimulation;

import de.Andre.FluidSimulation.Extentions.Point3D;

public class Cam {
    private double x,y,z;
    private Point3D camPos;
    private double roll, pitch, yaw;

    public Cam(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.camPos = new Point3D(x,y,z);
    }

    public void translate(double x, double y, double z){
        this.x += -x;
        this.y += -y;
        this.z += -z;
        this.camPos.addX(x).addY(y).addZ(z);
    }

    public void rotate(double xDegrees, double yDegrees, double zDegrees){
        this.roll += xDegrees;
        this.pitch += yDegrees;
        this.yaw += zDegrees;
    }

    public Point3D getCamPos() {
        return camPos;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getRoll() {
        return roll;
    }

    public double getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void resetAngle(){
        this.roll = 0d;
        this.pitch = 0d;
        this.yaw = 0d;
    }
}
