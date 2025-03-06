package ecs.Components;

import java.util.HashSet;

public class Movable extends Component {

    public enum Direction {
        Stopped,
        Up,
        Down,
        Left,
        Right
    }

    public HashSet<Direction> facing;
    public double moveInterval; // seconds

    public Movable(Direction facing, double moveInterval) {
        this.facing = new HashSet<>();
        this.facing.add(facing);
        this.moveInterval = moveInterval;
    }
}
