package ecs.Systems;

import ecs.Entities.BackGround;
import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;

public class Renderer extends System {

    private final Graphics2D graphics;

    public Renderer(Graphics2D graphics) {
        super(ecs.Components.Appearance.class,
                ecs.Components.Background.class);

        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        // Draw each of the game entities!
        // Draw a blue background for the gameplay area
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Background.class)) {
                var appearance = entity.get(ecs.Components.Appearance.class);
                Rectangle area = new Rectangle(-1f,-1f,2.0f,2.0f);
                graphics.draw(appearance.image, area, appearance.color);
            }
            // render the time
            // render the score
            // render the fuel
            // render the
            }
        }
    }
