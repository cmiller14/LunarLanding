package ecs.Components;

import edu.usu.graphics.Color;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Ground extends Component {
    public List<Line> lines;
    public boolean generate;
    public Color color;

    public class Line {
        public Vector3f start;
        public Vector3f finish;

        public Line(Vector3f start, Vector3f finish) {
            this.start = start;
            this.finish = finish;
        }
    }

    public Ground(Color color, Vector3f start, Vector3f finish) {
        this.lines = new ArrayList<>();
        this.lines.add(new Line(start, finish));
        this.generate = true;
        this.color = color;
    }
}
