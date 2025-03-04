package ecs.Systems;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

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

        // TODO: calculate the left and top according to the position
        float left = position.x;
        float top = position.y;
        float width = 1/5f;
        float height = 1/5f;

        Rectangle shipRec = new Rectangle(left, top, width, height, 0.12f);
        graphics.draw(appearance.image, shipRec, Color.WHITE);

    }
}
