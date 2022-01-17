package de.Andre.FluidSimulation.Entity;

import de.Andre.FluidSimulation.Cam;
import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Controller.UIController;
import de.Andre.FluidSimulation.Entity.Builder.ComplexEntityBuilder;
import de.Andre.FluidSimulation.Extentions.MyVector;
import de.Andre.FluidSimulation.Extentions.Point3D;
import de.Andre.FluidSimulation.Listeners.Keyboard;
import de.Andre.FluidSimulation.Listeners.Mouse;
import de.Andre.FluidSimulation.States.ClickType;
import de.Andre.FluidSimulation.Util;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    private List<IEntity> entities;
    private final ConfigController controller;
    private int prevX, prevY;
    private double moveSpeed = 10;
    private MyVector lightVector = MyVector.normalize(new MyVector(1,1,1));
    private UIController uiController;
    private Cam cam;
    public EntityManager(ConfigController controller){
        this.entities = new CopyOnWriteArrayList<IEntity>();
        this.controller = controller;
        Util.controller = this.controller;
        this.cam = new Cam(0,0,0);
    }

    public void init(UIController uiController){
        this.uiController = uiController;
        if(this.controller.isFluidSimulation())return;
//        this.entities.add(BasicEntityBuilder.createCube(new Point3D(0,0,0), 100,controller));
//        this.entities.add(BasicEntityBuilder.createCube(new Point3D(300,0,0), 100,new Color(1,1,1,1),controller));
//        this.entities.add(BasicEntityBuilder.createDiamond(new Point3D(0,0,0),50,Color.red,controller));
//        this.entities.add(BasicEntityBuilder.createDiamond(new Point3D(0,0,0), 15, 150 ,Color.red, controller));
//        this.entities.add(BasicEntityBuilder.createRubiksCube(Point3D.origin, 10, controller));
        this.entities.add(ComplexEntityBuilder.createRubiksCube(new Point3D(0,0,0), 100, 10, controller));
//        this.entities.add(BasicEntityBuilder.createSphere(new Point3D(0,0,0), 100, 10, Color.red, controller));
        this.rotate(true,0,0,0);
    }

    public void addEntity(IEntity e){
        this.entities.add(e);
    }

    public void removeEntity(IEntity e){
        this.entities.remove(e);
    }

    public List<IEntity> getEntities(){
        return this.entities;
    }

    public void setEntities(List<IEntity> entities){
        this.entities = entities;
    }

    public void update(){
        mouseUpdates(uiController.getMouse());
        keyboardUpdates();
    }

    private void keyboardUpdates() {
        Keyboard keyboard = uiController.getKeyboard();

        if (keyboard.isLeft()) {
            cam.translate(0, -moveSpeed, 0);
            for(IEntity entity: this.entities){
                entity.translate(0,moveSpeed,0);
            }
        } else if (keyboard.isRight()) {
            cam.translate(0, moveSpeed, 0);
            for(IEntity entity: this.entities){
                entity.translate(0,-moveSpeed,0);
            }
        } else
        if (keyboard.isUp()) {
            cam.translate(0, 0, moveSpeed);
            for(IEntity entity: this.entities){
                entity.translate(0,0,-moveSpeed);
            }
        } else if (keyboard.isDown()) {
            cam.translate(0, 0, -moveSpeed);
            for(IEntity entity: this.entities){
                entity.translate(0,0,moveSpeed);
            }
        } else if (keyboard.isForward()) {
            cam.translate(-moveSpeed, 0, 0);
            for(IEntity entity: this.entities){
                entity.translate(moveSpeed, 0,0);
            }
        } else if (keyboard.isBackward()) {
            cam.translate(moveSpeed, 0 , 0);
            for(IEntity entity: this.entities){
                entity.translate(-moveSpeed, 0,0);
            }
        }
        keyboard.update();
    }

    private void mouseUpdates(Mouse mouse){
        int x = mouse.getX();
        int y = mouse.getY();

        if (mouse.getMouseButton() == ClickType.LEFT_CLICK){
            int xDif = x-prevX;
            int yDif = y-prevY;
            this.rotate(true,0,yDif/(mouse.getRotationSensitivity()),-xDif/(mouse.getRotationSensitivity()));

            prevX = x;
            prevY = y;
        } else if(mouse.getMouseButton() == ClickType.RIGHT_CLICK){
            int xDif = x-prevX;
            this.rotate(true,xDif/mouse.getRotationSensitivity(),0,0);

            prevX = x;
        } else if(mouse.getMouseButton() == ClickType.UNKNOWN){
            prevX = x;
            prevY = y;
        }

        if (mouse.isScrollingDown()){
            Util.zoomOut();
            mouse.resetWheel();
        } else if (mouse.isScrollingUp()){
            Util.zoomIn();
            mouse.resetWheel();
        }

    }

    public void rotate(boolean direction, double xDegrees, double yDegrees, double zDegrees){
        rotateWithOutCam(direction,xDegrees,yDegrees,zDegrees);
        this.cam.rotate(xDegrees,yDegrees,zDegrees);
    }

    public void rotateWithOutCam(boolean direction, double xDegrees, double yDegrees, double zDegrees){
        for (IEntity entity : entities) {
            entity.rotate(direction, xDegrees,yDegrees,zDegrees, this.lightVector);
        }
    }

    public synchronized void render(Graphics g){
        List<IEntity> entities1 = entities;
        for (IEntity entity : entities1) {
            entity.render(g);
        }
    }

    public Cam getCam() {
        return cam;
    }

    public void clearEntities() {
        this.entities.clear();
    }
}
