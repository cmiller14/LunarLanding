package ecs.Systems;

import ecs.Components.Appearance;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class WinMessage extends System {

    private final Graphics2D graphics;

    public WinMessage(Graphics2D graphics2D) {
        this.graphics = graphics2D;
    }
    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.WinMessage.class)) {
                var appearance = entity.get(ecs.Components.Appearance.class);
                var winMessage = entity.get(ecs.Components.WinMessage.class);
                // display the background
                renderBackground(appearance);
                renderWords(appearance, winMessage);
            }
        }
    }

    private void renderBackground(Appearance appearance) {
        Rectangle area = new Rectangle(-1f,-1f,2.0f,2.0f);
        graphics.draw(appearance.image, area, appearance.color);
    }

    private void renderWords(Appearance appearance, ecs.Components.WinMessage winMessage) {
        final float TXT_HEIGHT = 0.2f;
        String scoreMessage = String.format("Score: %d", winMessage.score);
        String escapeMessage = "Press q to quit";
        float width1 = appearance.font.measureTextWidth(scoreMessage, TXT_HEIGHT);
        float width2 = appearance.font.measureTextWidth(escapeMessage, TXT_HEIGHT);
        graphics.drawTextByHeight(appearance.font, scoreMessage,0.0f - width1 / 2, 0 - TXT_HEIGHT /2, TXT_HEIGHT, 0.8f, appearance.color);
        graphics.drawTextByHeight(appearance.font, escapeMessage, 0.0f - width2 / 2, (0- TXT_HEIGHT /2) - TXT_HEIGHT, TXT_HEIGHT,0.8f, appearance.color);
    }
}
