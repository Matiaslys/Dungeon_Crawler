import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.io.ObjectInputFilter;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class PlayerControl extends Component {

    private PhysicsComponent physics;

    private LocalTimer shootTimer;

    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (DungeonApp.player.getComponent(HealthIntComponent.class).isZero()) {
            getDisplay().showMessageBox("You Died!");
            DungeonApp.player.getComponent(HealthIntComponent.class).restoreFully();
            DungeonApp.player.removeFromWorld();

            SpawnData data = new SpawnData(FXGL.getGameWorld().getSingleton(DungeonType.Spawn).getPosition());
            DungeonApp.player = FXGL.getGameWorld().spawn("Player", data);
            getGameScene().getViewport().bindToEntity(DungeonApp.player, getAppWidth() / 2, getAppHeight() / 2);


//                    Optional<Entity> Enemy1 = getGameScene().getGameWorld().getEntityByID("Enemy", 18);
//                    Optional<Entity> Enemy2 = getGameScene().getGameWorld().getEntityByID("Enemy",19);
//                    Optional<Entity> Enemy3 = getGameScene().getGameWorld().getEntityByID("Enemy",20);
//                    Optional<Entity> Enemy4 = getGameScene().getGameWorld().getEntityByID("Enemy",21);
//
//
//                     Enemy1.get().activeProperty();
//
//
//
//                    if (Enemy1.isEmpty()) {
//                        FXGL.getGameWorld().spawn("Enemy",336, 320);
//                    }
//                    if (Enemy2.isEmpty()) {
//                        FXGL.getGameWorld().spawn("Enemy",75 ,343);
//                    }
//                    if (Enemy3.isEmpty()) {
//                        FXGL.getGameWorld().spawn("Enemy",129.217 ,245.357);
//                    }
//                    if (Enemy4.isEmpty()) {
//                        FXGL.getGameWorld().spawn("Enemy",176.141 ,245.602);
//                    }



        }

    }


    public void shoot() {
        if (shootTimer.elapsed(Duration.seconds(0.5))) {
        Point2D position = entity.getPosition().add(3, 5);
        SpawnData data = new SpawnData(position);
        Point2D shootVector = FXGL.getInput().getVectorToMouse(position).normalize();
        data.put("direction", shootVector);
        Entity bullet = FXGL.spawn("PlayerBullet", data);
            shootTimer.capture();
        }
    }
        public void left () {
            physics.setVelocityX(-80);
        }
        public void right () {
            physics.setVelocityX(80);
        }
        public void up () {
            physics.setVelocityY(-80);
        }
        public void down () {
            physics.setVelocityY(80);
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

    }
