package ecs.Systems;

import ecs.Components.Ground;
import ecs.Components.Line;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UpdateGround extends System {
    private final int ITERATIONS = 7;
    @Override
    public void update(double elapsedTime) {
        // this is where I will update and generate the ground
        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Ground.class)) updateGround(entity);
        }
    }

    private void updateGround(ecs.Entities.Entity entity) {
        MyRandom random = new MyRandom();
        var ground = entity.get(ecs.Components.Ground.class);
        List<Line> newLines = new ArrayList<>();
        if (ground.generate) {
            addSafeZones(entity);
            for (int i = 0; i < ITERATIONS; i++) {
                // start with the line
                for (var line : ground.lines) {
                    if (!line.safe) {
                        Vector3f midpoint = new Vector3f();
                        midpoint.set(line.start).add(line.finish).mul(0.5f);
                        // compute the y elevation using
                        float g = (float)random.nextGaussian(0, 1);
                        float s = 0.5f;
                        float r = s * g * Math.abs(line.finish.x - line.start.x);
                        midpoint.y += r;
                        midpoint.y = Math.min(Math.max(midpoint.y, -0.1f), 0.5f);
                        Line newLine1 = new Line(line.start, midpoint, false);
                        Line newLine2 = new Line(midpoint, line.finish, false);
                        newLines.add(newLine1);
                        newLines.add(newLine2);
                    } else {
                        newLines.add(line);
                    }
                }
                ground.lines = deepCopy(newLines);
                newLines.clear();
            }
        }
        ground.generate = false;
    }

    private void addSafeZones(ecs.Entities.Entity entity) {
        var ground = entity.get(ecs.Components.Ground.class);
        connectLines(ground);
    }

    private void connectLines(Ground ground) {
        if (ground.numSafeZones == 2) {
            // generate two safe lines
            Line safeZone1 = generateSafeLine(null);
            Line safeZone2 = generateSafeLine(safeZone1);
            // connect all the lines
            Vector3f start = ground.lines.getFirst().start;
            Vector3f finish = ground.lines.getLast().finish;
            Line line1;
            Line line2;
            Line line3;
            if (safeZone1.start.x < safeZone2.start.x) {
                line1 = new Line(start, safeZone1.start, false);
                line2 = new Line(safeZone1.finish, safeZone2.start, false);
                line3 = new Line(safeZone2.finish, finish, false);
            } else {
                line1 = new Line(start, safeZone2.start, false);
                line2 = new Line(safeZone2.finish, safeZone1.start, false);
                line3 = new Line(safeZone1.finish, finish, false);
            }
            addLines(ground, line1, safeZone1, line2, safeZone2, line3);
        } else {
            Line safeLine = generateSafeLine(null);
            Vector3f start = ground.lines.getFirst().start;
            Vector3f finish = ground.lines.getFirst().finish;
            Line first = new Line(start, safeLine.start, false);
            Line second = new Line(safeLine.finish, finish, false);
            List<Line> newStartingLines = new ArrayList<>();
            newStartingLines.add(first);
            newStartingLines.add(safeLine);
            newStartingLines.add(second);
            ground.lines = newStartingLines;
        }

    }

    private void addLines(Ground ground, Line line1, Line safeZone1, Line line2, Line safeZone2, Line line3) {
        List<Line> newStartingLines = new ArrayList<>();
        newStartingLines.add(line1);
        newStartingLines.add(safeZone1);
        newStartingLines.add(line2);
        newStartingLines.add(safeZone2);
        newStartingLines.add(line3);
        ground.lines = newStartingLines;
    }

    private Line generateSafeLine(Line existingLine) {
        MyRandom rnd = new MyRandom();
        // must be a certain WIDTH
        float WIDTH = 0.20f;
        // must be a certain distance from the edge
        float minX = -1.0f + (WIDTH * 0.15f);
        float maxX = 1.0f - (WIDTH + (WIDTH * 0.15f));
        // if there is already a safe zone
        if (existingLine != null) {
            // make sure it does not touch by 15% of their WIDTH
            // get the range of values that x can be in
            float buffer = (float)(WIDTH * 0.15) + WIDTH;
            float x1 = rnd.nextRange(minX, existingLine.start.x - buffer);
            float x2 = rnd.nextRange(existingLine.finish.x + buffer, maxX);
            // check that the first line is not close to the edge
            if (x1 + WIDTH > existingLine.start.x) x1 = rnd.nextRange(existingLine.finish.x + buffer, maxX);;
            if (existingLine.finish.x + WIDTH > maxX ) x2 = rnd.nextRange(minX, existingLine.start.x - buffer);

            Random random = new Random();
            float x = random.nextBoolean() ? x1 : x2;
            // get the range of values that y can be in
            float safeY = rnd.nextRange(-0.1f, 0.5f);
            Vector3f start = new Vector3f(x, safeY, 0.0f);
            Vector3f finish = new Vector3f(x + WIDTH, safeY, 0.0f);
            return new Line(start, finish, true);
        } else {
            float safeY = rnd.nextRange(-0.1f, 0.5f);
            float safeX = rnd.nextRange(minX, maxX);
            Vector3f start = new Vector3f(safeX, safeY, 0.0f);
            Vector3f finish = new Vector3f(safeX+ WIDTH, safeY, 0.0f);
            return new Line(start, finish, true);
        }
    }

    private List<Line> deepCopy(List<Line> oldList) {
        List<Line> newCopy = new ArrayList<>();
        for (Line line : oldList) {
            newCopy.add(new Line(line));
        }
        return newCopy;
    }
}
