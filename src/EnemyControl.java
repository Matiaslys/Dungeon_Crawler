import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.ObjectComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.pathfinding.astar.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class EnemyControl extends Component {
    Image image = image("Right,Left.png");
    Image image1 = image("Up,Down.png");
    Image image2 = image("Up,idle.png");

    private PhysicsComponent physics;
    private LocalTimer shootTimer;
    private LocalTimer move;
    private LocalTimer movestop;
    private AnimatedTexture texture;
    private AnimationChannel animationDown, animationRightLeft, animationUp;
    private int surveillanceArea = 150;
    static boolean stop = false;



    public EnemyControl() {
        animationDown = new AnimationChannel(image1, 1, 37, 29, Duration.seconds(1), 0,0);
        animationRightLeft = new AnimationChannel(image, 1, 37, 29, Duration.seconds(1), 0,0);
        animationUp = new AnimationChannel(image2, 1, 37, 29, Duration.seconds(1), 0,0);
        texture = new AnimatedTexture(animationUp);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
        move = FXGL.newLocalTimer();
        move.capture();
        movestop = FXGL.newLocalTimer();
        movestop.capture();
    }


    public void EnemyHit() {
        entity.getComponent(HealthIntComponent.class).damage(1);
        if (entity.getComponent(HealthIntComponent.class).isZero()) {
            Point2D position = entity.getPosition();
            entity.removeFromWorld();
            int killLevel = DungeonApp.level;
            spawn("EnemyDead", position);
            FXGL.runOnce(() -> {
                if (DungeonApp.level == killLevel && getGameWorld().getSingleton(DungeonType.Player).getComponent(HealthIntComponent.class).getValue() < MainMenu.healthPlayer) {
                    getGameWorld().getSingleton(DungeonType.EnemyDead).removeFromWorld();
                }
            }, Duration.seconds(5));
        }
    }

    public void StopMove() {
        leftStop();
        rightStop();
        upStop();
        downStop();
    }

    @Override
    public void onUpdate(double tpf) {

        if (FXGLMath.abs(physics.getVelocityX()) > 0) {
                texture.loopAnimationChannel(animationRightLeft);
        } else if (FXGLMath.abs(physics.getVelocityY()) > 0) {
            texture.loopAnimationChannel(animationDown);
        }

        // Ai shoot player
        if (shootTimer.elapsed(Duration.seconds(MainMenu.bulletTimer))) {
            FXGL.getGameWorld()
                    .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                    .ifPresent(Player -> {
                        shoot(Player);
                        shootTimer.capture();
                    });
        }

        if (!(DungeonApp.player.getY() - entity.getY() > -surveillanceArea) && (DungeonApp.player.getY() - entity.getY() < surveillanceArea) && (DungeonApp.player.getX() - entity.getX() > -surveillanceArea) && (DungeonApp.player.getX() - entity.getX() < surveillanceArea) && stop) {
            stop = false;
        }
        // Ai movement
        int randommove = (int) (Math.random() * 4 + 1);

            if (move.elapsed(Duration.seconds(2)) && !stop) {
                movestop.capture();
                switch (randommove) {
                    case 1:
                        left();
                        FXGL.runOnce(() -> {
                            leftStop();
                        }, Duration.seconds(0.5));
                        break;
                    case 2:
                        right();
                        FXGL.runOnce(() -> {
                            rightStop();
                        }, Duration.seconds(0.5));
                        break;
                    case 3:
                        up();
                        FXGL.runOnce(() -> {
                            upStop();
                        }, Duration.seconds(0.5));
                        break;
                    case 4:
                        down();
                        FXGL.runOnce(() -> {
                            downStop();
                        }, Duration.seconds(0.5));
                        break;
                }
                move.capture();
                movestop.capture();
            } else if ((DungeonApp.player.getY() - entity.getY() > -surveillanceArea) && (DungeonApp.player.getY() - entity.getY() < surveillanceArea) && (DungeonApp.player.getX() - entity.getX() > -surveillanceArea) && (DungeonApp.player.getX() - entity.getX() < surveillanceArea)) {
                Point2D position = getEntity().getPosition().add(25/2, 39/2);
                Point2D direction = getGameWorld().getSingleton(DungeonType.Player).getPosition().subtract(position);
                double lookx = direction.getX();
                double looky = direction.getY();

                double look = Math.atan2(lookx,looky)*180/Math.PI;
                if (texture.getAnimationChannel() == animationUp){
                    entity.setRotation(-(look + 180));
                }
                if (texture.getAnimationChannel() == animationDown && getEntity().getScaleY() == -1){
                    entity.setRotation(-(look));
                    getEntity().setScaleY(1);
                }
                if (texture.getAnimationChannel() == animationDown){
                    entity.setRotation(-(look));
                }
                if (texture.getAnimationChannel() == animationRightLeft){
                    entity.setRotation(-(look) + 90);
                }
                if (texture.getAnimationChannel() == animationRightLeft && getEntity().getScaleX() == -1){
                    entity.setRotation(-(look) + 270);
                }

            }
            if (physics.getVelocityY() > 41 ||physics.getVelocityY() < -41 || physics.getVelocityX() > 41 ||physics.getVelocityX() < -41){
                StopMove();
            }

        }

    public void left () {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-40);

    }
    public void right () {
        getEntity().setScaleX(1);
        physics.setVelocityX(40);

    }
    public void up () {
        getEntity().setScaleY(-1);
        physics.setVelocityY(-40);

    }
    public void down () {
        getEntity().setScaleY(1);
        physics.setVelocityY(40);
    }
    public void leftStop () {
        physics.setVelocityX(0);
    }
    public void rightStop () {
        physics.setVelocityX(0);
    }
    public void upStop () {
        physics.setVelocityY(0);
    }
    public void downStop () {
        physics.setVelocityY(0);
    }


    private void shoot(Entity Player) {
        if ((DungeonApp.player.getY() - entity.getY() > -surveillanceArea) && (DungeonApp.player.getY() - entity.getY() < surveillanceArea) && (DungeonApp.player.getX() - entity.getX() > -surveillanceArea) && (DungeonApp.player.getX() - entity.getX() < surveillanceArea) || FXGL.<DungeonApp>getAppCast().bossBattle) {
            Sound sound = getAssetLoader().loadSound("normal (0.38 s).wav");
            getAudioPlayer().playSound(sound);
            Point2D position = getEntity().getPosition().add(25/2, 39/2);
            SpawnData data = new SpawnData(position);
            Point2D direction = Player.getPosition().subtract(position);
            data.put("direction", direction);
            Entity bullet = FXGL.spawn("EnemyBullet", data);
        }
    }

}
