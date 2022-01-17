package de.Andre.FluidSimulation.Listeners;

import de.Andre.FluidSimulation.Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[66568];
    private boolean left, right, up, down, forward, backward;


    public void update() {
        this.left = this.keys[KeyEvent.VK_LEFT] || this.keys[KeyEvent.VK_A];
        this.right = this.keys[KeyEvent.VK_RIGHT] || this.keys[KeyEvent.VK_D];
        this.forward = this.keys[KeyEvent.VK_UP] || this.keys[KeyEvent.VK_W];
        this.backward = this.keys[KeyEvent.VK_DOWN] || this.keys[KeyEvent.VK_S];
        this.up = this.keys[KeyEvent.VK_UP] || this.keys[KeyEvent.VK_SPACE];
        this.down = this.keys[KeyEvent.VK_DOWN] || this.keys[KeyEvent.VK_SHIFT];
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return backward;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_U) {
            new Thread(() -> Util.controller.getFluidController().step()).start();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            Util.controller.getRenderController().getEntityManager().getCam().resetAngle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
