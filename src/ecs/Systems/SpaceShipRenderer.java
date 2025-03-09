package ecs.Systems;

import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import org.joml.Vector2f;

public class SpaceShipRenderer extends System{

    private final Graphics2D graphics;

    public SpaceShipRenderer(Graphics2D graphics) {
        super(ecs.Components.Ship.class,
                ecs.Components.Appearance.class,
                ecs.Components.Position.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        // Draw each of the game entities!
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) renderEntity(entity);
        }

    }

    private void renderEntity(ecs.Entities.Entity entity) {
        var appearance = entity.get(ecs.Components.Appearance.class);
        var position = entity.get(ecs.Components.Position.class);

        float left = position.x;
        float top = position.y;
        float width = position.width;
        float height = position.width;

        Rectangle shipRec = new Rectangle(left, top, width, height, 0.12f);

        Vector2f center = new Vector2f(
                left + shipRec.width / 2,
                top + shipRec.height / 2);

        graphics.draw(appearance.image, shipRec, position.rotation + (float)Math.PI*3/2, center, appearance.color);


    }
}
