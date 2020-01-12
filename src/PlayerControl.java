import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;


public class PlayerControl extends Component {
    private PhysicsComponent physics;

    public void left() {
        physics.setVelocityX(-30);
    }
    public void right() {
        physics.setVelocityX(30);
    }
    public void up() {
        physics.setVelocityY(-30);
    }
    public void down() {
        physics.setVelocityY(30);
    }
    public void leftStop() {
        physics.setVelocityX(0);
    }
    public void rightStop() {
        physics.setVelocityX(0);
    }
    public void upStop() {
        physics.setVelocityY(0);
    }
    public void downStop() {
        physics.setVelocityY(0);
    }

}
