import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class EnemySpawn extends Component {
    private LocalTimer Spawn;

    @Override
    public void onAdded() {
        Spawn = FXGL.newLocalTimer();
        Spawn.capture();
    }


    public void enemySpawn() {
        if (Spawn.elapsed(Duration.seconds(MainMenu.EnemySpawn))){
            spawn("Enemy", 409,190.50);
            spawn("Enemy", 59.75,189.75);
            Spawn.capture();
        }
    }
}
