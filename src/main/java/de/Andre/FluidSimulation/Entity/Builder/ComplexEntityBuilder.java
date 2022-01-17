package de.Andre.FluidSimulation.Entity.Builder;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Entity.Entity;
import de.Andre.FluidSimulation.Entity.IEntity;
import de.Andre.FluidSimulation.Extentions.MultiPolygonStructure;
import de.Andre.FluidSimulation.Extentions.Point3D;

import java.util.ArrayList;

public class ComplexEntityBuilder {
    public static IEntity createRubiksCube(Point3D center, double size, ConfigController controller){
        return ComplexEntityBuilder.createRubiksCube(center,size,5,controller);
    }

    public static IEntity createRubiksCube(Point3D center, double size, int cubespacing ,ConfigController controller){
        ArrayList<MultiPolygonStructure> structures = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    structures.addAll(BasicEntityBuilder.createCube(center.clone().addZ(i*size).addY(j*size).addX(k*size), (size/2)-cubespacing, controller).getTileObjects());
                }
            }
        }

        return new Entity(structures);
    }
}
