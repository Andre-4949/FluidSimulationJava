package de.Andre.FluidSimulation.Extentions;

public class MyVector {
    //logic translated from wikipedia
    //I know this isn't the best source for information, but it works
    public double x, y, z;

    public MyVector(){
        this.x = this.y = this.z = 0;
    }

    public MyVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MyVector(MyVector v1, MyVector v2){

    }
    public MyVector(Point3D p1, Point3D p2){
        this.x = p2.x - p1.x;
        this.y = p2.y - p1.y;
        this.z = p2.z - p1.z;
    }

    public static double dot(MyVector v1, MyVector v2){
        return  v1.x * v2.x +
                v1.y * v2.y +
                v1.z * v2.z;
    }

    public static MyVector cross(MyVector v1, MyVector v2){
        return new MyVector(
                v1.y*v2.z - v1.z*v2.y,
                v1.z*v2.x - v1.x*v2.z,
                v1.x*v2.y - v1.y*v2.x
        );
    }

    public static MyVector normalize(MyVector v){
        double magnitude = Math.sqrt(MyVector.dot(v,v));
        return new MyVector(v.x/magnitude, v.y/magnitude, v.z/magnitude);
    }
}
