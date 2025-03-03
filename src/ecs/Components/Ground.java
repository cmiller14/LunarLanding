package ecs.Components;

import edu.usu.graphics.Color;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Ground extends Component {
    public List<Line> lines;
    public boolean generate;
    public Color color;

    public Ground(Color color, Vector3f start, Vector3f finish) {
        this.lines = new ArrayList<>();
        this.lines.add(new Line(start, finish, false));
        this.generate = true;
        this.color = color;
    }
}
