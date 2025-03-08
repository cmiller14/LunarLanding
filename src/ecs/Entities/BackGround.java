package ecs.Entities;

import ecs.Components.Appearance;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;

public class BackGround{
    public static Entity create(Texture image, Color color, Font font) {
        var background = new Entity();
        background.add(new ecs.Components.Appearance(image, color));
        background.add(new ecs.Components.Background());
        background.get(Appearance.class).font = font;
        return background;
    }
}
