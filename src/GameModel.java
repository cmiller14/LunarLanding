import ecs.Components.Ship;
import ecs.Components.ThrustSound;
import ecs.Entities.*;
import ecs.Entities.Pause;
import ecs.Systems.*;
import ecs.Systems.Countdown;
import ecs.Systems.KeyboardInput;
import ecs.Systems.WinMessage;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import edu.usu.graphics.*;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private Serializer serializer = new Serializer();

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
    private ecs.Systems.ParticleUpdater sysParticleUpdater;
    private ecs.Systems.ParticleRenderer sysParticleRenderer;

    private Entity ground;
    private int safeZones = 2;

    private Entity ship;
    private SoundManager audio;
    private final float startX = 0.0f;
    private final float startY = -0.65f;

    private Entity pause;
    private Entity winMessage;
    private Entity smokeParticles;
    private Entity fireParticles;

    public void initialize(Graphics2D graphics) {
        var ship = new Texture("resources/images/ship.png");
        var background = new Texture("resources/images/spaceBackground.jpg");
        var pauseImage = new Texture("resources/images/pause-background.jpg");
        var smokeImage = new Texture("resources/images/smoke.png");
        var fireImage = new Texture("resources/images/fire.png");
        var font = new Font("resources/fonts/Roboto-Regular.ttf", 36, false);
        var fontCountDown = new Font("resources/fonts/Roboto-Regular.ttf", 72, false);
        this.audio = new SoundManager();
        Sound crash = audio.load("crash", "resources/audio/effect-1.ogg", false);
        Sound win = audio.load("win", "resources/audio/effect-2.ogg", false);
        Sound thrust = audio.load("thrust", "resources/audio/thruster.ogg", false);

        initializeSystems(graphics, pauseImage, fontCountDown);
        initializeBackground(background, font);
        initializeGround();
        initializeShip(ship, font, win, crash, thrust);
        initializeParticle(smokeImage, fireImage);
        initializeCountdown(fontCountDown);
    }

    private void initializeSystems(Graphics2D graphics, Texture pauseImage, Font fontCountDown) {
        sysKeyboardInput = new KeyboardInput(graphics.getWindow());
        sysGroundRenderer = new GroundRenderer(graphics);
        sysGroundUpdater = new UpdateGround(
                (Entity entity) -> { // on win
                    removeEntity(entity);
                    removeAllEntities();
                    // display win message with score
                    initializeWinMessage(pauseImage, fontCountDown, this.ship.get(Ship.class).score);
                    // save the score
                    this.serializer.saveScore(winMessage.get(ecs.Components.WinMessage.class));
                    // instructions for how to close escape
                    this.ship.get(ThrustSound.class).sound.cleanup();
                }
        );
        sysSpaceShipRenderer = new SpaceShipRenderer(graphics);
        sysSpaceShipUpdater = new UpdateSpaceShip(
                () -> { // onWin
                    initializeCountdown(fontCountDown);
                    removeEntity(this.ground);
                    safeZones--;
                    initializeGround();
                },
                () -> { // crash
                    initializeWinMessage(pauseImage, fontCountDown, this.ship.get(Ship.class).score);
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
                    SpaceShip.enableControls(this.ship);
                    SpaceShip.enableMovement(this.ship);
                    this.ship.get(Ship.class).countdown = false;
                    this.smokeParticles.get(ecs.Components.Particles.class).countdown = false;
                    this.smokeParticles.get(ecs.Components.Particles.class).particles.clear();
                    addEntity(this.ship);
                });
        sysPause = new ecs.Systems.Pause(graphics,
                (Entity entity) -> {
                    removeEntity(entity);
                    this.ship.get(Ship.class).pause = false;
                    initializeCountdown(fontCountDown);
                });
        sysWinMessage = new WinMessage(graphics);
        sysParticleRenderer = new ParticleRenderer(graphics);
        sysParticleUpdater = new ParticleUpdater();
    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update
        sysGroundUpdater.update(elapsedTime);
        sysSpaceShipUpdater.update(elapsedTime);
        sysCollisionUpdater.update(elapsedTime);
        sysParticleUpdater.update(elapsedTime);

        for (var entity : removeThese) {
            removeEntity(entity);
        }
        removeThese.clear();

        for (var entity : addThese) {
            addEntity(entity);
        }
        addThese.clear();

        // Because ECS framework, rendering is now part of the update
        sysWinMessage.update(elapsedTime);
        sysRenderer.update(elapsedTime);
        sysGroundRenderer.update(elapsedTime);
        sysSpaceShipRenderer.update(elapsedTime);
        sysCountdown.update(elapsedTime);
        sysPause.update(elapsedTime);
        sysParticleRenderer.update(elapsedTime);
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
        sysParticleUpdater.add(entity);
        sysParticleRenderer.add(entity);
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
        sysParticleRenderer.remove(entity.getId());
        sysParticleUpdater.remove(entity.getId());
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
        sysParticleUpdater.removeAll();
        sysParticleRenderer.removeAll();
    }

    private void initializeWinMessage(Texture image, Font font, int score) {
        var winMessage = ecs.Entities.WinMessage.create(image, font, score);
        this.winMessage = winMessage;
        addEntity(winMessage);
    }

    private void initializeCountdown(Font font) {
        var countDown = ecs.Entities.Countdown.create(font);
        this.ship.get(ecs.Components.Ship.class).countdown = true;
        this.smokeParticles.get(ecs.Components.Particles.class).countdown = true;
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

    private void initializeShip(Texture image, Font font, Sound win, Sound crash, Sound thrust) {
        float width = 1/5f;
        var ship = SpaceShip.create(image, font, Color.WHITE, win, crash, thrust, startX, startY, width);
        this.ship = ship;
        addEntity(ship);
    }

    private void initializePause(Texture image, Font font ) {
        var pause = Pause.create(image, Color.WHITE, font);
        addEntity(pause);
        this.pause = pause;
    }

    private void initializeParticle(Texture image, Texture fire) {
        var particles = Particles.create(
                image,
                fire,
                Color.WHITE,
                new Vector2f(0, 0),
                0.01f, 0.005f,
                0.12f, 0.05f,
                2, 0.5f
        );
        this.smokeParticles = particles;
        addEntity(particles);
    }

}
