package ecs.Components;

import org.joml.Vector2f;

import java.util.HashMap;

public class Particles extends Component{
    public Vector2f center;
    public float sizeMean;
    public float sizeStdDev;
    public float speedMean;
    public float speedStdDev;
    public float lifetimeMean;
    public float lifetimeStdDev;
    public boolean createParticles;
    public boolean countdown = false;

    public final HashMap<Long, Particle> particles = new HashMap<>();

    public Particles() {

    }

    public Particles(Vector2f center, float sizeMean, float sizeStdDev, float speedMean, float speedStdDev, float lifetimeMean, float lifetimeStdDev) {
        this.center = center;
        this.sizeMean = sizeMean;
        this.sizeStdDev = sizeStdDev;
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
        this.lifetimeMean = lifetimeMean;
        this.lifetimeStdDev = lifetimeStdDev;
    }
}
