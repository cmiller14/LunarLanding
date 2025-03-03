package ecs.Components;

import org.joml.Vector3f;

public class Line extends Component {
    public Vector3f start;
    public Vector3f finish;
    public boolean safe;

    public Line(Vector3f start, Vector3f finish, boolean safe) {
        this.start = start;
        this.finish = finish;
        this.safe = safe;
    }

    public Line(Line line) {
        this.start = line.start;
        this.finish = line.finish;
        this.safe = line.safe;
    }
}
