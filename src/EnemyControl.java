import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;

import javafx.util.Duration;

import java.util.Optional;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class EnemyControl extends Component {
    private PhysicsComponent physics;
    private LocalTimer shootTimer;
    private LocalTimer move;
    private LocalTimer movestop;
    boolean Guard;
    boolean melee;


    @Override
    public void onAdded() {
        shootTimer = FXGL.newLocalTimer();
        shootTimer.capture();
        move = FXGL.newLocalTimer();
        move.capture();
        movestop = FXGL.newLocalTimer();
        movestop.capture();
    }
    Random random = new Random();
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
        int randommove = (int) (Math.random() * 4 + 1);

            if (move.elapsed(Duration.seconds(2))) {
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
            }
            if  ((DungeonApp.player.getY() - entity.getY() > 0) && (DungeonApp.player.getY() - entity.getY() < 640) && (DungeonApp.player.getY() - entity.getY() > DungeonApp.player.getX() - entity.getX())) {

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
