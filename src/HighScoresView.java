import ecs.Components.WinMessage;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class HighScoresView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.HighScores;
    private Font font;

    private Serializer serializer;
    private HighScoresGameState gameState;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        this.serializer = new Serializer();
        this.gameState = new HighScoresGameState();
        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });


    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.HighScores;
        this.serializer.loadGameState(gameState);
    }


    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        if (!this.gameState.initialized) {
            this.serializer.loadGameState(gameState);
        } else {
            orderScores();
        }
    }

    @Override
    public void render(double elapsedTime) {
        String message = "These are the high scores";
        final float height = 0.075f;
        float width = font.measureTextWidth(message, height);

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);

        if (this.gameState.initialized) {
            float top = 0 - height/2;
            for (int i = 0; i < 5 && i < this.gameState.scores.size(); i++) {
                message = String.format("%d", this.gameState.scores.get(i).score);
                width = font.measureTextWidth(message, height);
                top += height;
                graphics.drawTextByHeight(font, message, 0.0f - width / 2, top, height, Color.YELLOW);
            }


        }
    }

    private void orderScores() {
        Collections.sort(this.gameState.scores, (wm1, wm2) -> {
            return Integer.compare(wm2.score, wm1.score);  // Ascending order
        });
    }
}
