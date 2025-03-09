import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class HelpView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.Help;
    private Font font;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.Help;
    }


    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        final String message = "This is how to play the game";
        final String message1 = "Up arrow key to thrust ship";
        final String message2 = "Left and Right arrow keys to rotate ship";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);
        final float width1 = font.measureTextWidth(message1, height);
        final float width2 = font.measureTextWidth(message2, height);

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);
        graphics.drawTextByHeight(font, message1, 0.0f - width1 / 2, 0 - height / 2 + 0.075f, height, Color.YELLOW);
        graphics.drawTextByHeight(font, message2, 0.0f - width2 / 2, 0 - height / 2 + 0.075f + 0.075f, height, Color.YELLOW);
    }
}
