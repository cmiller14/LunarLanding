package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import org.joml.Vector3f;

public class BackGround{
    public static Entity create(Texture image, Color color) {
        var background = new Entity();
        background.add(new ecs.Components.Appearance(image, color));
        background.add(new ecs.Components.Background());
        return background;
    }
}
