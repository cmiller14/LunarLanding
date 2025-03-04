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
        position.velocityY = 0.1f;
        position.velocityX = 0.1f;
        if (movable.facing.contains(Movable.Direction.Up)) {
            position.y -= position.velocityY * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Down)) {
            position.y += position.velocityY * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Left)) {
            position.x -= position.velocityX * (float)elapsedTime;
        }
        if (movable.facing.contains(Movable.Direction.Right)) {
            position.x += position.velocityX * (float)elapsedTime;
        }
        movable.facing.clear();
        position.x = Math.min(1.0f, Math.max(position.x, -1.0f));
        position.y = Math.min(1.0f, Math.max(position.y, -1.0f));
    }
}
