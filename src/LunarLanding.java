import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;

public class LunarLanding {
    public static void main(String[] args) {
        try (Graphics2D graphics = new Graphics2D(1080, 1080, "Lunar Landing")) {
            graphics.initialize(Color.CORNFLOWER_BLUE);
            Game game = new Game(graphics);
            game.initialize();
            game.run();
            game.shutdown();
        }
    }
}