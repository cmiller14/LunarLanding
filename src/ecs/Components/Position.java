package ecs.Components;

import org.joml.Vector2f;


import java.util.ArrayList;
import java.util.List;

public class Position extends Component {
    public float x;
    public float y;
    public float velocityX;
    public float velocityY;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
        this.velocityX = 0.0f;
        this.velocityY = 0.0f;
    }
}
