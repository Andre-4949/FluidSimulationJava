package de.Andre.FluidSimulation.Extentions;//h

import de.Andre.FluidSimulation.Controller.ConfigController;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiPolygonStructure implements IExtension, CustomObjects {
    private ArrayList<Polygon3D> polygon3DS = new ArrayList<>();
    private Color color;
    private ConfigController controller;

    public MultiPolygonStructure(Color color,Polygon3D... polygon3DS) {
        this.polygon3DS.addAll(Arrays.asList(polygon3DS));
        this.color = color;
        this.controller = this.polygon3DS.get(0).getController();
    }

    public MultiPolygonStructure(Polygon3D... polygon3DS) {
        this.polygon3DS.addAll(Arrays.asList(polygon3DS));
        this.color = Color.GREEN;
        this.controller = this.polygon3DS.get(0).getController();
    }

    public MultiPolygonStructure(ArrayList<Polygon3D> polygon3DS) {
        this.polygon3DS = polygon3DS;
        this.color = Color.GREEN;
        this.controller = this.polygon3DS.get(0).getController();
    }
    public MultiPolygonStructure(Color color, ArrayList<Polygon3D> polygon3DS) {
        this.polygon3DS = polygon3DS;
        this.color = color;
        this.controller = this.polygon3DS.get(0).getController();
    }

    @Override
    public void render(Graphics g) {
        for (Polygon3D polygon3D : this.polygon3DS) {
            polygon3D.render(g);
        }
    }

    public synchronized void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector){
        for (Polygon3D p : polygon3DS) {
            p.rotate(CW,xDegrees,yDegrees,zDegrees, lightVector);
        }
        this.sortPolygons();
    }

    public void translate(double x, double y, double z){
        this.polygon3DS.forEach(poly->poly.translate(x,y,z));
        this.sortPolygons();
    }

    private void sortPolygons() {
        this.polygon3DS = Polygon3D.sortPolygons(this.polygon3DS);
    }

    public void setColor(Color color) {
        this.color = color;
        for (Polygon3D polygon3D : this.polygon3DS) {
            polygon3D.setBaseColor(color);
        }
    }

    @Override
    public ArrayList<Polygon3D> getPolygons() {
        return this.polygon3DS;
    }

    public void setDecayingPolygonColor(double decayRate){
        this.color = color;
        for (Polygon3D polygon3D : this.polygon3DS) {
            polygon3D.setBaseColor(color);
            int r = (int) (this.color.getRed()*decayRate);
            int g = (int) (this.color.getGreen()*decayRate);
            int b = (int) (this.color.getBlue()*decayRate);
            this.color = new Color(r,g,b);
        }
    }

    public void setDecayingPolygonColor(){
        double decayRate = 0.95;
        this.setDecayingPolygonColor(decayRate);
    }
}
