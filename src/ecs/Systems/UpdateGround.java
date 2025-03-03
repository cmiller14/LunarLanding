package ecs.Systems;

import ecs.Components.Line;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class UpdateGround extends System{
    private final int ITERATIONS = 7;
    @Override
    public void update(double elapsedTime) {
        // this is where I will update and generate the ground
        for (var entity : entities.values()) {
            updateGround(entity);
        }
    }

    private void updateGround(ecs.Entities.Entity entity) {
        MyRandom random = new MyRandom();
        var ground = entity.get(ecs.Components.Ground.class);
        List<Line> newLines = new ArrayList<>();
        if (ground.generate) {
            for (int i = 0; i < ITERATIONS; i++) {
                // start with the line
                for (var line : ground.lines) {
                    Vector3f midpoint = new Vector3f();
                    midpoint.set(line.start).add(line.finish).mul(0.5f);
                    // compute the y elevation using
                    float g = (float)random.nextGaussian(0, 1);
                    float s = 0.4f;
                    float r = s * g * Math.abs(line.finish.x - line.start.x);
                    midpoint.y += r;
                    midpoint.y = Math.min(Math.max(midpoint.y, -0.1f), 0.5f);
                    Line newLine1 = new Line(line.start, midpoint, false);
                    Line newLine2 = new Line(midpoint, line.finish, false);
                    newLines.add(newLine1);
                    newLines.add(newLine2);
                }
                ground.lines = deepCopy(newLines);
                newLines.clear();
            }
        }
        ground.generate = false;
    }

    private void addSafeZone(ecs.Entities.Entity entity) {
        // TODO: add the safe zones to the ground
        // must be a certain distance from the edge
        // must be a certain width
        //

    }

    private List<Line> deepCopy(List<Line> oldList) {
        List<Line> newCopy = new ArrayList<>();
        for (Line line : oldList) {
            newCopy.add(new Line(line));
        }
        return newCopy;
    }
}
