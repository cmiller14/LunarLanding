package ecs.Systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class Renderer extends System {

    private final Graphics2D graphics;

    public Renderer(Graphics2D graphics) {
        super(ecs.Components.Appearance.class,
                ecs.Components.Position.class);

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

        }
}
