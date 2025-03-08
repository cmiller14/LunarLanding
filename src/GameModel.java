import ecs.Components.Ship;
import ecs.Entities.*;
import ecs.Entities.Pause;
import ecs.Systems.*;
import ecs.Systems.Countdown;
import ecs.Systems.KeyboardInput;
import ecs.Systems.WinMessage;
import edu.usu.graphics.*;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
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
    private ecs.Systems.Countdown sysCountdown;
    private ecs.Systems.Pause sysPause;
    private ecs.Systems.WinMessage sysWinMessage;

    private Entity ground;
    private int safeZones = 2;

    private Entity ship;
    private final float startX = 0.0f;
    private final float startY = -0.65f;

    private Entity pause;

    public void initialize(Graphics2D graphics) {
        var ship = new Texture("resources/images/ship.png");
        var background = new Texture("resources/images/spaceBackground.jpg");
        var pauseImage = new Texture("resources/images/pause-background.jpg");
        var font = new Font("resources/fonts/Roboto-Regular.ttf", 36, false);
        var fontCountDown = new Font("resources/fonts/Roboto-Regular.ttf", 72, false);

        sysKeyboardInput = new KeyboardInput(graphics.getWindow());
        sysGroundRenderer = new GroundRenderer(graphics);
        sysGroundUpdater = new UpdateGround(
                (Entity entity) -> { // on win
                    removeEntity(entity);
                    removeAllEntities();
                    // save the score
                    // display win message with score
                    initializeWinMessage(pauseImage, fontCountDown, this.ship.get(Ship.class).score);
                    // instructions for how to close escape
                }
        );
        sysSpaceShipRenderer = new SpaceShipRenderer(graphics);
        sysSpaceShipUpdater = new UpdateSpaceShip(
                () -> { // onComplete
                    initializeCountdown(fontCountDown);
                    removeEntity(this.ground);
                    safeZones--;
                    initializeGround();
                },
                () -> { // onWin
                    removeEntity(this.ship);
                },
                () -> { // onPause
                    initializePause(pauseImage, fontCountDown);
                }
        );
        sysCollisionUpdater = new CollisionUpdater();
        sysRenderer = new Renderer(graphics);
        sysCountdown = new Countdown(graphics,
                (Entity entity) -> {
                    removeEntity(entity);
                    ecs.Entities.SpaceShip.enableControls(this.ship);
                    ecs.Entities.SpaceShip.enableMovement(this.ship);
                    this.ship.get(Ship.class).countdown = false;
                    addEntity(this.ship);
                });
        sysPause = new ecs.Systems.Pause(graphics,
                (Entity entity) -> {
                    removeEntity(entity);
                    this.ship.get(Ship.class).pause = false;
                    initializeCountdown(fontCountDown);
                });
        sysWinMessage = new WinMessage(graphics);
        initializeBackground(background, font);
        initializeGround();
        initializeShip(ship, font);
        initializeCountdown(fontCountDown);
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
        sysCountdown.update(elapsedTime);
        sysPause.update(elapsedTime);
        sysWinMessage.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysGroundRenderer.add(entity);
        sysGroundUpdater.add(entity);
        sysSpaceShipRenderer.add(entity);
        sysSpaceShipUpdater.add(entity);
        sysCollisionUpdater.add(entity);
        sysRenderer.add(entity);
        sysCountdown.add(entity);
        sysPause.add(entity);
        sysWinMessage.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysGroundRenderer.remove(entity.getId());
        sysGroundUpdater.remove(entity.getId());
        sysSpaceShipRenderer.remove(entity.getId());
        sysSpaceShipUpdater.remove(entity.getId());
        sysCollisionUpdater.remove(entity.getId());
        sysRenderer.remove(entity.getId());
        sysCountdown.remove(entity.getId());
        sysPause.remove(entity.getId());
        sysWinMessage.remove(entity.getId());
    }

    private void removeAllEntities() {
        sysKeyboardInput.removeAll();
        sysGroundRenderer.removeAll();
        sysGroundUpdater.removeAll();
        sysSpaceShipRenderer.removeAll();
        sysSpaceShipUpdater.removeAll();
        sysCollisionUpdater.removeAll();
        sysRenderer.removeAll();
        sysCountdown.removeAll();
        sysPause.removeAll();
        sysWinMessage.removeAll();
    }

    private void initializeWinMessage(Texture image, Font font, int score) {
        var winMessage = ecs.Entities.WinMessage.create(image, font, score);
        addEntity(winMessage);
    }

    private void initializeCountdown(Font font) {
        var countDown = ecs.Entities.Countdown.create(font);
        this.ship.get(ecs.Components.Ship.class).countdown = true;
        addEntity(countDown);
    }

    private void initializeBackground(Texture image, Font font) {
        var background = BackGround.create(image, Color.WHITE, font);
        addEntity(background);
    }

    private void initializeGround() {
        MyRandom rnd = new MyRandom();
        float startY = rnd.nextRange(-0.1f, 0.5f);
        float finishY = rnd.nextRange(-0.1f, 0.5f);
        Vector3f start = new Vector3f(-1.0f, startY, 0.0f);
        Vector3f finish = new Vector3f(1.0f, finishY, 0.0f);
        var ground = Ground.create(start, safeZones, finish, Color.WHITE);
        this.ground = ground;
        addEntity(ground);
    }

    private void initializeShip(Texture image, Font font) {
        float width = 1/5f;
        var ship = SpaceShip.create(image, font, Color.WHITE, startX, startY, width);
        this.ship = ship;
        addEntity(ship);
    }

    private void initializePause(Texture image, Font font ) {
        var pause = Pause.create(image, Color.WHITE, font);
        addEntity(pause);
        this.pause = pause;
    }

}
