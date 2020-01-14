import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.io.ObjectInputFilter;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;


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
