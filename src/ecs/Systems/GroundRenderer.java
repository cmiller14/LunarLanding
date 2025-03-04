package ecs.Systems;

import ecs.Components.Line;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Triangle;
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

//        Rectangle shipRec = new Rectangle(0.0f, 0.0f, 0.1f, 0.01f, 0.12f);
//        graphics.draw(appearance.image, shipRec, Color.WHITE);

//        Vector3f start = new Vector3f(-1.0f, 0.8f, 0.0f);
//        Vector3f end = new Vector3f(1.0f, 0.8f, 0.0f);
//        graphics.draw(start, end, Color.WHITE);
    }

    private void renderEntity(ecs.Entities.Entity entity) {
        var ground = entity.get(ecs.Components.Ground.class);
        for (int l = 0; l < ground.lines.size(); l++) {
            // draw out each line
            var line = ground.lines.get(l);
            graphics.draw(line.start, line.finish, ground.color);
            fillGround(line, 1.0f, 0.0f, ground.color);
            fillGround(line, 0.05f, 0.1f, Color.GREEN);
        }
    }

    private void fillGround(Line line, float depth, float layer, Color color) {
        // form triangle 1
        float bottomY = Math.min(line.start.y + depth, 1.0f);
        Vector3f topLeft = line.start;
        Vector3f topRight = line.finish;
        Vector3f bottom = new Vector3f(topLeft.x, bottomY, layer);
        Triangle t1 = new Triangle(topLeft, topRight, bottom);
        // form triangle 2
        bottomY = Math.min(line.finish.y + depth, 1.0f);
        Vector3f bottomRight = new Vector3f(topRight.x, bottomY, layer);
        Triangle t2 = new Triangle(bottom, topRight, bottomRight);
        graphics.draw(t1, color);
        graphics.draw(t2, color);
    }
}