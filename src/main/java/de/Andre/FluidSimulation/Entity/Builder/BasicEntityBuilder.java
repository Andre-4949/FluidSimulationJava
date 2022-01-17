package de.Andre.FluidSimulation.Entity.Builder;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Entity.Entity;
import de.Andre.FluidSimulation.Entity.IEntity;
import de.Andre.FluidSimulation.Extentions.MultiPolygonStructure;
import de.Andre.FluidSimulation.Extentions.Point3D;
import de.Andre.FluidSimulation.Extentions.Polygon3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicEntityBuilder {
    public static IEntity createCube(Point3D center, double size,Color c, ConfigController controller){
        double cxpl = center.x + size;
        double cxml = center.x - size;

        double cyph = center.y + size;
        double cymh = center.y - size;

        double czpw = center.z + size;
        double czmw = center.z - size;

        Point3D p1 = new Point3D(cxpl, cymh, czmw);
        Point3D p2 = new Point3D(cxpl, cyph, czmw);
        Point3D p3 = new Point3D(cxpl, cyph, czpw);
        Point3D p4 = new Point3D(cxpl, cymh, czpw);
        Point3D p5 = new Point3D(cxml, cymh, czmw);
        Point3D p6 = new Point3D(cxml, cyph, czmw);
        Point3D p7 = new Point3D(cxml, cyph, czpw);
        Point3D p8 = new Point3D(cxml, cymh, czpw);


        MultiPolygonStructure cube = new MultiPolygonStructure(new ArrayList<>(Arrays.asList(
                new Polygon3D(c,controller, p1, p4, p3, p2),
                new Polygon3D(c,controller, p5, p6, p7, p8),
                new Polygon3D(c,controller, p1, p2, p6, p5),
                new Polygon3D(c,controller, p1, p5, p8, p4),
                new Polygon3D(c,controller, p2, p3, p7, p6),
                new Polygon3D(c, controller, p4, p8, p7, p3)
        )));
        return new Entity(new ArrayList<>() {{
            add(cube);
        }}, controller);
    }

    public static IEntity createRubiksCube(Point3D center, double size, ConfigController controller){
        double cxpl = center.x + size;
        double cxml = center.x - size;

        double cyph = center.y + size;
        double cymh = center.y - size;

        double czpw = center.z + size;
        double czmw = center.z - size;

        Point3D p1 = new Point3D(cxpl, cymh, czmw);
        Point3D p2 = new Point3D(cxpl, cyph, czmw);
        Point3D p3 = new Point3D(cxpl, cyph, czpw);
        Point3D p4 = new Point3D(cxpl, cymh, czpw);
        Point3D p5 = new Point3D(cxml, cymh, czmw);
        Point3D p6 = new Point3D(cxml, cyph, czmw);
        Point3D p7 = new Point3D(cxml, cyph, czpw);
        Point3D p8 = new Point3D(cxml, cymh, czpw);


        MultiPolygonStructure cube = new MultiPolygonStructure(new ArrayList<>(Arrays.asList(
                new Polygon3D(Color.green,controller, p1, p4, p3, p2),
                new Polygon3D(Color.yellow,controller, p5, p6, p7, p8),
                new Polygon3D(Color.red,controller, p1, p2, p6, p5),
                new Polygon3D(Color.blue,controller, p1, p5, p8, p4),
                new Polygon3D(Color.white,controller, p2, p3, p7, p6),
                new Polygon3D(Color.orange, controller, p4, p8, p7, p3)
        )));
        return new Entity(new ArrayList<>() {{
            add(cube);
        }}, controller);
    }

    public static IEntity createCube(Point3D center, double size, ConfigController controller){
        return BasicEntityBuilder.createCube(center,size,Color.green,controller);
    }

    public static IEntity createDiamond(Point3D center, int edges, double inFactor, double size, Color color, ConfigController controller){
        ArrayList<MultiPolygonStructure> polygonStructures = new ArrayList<>();
        Point3D bottom = center.addZ(-size/2);

        Point3D[] outerRingPoints = new Point3D[edges];
        Point3D[] innerRingPoints = new Point3D[edges];

        for (int i = 0; i < edges; i++) {
            double theta = ((2*Math.PI)/edges)*i;//divide circle in equal pieces and then multiply by i
            double x = -Math.sin(theta) * size/2;
            double y = Math.cos(theta) * size/2;
            double z = size/2;
            outerRingPoints[i] = new Point3D(center.x + x, center.y + y, center.z + z * inFactor);
            innerRingPoints[i] = new Point3D(center.x + x * inFactor, center.y + y *inFactor, center.z + z );
        }

        Polygon3D polygons[] = new Polygon3D[(edges * 2)+1];
        for (int i = 0; i < edges; i++) {
            polygons[i] = new Polygon3D(color,controller,outerRingPoints[i],bottom,outerRingPoints[(i+1)%edges]); //triangles
        }

        for (int i = 0; i < edges; i++) {
            polygons[i+edges] = new Polygon3D(color,controller,outerRingPoints[i], outerRingPoints[(i+1)%edges], innerRingPoints[(i+1)%edges], innerRingPoints[i]);
        }
        polygons[edges*2] = new Polygon3D(color, controller, innerRingPoints);

        MultiPolygonStructure structure = new MultiPolygonStructure(color,polygons);
        polygonStructures.add(structure);

        return new Entity(polygonStructures, controller);
    }

    public static IEntity createDiamond(Point3D center, double inFactor, double size, Color color, ConfigController controller){
        return createDiamond(center, 10, inFactor, size, color, controller);
    }

    public static IEntity createDiamond(Point3D center, double size, Color color, ConfigController controller){
        return createDiamond(center, 0.8, size, color, controller);
    }

    public static IEntity createDiamond(Point3D center, int edges, double size, Color color, ConfigController controller){
        return createDiamond(center, edges, 0.8, size, color, controller);
    }


    public static IEntity createSphere(Point3D center, double size, int resolution, Color color, ConfigController controller){
        ArrayList<MultiPolygonStructure> structures = new ArrayList<>();
        ArrayList<Polygon3D> polygons = new ArrayList<Polygon3D>();

        Point3D bottom = center.clone().addZ(-size/2);
        Point3D top = center.clone().addZ(size/2);

        Point3D[][] points = new Point3D[resolution-1][resolution];

        for(int i = 1; i<resolution; i++){
            double theta = Math.PI/resolution*i;
            double zPos = -Math.cos(theta)*size/2;
            double currentRadius = Math.abs(Math.sin(theta)*size/2);
            for(int j = 0; j<resolution; j++){
                double alpha = 2*Math.PI/resolution*j;
                double xPos = -Math.sin(alpha)*currentRadius;
                double yPos = Math.cos(alpha)*currentRadius;
                points[i-1][j] = new Point3D(center.x + xPos, center.y + yPos, center.z + zPos);
            }
        }

        for(int i = 1; i<=resolution; i++){
            for (int j = 0; j < resolution; j++) {
                if(i==1){
                    polygons.add(new Polygon3D(
                            controller,
                            points[i-1][j],
                            points[i-1][(j+1)%resolution],
                            bottom
                    ));
                } else if(i==resolution){
                    polygons.add(new Polygon3D(
                            controller,
                            points[i-2][(j+1)%resolution],
                            points[i-2][j],
                            top
                    ));
                } else {
                    polygons.add(new Polygon3D(
                            controller,
                            points[i-1][j],
                            points[i-1][(j+1)%resolution],
                            points[i-2][(j+1)%resolution],
                            points[i-2][j]
                    ));
                }

            }
        }


        Polygon3D[] polygonArray = new Polygon3D[polygons.size()];
        polygonArray = polygons.toArray(polygonArray);


        MultiPolygonStructure structure = new MultiPolygonStructure(color, polygons);
        structures.add(structure);

        return new Entity(structures, controller);
    }
}
