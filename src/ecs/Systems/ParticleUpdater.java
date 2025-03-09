package ecs.Systems;

import ecs.Components.Particle;
import ecs.Components.Particles;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ParticleUpdater extends System{
    private final MyRandom random = new MyRandom();
    boolean explosion = false;

    @Override
    public void update(double gameTime) {
        Particles particles = new Particles();
        boolean countdown = false;
        boolean crash = false;
        for (var entity: entities.values()) {
            if (entity.contains(ecs.Components.Countdown.class)) {
                countdown = true;
            }
            if (entity.contains(ecs.Components.Ship.class)) {
                var ship = entity.get(ecs.Components.Ship.class);
                if (ship.collision && !ship.win) {
                    crash = true;
                }
            }
        }
        if (countdown) return;
        for (var entity: entities.values()) {
            if (entity.contains(ecs.Components.Particles.class)) {
                particles = entity.get(Particles.class);
                // Update existing particles
                List<Long> removeMe = new ArrayList<>();
                for (Particle p : particles.particles.values()) {
                    if (!p.update(gameTime)) {
                        removeMe.add(p.name);
                    }
                }
                // Remove dead particles
                for (Long key : removeMe) {
                    particles.particles.remove(key);
                }
                // createThrustParticle new particles
                if (particles.createParticles) {
                    shipThrust(3, particles, 0.1f, 0.4f);
                }
                if (crash && !explosion) {
                    shipCrash(particles);
                };
            }
        }
        for (var entity: entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) {
                if (!entity.contains(ecs.Components.Movable.class)) {
                    return;
                }
                var position = entity.get(ecs.Components.Position.class);
                var ship = entity.get(ecs.Components.Ship.class);

                float left = position.x;
                float top = position.y;
                float width = position.width;
                float height = position.width;

                Vector2f center = new Vector2f(
                        left + width / 2,
                        top + height / 2);

                float angleX = (float)Math.cos(ship.angle + Math.PI/2);
                float angleY = (float)Math.sin(ship.angle + Math.PI/2);

                Vector2f newCenter = new Vector2f(center.x + 0.05f * angleX,
                        center.y + 0.05f * angleY);

                particles.center.x = newCenter.x;
                particles.center.y = newCenter.y;

            }
        }
    }

    private void shipThrust(int x, Particles particles, float min, float max) {
        for (int i = 0; i < x; i++) {
            var particle = createThrustParticle(particles, min, max);
            particles.particles.put(particle.name, particle);
        }
    }

    private void shipCrash(Particles particles) {
        for (int i = 0; i < 5; i++) {
            var particle = createExplosionParticle(particles);
            particles.particles.put(particle.name, particle);
        }
    }

    private Particle createExplosionParticle(Particles particles) {
        float size = 0.1f;
        var p = new Particle(
                new Vector2f(particles.center.x, particles.center.y),
                this.random.nextCircleVector(),
                0.12f,
                new Vector2f(size,size),
                particles.lifetimeMean
        );
        return p;
    }

    private Particle createThrustParticle(Particles particles, float min, float max) {
        float size = (float) this.random.nextGaussian(particles.sizeMean, particles.sizeStdDev);
        var p = new Particle(
                new Vector2f(particles.center.x, particles.center.y),
                this.random.nextCircleVectorRange(min, max),
                (float) this.random.nextGaussian(particles.speedMean, particles.speedStdDev),
                new Vector2f(size, size),
                this.random.nextGaussian(particles.lifetimeMean, particles.lifetimeStdDev));
        return p;
    }
}
