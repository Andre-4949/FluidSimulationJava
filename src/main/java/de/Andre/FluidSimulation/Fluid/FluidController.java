package de.Andre.FluidSimulation.Fluid;

import de.Andre.FluidSimulation.Controller.ConfigController;
import de.Andre.FluidSimulation.Util;

public class FluidController {
    private int N = (int) Math.pow(2, 5); // 0: 1 ; 1 : 2; 2:4;3:8;4:16;5:32;6:64
    private final FluidCube fluidCube;
    private float diffusion = 0;
    private float viscosity = 0.2f;
    private float dt = 0.2f;
    private final ConfigController controller = Util.controller;

    public FluidController(int N) {
        this.N = N;
        this.fluidCube = new FluidCube(N, diffusion, viscosity, dt);
    }

    public FluidController() {
        this.fluidCube = new FluidCube(N, diffusion, viscosity, dt);
    }


    public void step() {
        this.fluidCube.step();
        this.fluidCube.render();
//        this.fluidCube.fade();

        this.fluidCube.addDensity(N / 2, N / 2, N / 2, 40);
        this.fluidCube.addVelocity(N / 2, N / 2, N / 2,0,0,1f);
        this.fluidCube.gravity();
        this.fluidCube.fade();
    }
}
