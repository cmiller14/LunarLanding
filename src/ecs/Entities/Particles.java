package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

public class Particles {
    public static Entity create(Texture image,
                                Texture fire,
                                Color color,
                                Vector2f center,
                                float sizeMean,
                                float sizeStdDev,
                                float speedMean,
                                float speedStdDev,
                                float lifetimeMean,
                                float lifetimeStdDev) {
        var particles = new Entity();
        particles.add(new ecs.Components.Appearance(image, color));
        particles.add(new ecs.Components.Particles(center,
                sizeMean,
                sizeStdDev,
                speedMean,
                speedStdDev,
                lifetimeMean,
                lifetimeStdDev));
        particles.add(new ecs.Components.KeyboardControlled());
        particles.add(new ecs.Components.Fire(fire));
        return particles;
    }
}
