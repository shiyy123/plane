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

    public static Player player;// ���
    private ArrayList<Bullet> playerBullets;// ����ӵ�

    private ArrayList<MagicBullet> playerMagicBullets;//ħ���ӵ�

    private ArrayList<Enemy> enemies;// �����б�
    private ArrayList<Bullet> enemiesBullets;// �����ӵ��б�

    private ArrayList<Magic> magics;// ������Ʒ�б�
    private ArrayList<Explosion> explosions;// ��ըЧ���б�

    public Player getPlayer() {
        return player;
    }

    public GameService() {
        super();
        GameParameter.toTalScore = GameParameter.START_SCORE;// ��ʼ�Ʒ�
        // ����ӵ��б�
        playerBullets = new ArrayList<>();
        // ħ���ӵ��б�
        playerMagicBullets = new ArrayList<>();
        // �����б�
        enemies = new ArrayList<>();
        // �����ӵ��б�
        enemiesBullets = new ArrayList<>();
        // �����б�
        magics = new ArrayList<>();
        // ��ը��Ч�б�
        explosions = new ArrayList<>();

        // ��������
        new Music("background.mp3", true).start();
    }

    void paint(Graphics g) throws IOException {
        // �����
        if (player != null)
            player.draw(g);
        // ������ӵ�
        if (!playerBullets.isEmpty()) {
            for (Bullet playerBullet : playerBullets) {
                if (playerBullet.isAlive()) {
                    playerBullet.draw(g);
                }
            }
        }
        // ��ħ���ӵ�
        if (!playerMagicBullets.isEmpty()) {
            for (MagicBullet playerMagicBullet : playerMagicBullets) {
                if (playerMagicBullet.isAlive()) {
                    playerMagicBullet.draw(g);
                }
            }
        }
        // ������
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive())
                    enemy.draw(g);
            }
        }
        // �������ӵ�
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    enemiesBullet.draw(g);
                }
            }
        }
        // ��ħ����Ʒ
        if (!magics.isEmpty()) {
            for (Magic magic : magics) {
                if (magic.isAlive()) {
                    magic.draw(g);
                }
            }
        }
        // ����ըЧ��
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
        // ���һ��ֻ��һ��BOSS
        if (GameParameter.currentLevel == 4) {
            if (enemies.isEmpty()) {
                enemies.add(new Boss("img\\Boss.png"));
                return;
            }
        }
        // �����ؿ�
        if (enemies.isEmpty() || enemies.get(enemies.size() - 1).getY() > 200) {// ��һ�������ƶ�����200
            enemies.add(new Enemy(GameUtil.getLevelEnemyPath(GameParameter.currentLevel), GameParameter.currentLevel));
        }
    }

    public void othersMove() {
        //�����ƶ�
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    enemy.move();
                }
            }
        }
        //�����ӵ��ƶ�
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    enemiesBullet.move();
                }
            }
        }
        //ħ����Ʒ�ƶ�
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
            GameParameter.isSpace = false;// �����������������ӵ�

            Bullet playerBullet = player.attack();
            playerBullets.add(playerBullet);
            // �ӵ�������Ч
            new Music("fire.mp3", false).start();
        }
    }

    public void playerMagicBulletsGenerate() throws IOException {
        if (GameParameter.isJ && player.getMagicBulletNum() != 0) {
            GameParameter.isJ = false;

            MagicBullet playerMagicBullet = player.magicAttack();
            playerMagicBullets.add(playerMagicBullet);
            // �ӵ�������Ч
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
            boolean flag = false;// �ܷ����ӵ�

            if (enemy.getBulletsNum() != 0) {// �����ӵ�
                if (GameParameter.currentLevel == 4) {// BOSS���޷����ӵ�
                    flag = true;
                } else if (enemy.getCurrentBullet() == null) {// ��һ���ӵ�
                    flag = true;
                } else { // ���ǵ�һ���ӵ�
                    if (enemy.getCurrentBullet().getY() > GameUtil.getRandomNum(500, 600)) {// ǰһ���ӵ�Y����һ�������
                        flag = true;
                    }
                }
            }
            // ���Է����ӵ�
            if (flag) {
                Bullet enemyBullet = enemy.directionalAttack(player.getX(), player.getY());
                enemy.setCurrentBullet(enemyBullet);
                enemiesBullets.add(enemyBullet);
            }
        }

    }

    public void collisionDetect() throws IOException {
        // ������ײ
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    // �����������ײ
                    if (GameUtil.isCrossing(enemy.getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.AIRCRAFT_HURT_POINT);// ����ܵ��˺�
                        enemy.hurt(GameParameter.AIRCRAFT_HURT_POINT);// �����ܵ��˺�
                    }
                    // ����������ӵ���ײ
                    if (!playerBullets.isEmpty()) {
                        for (Bullet playerBullet : playerBullets) {
                            if (GameUtil.isCrossing(playerBullet.getRectangle(),
                                    enemy.getRectangle())) {
                                playerBullet.setAlive(false);// ����ӵ�����
                                enemy.hurt(GameParameter.BULLET_HURT_POINT);// �����ܵ��˺�
                            }
                        }
                    }
                    //���������ħ���ӵ���ײ
                    if (!playerMagicBullets.isEmpty()) {
                        for (MagicBullet playerMagicBullet : playerMagicBullets) {
                            if (GameUtil.isCrossing(playerMagicBullet.getRectangle(),
                                    enemy.getRectangle())) {
                                playerMagicBullet.setAlive(false);// ���ħ���ӵ�����
                                enemy.hurt(GameParameter.MAGIC_BULLET_HURT_POINT);
                            }
                        }
                    }
                    // �жϵ����Ƿ�����
                    if (enemy.getHealth() <= 0) {
                        enemy.setAlive(false);// ��������
                        GameParameter.toTalScore += GameParameter.ENEMY_HURT_SCORE;// �ӷ�
                        // �Ƿ����ħ����Ʒ
                        if (GameUtil.isMagicFall()) {
                            Magic magic = new Magic(enemy.getX(), enemy.getX(),
                                    GameUtil.getRandomMagicType());
                            magics.add(magic);
                        }
                        // ���ű�ըЧ��
                        Explosion explosion = new Explosion(enemy.getX(), enemy.getY());
                        explosions.add(explosion);
                    }
                }
            }
        }
        // �����ӵ���ײ
        if (!enemiesBullets.isEmpty()) {
            for (Bullet enemiesBullet : enemiesBullets) {
                if (enemiesBullet.isAlive()) {
                    // �����ӵ��������ײ
                    if (GameUtil.isCrossing(enemiesBullet.getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.BULLET_HURT_POINT);// ����ܵ��˺�
                        enemiesBullet.setAlive(false);// �����ӵ�����
                    }
                    // �����ӵ�������ӵ���ײ
                    if (!playerBullets.isEmpty() && enemiesBullet.isAlive()) {
                        for (Bullet playerBullet : playerBullets) {
                            if (GameUtil.isCrossing(playerBullet.getRectangle(),
                                    enemiesBullet.getRectangle())) {
                                GameParameter.toTalScore += GameParameter.BULLET_HURT_SCORE;//�ӷ�
                                playerBullet.setAlive(false);// ����ӵ�����
                                enemiesBullet.setAlive(false);// �����ӵ�����
                            }
                        }
                    }
                    // �����ӵ������ħ���ӵ���ײ
                    if (!playerMagicBullets.isEmpty() && enemiesBullet.isAlive()) {
                        for (MagicBullet playerMagicBullet : playerMagicBullets) {
                            if (GameUtil.isCrossing(playerMagicBullet.getRectangle(),
                                    enemiesBullet.getRectangle())) {
                                GameParameter.toTalScore += GameParameter.BULLET_HURT_SCORE;// �ӷ�
                                playerMagicBullet.setAlive(false);// ����ӵ�����
                                enemiesBullet.setAlive(false);// �����ӵ�����
                            }
                        }
                    }
                }
            }
        }
        // ħ����Ʒ��ײ
        if (!magics.isEmpty()) {
            for (Magic magic : magics) {
                if (magic.isAlive()) {
                    if (GameUtil.isCrossing(magic.getRectangle(), player.getRectangle())) {
                        if (magic.getType() == MagicType.HEALTH) {
                            player.hurt(-GameParameter.MAGIC_HEALTH_POINT);//������ֵ
                        } else {
                            player.bulletAdd(GameParameter.MAGIC_BULLET_POINT);//���ӵ���Ŀ
                        }
                        magic.setAlive(false);
                    }
                }
            }
        }
    }

    public void deathRemove() {
        //�����������
        if (player.getHealth() <= 0) player.setAlive(false);

        // ��������ӵ��Ƴ�
        if (!playerBullets.isEmpty()) {
            for (int i = 0; i < playerBullets.size(); i++) {
                if (!playerBullets.get(i).isAlive()) {
                    playerBullets.remove(i);
                }
            }
        }
        // �������ħ���ӵ��Ƴ�
        if (!playerMagicBullets.isEmpty()) {
            for (int i = 0; i < playerMagicBullets.size(); i++) {
                if (!playerMagicBullets.get(i).isAlive()) {
                    playerMagicBullets.remove(i);
                }
            }
        }
        // ���������Ƴ�
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isAlive()) {
                    enemies.remove(i);
                }
            }
        }
        // ���������ӵ��Ƴ�
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (!enemiesBullets.get(i).isAlive()) {
                    enemiesBullets.remove(i);
                }
            }
        }
        // ����ħ����Ʒ�Ƴ�
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
