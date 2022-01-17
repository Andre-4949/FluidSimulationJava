package de.Andre.FluidSimulation.Entity;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Extentions.MultiPolygonStructure;
import de.Andre.FluidSimulation.Extentions.Point3D;
import de.Andre.FluidSimulation.Extentions.Polygon3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicEntityBuilder {
    public static IEntity createCube(Point3D center, double size, ConfigController controller){
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
                new Polygon3D(Color.BLACK,controller, p1, p2, p3, p4),
                new Polygon3D(Color.GREEN,controller, p5, p6, p7, p8),
                new Polygon3D(Color.MAGENTA,controller, p1, p2, p6, p5),
                new Polygon3D(Color.ORANGE,controller, p1, p5, p8, p4),
                new Polygon3D(Color.CYAN,controller, p2, p6, p7, p3),
                new Polygon3D(Color.YELLOW, controller, p4, p3, p7, p8)
        )));
        return new Entity(new ArrayList<MultiPolygonStructure>(){{add(cube);}}, controller);
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
        structure.setDecayingPolygonColor(0.999999999999);
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
}
