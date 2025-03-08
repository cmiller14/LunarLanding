package ecs.Systems;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class Countdown extends System {

    private final Graphics2D graphics;

    public interface ICountdownComplete {
        void invoke(Entity entity);
    }

    private final Countdown.ICountdownComplete onComplete;


    public Countdown(Graphics2D graphics, ICountdownComplete onComplete) {
        super(ecs.Components.Countdown.class);
        this.graphics = graphics;
        this.onComplete = onComplete;
    }

    @Override
    public void update(double elapsedTime) {
        final float TXT_HEIGHT = 0.5f;
        for (var entity : entities.values()) {
            var countdown = entity.get(ecs.Components.Countdown.class);
            countdown.timeRemaining -= elapsedTime;
            if (countdown.timeRemaining < 0) {
                this.onComplete.invoke(entity);
            } else {
                String time = String.format("%d", (int)Math.floor(countdown.timeRemaining + 1));
                float width = countdown.font.measureTextWidth(time, TXT_HEIGHT);
                graphics.drawTextByHeight(countdown.font, time,0.0f - width / 2, 0-TXT_HEIGHT/2, TXT_HEIGHT, 0.5f, Color.GREEN);
            }
        }
    }
}
