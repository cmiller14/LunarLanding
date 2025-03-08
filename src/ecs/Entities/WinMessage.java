package ecs.Entities;

import ecs.Components.Appearance;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;
import org.joml.Vector3f;

public class WinMessage {
    public static Entity create(Texture image, Font font, int score) {
        var winMessage = new Entity();
        winMessage.add(new ecs.Components.Appearance(image, Color.WHITE));
        winMessage.get(Appearance.class).font = font;
        winMessage.add(new ecs.Components.WinMessage(score));
        winMessage.add(new ecs.Components.KeyboardControlled());
        return winMessage;
    }
}
