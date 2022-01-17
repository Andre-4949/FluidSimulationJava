package de.Andre.FluidSimulation.Entity;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Extentions.MultiPolygonStructure;
import de.Andre.FluidSimulation.Extentions.MyVector;
import de.Andre.FluidSimulation.Extentions.Polygon3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Entity implements IEntity{
    private List<MultiPolygonStructure> tileObjects;
    private Polygon3D[] polygon3DS;
    private final ConfigController controller;

    public Entity(List<MultiPolygonStructure> tileObjects){
        this.tileObjects = tileObjects;
        this.controller = this.tileObjects.get(0).getPolygons().get(0).getController();
        updatePolygon3Ds();
        this.polygon3DS = this.sortPolygons();
    }

    private void updatePolygon3Ds() {
        ArrayList<Polygon3D> polys = new ArrayList<>();
        this.tileObjects.forEach(x->polys.addAll(x.getPolygons()));
        this.polygon3DS = new Polygon3D[polys.size()];
        for (int i = 0; i < polygon3DS.length; i++) {
            this.polygon3DS[i] = polys.get(i);
        }
    }

    public Entity(List<MultiPolygonStructure> tileObjects, ConfigController controller){
        this.tileObjects = tileObjects;
        this.controller = controller;
        updatePolygon3Ds();
        this.polygon3DS = this.sortPolygons();
    }

    @Override
    public void render(Graphics g) {
        this.polygon3DS = this.sortPolygons();
        for (Polygon3D polygon3D : polygon3DS) {
            polygon3D.render(g);
        }
    }

    @Override
    public void translate(double x, double y, double z) {
        for(MultiPolygonStructure structure: this.tileObjects){
            structure.translate(x,y,z);
        }
        this.polygon3DS = Polygon3D.sortPolygons(polygon3DS);
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        for (MultiPolygonStructure tileObject : this.tileObjects) {
            tileObject.rotate(CW, xDegrees,yDegrees,zDegrees, lightVector);
        }
        this.polygon3DS = Polygon3D.sortPolygons(polygon3DS);
    }

    public List<MultiPolygonStructure> getTileObjects() {
        return tileObjects;
    }

    private Polygon3D[] sortPolygons(){
        return Polygon3D.sortPolygons(this.polygon3DS);
    }
}
