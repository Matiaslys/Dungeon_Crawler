import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class MainMenu extends FXGLMenu {
    static int healthPlayer;
    static int healthEnemy;
    static int EnemySpawn;
    static int bulletSpeed;
    static double bulletTimer;
    static int BossHealth;
    public MainMenu(MenuType type) {
        super(type);

        VBox vBox1 = new VBox();
        VBox vBox2 = new VBox();

        double centerX = FXGL.getAppWidth() / 2 - 240 / 2;
        double centerY = FXGL.getAppHeight() / 2;

        // First menu.
        var stackP = new StackPane();
        stackP.setTranslateX(centerX);
        stackP.setTranslateY(centerY - 120);
        var textRect = new Rectangle(200, 40);
        var text = FXGL.getUIFactory().newText("Escape The Dungeon", Color.WHITE, 24);
        text.setUnderline(true);

        stackP.getChildren().addAll(textRect, text);

        var start = new GameMenuButton("Start", () -> {
            getMenuRoot().getChildren().set(0, vBox2);
        });

        start.setTranslateX(centerX);
        start.setTranslateY(centerY - 60);

        var exit = new GameMenuButton("Exit", this::fireExit);

        vBox1.getChildren().addAll(stackP ,start, exit);

        exit.setTranslateX(centerX);
        exit.setTranslateY(centerY - 40);

        // Difficulty menu.

        var easy = new GameMenuButton("Easy", () -> {
            this.fireNewGame();

            healthEnemy = 1;
            healthPlayer = 200;
            EnemySpawn = 5;
            bulletSpeed = 150;
            bulletTimer = 2;
            BossHealth = 300;
            DungeonApp.firstStart = true;

            getMenuRoot().getChildren().set(0, vBox1);
        });

        easy.setTranslateX(centerX + 20);
        easy.setTranslateY(centerY - 100);

        var normal = new GameMenuButton("Normal", () -> {
            this.fireNewGame();

            healthEnemy = 2;
            healthPlayer = 150;
            EnemySpawn = 5;
            bulletSpeed = 225;
            bulletTimer = 1.50;
            BossHealth = 250;
            DungeonApp.firstStart = true;
            getMenuRoot().getChildren().set(0, vBox1);
        });

        normal.setTranslateX(centerX + 20);
        normal.setTranslateY(centerY - 80);

        var hard = new GameMenuButton("Hard", () -> {
            this.fireNewGame();

            healthEnemy = 3;
            healthPlayer = 100;
            EnemySpawn = 5;
            bulletSpeed = 300;
            bulletTimer = 1;
            BossHealth = 200;
            DungeonApp.firstStart = true;
            getMenuRoot().getChildren().set(0, vBox1);
        });

        hard.setTranslateX(centerX + 20);
        hard.setTranslateY(centerY - 60);

        var back = new GameMenuButton("Back", () -> {
            getMenuRoot().getChildren().set(0, vBox1);
        });

        back.setTranslateX(centerX + 20);
        back.setTranslateY(centerY + 40);

        vBox2.getChildren().addAll(easy, normal, hard, back);

        getMenuRoot().getChildren().add(vBox1);
    }


    @NotNull
    @Override
    protected Button createActionButton(StringBinding Start, @NotNull Runnable action) {
        return new Button(Start.get());
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull String Start, @NotNull Runnable Action) {
        return new Button(Start);
    }

    @NotNull
    @Override
    protected Node createBackground(double width, double height) {
        return new Rectangle(width, height);
    }

    @NotNull
    @Override
    protected Node createProfileView(@NotNull String profileName) {
        return new Text(profileName);
    }

    @NotNull
    @Override
    protected Node createTitleView(@NotNull String title) {
        return new Text(title);
    }

    @NotNull
    @Override
    protected Node createVersionView(@NotNull String version) {
        return new Text(version);
    }

    private static class GameMenuButton extends StackPane {
        public GameMenuButton(String name, Runnable action) {
            var bg = new Rectangle(200, 40);
            bg.setStroke(Color.WHITE);

            var text = FXGL.getUIFactory().newText(name, Color.WHITE, 18);

            bg.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());

            getChildren().addAll(bg, text);
        }

    }
}

