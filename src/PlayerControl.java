import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.ObjectInputFilter;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class PlayerControl extends Component {
    Image image = image("Player.png");
    Image image2 = image("Up,Down(player).png");
    Image image3 = image("Shoot Down,Up(player).png");
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel animationRightLeft, animationShotRightLeft, animationUpDown,animationshotUpDown;
    private LocalTimer shootTimer;
    private LocalTimer dash;

    public PlayerControl() {
        animationRightLeft = new AnimationChannel(image, 2, 74/2, 39, Duration.seconds(1), 0,0);
        animationShotRightLeft = new AnimationChannel(image, 2 , 74/2,39, Duration.seconds(1),1,1);
        animationUpDown = new AnimationChannel(image2, 1 , 30,39, Duration.seconds(1),0,0);
        animationshotUpDown = new AnimationChannel(image3, 1 , 43,39, Duration.seconds(1),0,0);

        texture = new AnimatedTexture(animationUpDown);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
        dash = FXGL.newLocalTimer();
        dash.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (FXGLMath.abs(physics.getVelocityX()) > 0) {
            texture.loopAnimationChannel(animationRightLeft);
        } else if (FXGLMath.abs(physics.getVelocityY()) > 0) {
            texture.loopAnimationChannel(animationUpDown);
        }
        if (dash.elapsed(Duration.seconds(0.3))) {
            DungeonApp.left = true;
            DungeonApp.right = true;
            DungeonApp.up = true;
            DungeonApp.down = true;
            dash.capture();
        }
    }


    public void shoot() {
        if (shootTimer.elapsed(Duration.seconds(0.5))) {
            Sound sound = getAssetLoader().loadSound("normal (0.38 s).wav");
            getAudioPlayer().playSound(sound);
        Point2D position = entity.getPosition().add(25/2, 39/2);
        SpawnData data = new SpawnData(position);
        Point2D shootVector = FXGL.getInput().getVectorToMouse(position).normalize();
            //down
            if ((FXGL.getInput().getMouseYWorld() - entity.getY() > 0) && (FXGL.getInput().getMouseXWorld() - entity.getX() > -100) && (FXGL.getInput().getMouseYWorld() - entity.getX() < 150)) {
                texture.loopAnimationChannel(animationshotUpDown);
                getEntity().setScaleY(1);
            }
            //left
            if ((FXGL.getInput().getMouseYWorld() - entity.getY() > -100) && (FXGL.getInput().getMouseYWorld() - entity.getY() < 150) && (FXGL.getInput().getMouseYWorld() - entity.getX() < 0)) {
                texture.loopAnimationChannel(animationShotRightLeft);
                getEntity().setScaleX(-1);
            }
            //right
            if ((FXGL.getInput().getMouseYWorld() - entity.getY() > -100) && (FXGL.getInput().getMouseYWorld() - entity.getY() < 150) && (FXGL.getInput().getMouseXWorld() - entity.getX() > 0)) {
                texture.loopAnimationChannel(animationShotRightLeft);
                getEntity().setScaleX(1);
            }
            //up
            if ((FXGL.getInput().getMouseYWorld() - entity.getY() < 0) && (FXGL.getInput().getMouseXWorld() - entity.getX() > -100) && (FXGL.getInput().getMouseYWorld() - entity.getX() < 150)) {
                texture.loopAnimationChannel(animationshotUpDown);
                getEntity().setScaleY(-1);
            }
            data.put("direction", shootVector);
        Entity bullet = FXGL.spawn("PlayerBullet", data);
            shootTimer.capture();
        }
    }

        public void left () {
            getAudioPlayer().resumeMusic(FXGL.<DungeonApp>getAppCast().music());
            getEntity().setScaleX(-1);
            if (!DungeonApp.left) {
                physics.setVelocityX(-300);
            }
            else {
                physics.setVelocityX(-80);
            }
        }
        public void right () {
            getAudioPlayer().resumeMusic(FXGL.<DungeonApp>getAppCast().music());
            getEntity().setScaleX(1);
            if (!DungeonApp.right) {
                physics.setVelocityX(300);
            }
            else {
                physics.setVelocityX(80);
            }
        }
        public void up () {
            getAudioPlayer().resumeMusic(FXGL.<DungeonApp>getAppCast().music());
            getEntity().setScaleY(-1);
            if (!DungeonApp.up) {
                physics.setVelocityY(-300);
            }
            else {
                physics.setVelocityY(-80);
            }
        }
        public void down () {
            getAudioPlayer().resumeMusic(FXGL.<DungeonApp>getAppCast().music());
            getEntity().setScaleY(1);
            if (!DungeonApp.down) {
                physics.setVelocityY(300);
            }
            else {
                physics.setVelocityY(80);
            }
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

        public void playerHit() {
            entity.getComponent(HealthIntComponent.class).damage(10);

            if (entity.getComponent(HealthIntComponent.class).isZero()) {
                FXGL.<DungeonApp>getAppCast().playerDeath();
            }
        }

    }
