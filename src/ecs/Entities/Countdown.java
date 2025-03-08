package ecs.Entities;

import edu.usu.graphics.Font;

public class Countdown{
    public static Entity create(Font font) {
        var countDown = new Entity();
        countDown.add(new ecs.Components.Countdown());
        countDown.get(ecs.Components.Countdown.class).font = font;
        return countDown;
    }
}
