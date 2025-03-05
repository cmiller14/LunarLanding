package ecs.Components;

public class Position extends Component {
    public float x;
    public float y;
    public float velocityX;
    public float velocityY;
    public float rotation;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
        this.velocityX = 0.0f;
        this.velocityY = 0.0f;
        this.rotation = 0.0f;
    }
}
