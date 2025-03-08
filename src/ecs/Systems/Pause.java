package ecs.Systems;

import ecs.Components.Appearance;
import ecs.Entities.Entity;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public class Pause extends System {

    public interface IPauseComplete {
        void invoke(Entity entity);
    }

    private final IPauseComplete onComplete;
    private final Graphics2D graphics;

    public Pause(Graphics2D graphics, IPauseComplete onComplete) {
        super(ecs.Components.Pause.class, ecs.Components.Appearance.class);
        this.onComplete = onComplete;
        this.graphics = graphics;
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Pause.class)) {
                var pause = entity.get(ecs.Components.Pause.class);
                var appearance = entity.get(ecs.Components.Appearance.class);
                if (pause.pause) {
                    renderWords(appearance);
                    Rectangle area = new Rectangle(-1f,-1f,2.0f,2.0f, 0.35f);
                    graphics.draw(appearance.image, area, appearance.color);
                } else {
                    onComplete.invoke(entity);
                }
            }
        }
    }

    private void renderWords(Appearance appearance) {
        final float TXT_HEIGHT = 0.2f;
        String unpauseMessage = "Press p or esc to unpause";
        String escapeMessage = "Press q to quit";
        float width1 = appearance.font.measureTextWidth(unpauseMessage, TXT_HEIGHT);
        float width2 = appearance.font.measureTextWidth(escapeMessage, TXT_HEIGHT);
        graphics.drawTextByHeight(appearance.font, unpauseMessage,0.0f - width1 / 2, 0 - TXT_HEIGHT /2, TXT_HEIGHT, 0.4f, appearance.color);
        graphics.drawTextByHeight(appearance.font, escapeMessage, 0.0f - width2 / 2, (0- TXT_HEIGHT /2) - TXT_HEIGHT, TXT_HEIGHT,0.4f, appearance.color);
    }
}
