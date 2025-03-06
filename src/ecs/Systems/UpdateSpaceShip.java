package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.Position;

public class UpdateSpaceShip extends System {

    @Override
    public void update(double elapsedTime) {
        // this is where I will update the spaceship for its movement and such
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) updateShip(entity, elapsedTime);
        }
    }

    private void updateShip(ecs.Entities.Entity entity, double elapsedTime) {
        var position = entity.get(ecs.Components.Position.class);
        var movable = entity.get(ecs.Components.Movable.class);
        var ship = entity.get(ecs.Components.Ship.class);

        // stop ship if win or collision
        if (ship.collision) {
            position.velocityX = 0.0f;
            position.velocityY = 0.0f;
            return;
        }
        if (ship.win) {
            position.velocityX = 0.0f;
            position.velocityY = 0.0f;
        }

        float ACCELERATION = 0.12f;
        float ROTATION = 1.5f;

        updateRotation((float) elapsedTime, movable, position, ROTATION);
        updateVelocity((float) elapsedTime, movable, position, ACCELERATION);

        // add gravity
        float GRAVITY = 0.06f;
        position.velocityY += GRAVITY * (float)elapsedTime;

        updatePosition((float) elapsedTime, position, movable);


    }

    private void updateRotation(float elapsedTime, Movable movable, Position position, float rotationRate) {
        if (movable.facing.contains(Movable.Direction.Left)) {
            position.rotation -= rotationRate * elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Right)) {
            position.rotation += rotationRate * elapsedTime;
        }
    }

    private static void updatePosition(float elapsedTime, Position position, Movable movable) {
        // then I update the position based on the velocity
        position.y += position.velocityY * elapsedTime;
        position.x += position.velocityX * elapsedTime;
        movable.facing.clear();
        position.x = Math.min(1.0f, Math.max(position.x, -1.0f));
        position.y = Math.min(1.0f, Math.max(position.y, -1.0f));
    }

    private static void updateVelocity(float elapsedTime, Movable movable, Position position, float ACCELERATION) {
        // first I should update the velocity based on acceleration and rotation
        if (movable.facing.contains(Movable.Direction.Up)) {
            float accelerationX = ACCELERATION * (float)Math.cos(position.rotation);
            float accelerationY = ACCELERATION * (float)Math.sin(position.rotation);

            position.velocityY -= accelerationY * elapsedTime;
            position.velocityX -= accelerationX * elapsedTime;

        }
    }
}
