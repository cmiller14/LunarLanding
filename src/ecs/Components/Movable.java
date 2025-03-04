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
    public int segmentsToAdd = 0;
    public double moveInterval; // seconds
    public double elapsedInterval;

    public Movable(Direction facing, double moveInterval) {
        this.facing = new HashSet<>();
        this.facing.add(facing);
        this.moveInterval = moveInterval;
    }
}
