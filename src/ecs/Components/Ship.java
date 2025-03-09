package ecs.Components;

public class Ship extends Component{
    public boolean collision;
    public boolean win;
    public boolean pause;
    public boolean countdown;
    public boolean launchPause;
    public int score;
    public int level;
    public float time;
    public float scoreCountdown;
    public double crashCountdown;
    public float fuel;
    public float speed;
    public float angle;

    public Ship() {
        this.collision = false;
        this.win = false;
        this.pause = false;
        this.score = 1000;
        this.time = 0.0f;
        this.scoreCountdown = 0.0f;
        this.fuel = 20.0f;
        this.speed = 0.0f;
        this.angle = 0.0f;
        this.launchPause = false;
        this.level = 1;
        this.countdown = false;
        this.crashCountdown = 3;
    }

    public Ship(Ship ship) {
        this.collision = ship.collision;
        this.win = ship.win;
        this.pause = ship.pause;
        this.score = ship.score;
        this.time = ship.time;
        this.scoreCountdown = ship.scoreCountdown;
        this.fuel = ship.fuel;
        this.speed = ship.speed;
        this.angle = ship.angle;
        this.launchPause = ship.launchPause;
        this.level = ship.level;
        this.countdown = ship.countdown;
        this.crashCountdown = ship.crashCountdown;
    }
}
