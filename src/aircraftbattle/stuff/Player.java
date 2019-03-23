package aircraftbattle.stuff;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import aircraftbattle.game.GameParameter;
import aircraftbattle.game.GameParameter.BulletType;
import aircraftbattle.util.GameUtil;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class Player extends GameComponent {

    private int score;
    private int health;
    private int bulletsNum;
    private int magicBulletNum;
    private boolean isAlive;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
//        return GameParameter.playerHealth[GameParameter.currentLevel];
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBulletsNum() {
        return bulletsNum;
    }

    public void setBulletsNum(int bulletsNum) {
        this.bulletsNum = bulletsNum;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Player(String path, int level) throws IOException {
        super(GameParameter.PLAYER_SPAWN_X, GameParameter.PLAYER_SPAWN_Y, GameParameter.PLAYER_SPEED_X,
                GameParameter.PLAYER_SPEED_Y, path);
        this.setAlive(true);
        this.bulletsNum = GameParameter.START_BULLETS[level];
        this.magicBulletNum = GameParameter.START_MAGIC_BULLETS[level];
        this.health = GameParameter.playerHealth[level];
        this.score = GameParameter.toTalScore;
    }

    public void changeImage(boolean isWisdom) throws IOException {
        String path = GameUtil.getTypedPlayerPath(isWisdom);
        image = ImageIO.read(new File(path));
    }

    @Override
    public void move() {
        if (GameParameter.isW) {
            if (y > GameParameter.AIRCRAFT_IMAGE_LENGTH / 2)
                y -= speedY;
        }
        if (GameParameter.isA) {
            if (x > 0)
                x -= speedX;
        }
        if (GameParameter.isS) {
            if (y < GameParameter.FRAME_HEIGHT - GameParameter.AIRCRAFT_IMAGE_LENGTH)
                y += speedY;
        }
        if (GameParameter.isD) {
            if (x <= GameParameter.FRAME_WIDTH - GameParameter.AIRCRAFT_IMAGE_LENGTH)
                x += speedX;
        }
    }

    public Bullet attack() throws IOException {
        if (bulletsNum != 0 || GameParameter.skill2Flag) {
            if (!GameParameter.skill2Flag) {
                bulletsNum--;// 子弹数量减少
            }
        }

        String path = GameUtil.getTypedBulletPath(BulletType.PLAYER);
        Bullet bullet = new Bullet(
                x + GameParameter.AIRCRAFT_IMAGE_LENGTH / 2 - GameParameter.BULLET_IMAGE_WIDTH / 2 + 4, // 4是个修正值
                y - GameParameter.BULLET_IMAGE_HEIGHT, path, BulletType.PLAYER);

        return bullet;
    }

    public MagicBullet magicAttack() throws IOException {
        if (magicBulletNum != 0) {
            magicBulletNum--;
        }

        String path = GameUtil.getTypedBulletPath(BulletType.MAGIC);
        MagicBullet magicBullet = new MagicBullet(
                x + GameParameter.AIRCRAFT_IMAGE_LENGTH / 2 - GameParameter.BULLET_IMAGE_WIDTH / 2 + 4,
                y - GameParameter.BULLET_IMAGE_HEIGHT,
                path, BulletType.MAGIC);

        return magicBullet;
    }

    public void hurt(int hurtPoint) {
        if (!GameParameter.skill1Flag) {
            this.health -= hurtPoint;
//            GameParameter.playerHealth[GameParameter.currentLevel] -= hurtPoint;
        }

        if (hurtPoint < 0 && !GameParameter.isWisdom) {
            health += GameParameter.MAGIC_HEALTH_POINT;//力量型战机两倍生命
        }

        new Music("hurt.mp3", false).start();//音效
    }

    public void bulletAdd(int number) {
        bulletsNum += number;

        if (GameParameter.isWisdom) bulletsNum += GameParameter.MAGIC_BULLET_POINT;//智慧型战机两倍子弹

        new Music("charge.mp3", false).start();//音效
    }

    public int getMagicBulletNum() {
        return magicBulletNum;
    }

    public void setMagicBulletNum(int magicBulletNum) {
        this.magicBulletNum = magicBulletNum;
    }
}
