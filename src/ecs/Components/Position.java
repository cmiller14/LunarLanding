package ecs.Components;

public class Position extends Component {
    public float x;
    public float y;
    public float velocityX;
    public float velocityY;
    public float rotation;
    public float width;
    public float radius;

    public Position(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.velocityX = 0.0f;
        this.velocityY = 0.0f;
        this.rotation = (float)Math.PI/2;
        this.width = width;
        this.radius = 1/20f;
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
        this.velocityY = position.velocityY;
        this.velocityX = position.velocityX;
        this.rotation = position.rotation;
        this.width = position.width;
        this.radius = position.radius;
    }
}
