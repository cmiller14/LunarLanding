package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import org.joml.Vector3f;

public class Ground {
    // I think that this entity will just be a ton of different positions that will then be able to be rendered into a line
    // There will also need to be polygons to draw down from the line


    public static Entity create(Vector3f start, Vector3f finish, Color color) {
        var ground = new Entity();
        ground.add(new ecs.Components.Ground(color, start, finish));
        ground.add(new ecs.Components.Collision());
        return ground;
    }
}
