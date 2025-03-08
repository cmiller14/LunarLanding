package ecs.Entities;

import ecs.Components.Appearance;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class SpaceShip {


    public static Entity create(Texture image, Font font, Color color, float x, float y, float width) {

        var spaceShip = new Entity();

        spaceShip.add(new ecs.Components.Ship());
        spaceShip.add(new ecs.Components.Appearance(image, color));
        spaceShip.get(Appearance.class).font = font;
        spaceShip.add(new ecs.Components.Position(x, y, width));
        spaceShip.add(new ecs.Components.Collision());
        return spaceShip;
    }

    public static void enableControls(Entity spaceShip) {
        if (!spaceShip.contains(ecs.Components.KeyboardControlled.class)) {
            spaceShip.add(new ecs.Components.KeyboardControlled(
                    Map.of(
                            GLFW_KEY_UP, ecs.Components.Movable.Direction.Up,
                            GLFW_KEY_DOWN, ecs.Components.Movable.Direction.Down,
                            GLFW_KEY_LEFT, ecs.Components.Movable.Direction.Left,
                            GLFW_KEY_RIGHT, ecs.Components.Movable.Direction.Right
                    )));
        }
    }

    public static void enableMovement(Entity spaceShip) {
        final double MOVE_INTERVAL = .150; // seconds
        if (!spaceShip.contains(ecs.Components.Movable.class)) {
            spaceShip.add(new ecs.Components.Movable(ecs.Components.Movable.Direction.Stopped, MOVE_INTERVAL));
        }
    }
}
