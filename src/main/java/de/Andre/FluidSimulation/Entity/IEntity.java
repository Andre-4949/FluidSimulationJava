package de.Andre.FluidSimulation.Entity;

import de.Andre.FluidSimulation.Extentions.MultiPolygonStructure;
import de.Andre.FluidSimulation.Extentions.MyVector;

import java.awt.*;
import java.util.List;

public interface IEntity {
    void render(Graphics g);

    void translate(double x, double y, double z);

    void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector);

    public List<MultiPolygonStructure> getTileObjects();

}
