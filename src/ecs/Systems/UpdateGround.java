package ecs.Systems;

import org.joml.Vector3f;

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
        var ground = entity.get(ecs.Components.Ground.class);
        if (ground.generate) {
            for (int i = 0; i < ITERATIONS; i++) {
                // start with the line
                for (var line : ground.lines) {
                    // find the midpoint
                    Vector3f midpoint = new Vector3f();
                    midpoint.set(line.start).add(line.finish).mul(0.5f);
                    // compute the y elevation using
                    float g = 0f;
                    float s = 0f;
                    float r = 0f;
                    float y = (-.5f) * (line.start.y + line.finish.y) + r;
                    // y = 1/2(a_y+b_y)+r
                    // r = sg|b_x-a_x|
                    // s = surface roughness factor
                    // g = gaussian random number with mean of 0 and variance of 1

                }

            }
        }


    }
}
