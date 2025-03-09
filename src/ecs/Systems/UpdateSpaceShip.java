package ecs.Systems;

import ecs.Components.*;
import ecs.Entities.Entity;

public class UpdateSpaceShip extends System {
    private Ship pauseShip;
    private Position pausePosition;
    private boolean crash = false;

    public interface IShipComplete {
        void invoke();
    }

    private final UpdateSpaceShip.IShipComplete onComplete;
    private final UpdateSpaceShip.IShipComplete onCrash;
    private final UpdateSpaceShip.IShipComplete onPause;

    public UpdateSpaceShip(IShipComplete onComplete, IShipComplete onCrash, IShipComplete onPause) {
        super(ecs.Components.Ship.class);
        this.onComplete = onComplete;
        this.onCrash = onCrash;
        this.onPause = onPause;
        this.pauseShip = new Ship();
        this.pausePosition = new Position(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void update(double elapsedTime) {
        // this is where I will update the spaceship for its movement and such

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) updateShip(entity, elapsedTime);
        }
    }

    private void updateShip(ecs.Entities.Entity entity, double elapsedTime) {
        if (!entity.contains(ecs.Components.Movable.class)) {
            return;
        }
        var position = entity.get(ecs.Components.Position.class);
        var movable = entity.get(ecs.Components.Movable.class);
        var ship = entity.get(ecs.Components.Ship.class);

        if (ship.countdown) {
            return;
        }

        if (ship.level > 2) {
            onCrash.invoke();
            return;
        }

        if (ship.launchPause && !ship.pause) {
            // launches the pause menu
            onPause.invoke();
            // need to pause the movement of the ship
            ship.pause = true;
            this.pauseShip = new Ship(ship);
            this.pausePosition = new Position(position);
        }
        if (ship.pause) {
            ship.launchPause = false;
            return;
        }

        ship.time += (float)elapsedTime;
        ship.scoreCountdown += (float)elapsedTime;
        calculateScore(ship);

        // stop ship if win or collision
        if (ship.win) {
            entity.get(WinSound.class).sound.play();
            entity.get(ThrustSound.class).sound.stop();
            resetShip(entity);
            onComplete.invoke();
            return;
        }

        if (ship.collision) {
            if (!crash) {
                entity.get(CrashSound.class).sound.play();
                entity.get(ThrustSound.class).sound.cleanup();
                crash = true;
            }
            position.velocityY = 0f;
            position.velocityX = 0f;
            ship.crashCountdown -= elapsedTime;
            if (ship.crashCountdown < 0) {
                onCrash.invoke();
            }
            return;
        }

        float ACCELERATION = 0.12f;
        float ROTATION = 1.5f;

        updateRotation((float) elapsedTime, movable, position, ship, ROTATION);
        updateVelocity((float) elapsedTime, movable, position, ship, entity.get(ecs.Components.ThrustSound.class), ACCELERATION);

        // add gravity
        float GRAVITY = 0.06f;
        position.velocityY += GRAVITY * (float)elapsedTime;

        updatePosition((float) elapsedTime, position, movable);
        updateSpeed(ship, position);

    }

    private void calculateScore(Ship ship) {
        // more points if it takes them quicker
        // no points if they crash
        if (ship.collision) {
            ship.score = 0;
        } else if (!ship.win) {
            int seconds = (int) (ship.scoreCountdown % 60);
            if (seconds > 1) {
                ship.score -= 20;
                ship.scoreCountdown = 0;
            }

        }
    }

    private void updateRotation(float elapsedTime, Movable movable, Position position, Ship ship, float rotationRate) {
        if (movable.facing.contains(Movable.Direction.Left)) {
            position.rotation -= rotationRate * elapsedTime;
            ship.angle -= rotationRate * elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Right)) {
            position.rotation += rotationRate * elapsedTime;
            ship.angle += rotationRate  * elapsedTime;
        }
    }

    private void updateSpeed(Ship ship, Position position) {
        ship.speed = (float)Math.sqrt(
                (position.velocityX * position.velocityX)+(position.velocityY * position.velocityY)
        ) * 30;
    }

    private void updatePosition(float elapsedTime, Position position, Movable movable) {
        // then I update the position based on the velocity
        position.y += position.velocityY * elapsedTime;
        position.x += position.velocityX * elapsedTime;
        movable.facing.clear();
        position.x = Math.min(1.0f, Math.max(position.x, -1.0f));
        position.y = Math.min(1.0f, Math.max(position.y, -1.0f));
    }

    private void updateVelocity(float elapsedTime, Movable movable, Position position, Ship ship, ThrustSound sound, float ACCELERATION) {
        // first I should update the velocity based on acceleration and rotation
        if (ship.fuel <= 0) {
            ship.fuel = 0;
            return;
        }
        if (movable.facing.contains(Movable.Direction.Up)) {
            float accelerationX = ACCELERATION * (float)Math.cos(position.rotation);
            float accelerationY = ACCELERATION * (float)Math.sin(position.rotation);

            position.velocityY -= accelerationY * elapsedTime;
            position.velocityX -= accelerationX * elapsedTime;
            ship.fuel -= elapsedTime;
            if (!sound.sound.isPlaying()) sound.sound.play();
        } else {
            sound.sound.stop();
        }
    }

    private void resetShip(Entity entity) {
        var position = entity.get(ecs.Components.Position.class);
        var shipComp = entity.get(ecs.Components.Ship.class);
        final float startX = 0.0f;
        final float startY = -0.65f;
        position.x = startX;
        position.y = startY;
        position.velocityX = 0.0f;
        position.velocityY = 0.0f;
        position.rotation = (float)Math.PI/2;
        shipComp.time = 0.0f;
        shipComp.fuel = 20.f;
        shipComp.speed = 0.0f;
        shipComp.angle = 0.0f;
        shipComp.score += 1000;
        shipComp.win = false;
        shipComp.collision = false;
        shipComp.level += 1;
    }
}
