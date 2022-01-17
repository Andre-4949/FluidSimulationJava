package de.Andre.FluidSimulation.Controller;//h


import de.Andre.FluidSimulation.Fluid.FluidController;
import de.Andre.FluidSimulation.Main;

public class ConfigController {
    private final Main main;
    private final RenderController renderController;
    private final FluidController fluidController;
    private boolean isFluidSimulation = true;
    public ConfigController(Main main) {
        this.main = main;
        this.renderController = new RenderController(this);
        this.fluidController = new FluidController();
    }

    public Main getMain() {
        return main;
    }

    public RenderController getRenderController() {
        return renderController;
    }

    public FluidController getFluidController() {
        return fluidController;
    }

    public boolean isFluidSimulation() {
        return isFluidSimulation;
    }
}
