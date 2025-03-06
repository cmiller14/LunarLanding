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

    private ecs.Systems.KeyboardInput sysKeyboardInput;
    private ecs.Systems.GroundRenderer sysGroundRenderer;
    private ecs.Systems.UpdateGround sysGroundUpdater;
    private ecs.Systems.SpaceShipRenderer sysSpaceShipRenderer;
    private ecs.Systems.UpdateSpaceShip sysSpaceShipUpdater;
    private ecs.Systems.CollisionUpdater sysCollisionUpdater;
    private ecs.Systems.Renderer sysRenderer;

    public void initialize(Graphics2D graphics) {
        var ship = new Texture("resources/images/ship.png");
        var background = new Texture("resources/images/spaceBackground.jpg");

        sysKeyboardInput = new KeyboardInput(graphics.getWindow());
        sysGroundRenderer = new GroundRenderer(graphics);
        sysGroundUpdater = new UpdateGround();
        sysSpaceShipRenderer = new SpaceShipRenderer(graphics);
        sysSpaceShipUpdater = new UpdateSpaceShip();
        sysCollisionUpdater = new CollisionUpdater();
        sysRenderer = new Renderer(graphics);

        initializeBackground(background);
        initializeGround();
        initializeShip(ship);

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update
        sysGroundUpdater.update(elapsedTime);
        sysSpaceShipUpdater.update(elapsedTime);
        sysCollisionUpdater.update(elapsedTime);

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
        sysSpaceShipRenderer.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysGroundRenderer.add(entity);
        sysGroundUpdater.add(entity);
        sysSpaceShipRenderer.add(entity);
        sysSpaceShipUpdater.add(entity);
        sysCollisionUpdater.add(entity);
        sysRenderer.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysGroundRenderer.remove(entity.getId());
        sysGroundUpdater.remove(entity.getId());
        sysSpaceShipRenderer.remove(entity.getId());
        sysSpaceShipUpdater.remove(entity.getId());
        sysCollisionUpdater.remove(entity.getId());
        sysRenderer.remove(entity.getId());
    }

    private void initializeBackground(Texture image) {
        var background = BackGround.create(image, Color.WHITE);
        addEntity(background);
    }

    private void initializeGround() {
        MyRandom rnd = new MyRandom();
        float startY = rnd.nextRange(-0.1f, 0.5f);
        float finishY = rnd.nextRange(-0.1f, 0.5f);
        Vector3f start = new Vector3f(-1.0f, startY, 0.0f);
        Vector3f finish = new Vector3f(1.0f, finishY, 0.0f);
        var ground = Ground.create(start, finish, Color.WHITE);
        addEntity(ground);
    }

    private void initializeShip(Texture image) {
        float startX = 0.0f;
        float startY = -0.65f;
        float width = 1/5f;
        var ship = SpaceShip.create(image, Color.WHITE, startX, startY, width);
        addEntity(ship);
    }

}
