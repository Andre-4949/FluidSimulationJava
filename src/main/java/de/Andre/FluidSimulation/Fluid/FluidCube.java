package de.Andre.FluidSimulation.Fluid;

import de.Andre.FluidSimulation.Entity.Builder.BasicEntityBuilder;
import de.Andre.FluidSimulation.Entity.EntityManager;
import de.Andre.FluidSimulation.Extentions.Point3D;
import de.Andre.FluidSimulation.Util;

import java.awt.*;

public class FluidCube {

    private final int size;

    //time step
    private float dt;

    //diffusion
    private float diff;

    //viscosity
    private float visc;

    //previous density
    private float[] s;

    //density (now)
    private float[] density;

    //Velocities (now)
    private float[] Vx;
    private float[] Vy;
    private float[] Vz;

    //previous Velocities
    private float[] Vx0;
    private float[] Vy0;
    private float[] Vz0;


    private int iteration = 4;

    public FluidCube(int N, float diffusion, float viscosity, float dt) {

        this.size = N;
        this.dt = dt;
        this.diff = diffusion;
        this.visc = viscosity;

        this.s = new float[N * N * N];
        this.density = new float[N * N * N];

        this.Vx = new float[N * N * N];
        this.Vy = new float[N * N * N];
        this.Vz = new float[N * N * N];

        this.Vx0 = new float[N * N * N];
        this.Vy0 = new float[N * N * N];
        this.Vz0 = new float[N * N * N];
    }


    public void addDensity(int x, int y, int z, float amount) {
        System.out.println("addDensity");
        int index = IX(x, y, z);
        this.density[index] += amount;
    }

