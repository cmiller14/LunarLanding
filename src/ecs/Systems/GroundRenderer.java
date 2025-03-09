package ecs.Systems;

import ecs.Components.Line;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
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
    }

    private void renderEntity(ecs.Entities.Entity entity) {
        var ground = entity.get(ecs.Components.Ground.class);
        for (int l = 0; l < ground.lines.size(); l++) {
            // draw out each line
            var line = ground.lines.get(l);
            if (line.safe) {
                fillGround(line, 0.01f, 0.12f, Color.GREEN);
            }
            fillGround(line, 0.01f, 0.11f, Color.GREY);
            fillGround(line, 1.0f, 0.1f, Color.BLACK);
        }
    }

    private void fillGround(Line line, float depth, float layer, Color color) {
        float x1 = line.start.x;
        float x2 = line.finish.x;
        float y1 = line.start.y;
        float y2 = line.finish.y;

        // compute dx dy
        float dx = x2 - x1;
        float dy = y2 - y1;

        dx = -dx;
        float length = (float) Math.sqrt((dx * dx) + (dy* dy));
        // form first triangle
        Vector3f topLeft = line.start;
        Vector3f topRight = line.finish;
        Vector3f bottom = new Vector3f(line.start.x, line.start.y + depth, layer);
        Triangle t1 = new Triangle(topLeft, topRight, bottom);

        // solve for missing x value
        float knownX1 = bottom.x;
        float knownY1 = bottom.y;
        float knownY2 = line.finish.y + depth;
        float missingX = knownX1 + (float)Math.sqrt((length*length)-((knownY2 - knownY1) * (knownY2 - knownY1)));

        // form second triangle
        Vector3f bottomLeft = bottom;
        Vector3f top = line.finish;
        Vector3f bottomRight = new Vector3f(missingX, knownY2, layer);
        Triangle t2 = new Triangle(bottomLeft, top, bottomRight);

        graphics.draw(t1, color);
        graphics.draw(t2, color);

    }
}