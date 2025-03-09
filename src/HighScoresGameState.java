import ecs.Components.WinMessage;

import java.util.ArrayList;
import java.util.List;

public class HighScoresGameState {

    public HighScoresGameState() {
        this.initialized = false;
    }

    public HighScoresGameState(List<WinMessage> scores) {
        this.scores = scores;
        initialized = false;
    }

    public List<WinMessage> scores;
    public boolean initialized;

    public List<WinMessage> deepCopyScores(List<WinMessage> oldList) {
        List<WinMessage> newCopy = new ArrayList<>();
        for (WinMessage score : oldList) {
            newCopy.add(new WinMessage(score.score));
        }
        return this.scores = newCopy;
    }
}
