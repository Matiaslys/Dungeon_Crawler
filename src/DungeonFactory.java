import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
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
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    @Spawns("DoorBoss")
    public Entity newDoorBoss (SpawnData data) {


        return FXGL.entityBuilder()
                .type(DungeonType.DoorBoss)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();

    }

    @Spawns ("Enemy")
    public Entity newEnemy (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Enemy)
                .bbox(new HitBox(BoundingShape.box(25,29)))
                .with(physics)
                .with(new HealthIntComponent(MainMenu.healthEnemy))
                .with(new CollidableComponent(true))
                .with(new EnemyControl())
                .build();
    }

    @Spawns ("EnemyDead")
    public Entity newEnemyDead (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.EnemyDead)
                .bbox(new HitBox(BoundingShape.box(52,29)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new EnemeyDeadControl())
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

    @Spawns("BossBattle ")
    public Entity newBossBattle  (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.BossBattle)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") , data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();

    }

    @Spawns("EnemySpawn")
    public Entity newEnemySpawn (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.EnemySpawn)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") , data.<Integer>get("height"))))
                .with(new EnemySpawn())
                .build();

    }

    @Spawns("Boss(1)")
    public Entity newBoss (SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(100000));
        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Boss)
                .bbox(new HitBox(BoundingShape.box(48 , 41)))
                .with(new HealthIntComponent(MainMenu.BossHealth))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new BossControl())
                .build();

    }

    @Spawns("FirstAid")
    public Entity newFirstAid (SpawnData data) {
        return FXGL.entityBuilder()
                .type(DungeonType.FirstAid)
                .from(data)
                .view(texture("First Aid kit.png"))
                .bbox(new HitBox(BoundingShape.box(26 , 24)))
                .with(new CollidableComponent(true))
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
                .with(physics)
                .with(new HealthIntComponent(MainMenu.healthPlayer))
                .with(new CollidableComponent(true))
                .with(new PlayerControl())
                .build();
    }

    @Spawns("PlayerBullet")
    public Entity newPlayerBullet (SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.PlayerBullet)
                .viewWithBBox(new Rectangle(8.5,2.5, Color.GREEN))
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
                .viewWithBBox(new Rectangle(8.5,2.5, Color.RED))
                .with(new ProjectileComponent(data.get("direction"), MainMenu.bulletSpeed))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("Laser")
    public Entity newEnemyLaser (SpawnData data) {

        return FXGL.entityBuilder()
                .from(data)
                .type(DungeonType.Laser)
                .viewWithBBox(new Rectangle(6.5,0.5, Color.RED))
                .with(new ProjectileComponent(data.get("direction"), MainMenu.bulletSpeed))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }
    @Spawns("Lampe")
    public Entity newLampe (SpawnData data) {

        return FXGL.entityBuilder()
                .type(DungeonType.Lampe)
                .from(data)
                .view("lampe.gif")
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .build();
    }
}
