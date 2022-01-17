package de.Andre.FluidSimulation.Templates;

import de.Andre.FluidSimulation.Extentions.Point3D;
import de.Andre.FluidSimulation.Util;

import java.awt.*;
import java.util.Arrays;

public class Polygon3D {
    private Point3D[] points;
    private Color color = Color.red;

    public Polygon3D(Point3D... points) {
        this.points = new Point3D[points.length];
        for(int i = 0; i<points.length;i++){
            Point3D p = points[i];
            this.points[i] = new Point3D(p.x, p.y, p.z);
        }
    }

    public void render(Graphics g){
        Polygon p = new Polygon();
        for(int i = 0; i<points.length; i++){
            Point pt = Util.Point3DtoPoint(points[i]);
            p.addPoint(pt.x,pt.y);
        }
        Color c = g.getColor();
        g.setColor(this.color);
        g.fillPolygon(p);
        g.setColor(c);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
