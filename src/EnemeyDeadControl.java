import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class EnemeyDeadControl extends Component {

    Image image = image("Dead(Right).png");
    private AnimatedTexture texture;


    public EnemeyDeadControl() {
        AnimationChannel dead = new AnimationChannel(image, 1, 52, 25, Duration.seconds(1), 0, 0);
        texture = new AnimatedTexture(dead);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.Player);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.PlayerBullet);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.Enemy);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.EnemyBullet);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.Laser);
        entity.getComponent(CollidableComponent.class).addIgnoredType(DungeonType.Boss);
    }

}
