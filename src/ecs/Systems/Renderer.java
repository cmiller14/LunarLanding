package ecs.Systems;

import ecs.Components.Appearance;
import ecs.Components.Ship;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class Renderer extends System {

    private final Graphics2D graphics;

    public Renderer(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        // Draw each of the game entities!
        // Draw a blue background for the gameplay area
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Background.class)) {
                var appearance = entity.get(ecs.Components.Appearance.class);
                renderBackground(appearance);
            }
            if (entity.contains(ecs.Components.Ship.class)) {
                var appearance = entity.get(ecs.Components.Appearance.class);
                var ship = entity.get(ecs.Components.Ship.class);
                renderTime(ship, appearance);
                // render the score
                String score = "Score: " + Integer.toString(ship.score);
                graphics.drawTextByHeight(appearance.font, score, 0.6f, -0.30f, 0.05f, 0.2f, Color.WHITE);
                // render the fuel
                renderFuel(ship, appearance);
                // render the speed
                renderSpeed(ship, appearance);
                // render the angle
                renderAngle(ship, appearance);
            }
        }
    }

    private void renderAngle(Ship ship, Appearance appearance) {
        double angleDegrees = ship.angle * (180/Math.PI);
        boolean acceptableAngle = angleDegrees >= -5 && angleDegrees < 5;
        Color color = Color.WHITE;
        if (acceptableAngle) color = Color.GREEN;
        String formattedAngle = String.format("Angle: %.2f%n degrees", angleDegrees);
        graphics.drawTextByHeight(appearance.font, formattedAngle, 0.6f, -0.35f, 0.05f, 0.2f, color);
    }

    private void renderSpeed(Ship ship, Appearance appearance) {
        Color color = Color.GREEN;
        if (ship.speed > 2) color = Color.WHITE;
        String formattedFuel = String.format("speed: %.2f%n m/s", ship.speed);
        graphics.drawTextByHeight(appearance.font, formattedFuel, 0.6f, -0.40f, 0.05f, 0.2f, color);
    }

    private void renderTime(Ship ship, Appearance appearance) {
        // Convert to minutes, seconds, and milliseconds
        int minutes = (int) (ship.time / 60);       // Get whole minutes
        int seconds = (int) (ship.time % 60);       // Get whole seconds

        String formattedTime = String.format("Time: %02d:%02d", minutes, seconds);
        graphics.drawTextByHeight(appearance.font, formattedTime, 0.6f, -0.50f, 0.05f, 0.2f, Color.WHITE);
    }

    private void renderFuel(Ship ship, Appearance appearance) {
        Color color = Color.GREEN;
        if (ship.fuel <= 0) color = Color.WHITE;
        // Convert to minutes, seconds, and milliseconds
        int seconds = (int) (ship.fuel % 60);       // Get whole seconds

        String formattedFuel = String.format("Fuel: %02d", seconds);
        graphics.drawTextByHeight(appearance.font, formattedFuel, 0.6f, -0.45f, 0.05f, 0.2f, color);
    }

    private void renderBackground(Appearance appearance) {
        Rectangle area = new Rectangle(-1f,-1f,2.0f,2.0f);
        graphics.draw(appearance.image, area, appearance.color);
    }
}
