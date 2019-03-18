package aircraftbattle.game;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import aircraftbattle.game.GameParameter.MagicType;
import aircraftbattle.stuff.*;
import aircraftbattle.util.GameUtil;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class GameService {

    public static Player player;// 玩家
    private ArrayList<Bullet> playerBullets;// 玩家子弹

    private ArrayList<MagicBullet> playerMagicBullets;//魔法子弹

    private ArrayList<Enemy> enemies;// 敌人列表
    private ArrayList<Bullet> enemiesBullets;// 敌人子弹列表

    private ArrayList<Magic> magics;// 掉落物品列表
    private ArrayList<Explosion> explosions;// 爆炸效果列表

    public Player getPlayer() {
        return player;
    }

    public GameService() {
        super();
        GameParameter.toTalScore = GameParameter.START_SCORE;// 开始计分
        // 玩家子弹列表
        playerBullets = new ArrayList<>();
        // 魔法子弹列表
        playerMagicBullets = new ArrayList<>();
        // 敌人列表
        enemies = new ArrayList<>();
        // 敌人子弹列表
        enemiesBullets = new ArrayList<>();
        // 道具列表
        magics = new ArrayList<>();
        // 爆炸特效列表
        explosions = new ArrayList<>();

        // 背景音乐
        new Music("background.mp3", true).start();
    }

    void paint(Graphics g) throws IOException {
        // 画玩家
        if (player != null)
            player.draw(g);
        // 画玩家子弹
        if (!playerBullets.isEmpty()) {
            for (Bullet playerBullet : playerBullets) {
                if (playerBullet.isAlive()) {
                    playerBullet.draw(g);
                }
            }
        }
        // 画魔法子弹
        if (!playerMagicBullets.isEmpty()) {
            for (MagicBullet playerMagicBullet : playerMagicBullets) {
                if (playerMagicBullet.isAlive()) {
                    playerMagicBullet.draw(g);
                }
            }
        }
        // 画敌人
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive())
                    enemy.draw(g);
            }
        }
        // 画敌人子弹
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    enemiesBullet.draw(g);
                }
            }
        }
        // 画魔法物品
        if (!magics.isEmpty()) {
            for (Magic magic : magics) {
                if (magic.isAlive()) {
                    magic.draw(g);
                }
            }
        }
        // 画爆炸效果
        if (!explosions.isEmpty()) {
            for (Explosion explosion : explosions) {
                if (explosion.isAlive()) {
                    explosion.draw(g);
                }
            }
        }
    }

    void clear() {
        playerBullets.clear();
        enemies.clear();
        enemiesBullets.clear();
        magics.clear();
    }

    void generatePlayer() throws IOException {
        player = new Player(GameUtil.getTypedPlayerPath(GameParameter.isWisdom), GameParameter.currentLevel);
    }

    public void playerMove() {
        if (player.isAlive()) {
            player.move();
        }
    }

    public void generateEnemy() throws IOException {
        // 最后一关只有一个BOSS
        if (GameParameter.currentLevel == 4) {
            if (enemies.isEmpty()) {
                enemies.add(new Boss("img\\Boss.png"));
                return;
            }
        }
        // 其他关卡
        if (enemies.isEmpty() || enemies.get(enemies.size() - 1).getY() > 200) {// 上一个敌人移动超过200
            enemies.add(new Enemy(GameUtil.getLevelEnemyPath(GameParameter.currentLevel), GameParameter.currentLevel));
        }
    }

    public void othersMove() {
        //敌人移动
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    enemy.move();
                }
            }
        }
        //敌人子弹移动
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    enemiesBullet.move();
                }
            }
        }
        //魔法物品移动
        if (!magics.isEmpty()) {
            for (Magic magic : magics) {
                if (magic.isAlive()) {
                    magic.move();
                }
            }
        }
    }

    public void playerBulletsGenerate() throws IOException {
        if (GameParameter.isSpace && player.getBulletsNum() != 0) {
            GameParameter.isSpace = false;// 不加这句会连续发出子弹

            Bullet playerBullet = player.attack();
            playerBullets.add(playerBullet);
            // 子弹发射音效
            new Music("fire.mp3", false).start();
        }
    }

    public void playerMagicBulletsGenerate() throws IOException {
        if (GameParameter.isJ && player.getMagicBulletNum() != 0) {
            GameParameter.isJ = false;

            MagicBullet playerMagicBullet = player.magicAttack();
            playerMagicBullets.add(playerMagicBullet);
            // 子弹发射音效
            new Music("fire.mp3", false).start();
        }
    }

    public void playerBulletsMove() {
        if (!playerBullets.isEmpty()) {
            for (Bullet playerBullet : playerBullets) {
                if (playerBullet.isAlive()) {
                    playerBullet.move();
                }
            }
        }
    }

    public void playerMagicBulletsMove() {
        if (!playerMagicBullets.isEmpty()) {
            for (MagicBullet playerMagicBullet : playerMagicBullets) {
                if (playerMagicBullet.isAlive()) {
                    playerMagicBullet.move();
                }
            }
        }
    }

    public void enemyBulletsGenerate() throws IOException {
        for (Enemy enemy : enemies) {
            boolean flag = false;// 能否发射子弹

            if (enemy.getBulletsNum() != 0) {// 还有子弹
                if (GameParameter.currentLevel == 4) {// BOSS无限发射子弹
                    flag = true;
                } else if (enemy.getCurrentBullet() == null) {// 第一颗子弹
                    flag = true;
                } else { // 不是第一颗子弹
                    if (enemy.getCurrentBullet().getY() > GameUtil.getRandomNum(500, 600)) {// 前一颗子弹Y过了一个随机数
                        flag = true;
                    }
                }
            }
            // 可以发射子弹
            if (flag) {
                Bullet enemyBullet = enemy.directionalAttack(player.getX(), player.getY());
                enemy.setCurrentBullet(enemyBullet);
                enemiesBullets.add(enemyBullet);
            }
        }

    }

    public void collisionDetect() throws IOException {
        // 敌人碰撞
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    // 敌人与玩家碰撞
                    if (GameUtil.isCrossing(enemy.getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.AIRCRAFT_HURT_POINT);// 玩家受到伤害
                        enemy.hurt(GameParameter.AIRCRAFT_HURT_POINT);// 敌人受到伤害
                    }
                    // 敌人与玩家子弹碰撞
                    if (!playerBullets.isEmpty()) {
                        for (Bullet playerBullet : playerBullets) {
                            if (GameUtil.isCrossing(playerBullet.getRectangle(),
                                    enemy.getRectangle())) {
                                playerBullet.setAlive(false);// 玩家子弹死亡
                                enemy.hurt(GameParameter.BULLET_HURT_POINT);// 敌人受到伤害
                            }
                        }
                    }
                    //敌人与玩家魔法子弹碰撞
                    if (!playerMagicBullets.isEmpty()) {
                        for (MagicBullet playerMagicBullet : playerMagicBullets) {
                            if (GameUtil.isCrossing(playerMagicBullet.getRectangle(),
                                    enemy.getRectangle())) {
                                playerMagicBullet.setAlive(false);// 玩家魔法子弹死亡
                                enemy.hurt(GameParameter.MAGIC_BULLET_HURT_POINT);
                            }
                        }
                    }
                    // 判断敌人是否死亡
                    if (enemy.getHealth() <= 0) {
                        enemy.setAlive(false);// 敌人死亡
                        GameParameter.toTalScore += GameParameter.ENEMY_HURT_SCORE;// 加分
                        // 是否掉落魔法物品
                        if (GameUtil.isMagicFall()) {
                            Magic magic = new Magic(enemy.getX(), enemy.getX(),
                                    GameUtil.getRandomMagicType());
                            magics.add(magic);
                        }
                        // 播放爆炸效果
                        Explosion explosion = new Explosion(enemy.getX(), enemy.getY());
                        explosions.add(explosion);
                    }
                }
            }
        }
        // 敌人子弹碰撞
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    // 敌人子弹与玩家碰撞
                    if (GameUtil.isCrossing(enemiesBullet.getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.BULLET_HURT_POINT);// 玩家受到伤害
                        enemiesBullet.setAlive(false);// 敌人子弹死亡
                    }
                    // 敌人子弹与玩家子弹碰撞
                    if (!playerBullets.isEmpty() && enemiesBullet.isAlive()) {
                        for (Bullet playerBullet : playerBullets) {
                            if (GameUtil.isCrossing(playerBullet.getRectangle(),
                                    enemiesBullet.getRectangle())) {
                                GameParameter.toTalScore += GameParameter.BULLET_HURT_SCORE;//加分
                                playerBullet.setAlive(false);// 玩家子弹死亡
                                enemiesBullet.setAlive(false);// 敌人子弹死亡
                            }
                        }
                    }
                    // 敌人子弹与玩家魔法子弹碰撞
                    if (!playerMagicBullets.isEmpty() && enemiesBullet.isAlive()) {
                        for (MagicBullet playerMagicBullet : playerMagicBullets) {
                            if (GameUtil.isCrossing(playerMagicBullet.getRectangle(),
                                    enemiesBullet.getRectangle())) {
                                GameParameter.toTalScore += GameParameter.BULLET_HURT_SCORE;// 加分
                                playerMagicBullet.setAlive(false);// 玩家子弹死亡
                                enemiesBullet.setAlive(false);// 敌人子弹死亡
                            }
                        }
                    }
                }
            }
        }
        // 魔法物品碰撞
        if (!magics.isEmpty()) {
            for (Magic magic : magics) {
                if (magic.isAlive()) {
                    if (GameUtil.isCrossing(magic.getRectangle(), player.getRectangle())) {
                        if (magic.getType() == MagicType.HEALTH) {
                            player.hurt(-GameParameter.MAGIC_HEALTH_POINT);//加生命值
                        } else {
                            player.bulletAdd(GameParameter.MAGIC_BULLET_POINT);//加子弹数目
                        }
                        magic.setAlive(false);
                    }
                }
            }
        }
    }

    public void deathRemove() {
        //玩家死亡设置
        if (player.getHealth() <= 0) player.setAlive(false);

        // 死亡玩家子弹移除
        if (!playerBullets.isEmpty()) {
            for (int i = 0; i < playerBullets.size(); i++) {
                if (!playerBullets.get(i).isAlive()) {
                    playerBullets.remove(i);
                }
            }
        }
        // 死亡玩家魔法子弹移除
        if (!playerMagicBullets.isEmpty()) {
            for (int i = 0; i < playerMagicBullets.size(); i++) {
                if (!playerMagicBullets.get(i).isAlive()) {
                    playerMagicBullets.remove(i);
                }
            }
        }
        // 死亡敌人移除
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isAlive()) {
                    enemies.remove(i);
                }
            }
        }
        // 死亡敌人子弹移除
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (!enemiesBullets.get(i).isAlive()) {
                    enemiesBullets.remove(i);
                }
            }
        }
        // 死亡魔法物品移除
        if (!magics.isEmpty()) {
            for (int i = 0; i < magics.size(); i++) {
                if (!magics.get(i).isAlive()) {
                    magics.remove(i);
                }
            }
        }
    }

    public boolean gameEndDetect() {
        if (!player.isAlive() || player.getHealth() <= 0) {
            GameParameter.suspendFlag = false;
            return true;
        }
        return false;
    }

    public boolean nextLevelDetect() {
        return GameParameter.toTalScore >= GameParameter.TARGET_SCORE[GameParameter.currentLevel];
    }
}
