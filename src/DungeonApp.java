import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import java.io.File;
import java.net.MalformedURLException;


import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class DungeonApp extends GameApplication {


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(25 * 16);
        settings.setHeight(25 * 16);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setDeveloperMenuEnabled(true);
    }
    static Entity player;
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

        getInput().addAction(new UserAction("Up") {
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

        getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControl.class).shoot();
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    public void initUI() {

        HealthIntComponent hp = player.getComponent(HealthIntComponent.class);

        ProgressBar hpBar = ProgressBar.makeHPBar();
        hpBar.setMinValue(0);
        hpBar.setMaxValue(hp.getValue());
        hpBar.currentValueProperty().bind(hp.valueProperty());
        hpBar.setLayoutY(10);
        hpBar.setWidth(50);
        hpBar.setHeight(10);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.LEFT);
        hpBar.setFill(Color.GREEN);
        hpBar.setTraceFill(Color.GREEN.brighter());

        getGameScene().addUINode(hpBar);

    }
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new DungeonFactory());
        FXGL.getGameScene().setBackgroundColor(Color.rgb(5,0,18));
        var levelFile = new File("C:\\Users\\Matias\\Desktop\\Datamatiker\\Dungeon_Crawler\\src\\assets\\tmx\\Dungeon Crawler2.tmx");
        Level level;
        try {
            level = new TMXLevelLoader().load(levelFile.toURI().toURL(), FXGL.getGameWorld());
            FXGL.getGameWorld().setLevel(level);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

            SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
            player = FXGL.getGameWorld().spawn("Player", data);

            getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
            FXGL.getPhysicsWorld().setGravity(0, 0);

    }


    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.PlayerBullet, DungeonType.Wall) {
            @Override
            protected void onCollisionBegin(Entity PlayerBullet, Entity Wall) {
                PlayerBullet.removeFromWorld();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.EnemyBullet, DungeonType.Wall) {
            @Override
            protected void onCollisionBegin(Entity EnemyBullet, Entity Wall) {
                EnemyBullet.removeFromWorld();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Enemy, DungeonType.Player) {
            @Override
            protected void onCollisionBegin(Entity Enemy, Entity Player) {
                player.getComponent(HealthIntComponent.class).restoreFully();
            }
        });


        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.EnemyBullet, DungeonType.Player) {
            @Override
            protected void onCollisionBegin(Entity EnemyBullet, Entity Player) {
                EnemyBullet.removeFromWorld();

                player.getComponent(PlayerControl.class).playerHit();
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.PlayerBullet, DungeonType.Enemy) {
            @Override
            protected void onCollisionBegin(Entity PlayerBullet, Entity Enemy) {
                Enemy.getComponent(HealthIntComponent.class).damage(1);
                int hp = Enemy.getComponent(HealthIntComponent.class).getValue();
                PlayerBullet.removeFromWorld();
                if (hp <= 0) {
                    Enemy.removeFromWorld();
                }

            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Player, DungeonType.Door) {
            @Override
            protected void onCollisionBegin(Entity Player, Entity Door) {
            getDisplay().showMessageBox("You Escaped");
            }
        });
    }

    public void setlevel(String level) {
        FXGL.setLevelFromMap(level);

        SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
        DungeonApp.player = FXGL.getGameWorld().spawn("Player", data);
        getGameScene().getViewport().bindToEntity(DungeonApp.player, getAppWidth() / 2, getAppHeight() / 2);

        getGameScene().clearUINodes();
        initUI();
    }

    public void playerDeath() {
        getDisplay().showMessageBox("You Died!");
        setlevel("Dungeon Crawler2.tmx");
    }


    public static void main(String[] args) {
    launch(args);
    }
}
