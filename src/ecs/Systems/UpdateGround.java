package ecs.Systems;

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

                // find the mid point

                // compute the y elevation using
                // y = 1/2(a_y+b_y)+r
                // r = sg|b_x-a_x|
                // s = surface roughness factor
                // g = gaussian random number with mean of 0 and variance of 1

            }
        }


    }
}
