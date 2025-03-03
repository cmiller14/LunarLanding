import ecs.Entities.*;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import edu.usu.graphics.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();

    private ecs.Systems.Renderer sysRenderer;
    private ecs.Systems.KeyboardInput sysKeyboardInput;
    private ecs.Systems.GroundRenderer sysGroundRenderer;
    private ecs.Systems.UpdateGround sysGroundUpdater;

    public void initialize(Graphics2D graphics) {
        var texSquare = new Texture("resources/images/square-outline.png");

        sysRenderer = new Renderer(graphics);
        sysKeyboardInput = new KeyboardInput(graphics.getWindow());
        sysGroundRenderer = new GroundRenderer(graphics);
        sysGroundUpdater = new UpdateGround();

        initializeGround();

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update
        sysGroundUpdater.update(elapsedTime);

        for (var entity : removeThese) {
            removeEntity(entity);
        }
        removeThese.clear();

        for (var entity : addThese) {
            addEntity(entity);
        }
        addThese.clear();

        // Because ECS framework, rendering is now part of the update
        sysRenderer.update(elapsedTime);
        sysGroundRenderer.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysRenderer.add(entity);
        sysGroundRenderer.add(entity);
        sysGroundUpdater.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysRenderer.remove(entity.getId());
        sysGroundRenderer.remove(entity.getId());
        sysGroundUpdater.remove(entity.getId());
    }

    private void initializeGround() {
        MyRandom rnd = new MyRandom();
        float startY = rnd.nextRange(-0.1f, 0.5f);
        float finishY = rnd.nextRange(-0.1f, 0.5f);
        Vector3f start = new Vector3f(-1.0f, startY, 0.0f);
        Vector3f finish = new Vector3f(1.0f, finishY, 0.0f);
        var proposed = Ground.create(start, finish, Color.WHITE);
        addEntity(proposed);
    }





}
