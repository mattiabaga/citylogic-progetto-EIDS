package com.citylogic.controller;

import com.citylogic.model.GameController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameViewControllerTest {

    private static final int TIMEOUT_SECONDS = 10;

    @BeforeAll
    static void startJavaFxToolkit() throws Exception {
        CountDownLatch started = new CountDownLatch(1);

        try {
            Platform.startup(started::countDown);
        } catch (IllegalStateException alreadyStarted) {
            // JavaFX Toolkit è già stato avviato da un altro test.
            started.countDown();
        }

        assertTrue(started.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
                "JavaFX Toolkit non si è avviato in tempo");

        Platform.setImplicitExit(false);
    }
    @Test
    void advanceOneTickCallsGameControllerOnceAndRestoresUi() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(0)));

        onFxThread(() -> {
            invokeAdvanceTicks(context.viewController, 1);

            assertEquals(1, context.gameController.calls);
            assertFalse(context.root.isDisable());
            assertEquals(List.of(true, false), context.disableTransitions);
            return null;
        });
    }

    @Test
    void advanceFiveTicksCallsGameControllerFiveTimes() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(0)));

        onFxThread(() -> {
            invokeAdvanceTicks(context.viewController, 5);

            assertEquals(5, context.gameController.calls);
            assertFalse(context.root.isDisable());
            assertEquals(List.of(true, false), context.disableTransitions);
            return null;
        });
    }
   @Test
    void advanceTenTicksCallsGameControllerTenTimes() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(0)));

        onFxThread(() -> {
            invokeAdvanceTicks(context.viewController, 10);

            assertEquals(10, context.gameController.calls);
            assertFalse(context.root.isDisable());
            assertEquals(List.of(true, false), context.disableTransitions);
            return null;
        });
    }

    @Test
    void zeroTicksDoesNotDisableUiOrCallGameController() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(0)));

        onFxThread(() -> {
            invokeAdvanceTicks(context.viewController, 0);

            assertEquals(0, context.gameController.calls);
            assertFalse(context.root.isDisable());
            assertTrue(context.disableTransitions.isEmpty());
            return null;
        });
    }

    @Test
    void negativeTickCountIsRejected() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(0)));

        onFxThread(() -> {
            assertThrows(IllegalArgumentException.class,
                    () -> invokeAdvanceTicks(context.viewController, -1));

            assertEquals(0, context.gameController.calls);
            assertFalse(context.root.isDisable());
            assertTrue(context.disableTransitions.isEmpty());
            return null;
        });
    }
   @Test
    void uiIsRestoredAndAdvancementStopsWhenTickThrows() throws Exception {
        TestContext context = onFxThread(() -> createContext(new CountingGameController(2)));

        onFxThread(() -> {
            // advanceTicks mostra un Alert in caso di errore.
            // La callback lo chiude automaticamente durante il nested event loop
            // di Alert.showAndWait(), evitando interventi manuali nel test.
            Platform.runLater(() -> {
                for (Window window : List.copyOf(Window.getWindows())) {
                    if (window.isShowing()) {
                        window.hide();
                    }
                }
            });

            invokeAdvanceTicks(context.viewController, 5);

            assertEquals(2, context.gameController.calls,
                    "Il ciclo deve interrompersi al primo Tick che genera l'errore");
            assertFalse(context.root.isDisable(),
                    "La schermata deve essere riabilitata anche dopo un'eccezione");
            assertEquals(List.of(true, false), context.disableTransitions);
            return null;
        });
    }

    private static TestContext createContext(CountingGameController gameController)
            throws Exception {
        GameViewController viewController = new GameViewController();

        Field controllerField = GameViewController.class
                .getDeclaredField("gameController");
        controllerField.setAccessible(true);
        controllerField.set(viewController, gameController);

        Field gridHolderField = GameViewController.class
                .getDeclaredField("gridHolder");
        gridHolderField.setAccessible(true);

        StackPane root = new StackPane();
        StackPane gridHolder = new StackPane();
        root.getChildren().add(gridHolder);

        // Associa gridHolder a una Scene, come avviene nella schermata reale.
        new Scene(root);
        gridHolderField.set(viewController, gridHolder);

        List<Boolean> transitions = new ArrayList<>();
        root.disabledProperty().addListener((observable, oldValue, newValue) ->
                transitions.add(newValue));

        return new TestContext(viewController, gameController, root, transitions);
    }
      private static void invokeAdvanceTicks(GameViewController controller, int ticks)
            throws Exception {
        Method method = GameViewController.class
                .getDeclaredMethod("advanceTicks", int.class);
        method.setAccessible(true);
        method.invoke(controller, ticks);
    }

    private static <T> T onFxThread(Callable<T> action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            return action.call();
        }

        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private static final class CountingGameController extends GameController {
        private final int failOnCall;
        private int calls;

        private CountingGameController(int failOnCall) {
            this.failOnCall = failOnCall;
        }

        @Override
        public void nextTick() {
            calls++;

            if (failOnCall > 0 && calls == failOnCall) {
                throw new IllegalStateException("Errore simulato durante il Tick");
            }
        }
    }
      private static final class TestContext {
        private final GameViewController viewController;
        private final CountingGameController gameController;
        private final StackPane root;
        private final List<Boolean> disableTransitions;

        private TestContext(GameViewController viewController,
                            CountingGameController gameController,
                            StackPane root,
                            List<Boolean> disableTransitions) {
            this.viewController = viewController;
            this.gameController = gameController;
            this.root = root;
            this.disableTransitions = disableTransitions;
        }
    }
}