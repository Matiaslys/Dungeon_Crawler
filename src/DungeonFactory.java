import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;


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

    @Spawns ("Enemy")
    public Entity newEnemy (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
//        physics.setFixtureDef(new FixtureDef().density(100000));

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Enemy)
                .bbox(new HitBox(BoundingShape.box(25,29)))
//                .viewWithBBox(new Rectangle(11 ,11.5,  Color.RED))
                .with(physics)
                .with(new HealthIntComponent(1))
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .build();
    }

    @Spawns("Spawn")
    public Entity newSpawn (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.Spawn)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") , data.<Integer>get("height"))))
                .build();

    }

    @Spawns("Damage")
    public Entity newDamage (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.Damage)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") , data.<Integer>get("height"))))
                .build();

    }

    @Spawns("Player")
    public Entity newPlayer (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Player)
                .bbox(new HitBox(BoundingShape.box(25,39)))
//                .viewWithBBox(new Rectangle(12.50, 13, Color.BLUE))
                .with(physics)
                .with(new HealthIntComponent(100))
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
