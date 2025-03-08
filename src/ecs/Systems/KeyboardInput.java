package ecs.Systems;

import ecs.Components.Movable;
import ecs.Entities.Entity;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyboardInput extends System {

    private final long window;

    private final HashMap<Integer, Boolean> keysPressed = new HashMap<>();
    private final HashMap<Integer, CommandEntry> commandEntries = new HashMap<>();

    /**
     * Used to keep track of the details associated with a registered command
     */
    private record CommandEntry(int key, boolean keyPressOnly) {
    }

    public KeyboardInput(long window) {
        super(ecs.Components.KeyboardControlled.class);

        registerCommand(GLFW_KEY_P, true);
        registerCommand(GLFW_KEY_UP, false);
        registerCommand(GLFW_KEY_DOWN, false);
        registerCommand(GLFW_KEY_LEFT, false);
        registerCommand(GLFW_KEY_RIGHT, false);
        registerCommand(GLFW_KEY_ESCAPE, true);

        this.window = window;
    }

    private boolean isKeyNewlyPressed(int key) {
        return (glfwGetKey(window, key) == GLFW_PRESS) && !keysPressed.get(key);
    }

    public void registerCommand(int key, boolean keyPressOnly) {
        commandEntries.put(key, new CommandEntry(key, keyPressOnly));
        // Start out by assuming the key isn't currently pressed
        keysPressed.put(key, false);
    }

    @Override
    public void update(double gameTime) {

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) {
                shipKeys(entity);
            }
            if (entity.contains(ecs.Components.Pause.class)) {
                pauseKeys(entity);
            }
        }

        for (var entry : commandEntries.entrySet()) {
            keysPressed.put(entry.getKey(), glfwGetKey(window, entry.getKey()) == GLFW_PRESS);
        }
    }

    private void pauseKeys(Entity entity) {
        var pause = entity.get(ecs.Components.Pause.class);
        if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS && isKeyNewlyPressed(GLFW_KEY_P)) {
            pause.pause = !pause.pause;
        }
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS && isKeyNewlyPressed(GLFW_KEY_ESCAPE)) {
            pause.pause = !pause.pause;
        }
    }

    private void shipKeys(Entity entity) {
        var movable = entity.get(Movable.class);
        var input = entity.get(ecs.Components.KeyboardControlled.class);
        var ship = entity.get(ecs.Components.Ship.class);

        if (glfwGetKey(window, input.lookup.get(Movable.Direction.Up)) == GLFW_PRESS) {
            movable.facing.add(input.keys.get(GLFW_KEY_UP));
        }
        if (glfwGetKey(window, input.lookup.get(Movable.Direction.Down)) == GLFW_PRESS) {
            movable.facing.add(input.keys.get(GLFW_KEY_DOWN));

        }
        if (glfwGetKey(window, input.lookup.get(Movable.Direction.Left)) == GLFW_PRESS) {
            movable.facing.add(input.keys.get(GLFW_KEY_LEFT));

        }
        if (glfwGetKey(window, input.lookup.get(Movable.Direction.Right)) == GLFW_PRESS) {
            movable.facing.add(input.keys.get(GLFW_KEY_RIGHT));
        }
        if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS && isKeyNewlyPressed(GLFW_KEY_P)) {
            ship.launchPause = !ship.launchPause;
        }
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS && isKeyNewlyPressed(GLFW_KEY_ESCAPE)) {
            ship.launchPause = !ship.launchPause;
        }
    }
}
