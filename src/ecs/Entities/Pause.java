package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;


public class Pause {
    public static Entity create(Texture image, Color color, Font font) {
        var pause = new Entity();
        pause.add(new ecs.Components.Appearance(image, color));
        pause.get(ecs.Components.Appearance.class).font = font;
        pause.add(new ecs.Components.Pause());
        pause.add(new ecs.Components.KeyboardControlled());
        return pause;
    }
}
