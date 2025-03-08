package ecs.Systems;

public class Updater extends System {

    @Override
    public void update(double elapsedTime) {
        boolean win = false;
        boolean collision = false;

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ship.class)) {
                var ship = entity.get(ecs.Components.Ship.class);
                win = ship.win;
                collision = ship.collision;
            }
        }

        // update the timeRemaining and set it to 0 if the game is over
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Background.class)) {
                var background = entity.get(ecs.Components.Background.class);
                if (!win && !collision) {
                    background.time += (float) elapsedTime;
                }
            }
        }

    }
}
