package de.Andre.FluidSimulation.Controller;

import de.Andre.FluidSimulation.Listeners.Keyboard;
import de.Andre.FluidSimulation.Listeners.Mouse;

import javax.swing.*;

public class UIController {
    private final ConfigController controller;
    private final Mouse mouse;
    private final Keyboard keyboard;

    public UIController(ConfigController controller){
        this.controller = controller;
        this.mouse = new Mouse(controller);
        this.keyboard = new Keyboard();
    }

    public void registerListener(JFrame f){
        f.addMouseListener(this.mouse);
        f.addMouseMotionListener(this.mouse);
        f.addMouseWheelListener(this.mouse);
        f.addKeyListener(this.keyboard);
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }
}
