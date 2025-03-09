import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import ecs.Components.WinMessage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Serializer implements Runnable {

    private enum Activity {
        Nothing,
        Load,
        Save
    }

    private boolean done = false;
    private final Lock lockSignal = new ReentrantLock();
    private final Condition doSomething = lockSignal.newCondition();
    private Activity doThis = Activity.Nothing;

    private WinMessage score;

    private JsonArray jsonArray = new JsonArray();
    private HighScoresGameState gameState;

    private final Thread tInternal;

    public Serializer() {
        this.tInternal = new Thread(this);
        this.tInternal.start();
    }

    @Override
    public void run() {
        try {
            while (!done) {
                // Wait for a signal to do something
                lockSignal.lock();
                doSomething.await();
                lockSignal.unlock();

                // Based on what was requested, do something
                switch (doThis) {
                    case Activity.Nothing -> {}
                    case Activity.Save -> saveSomething();
                    case Activity.Load -> loadSomething();
                }
            }
        } catch (Exception ex) {
            System.out.printf("Something bad happened: %s\n", ex.getMessage());
        }
    }

    /// Public method used by client code to request the game state is saved
    /// NOTE: This does not prevent against race conditions if the gameState object
    ///       is modified while the saving is taking place.  A production level
    ///       approach would have an event held by the client signaled when
    ///       the saving is complete.
    public void saveScore(WinMessage score) {
        lockSignal.lock();

        this.score = score;
        doThis = Activity.Save;
        doSomething.signal();

        lockSignal.unlock();
    }

    /// Public method used the client code to request the game state is loaded.
    /// NOTE: Same comment about race conditions as above.
    public void loadGameState(HighScoresGameState scores) {
        lockSignal.lock();

        this.gameState = scores;
        doThis = Activity.Load;
        doSomething.signal();

        lockSignal.unlock();
    }

    /// Public method used to signal this code to perform a graceful shutdown
    public void shutdown() {
        try {
            lockSignal.lock();

            doThis = Activity.Nothing;
            done = true;
            doSomething.signal();

            lockSignal.unlock();

            tInternal.join();
        } catch (Exception ex) {
            System.out.printf("Failure to gracefully shutdown thread: %s\n", ex.getMessage());
        }
    }

    /// This is where the actual serialization of the game state is performed.  Have
    /// chosen to save in JSON format for readability for the demo, but the state
    /// could have been stored using a binary serializer for more efficient storage
    private synchronized void saveSomething() {
        System.out.println("saving something...");
        // first check if the gamestate file exists
        // then load in the json data to the jsonArray
        // Read existing gameState from the file
        File file = new File("gamestate.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                jsonArray = gson.fromJson(reader, JsonArray.class);
                if (jsonArray == null) jsonArray = new JsonArray();
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }


        try (FileWriter writer = new FileWriter("gamestate.json")) {
            JsonElement scoreJson = gson.toJsonTree(this.score);
            jsonArray.add(scoreJson);
            gson.toJson(jsonArray, writer);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /// This is where the actual deserialization of the game state is performed.
    /// Same note as above regarding the choice to use JSON formatting.
    private synchronized void loadSomething() {
        System.out.println("loading something...");
        try (FileReader reader = new FileReader("gamestate.json")) {
            Gson gson = new Gson();
            jsonArray = gson.fromJson(reader, JsonArray.class);
            if (jsonArray == null) jsonArray = new JsonArray();
            List<ecs.Components.WinMessage> scoresList = gson.fromJson(jsonArray, new TypeToken<ArrayList<ecs.Components.WinMessage>>(){}.getType());
            List<WinMessage> deepCopy = deepCopy(scoresList);
            HighScoresGameState gameState = new HighScoresGameState(deepCopy);
            gameState.initialized = true;
            this.gameState.initialized = gameState.initialized;
            this.gameState.deepCopyScores(scoresList);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<WinMessage> deepCopy(List<WinMessage> oldList) {
        List<WinMessage> newCopy = new ArrayList<>();
        for (WinMessage score : oldList) {
            newCopy.add(new WinMessage(score.score));
        }
        return newCopy;
    }
}