    public synchronized void render() {
        EntityManager em = Util.controller.getRenderController().getEntityManager();
        em.clearEntities();
        System.out.println("render");
        long n = System.currentTimeMillis();
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                for (int z = 0; z < this.size; z++) {
                    int index = this.IX(x, y, z);
                    float d = this.density[index];
                    Color c;

                    c = new Color(1, 0, 0, d != 0.0 ? Math.min(Math.max(d, 10) * 5, 255) / 255 : 0);

                    int halfSize = this.size / 2;
                    em.addEntity(
                            BasicEntityBuilder.createCube(
                                    new Point3D(x * 3 - halfSize, y * 3 - halfSize, z * 3 - halfSize).subtractPoint(em.getCam().getCamPos()),
                                    1,
                                    c,
                                    Util.controller));
                }
            }
        }
        System.out.println(System.currentTimeMillis()-n);
        em.rotateWithOutCam(true, em.getCam().getRoll() / 2, em.getCam().getPitch() / 2, em.getCam().getYaw() / 2);
    }

    public void gravity() {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                for (int z = 0; z < this.size; z++) {
                    this.addVelocity(x, y, z, 0,0f,-0.5f);
                }
            }
        }
    }

    private int IX(int x, int y, int z) {
        return (x +
                (y * this.size) +
                (z * this.size * this.size)) % this.s.length;
    }

    public void addVelocity(int x, int y, int z, float amountX, float amountY, float amountZ) {
        int index = IX(x, y, z);

        this.Vx[index] += amountX;
        this.Vy[index] += amountY;
        this.Vz[index] += amountZ;
    }

    public void step() {
        float visc = this.visc;
        float diff = this.diff;
        float dt = this.dt;
        float[] Vx = this.Vx;
        float[] Vy = this.Vy;
        float[] Vz = this.Vz;
        float[] Vx0 = this.Vx0;
        float[] Vy0 = this.Vy0;
        float[] Vz0 = this.Vz0;
        float[] s = this.s;
        float[] density = this.density;

        diffuse(1, Vx0, Vx, visc, dt);
        diffuse(2, Vy0, Vy, visc, dt);
        diffuse(3, Vz0, Vz, visc, dt);

        project(Vx0, Vy0, Vz0, Vx, Vy);

        advect(1, Vx, Vx0, Vx0, Vy0, Vz0, dt);
        advect(2, Vy, Vy0, Vx0, Vy0, Vz0, dt);
        advect(3, Vz, Vz0, Vx0, Vy0, Vz0, dt);

        project(Vx, Vy, Vz, Vx0, Vy0);

        diffuse(0, s, density, diff, dt);
        advect(0, density, s, Vx, Vy, Vz, dt);
    }

    private void diffuse(int b, float[] vx0, float[] vx, float diff, float dt) {
        float a = dt * diff * (this.size - 2) * (this.size - 2);
        lin_solve(b, vx0, vx, a, 1 + 6 * a);
    }

    private void lin_solve(int b, float[] vx0, float[] vx, float a, float c) {
        float cRecip = 1.0F / c;
        for (int k = 0; k < this.iteration; k++) {
            for (int m = 1; m < this.size - 1; m++) {
                for (int j = 1; j < this.size - 1; j++) {
                    for (int i = 1; i < this.size - 1; i++) {
                        vx0[IX(i, j, m)] =
                                (vx[IX(i, j, m)]
                                        + a *
                                        (
                                                vx0[IX(i + 1, j, m)] +
                                                        vx0[IX(i - 1, j, m)] +
                                                        vx0[IX(i, j + 1, m)] +
                                                        vx0[IX(i, j - 1, m)] +
                                                        vx0[IX(i, j, m + 1)] +
                                                        vx0[IX(i, j, m - 1)]
                                        )
                                ) * cRecip;
                    }
                }
            }
            set_bnd(b, vx0);
        }
    }


    private void project(float[] velocX, float[] velocY, float[] velocZ, float[] p, float[] div) {
        for (int k = 1; k < this.size - 1; k++) {
            for (int j = 1; j < this.size - 1; j++) {
                for (int i = 1; i < this.size - 1; i++) {
                    div[IX(i, j, k)] = -0.5f * (
                            velocX[IX(i + 1, j, k)] -
                                    velocX[IX(i - 1, j, k)] +
                                    velocY[IX(i, j + 1, k)] -
                                    velocY[IX(i, j - 1, k)] +
                                    velocZ[IX(i, j, k + 1)] -
                                    velocZ[IX(i, j, k - 1)]
                    ) / this.size;
                    p[IX(i, j, k)] = 0;
                }
            }
        }
        set_bnd(0, div);
        set_bnd(0, p);
        lin_solve(0, p, div, 1, 6);

        for (int k = 1; k < this.size - 1; k++) {
            for (int j = 1; j < this.size - 1; j++) {
                for (int i = 1; i < this.size - 1; i++) {
                    velocX[IX(i, j, k)] -= 0.5f * (p[IX(i + 1, j, k)]
                            - p[IX(i - 1, j, k)]) * this.size;
                    velocY[IX(i, j, k)] -= 0.5f * (p[IX(i, j + 1, k)]
                            - p[IX(i, j - 1, k)]) * this.size;
                    velocZ[IX(i, j, k)] -= 0.5f * (p[IX(i, j, k + 1)]
                            - p[IX(i, j, k - 1)]) * this.size;
                }
            }
        }
        set_bnd(1, velocX);
        set_bnd(2, velocY);
        set_bnd(3, velocZ);
    }

    private void advect(int b, float[] d, float[] d0, float[] velocX, float[] velocY, float[] velocZ, float dt) {
        {
            float i0, i1, j0, j1, k0, k1;

            float dtx = dt * (this.size - 2);
            float dty = dt * (this.size - 2);
            float dtz = dt * (this.size - 2);

            float s0, s1, t0, t1, u0, u1;
            float tmp1, tmp2, tmp3, x, y, z;

            float Nfloat = this.size;
            float ifloat, jfloat, kfloat;
            int i, j, k;

            for (k = 1, kfloat = 1; k < this.size - 1; k++, kfloat++) {
                for (j = 1, jfloat = 1; j < this.size - 1; j++, jfloat++) {
                    for (i = 1, ifloat = 1; i < this.size - 1; i++, ifloat++) {
                        tmp1 = dtx * velocX[IX(i, j, k)];
                        tmp2 = dty * velocY[IX(i, j, k)];
                        tmp3 = dtz * velocZ[IX(i, j, k)];
                        x = ifloat - tmp1;
                        y = jfloat - tmp2;
                        z = kfloat - tmp3;

                        if (x < 0.5f) x = 0.5f;
                        if (x > Nfloat + 0.5f) x = Nfloat + 0.5f;
                        i0 = (float) Math.floor(x);//floor
                        i1 = i0 + 1.0f;
                        if (y < 0.5f) y = 0.5f;
                        if (y > Nfloat + 0.5f) y = Nfloat + 0.5f;
                        j0 = (float) Math.floor(y);//floor
                        j1 = j0 + 1.0f;
                        if (z < 0.5f) z = 0.5f;
                        if (z > Nfloat + 0.5f) z = Nfloat + 0.5f;
                        k0 = (float) Math.floor(z);//floor
                        k1 = k0 + 1.0f;

                        s1 = x - i0;
                        s0 = 1.0f - s1;
                        t1 = y - j0;
                        t0 = 1.0f - t1;
                        u1 = z - k0;
                        u0 = 1.0f - u1;

                        int i0i = (int) i0;
                        int i1i = (int) i1;
                        int j0i = (int) j0;
                        int j1i = (int) j1;
                        int k0i = (int) k0;
                        int k1i = (int) k1;

                        d[IX(i, j, k)] =

                                s0 * (t0 * (u0 * d0[IX(i0i, j0i, k0i)]
                                        + u1 * d0[IX(i0i, j0i, k1i)])
                                        + (t1 * (u0 * d0[IX(i0i, j1i, k0i)]
                                        + u1 * d0[IX(i0i, j1i, k1i)])))
                                        + s1 * (t0 * (u0 * d0[IX(i1i, j0i, k0i)]
                                        + u1 * d0[IX(i1i, j0i, k1i)])
                                        + (t1 * (u0 * d0[IX(i1i, j1i, k0i)]
                                        + u1 * d0[IX(i1i, j1i, k1i)])));
                    }
                }
            }
            set_bnd(b, d);
        }
    }

    private void set_bnd(int b, float[] velocityArray) {
        for (int j = 1; j < this.size - 1; j++) {
            for (int i = 1; i < this.size - 1; i++) {
                velocityArray[IX(i, j, 0)] = b == 3 ? -velocityArray[IX(i, j, 1)] : velocityArray[IX(i, j, 1)];
                velocityArray[IX(i, j, this.size - 1)] = b == 3 ? -velocityArray[IX(i, j, this.size - 2)] : velocityArray[IX(i, j, this.size - 2)];
            }
        }
        for (int k = 1; k < this.size - 1; k++) {
            for (int i = 1; i < this.size - 1; i++) {
                velocityArray[IX(i, 0, k)] = b == 2 ? -velocityArray[IX(i, 1, k)] : velocityArray[IX(i, 1, k)];
                velocityArray[IX(i, this.size - 1, k)] = b == 2 ? -velocityArray[IX(i, this.size - 2, k)] : velocityArray[IX(i, this.size - 2, k)];
            }
        }
        for (int k = 1; k < this.size - 1; k++) {
            for (int j = 1; j < this.size - 1; j++) {
                velocityArray[IX(0, j, k)] = b == 1 ? -velocityArray[IX(1, j, k)] : velocityArray[IX(1, j, k)];
                velocityArray[IX(this.size - 1, j, k)] = b == 1 ? -velocityArray[IX(this.size - 2, j, k)] : velocityArray[IX(this.size - 2, j, k)];
            }
        }

        velocityArray[IX(0, 0, 0)] = 0.33f *
                (velocityArray[IX(1, 0, 0)]
                        + velocityArray[IX(0, 1, 0)]
                        + velocityArray[IX(0, 0, 1)]);

        velocityArray[IX(0, this.size - 1, 0)] = 0.33f *
                (velocityArray[IX(1, this.size - 1, 0)]
                        + velocityArray[IX(0, this.size - 2, 0)]
                        + velocityArray[IX(0, this.size - 1, 1)]);

        velocityArray[IX(0, 0, this.size - 1)] = 0.33f *
                (velocityArray[IX(1, 0, this.size - 1)]
                        + velocityArray[IX(0, 1, this.size - 1)]
                        + velocityArray[IX(0, 0, this.size)]);

        velocityArray[IX(0, this.size - 1, this.size - 1)] = 0.33f *
                (velocityArray[IX(1, this.size - 1, this.size - 1)]
                        + velocityArray[IX(0, this.size - 2, this.size - 1)]
                        + velocityArray[IX(0, this.size - 1, this.size - 2)]);

        velocityArray[IX(this.size - 1, 0, 0)] = 0.33f *
                (velocityArray[IX(this.size - 2, 0, 0)]
                        + velocityArray[IX(this.size - 1, 1, 0)]
                        + velocityArray[IX(this.size - 1, 0, 1)]);

        velocityArray[IX(this.size - 1, this.size - 1, 0)] = 0.33f *
                (velocityArray[IX(this.size - 2, this.size - 1, 0)]
                        + velocityArray[IX(this.size - 1, this.size - 2, 0)]
                        + velocityArray[IX(this.size - 1, this.size - 1, 1)]);

        velocityArray[IX(this.size - 1, 0, this.size - 1)] = 0.33f *
                (velocityArray[IX(this.size - 2, 0, this.size - 1)]
                        + velocityArray[IX(this.size - 1, 1, this.size - 1)]
                        + velocityArray[IX(this.size - 1, 0, this.size - 2)]);

        velocityArray[IX(this.size - 1, this.size - 1, this.size - 1)] = 0.33f *
                (velocityArray[IX(this.size - 2, this.size - 1, this.size - 1)]
                        + velocityArray[IX(this.size - 1, this.size - 2, this.size - 1)]
                        + velocityArray[IX(this.size - 1, this.size - 1, this.size - 2)]);

    }

    public void fade() {
        for (int i = 0; i < this.density.length; i++) {
            float d = density[i];
            float newD = d*0.9f;
            density[i] = newD;
        }
    }

}
