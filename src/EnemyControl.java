import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class EnemyControl extends Component {
    private PhysicsComponent physics;
    private LocalTimer shootTimer;

    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {

        if (shootTimer.elapsed(Duration.seconds(0.5))) {
            FXGL.getGameWorld()
                    .getClosestEntity(entity, e -> e.isType(DungeonType.Player))
                    .ifPresent(Player -> {
                        shoot(Player);
                        shootTimer.capture();
                    });
        }

    }

    public void left () {
        physics.setVelocityX(-40);
    }
    public void right () {
        physics.setVelocityX(40);
    }
    public void up () {
        physics.setVelocityY(-40);
    }
    public void down () {
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
        Point2D position = getEntity().getPosition();
        SpawnData data = new SpawnData(position);
        Point2D direction = Player.getPosition().subtract(position);
        data.put("direction", direction);
        Entity bullet = FXGL.spawn("EnemyBullet", data);
    }
















}
