package ecs.Systems;

import ecs.Components.Movable;

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
        float ACCELERATION = 0.12f;

        // first I should update the velocity based on acceleration
        if (movable.facing.contains(Movable.Direction.Up)) {
            position.velocityY += (-ACCELERATION) * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Down)) {
            position.velocityY += ACCELERATION * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Left)) {
            position.velocityX += (-ACCELERATION) * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Right)) {
            position.velocityX += ACCELERATION * (float)elapsedTime;
        }

        // add gravity
        float GRAVITY = 0.06f;
        position.velocityY += GRAVITY * (float)elapsedTime;

        // then I update the position based on the velocity
        position.y += position.velocityY * (float)elapsedTime;
        position.x += position.velocityX * (float)elapsedTime;

        movable.facing.clear();
        position.x = Math.min(1.0f, Math.max(position.x, -1.0f));
        position.y = Math.min(1.0f, Math.max(position.y, -1.0f));
    }
}
