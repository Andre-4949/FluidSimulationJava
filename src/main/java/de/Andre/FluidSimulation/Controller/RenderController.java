package de.Andre.FluidSimulation.Controller;//h

import de.Andre.FluidSimulation.Entity.EntityManager;
import de.Andre.FluidSimulation.Extentions.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class RenderController extends Canvas {
    private final ConfigController controller;
    private JFrame frame;
    private static final String title = "FluidSimulation";
    private UIController userInputController;
    private Thread thread;
    public static final boolean FULLSCREEN = false;
    public static final int WIDTH = !FULLSCREEN ? 800 : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    public static final int HEIGHT = !FULLSCREEN ? 600 : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    private boolean running = true;
    private int fps = 60;
    private EntityManager entityManager;

    private String mode = "x";

    public RenderController(ConfigController controller) {
        this.controller = controller;
        this.entityManager = new EntityManager(controller);
        this.frame = new JFrame(title);
        this.userInputController = new UIController(controller);
        setupForFrame();
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.setResizable(true);
    }

    private void setupForFrame() {
        this.frame.setLayout(null);
        this.frame.add(this);
        this.frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(true);
        this.userInputController.registerListener(this.frame);
        startUpdateTask();
        Point3D p = new Point3D(0, 0, 0);
        p.setTHICKNESS(10);
    }

    private void startUpdateTask() {
        this.thread = new Thread(() -> {
            try {
                Thread.sleep(2 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            final double ns = 1_000_000_000.0 / fps;
            double delta = 0;
            int frames = 0;
            this.entityManager.init(this.userInputController);
            if (controller.isFluidSimulation()) {
                this.controller.getFluidController().step();
                Thread t = new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(3000);
                            this.controller.getFluidController().step();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
                t.setDaemon(true);
                t.start();
            }
            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while (delta >= 1) { // This happens every fps
                    update();
                    delta--;
                }
                render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    this.frame.setTitle(title + " | " + frames + " fps");
                    frames = 0;
                }
            }
        });
        this.thread.start();
    }


    public JFrame getFrame() {
        return frame;
    }

    private void render() {
        this.frame.pack();

        BufferStrategy bs = this.frame.getBufferStrategy();
        if (bs == null) {
            try {
                this.frame.createBufferStrategy(3);
            } catch (IllegalStateException e) {

            }
            return;
        }
        Graphics g = bs.getDrawGraphics();
        drawBackground(g);
        this.entityManager.render(g);
        g.dispose();
        bs.show();
    }

    private void update() {
        this.entityManager.update();
        this.frame.pack();
    }

    private void drawBackground(Graphics g) {
//        g.setColor(new Color(145, 145, 145));
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);
        //this.frame.getContentPane().setBackground(Color.RED);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
