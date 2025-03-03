package ecs.Systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3f;


public class GroundRenderer extends System {

    private final Graphics2D graphics;

    public GroundRenderer(Graphics2D graphics) {
        super(ecs.Components.Ground.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        // Draw each of the game entities!
        for (var entity : entities.values()) {
            renderEntity(entity);
        }
    }

    private void renderEntity(ecs.Entities.Entity entity) {
        var ground = entity.get(ecs.Components.Ground.class);
        for (int l = 0; l < ground.lines.size(); l++) {
            // draw out each line
            var line = ground.lines.get(l);
            graphics.draw(line.start, line.finish, ground.color);
        }
    }
}