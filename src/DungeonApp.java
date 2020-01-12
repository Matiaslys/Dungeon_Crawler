import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.net.MalformedURLException;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class DungeonApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(25 * 16);
        settings.setHeight(26 * 16);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
    }
    private Entity player;

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).left();
            }
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).leftStop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).right();
            }
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).rightStop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).up();
            }
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).upStop();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).down();
            }
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControl.class).downStop();
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new DungeonFactory());
        var levelFile = new File("C:\\Users\\matia\\IdeaProjects\\Dungeon Crawler\\src\\assets\\tmx\\Dungeon Crawler2.tmx");
        Level level;
        try {
            level = new TMXLevelLoader().load(levelFile.toURI().toURL(), FXGL.getGameWorld());
            FXGL.getGameWorld().setLevel(level);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        player = FXGL.getGameWorld().spawn("Spawn", 160, 50);

        getGameScene().getViewport().setBounds(0, 0, getAppWidth(), getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);

        FXGL.getGameWorld().spawn("Enemy Spawn");
        FXGL.getPhysicsWorld().setGravity(0,0);
    }

    public static void main(String[] args) {
    launch(args);
    }
}
