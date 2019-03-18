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

    private Player player;// ���
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
        playerBullets = new ArrayList<Bullet>();
        // �����б�
        enemies = new ArrayList<Enemy>();
        // �����ӵ��б�
        enemiesBullets = new ArrayList<Bullet>();
        // �����б�
        magics = new ArrayList<Magic>();
        // ��ը��Ч�б�
        explosions = new ArrayList<Explosion>();

        // ��������
        new Music("background.mp3", true).start();
    }

    void paint(Graphics g) throws IOException {
        // �����
        if (player != null)
            player.draw(g);
        // ������ӵ�
        if (!playerBullets.isEmpty()) {
            for (int i = 0; i < playerBullets.size(); i++) {
                if (playerBullets.get(i).isAlive())
                    playerBullets.get(i).draw(g);
            }
        }
        // ������
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i).isAlive())
                    enemies.get(i).draw(g);
            }
        }
        // �������ӵ�
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (enemiesBullets.get(i).isAlive())
                    enemiesBullets.get(i).draw(g);
            }
        }
        // ��ħ����Ʒ
        if (!magics.isEmpty()) {
            for (int i = 0; i < magics.size(); i++) {
                if (magics.get(i).isAlive())
                    magics.get(i).draw(g);
            }
        }
        // ����ըЧ��
        if (!explosions.isEmpty()) {
            for (int i = 0; i < explosions.size(); i++) {
                if (explosions.get(i).isAlive())
                    explosions.get(i).draw(g);
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
        if (player.isAlive())
            player.move();
    }

    public void generateEnemy() throws IOException {
        // ���һ��ֻ��һ��BOSS
        if (GameParameter.currentLevel == 4) {
            if (enemies.isEmpty())
                enemies.add(new Boss("img\\Boss.png"));
            return;
        }
        // �����ؿ�
        if (enemies.isEmpty() || enemies.get(enemies.size() - 1).getY() > 200)// ��һ�������ƶ�����200
            enemies.add(new Enemy(GameUtil.getLevelEnemyPath(GameParameter.currentLevel), GameParameter.currentLevel));
    }

    public void othersMove() {
        //�����ƶ�
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i).isAlive())
                    enemies.get(i).move();
            }
        }
        //�����ӵ��ƶ�
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (enemiesBullets.get(i).isAlive())
                    enemiesBullets.get(i).move();
            }
        }
        //ħ����Ʒ�ƶ�
        if (!magics.isEmpty()) {
            for (int i = 0; i < magics.size(); i++) {
                if (magics.get(i).isAlive())
                    magics.get(i).move();
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
        if(GameParameter.isJ && player.getMagicBulletNum()!=0){
            GameParameter.isJ=false;

            MagicBullet playerMagicBullet=player.magicAttack();
            // TODO do

        }
    }

    public void playerBulletsMove() {
        if (!playerBullets.isEmpty()) {
            for (int i = 0; i < playerBullets.size(); i++) {
                if (playerBullets.get(i).isAlive())
                    playerBullets.get(i).move();
            }
        }
    }

    public void enemyBulletsGenerate() throws IOException {
        for (int i = 0; i < enemies.size(); i++) {
            boolean flag = false;// �ܷ����ӵ�

            if (enemies.get(i).getBulletsNum() != 0) {// �����ӵ�
                if (GameParameter.currentLevel == 4)// BOSS���޷����ӵ�
                    flag = true;
                else if (enemies.get(i).getCurrentBullet() == null)// ��һ���ӵ�
                    flag = true;
                else { // ���ǵ�һ���ӵ�
                    if (enemies.get(i).getCurrentBullet().getY() > GameUtil.getRandomNum(500, 600))// ǰһ���ӵ�Y����һ�������
                        flag = true;
                }
            }
            // ���Է����ӵ�
            if (flag) {
                Bullet enemyBullet = enemies.get(i).directionalAttack(player.getX(), player.getY());
                enemies.get(i).setCurrentBullet(enemyBullet);
                enemiesBullets.add(enemyBullet);
            }
        }

    }

    public void collisionDetect() throws IOException {
        // ������ײ
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i).isAlive()) {
                    // �����������ײ
                    if (GameUtil.isCrossing(enemies.get(i).getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.AIRCRAFT_HURT_POINT);// ����ܵ��˺�
                        enemies.get(i).hurt(GameParameter.AIRCRAFT_HURT_POINT);// �����ܵ��˺�
                    }
                    // ����������ӵ���ײ
                    if (!playerBullets.isEmpty()) {
                        for (int j = 0; j < playerBullets.size(); j++) {
                            if (GameUtil.isCrossing(playerBullets.get(j).getRectangle(),
                                    enemies.get(i).getRectangle())) {
                                playerBullets.get(j).setAlive(false);// ����ӵ�����
                                enemies.get(i).hurt(GameParameter.BULLET_HURT_POINT);// �����ܵ��˺�
                            }
                        }
                    }
                    // �жϵ����Ƿ�����
                    if (enemies.get(i).getHealth() <= 0) {
                        enemies.get(i).setAlive(false);// ��������
                        GameParameter.toTalScore += GameParameter.ENEMY_HURT_SCORE;// �ӷ�
                        // �Ƿ����ħ����Ʒ
                        if (GameUtil.isMagicFall()) {
                            Magic magic = new Magic(enemies.get(i).getX(), enemies.get(i).getX(),
                                    GameUtil.getRandomMagicType());
                            magics.add(magic);
                        }
                        // ���ű�ըЧ��
                        Explosion explosion = new Explosion(enemies.get(i).getX(), enemies.get(i).getY());
                        explosions.add(explosion);
                    }
                }
            }
        }
        // �����ӵ���ײ
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (enemiesBullets.get(i).isAlive()) {
                    // �����ӵ��������ײ
                    if (GameUtil.isCrossing(enemiesBullets.get(i).getRectangle(), player.getRectangle())) {
                        player.hurt(GameParameter.BULLET_HURT_POINT);// ����ܵ��˺�
                        enemiesBullets.get(i).setAlive(false);// �����ӵ�����
                    }
                    // �����ӵ�������ӵ���ײ
                    if (!playerBullets.isEmpty() && enemiesBullets.get(i).isAlive()) {
                        for (int j = 0; j < playerBullets.size(); j++) {
                            if (GameUtil.isCrossing(playerBullets.get(j).getRectangle(),
                                    enemiesBullets.get(i).getRectangle())) {
                                GameParameter.toTalScore += GameParameter.BULLET_HURT_SCORE;//�ӷ�
                                playerBullets.get(j).setAlive(false);// ����ӵ�����
                                enemiesBullets.get(i).setAlive(false);// �����ӵ�����
                            }
                        }
                    }
                }
            }
        }
        // ħ����Ʒ��ײ
        if (!magics.isEmpty()) {
            for (int i = 0; i < magics.size(); i++) {
                if (magics.get(i).isAlive()) {
                    if (GameUtil.isCrossing(magics.get(i).getRectangle(), player.getRectangle())) {
                        if (magics.get(i).getType() == MagicType.HEALTH)
                            player.hurt(-GameParameter.MAGIC_HEALTH_POINT);//������ֵ
                        else
                            player.bulletAdd(GameParameter.MAGIC_BULLET_POINT);//���ӵ���Ŀ
                        magics.get(i).setAlive(false);
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
                if (!playerBullets.get(i).isAlive())
                    playerBullets.remove(i);
            }
        }
        // ���������Ƴ�
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isAlive())
                    enemies.remove(i);
            }
        }
        // ���������ӵ��Ƴ�
        if (!enemiesBullets.isEmpty()) {
            for (int i = 0; i < enemiesBullets.size(); i++) {
                if (!enemiesBullets.get(i).isAlive())
                    enemiesBullets.remove(i);
            }
        }
        // ����ħ����Ʒ�Ƴ�
        if (!magics.isEmpty()) {
            for (int i = 0; i < magics.size(); i++) {
                if (!magics.get(i).isAlive())
                    magics.remove(i);
            }
        }
    }

    public boolean gameEndDetecte() {
        if (player.isAlive() == false || player.getHealth() <= 0) {
            GameParameter.suspendFlag = false;
            return true;
        }
        return false;
    }

    public boolean nextLevelDetecte() {
        if (GameParameter.toTalScore >= GameParameter.TARGET_SCORE[GameParameter.currentLevel])
            return true;
        return false;
    }
}
