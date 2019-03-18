package aircraftbattle.stuff;

import aircraftbattle.game.GameParameter;
import aircraftbattle.game.GameParameter.*;

import java.io.IOException;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class MagicBullet extends GameComponent {

    private boolean isAlive;
    private BulletType type;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public MagicBullet(int x, int y, String path, BulletType type) throws IOException {
        super(x, y, GameParameter.MAGIC_BULLET_SPEED_X, GameParameter.MAGIC_BULLET_SPEED_Y, path);
        isAlive = true;
        this.type = type;
    }

    @Override
    public void move() {
        if (y < 0 || y > GameParameter.FRAME_HEIGHT) {
            isAlive = false;
            return;
        }

        if (type == BulletType.MAGIC) {
            y -= speedY;
            x -= speedX;
        }
    }
}
