import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Point2D;

public class DungeonFactory implements EntityFactory {

    @Spawns("Wall")
    public Entity newWall (SpawnData data) {


        return FXGL.entityBuilder()
                .type(DungeonType.Wall)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .build();

    }

    @Spawns("Door")
    public Entity newDoor (SpawnData data) {


        return FXGL.entityBuilder()
                .type(DungeonType.Door)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();

    }

    @Spawns("Enemy Spawn")
    public Entity newEnemySpawn (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder()
                .type(DungeonType.EnemySpawn)
                .from(data)
                .viewWithBBox(new Rectangle(11 ,11.5,  Color.RED))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .build();

    }
//player
    @Spawns("Spawn")
    public Entity newSpawn (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.Spawn)
                .from(data)
                //.bbox(new HitBox(new Point2D(data.getX(), data.getY()),BoundingShape.box(12.5 ,13)))
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") ,data.<Integer>get("height"))))
                .build();

    }

    @Spawns("Player")
    public Entity newPlayer (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Player)
                .viewWithBBox(new Rectangle(12.50, 13, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }

    @Spawns("PlayerBullet")
    public Entity newPlayerBullet (SpawnData data) {

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.PlayerBullet)
                .viewWithBBox(new Rectangle(6.5,1.5, Color.BLUE))
                .with(new ProjectileComponent(data.get("direction"), 300))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("EnemyBullet")
    public Entity newEnemyBullet (SpawnData data) {

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.EnemyBullet)
                .viewWithBBox(new Rectangle(6.5,1.5, Color.RED))
                .with(new ProjectileComponent(data.get("direction"), 300))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

}
