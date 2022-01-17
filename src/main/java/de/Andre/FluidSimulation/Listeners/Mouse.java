package de.Andre.FluidSimulation.Listeners;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.States.ClickType;

import java.awt.event.*;
import java.util.logging.Logger;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int x = -1;
    private int y = -1;
    private int mouseButton = -1;
    private int scroll = 0;
    private float rotationSensitivity = 1;
    private final ConfigController controller;
    private final Logger logger;

    public Mouse(ConfigController controller) {
        this.controller = controller;
        this.logger = controller.getMain().getLogger();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseButton = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.scroll = e.getWheelRotation();
        if (this.rotationSensitivity-e.getWheelRotation() > 0F && this.mouseButton!=-1) this.rotationSensitivity = Math.abs(this.rotationSensitivity- e.getWheelRotation());
    }

    public void resetWheel(){
        this.scroll = 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


    public float getRotationSensitivity() {
        return rotationSensitivity;
    }

    public ClickType getMouseButton() {
        return switch (this.mouseButton) {
            case 1 -> ClickType.LEFT_CLICK;
            case 2 -> ClickType.MIDDLE_CLICK;
            case 3 -> ClickType.RIGHT_CLICK;
            case 4 -> ClickType.EXTRA_BUTTON_ONE;
            case 5 -> ClickType.EXTRA_BUTTON_TWO;
            default -> ClickType.UNKNOWN;
        };
    }

    public boolean isScrollingUp(){return scroll==-1;}
    public boolean isScrollingDown(){return scroll==1;}

    public int getScroll() {
        return scroll;
    }
}
