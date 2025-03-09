package ecs.Systems;

import ecs.Components.Ship;
import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class ParticleRenderer extends System {
    private Graphics2D graphics;

    public ParticleRenderer(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        boolean fuelGone = false;
        boolean crash = false;
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) {
                var ship = entity.get(ecs.Components.Ship.class);
                fuelGone = ship.fuel <= 0;
                if (ship.collision && !ship.win) {
                    crash = true;
                }
            }
        }
        if (fuelGone) return;
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Particles.class)) {
                if (crash) {
                    // render the crash
                    renderCrash(entity);
                } else {
                    render(entity);
                }
            }
        }
    }

    private void renderCrash(Entity entity) {
        var particles = entity.get(ecs.Components.Particles.class);
        var fire = entity.get(ecs.Components.Fire.class);
        if (particles.countdown) return;
        for (var particle : particles.particles.values()) {
            graphics.draw(fire.fire, particle.area, particle.rotation, particle.center, Color.WHITE);
        }
    }

    private void render(Entity entity) {
        var particles = entity.get(ecs.Components.Particles.class);
        if (particles.countdown) return;
        var appearance = entity.get(ecs.Components.Appearance.class);
        for (var particle : particles.particles.values()) {
            graphics.draw(appearance.image, particle.area, particle.rotation, particle.center, Color.WHITE);
        }
    }
}
