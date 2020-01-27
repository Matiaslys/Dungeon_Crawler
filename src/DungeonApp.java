import com.almasb.fxgl.app.*;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
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
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Map;


import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class DungeonApp extends GameApplication {


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(25 * 16);
        settings.setHeight(25 * 16);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setDeveloperMenuEnabled(false);
        settings.setMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu(MenuType.MAIN_MENU);
            }

            @NotNull
            @Override
            public FXGLMenu newGameMenu() {
                return new GameMenu(MenuType.GAME_MENU);
            }
        });
    }

    private static final int MAX_LEVEL = 2;
    private static final int STARTING_LEVEL = 0;
    private static final boolean DEVELOPING_NEW_LEVEL = false;
    static Entity player;
    boolean bossBattle = false;
    boolean bossIsAlive = false;
    boolean firstStart = true;
    static boolean left = true;
    static boolean right = true;
    static boolean up = true;
    static boolean down = true;


    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Dash") {
            @Override
            protected void onActionBegin() {
                left = false;
                right = false;
                up = false;
                down = false;
            }
        }, KeyCode.SPACE);


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
        hpBar.setLayoutY(getAppHeight()-15);
        hpBar.setWidth(50);
        hpBar.setHeight(10);
        hpBar.setLabelVisible(true);
        hpBar.setLabelPosition(Position.LEFT);
        hpBar.setFill(Color.GREEN);
        hpBar.setTraceFill(Color.GREEN.brighter());

        getGameScene().addUINode(hpBar);


    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", STARTING_LEVEL);
    }


    @Override
    protected void initGame() {
        music();

        FXGL.getGameWorld().addEntityFactory(new DungeonFactory());
        FXGL.getGameScene().setBackgroundColor(Color.rgb(5, 0, 18));

//        var levelFile = new File("C:\\Users\\matia\\IdeaProjects\\Dungeon Crawler\\src\\assets\\levels\\level0.tmx");
//        Level level;
//        try {
//            level = new TMXLevelLoader().load(levelFile.toURI().toURL(), FXGL.getGameWorld());
//            FXGL.getGameWorld().setLevel(level);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        nextLevel();

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

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.PlayerBullet, DungeonType.Boss) {
            @Override
            protected void onCollisionBegin(Entity PlayerBullet, Entity Boss) {
                int hp = Boss.getComponent(HealthIntComponent.class).getValue();
                Boss.getComponent(HealthIntComponent.class).damage(10);
                PlayerBullet.removeFromWorld();
                if (hp <= 10) {
                    Boss.removeFromWorld();
                    bossIsAlive = false;
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Player, DungeonType.FirstAid) {
            @Override
            protected void onCollisionBegin(Entity Player, Entity FirstAid) {
                if (player.getComponent(HealthIntComponent.class).getValue() < player.getComponent(HealthIntComponent.class).getMaxValue()) {
                    player.getComponent(HealthIntComponent.class).restore(50);
                    FirstAid.removeFromWorld();
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.BossBattle, DungeonType.Player) {
            @Override
            protected void onCollisionBegin(Entity Player, Entity BossBattle) {
                bossBattle = true;

                if (getGameWorld().getSingleton(DungeonType.Boss).isActive()) {
                    bossIsAlive = true;
                    HealthIntComponent Bosshp = getGameWorld().getSingleton(DungeonType.Boss).getComponent(HealthIntComponent.class);

                    ProgressBar BosshpBar = ProgressBar.makeHPBar();
                    BosshpBar.setMinValue(0);
                    BosshpBar.setMaxValue(Bosshp.getValue());
                    BosshpBar.currentValueProperty().bind(Bosshp.valueProperty());
                    BosshpBar.setLayoutY(5);
                    BosshpBar.setLayoutX(getAppWidth() / 2 - 100);
                    BosshpBar.setWidth(100);
                    BosshpBar.setHeight(10);
                    BosshpBar.setLabelVisible(true);
                    BosshpBar.setLabelPosition(Position.LEFT);
                    BosshpBar.setFill(Color.RED);
                    BosshpBar.setTraceFill(Color.RED.brighter());

                    getGameScene().addUINode(BosshpBar);
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Laser, DungeonType.Player) {
            @Override
            protected void onCollisionBegin(Entity Laser, Entity Player) {
                Player.getComponent(HealthIntComponent.class).damage(1);
                Laser.removeFromWorld();
                if (Player.getComponent(HealthIntComponent.class).isZero()) {
                    playerDeath();
                }
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Laser, DungeonType.Wall) {
            @Override
            protected void onCollisionBegin(Entity Laser, Entity Wall) {
                Laser.removeFromWorld();
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
               int lifeRemaining = player.getComponent(HealthIntComponent.class).getValue();
                getGameScene().getViewport().fade(() -> {
                    nextLevel();
                    SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
                    player = FXGL.getGameWorld().spawn("Player", data);
                    getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
                    FXGL.getPhysicsWorld().setGravity(0, 0);
                    getGameScene().clearUINodes();
                    initUI();
                    player.getComponent(HealthIntComponent.class).setValue(lifeRemaining);
                });
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(DungeonType.Player, DungeonType.DoorBoss) {
            @Override
            protected void onCollisionBegin(Entity Player, Entity DoorBoss) {
                if (!bossIsAlive) {
                    int lifeRemaining = player.getComponent(HealthIntComponent.class).getValue();
                    getGameScene().getViewport().fade(() -> {
                        nextLevel();
                        SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
                        player = FXGL.getGameWorld().spawn("Player", data);
                        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
                        FXGL.getPhysicsWorld().setGravity(0, 0);
                        getGameScene().clearUINodes();
                        initUI();
                        player.getComponent(HealthIntComponent.class).setValue(lifeRemaining);
                    });
                }
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {

        if (bossBattle) {
            getGameWorld().getSingleton(DungeonType.EnemySpawn).getComponent(EnemySpawn.class).enemySpawn();
        }
    }

    public Music music() {
        Music music = getAssetLoader().loadMusic("y2mate.com - mutant_year_zero_road_to_eden_full_soundtrack_Jr7dJim426k.mp3");
        getAudioPlayer().playMusic(music);

        return music;
    }

    public void playerDeath() {
        getAudioPlayer().pauseMusic(music());

        Sound sound = getAssetLoader().loadSound("y2mate.com - dark_souls_you_died_sound_effect_j_nV2jcTFvA_360p.mp4");
        getAudioPlayer().playSound(sound);
        getDisplay().showMessageBox("You Died!", () -> {
            getAudioPlayer().resumeMusic(music());
        });
        setLevel(geti("level"));
        SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
        DungeonApp.player = FXGL.getGameWorld().spawn("Player", data);
        getGameScene().getViewport().bindToEntity(DungeonApp.player, getAppWidth() / 2, getAppHeight() / 2);

        getGameScene().clearUINodes();
        initUI();
    }

    private void nextLevel() {
        if (geti("level") == MAX_LEVEL) {
            getDisplay().showMessageBox("You finished the game!", () -> {
                getGameController().exit();
            });
            //player.removeFromWorld();
            return;
        }
        // we play test the same level if dev mode
        // so only increase level if release mode
        if (!DEVELOPING_NEW_LEVEL && !firstStart) {
            inc("level", +1);
        }
        firstStart = false;
        setLevel(geti("level"));
    }

    public void setLevel(int levelNum) {
        if (DEVELOPING_NEW_LEVEL) {
            levelNum = 2;
        }

        var levelFile = new File("level0.tmx");

        Level level;


        if (DEVELOPING_NEW_LEVEL && levelFile.exists()) {

            try {
                level = new TMXLevelLoader().load(levelFile.toURI().toURL(), getGameWorld());
                getGameWorld().setLevel(level);

                System.out.println("Success");

            } catch (Exception e) {
                level = FXGL.setLevelFromMap("level" + levelNum + ".tmx");
            }
        } else {
            level = FXGL.setLevelFromMap("level" + levelNum + ".tmx");
        }

    }
    public static void main (String[]args){
        launch(args);
    }
}
