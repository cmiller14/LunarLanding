package ecs.Systems;

import ecs.Components.Line;
import ecs.Entities.Entity;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CollisionUpdater extends System {
    @Override
    public void update(double elapsedTime) {
        List<Entity> collisions = new ArrayList<>();
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Collision.class)) collisions.add(entity);
        }

        for (var entity : collisions) {
            // check collision but really only need to check if space ship collides with anything else
            if (entity.contains(ecs.Components.Ship.class)) checkGroundCollision(collisions, entity);
        }
    }

    private void checkGroundCollision(List<Entity> collisions, Entity ship) {
        // remove ship entity from collision
        var shipPosition = ship.get(ecs.Components.Position.class);
        var shipComp = ship.get(ecs.Components.Ship.class);


        // check through all collision entities to see if they collied with the ship
        for (var entity : collisions) {
            if (entity.contains(ecs.Components.Ground.class)) {
                var ground = entity.get(ecs.Components.Ground.class);
                for (Line line : ground.lines) {
                    Vector2f pt1 = new Vector2f(line.start.x, line.start.y);
                    Vector2f pt2 = new Vector2f(line.finish.x, line.finish.y);
                    Vector2f circleCenter = new Vector2f(
                            shipPosition.x + shipPosition.width  / 2,
                            shipPosition.y + shipPosition.width / 2);
                    boolean collision = lineCircleIntersection(pt1, pt2, circleCenter, shipPosition.radius);
                    if (collision) {
                        // check to see if win or crash
                        // On one of the pre-defined safe landing zones
                        // Speed less than 2 m/s
                        // Angle between 355 and 5 degrees
                        // Note: Upon safe landing, player can no longer control the ship; rotation and thrust controls controls disabled.
                        shipComp.collision = true;
                    }
                }

            }
        }
    }

    private boolean checkWinOrCrash() {
        return true;
    }

    private boolean lineCircleIntersection(Vector2f pt1, Vector2f pt2, Vector2f circleCenter, float circleRadius) {
        // Translate points to circle's coordinate system
        Vector2f d = pt2.sub(pt1); // Direction vector of the line
        Vector2f f = pt1.sub(circleCenter); // Vector from circle center to the start of the line

        float a = d.dot(d);
        float b = 2 * f.dot(d);
        float c = f.dot(f) - circleRadius * circleRadius;

        float discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, no real roots and thus no intersection
        if (discriminant < 0) {
            return false;
        }

        // Check if the intersection points are within the segment
        discriminant = (float) Math.sqrt(discriminant);
        float t1 = (-b - discriminant) / (2 * a);
        float t2 = (-b + discriminant) / (2 * a);

        if (t1 >= 0 && t1 <= 1) {
            return true;
        }
        if (t2 >= 0 && t2 <= 1) {
            return true;
        }

        return false;
    }
}
