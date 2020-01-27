import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class BossControl extends Component {
    private PhysicsComponent physics;
    Image image = image("Boss(1).png");
    Image image1 = image("Boss(1)UpDown.png");
    private LocalTimer shootTimer;
    private AnimatedTexture texture;
    private AnimationChannel animationUpDown, animationRightLeft;
    private LocalTimer Random;
    private LocalTimer Laser;

    public BossControl(){
            animationRightLeft = new AnimationChannel(image, 1, 48, 41, Duration.seconds(1), 0,0);
            animationUpDown = new AnimationChannel(image1, 1, 48, 41, Duration.seconds(1), 0,0);
            texture = new AnimatedTexture(animationUpDown);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
        Random = FXGL.newLocalTimer();
        Random.capture();
        Laser = FXGL.newLocalTimer();
        Laser.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        texture.loopAnimationChannel(animationUpDown);

        if ((DungeonApp.player.getY() - entity.getY() > 0) && (DungeonApp.player.getY() - entity.getY() < 150) && (DungeonApp.player.getX() - entity.getX() > -10) && (DungeonApp.player.getX() - entity.getX() < 10)) {
            getEntity().setScaleY(1);
            texture.loopAnimationChannel(animationUpDown);
        }
        if ((DungeonApp.player.getY() - entity.getY() > -10) && (DungeonApp.player.getY() - entity.getY() < 10) && (DungeonApp.player.getX() - entity.getX() > -150) && (DungeonApp.player.getX() - entity.getX() < 0)) {
            getEntity().setScaleX(-1);
            texture.loopAnimationChannel(animationRightLeft);

        }
        if ((DungeonApp.player.getY() - entity.getY() > -10) && (DungeonApp.player.getY() - entity.getY() < 10) && (DungeonApp.player.getX() - entity.getX() > 0) && (DungeonApp.player.getX() - entity.getX() < 150)) {
            getEntity().setScaleX(1);
            texture.loopAnimationChannel(animationRightLeft);

        }
        if ((DungeonApp.player.getY() - entity.getY() > -150) && (DungeonApp.player.getY() - entity.getY() < 0) && (DungeonApp.player.getX() - entity.getX() > -10) && (DungeonApp.player.getX() - entity.getX() < 10)) {
            getEntity().setScaleY(1);
            texture.loopAnimationChannel(animationUpDown);
        }

        if (shootTimer.elapsed(Duration.seconds(2))) {
            if (entity.getComponent(HealthIntComponent.class).getValue() <= 75 && entity.getComponent(HealthIntComponent.class).getValue() > 50) {

                FXGL.getGameWorld()
                        .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                        .ifPresent(this::shootShotgun);
                FXGL.getGameWorld()
                        .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                        .ifPresent(Player -> {
                            shootSpread(Player);
                            shootTimer.capture();
                        });
            } else if (entity.getComponent(HealthIntComponent.class).getValue() <= 50) {
                if (Laser.elapsed(Duration.seconds(0))) {
                    FXGL.getGameWorld()
                            .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                            .ifPresent(Player -> {
                                Laser(Player);
                                Laser.capture();
                            });
                }
            } else {
                    FXGL.getGameWorld()
                            .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                            .ifPresent(Player -> {
                                shootShotgun(Player);
                                shootTimer.capture();
                            });
            }
        }
    }

    private void shootSpread(Entity Player){
        if (Random.elapsed(Duration.seconds(2))) {
            Sound sound = getAssetLoader().loadSound("normal (0.38 s).wav");
            getAudioPlayer().playSound(sound);
            int randommovex = (int) (Math.random() * 150 + 1);
            int randommovey = (int) (Math.random() * 50 + 1);
            int randommovex1 = (int) (Math.random() * 150 + 1);
            int randommovey1 = (int) (Math.random() * -150 + 1);
            int randommovex2 = (int) (Math.random() * 150 + 1);
            int randommovey2 = (int) (Math.random() * 150 + 1);
            int randommovex3 = (int) (Math.random() * -150 + 1);
            int randommovey3 = (int) (Math.random() * 150 + 1);
            Point2D position = getEntity().getPosition().add(25/2, 39/2);
            SpawnData data = new SpawnData(position);
            SpawnData data2 = new SpawnData(position);
            SpawnData data3 = new SpawnData(position);
            SpawnData data4 = new SpawnData(position);
            Point2D direction = Player.getPosition().subtract(position).add(randommovex, randommovey);
            Point2D direction2 = Player.getPosition().subtract(position).add(randommovex1, randommovey1);
            Point2D direction3 = Player.getPosition().subtract(position).subtract(randommovex2, randommovey2);
            Point2D direction4 = Player.getPosition().subtract(position).subtract(randommovex3, randommovey3);
            data.put("direction", direction);
            data2.put("direction", direction2);
            data3.put("direction", direction3);
            data4.put("direction", direction4);
            FXGL.spawn("EnemyBullet", data);
            FXGL.spawn("EnemyBullet", data2);
            FXGL.spawn("EnemyBullet", data3);
            FXGL.spawn("EnemyBullet", data4);
            Random.capture();
        }
    }

    private void shootShotgun(Entity Player) {
        Sound sound = getAssetLoader().loadSound("normal (0.38 s).wav");
        getAudioPlayer().playSound(sound);
            Point2D position = getEntity().getPosition().add(25/2, 39/2);
            SpawnData data = new SpawnData(position);
            SpawnData data2 = new SpawnData(position);
            SpawnData data3 = new SpawnData(position);
            SpawnData data4 = new SpawnData(position);
            SpawnData data5 = new SpawnData(position);
            Point2D direction = Player.getPosition().subtract(position);
            Point2D direction2 = Player.getPosition().subtract(position).add(5,7);
            Point2D direction3 = Player.getPosition().subtract(position).add(4,3);
            Point2D direction4 = Player.getPosition().subtract(position).subtract(7,5);
            Point2D direction5 = Player.getPosition().subtract(position).subtract(2,3);
            data.put("direction", direction);
            data2.put("direction",direction2);
            data3.put("direction",direction3);
            data4.put("direction",direction4);
            data5.put("direction",direction5);
            Entity bullet = FXGL.spawn("EnemyBullet", data);
            FXGL.spawn("EnemyBullet", data2);
            FXGL.spawn("EnemyBullet", data3);
            FXGL.spawn("EnemyBullet", data4);
            FXGL.spawn("EnemyBullet", data5);
    }

    private void Laser(Entity Player) {
        Sound sound = getAssetLoader().loadSound("normal (0.38 s).wav");
        getAudioPlayer().playSound(sound);
        Point2D position = getEntity().getPosition().add(25/2, 39/2);
        SpawnData data = new SpawnData(position);
        Point2D direction = Player.getPosition().subtract(position).add(25/2, 39/2);
        data.put("direction", direction);
        Entity bullet = FXGL.spawn("Laser", data);
    }


}
