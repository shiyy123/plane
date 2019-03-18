package aircraftbattle.game;

import aircraftbattle.stuff.Detection;
import aircraftbattle.stuff.EnemyThread;
import aircraftbattle.stuff.PlayerThread;
import aircraftbattle.util.GameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameFrame extends JFrame {

    private JPanel startPanel, helpPanel, choosePanel, goPanel, endPanel;// 5����������
    private GamingPanel gamingPanel;// ��Ϸ����

    private GameService service;// ��Ϸ������
    private CardLayout cardLayout;// ���Ʋ���

    public GameService getService() {
        return service;
    }

    private GameFrame getInstance() {
        return this;
    }

    public GameFrame() {
        super();
        initialize();
    }

    private void initialize() {
        // ��Ϸ���������������
        this.setTitle("�ɻ���ս");
        this.setSize(GameParameter.FRAME_WIDTH, GameParameter.FRAME_HIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        // ������Ϸ������
        service = new GameService();

        // ����6����ͬ��JPanel
        StartPanelBuild(this);
        HelpPanelBuild(this);
        ChoosePanelBuild(this);
        GamingPanelBuild(this, 0);
        GoPanelBuild(this);
        EndPanelBuild(this);

        // ��Ӽ��̼�����
        setFocusable(true);// ����Ϊ��������Ӧ�����¼�
        addKeyListener(new KeyAdapter() {
            // ���̰���
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        GameParameter.isW = true;
                        break;
                    case KeyEvent.VK_S:
                        GameParameter.isS = true;
                        break;
                    case KeyEvent.VK_A:
                        GameParameter.isA = true;
                        break;
                    case KeyEvent.VK_D:
                        GameParameter.isD = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        GameParameter.isSpace = true;
                        break;
                }
            }

            // �����ɿ�
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        GameParameter.isW = false;
                        break;
                    case KeyEvent.VK_S:
                        GameParameter.isS = false;
                        break;
                    case KeyEvent.VK_A:
                        GameParameter.isA = false;
                        break;
                    case KeyEvent.VK_D:
                        GameParameter.isD = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        GameParameter.isSpace = false;
                        break;
                }
            }

        });
    }

    //---------------------------------------------------------------------------
    private void StartPanelBuild(GameFrame gameFrame) {
        // ������������Panel
        startPanel = GameUtil.getBackgroundJPanel();

        // ����Panel����
        JLabel title = GameUtil.getIconSizeJLabel("img\\start1.png", 100, 100);
        JButton button1 = GameUtil.getIconSizeJButton("img\\start2.png", 100, 650);
        button1.addActionListener(e -> CardChange("choose"));
        JButton button2 = GameUtil.getIconSizeJButton("img\\start3.png", 450, 650);
        button2.addActionListener(e -> CardChange("help"));

        // �����ݼ���Panel
        startPanel.add(button1);
        startPanel.add(button2);
        startPanel.add(title);

        // ��Panel����Frame
        gameFrame.add(startPanel, "start");
    }

    private void HelpPanelBuild(GameFrame gameFrame) {
        helpPanel = GameUtil.getBackgroundJPanel();

        JLabel label1 = GameUtil.getIconSizeJLabel("img\\help1.png", 300, 50);
        JLabel label2 = GameUtil.getIconSizeJLabel("img\\help2.png", 100, 180);
        JButton button1 = GameUtil.getIconSizeJButton("img\\help3.png", 100, 850);
        button1.addActionListener(e -> CardChange("start"));

        helpPanel.add(label1);
        helpPanel.add(label2);
        helpPanel.add(button1);
        gameFrame.add(helpPanel, "help");
    }

    private void ChoosePanelBuild(GameFrame gameFrame) {
        choosePanel = GameUtil.getBackgroundJPanel();

        JLabel label1 = GameUtil.getIconSizeJLabel("img\\choose1.png", 80, 80);
        JButton startButton = GameUtil.getIconSizeJButton("img\\choose2.png", 470, 800);
        startButton.addActionListener(e -> {
            // �л�����һ������ǰ������ҷɻ�����
            try {
                service.getPlayer().changeImage(GameParameter.isWisdom);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            GameParameter.suspendFlag = true;// �߳�ѭ������

            // start collision detection; player move, fight; enemy move, fight threads
            new Detection(getInstance()).start();
            new PlayerThread(getInstance()).start();
            new EnemyThread(getInstance()).start();

            CardChange("gaming");
        });
        // ѡ��ս��
        JLabel chooseFrame = GameUtil.getIconSizeJLabel("img\\Frame.png", 50, 323);// ��ʼ��ѡ���
        JButton one = GameUtil.getIconSizeJButton("img\\ChoosePlayer1.png", 80, 350);
        one.addActionListener(e -> {
            chooseFrame.setLocation(50, 323);
            GameParameter.isWisdom = false;// ѡ��������ս��
        });
        JButton two = GameUtil.getIconSizeJButton("img\\ChoosePlayer2.png", 421, 350);
        two.addActionListener(e -> {
            chooseFrame.setLocation(390, 323);
            GameParameter.isWisdom = true;// ѡ���ǻ���ս��
        });

        choosePanel.add(label1);
        choosePanel.add(startButton);
        choosePanel.add(one);
        choosePanel.add(two);
        choosePanel.add(chooseFrame);
        gameFrame.add(choosePanel, "choose");

    }

    private void GamingPanelBuild(GameFrame gameFrame, int level) {
        gamingPanel = new GamingPanel();

        // TestButton������������������������������������������������������������������������������������������������������������������������
//        JButton go = TestButton(40, 40, 0, 0);
//        go.setText("G");
//        go.addActionListener(e -> CardChange("go"));
//        gamingPanel.add(go);
//
//        JButton end = TestButton(40, 40, 100, 0);
//        end.setText("E");
//        end.addActionListener(e -> CardChange("end"));
//        gamingPanel.add(end);
        // ��������������������������������������������������������������������������������������������������������������������������������������������

        gameFrame.add(gamingPanel, "gaming");
    }

    private void GoPanelBuild(GameFrame gameFrame) {
        goPanel = GameUtil.getBackgroundJPanel();

        JLabel label1 = GameUtil.getIconSizeJLabel("img\\Go1.png", 100, 100);
        JButton button1 = GameUtil.getIconSizeJButton("img\\Go2.png", 400, 720);
        button1.addActionListener(e -> {
            if (GameParameter.currentLevel == 4)
                System.exit(0);
            service.clear();// ����
            GamingPanelBuild(gameFrame, ++GameParameter.currentLevel);// ������һ��
            CardChange("gaming");
        });

        goPanel.add(label1);
        goPanel.add(button1);
        gameFrame.add(goPanel, "go");
    }

    private void EndPanelBuild(GameFrame gameFrame) {
        endPanel = GameUtil.getBackgroundJPanel();

        JLabel label1 = GameUtil.getIconSizeJLabel("img\\End1.png", 100, 100);
        JButton button1 = GameUtil.getIconSizeJButton("img\\End2.png", 100, 720);
        JButton button2 = GameUtil.getIconSizeJButton("img\\End3.png", 450, 720);

        // ������Ϸ
        button1.addActionListener(e -> System.exit(0));
        // ���¿�ʼ
        button2.addActionListener(e -> {
            GameParameter.suspendFlag = false;// �߳�ѭ��������
            GameParameter.toTalScore = GameParameter.START_SCORE;// ���¼Ʒ�
            GameParameter.currentLevel = GameParameter.START_LEVEL;// �¹ؿ�
            GamingPanelBuild(gameFrame, GameParameter.START_LEVEL);// ������ʼ�ؿ�
            service.clear();// ����

            CardChange("choose");
        });

        endPanel.add(button1);
        endPanel.add(button2);
        endPanel.add(label1);
        gameFrame.add(endPanel, "end");
    }
    //---------------------------------------------------------------------------

    private class GamingPanel extends JPanel {
        JLabel scoreLabel, targetLabel, bulletLabel, healthLabel;// ������ǩ

        public GamingPanel() {
            super();
            initialize();

            // �������ս��
            try {
                service.generatePlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void initialize() {
            setLayout(null);
            setBackground(Color.white);
            setSize(GameParameter.FRAME_WIDTH, GameParameter.FRAME_HIGHT);

            // �÷�
            JLabel label1 = GameUtil.getIconSizeJLabel("img\\gaming1.png", 50, 50);

            // Ŀ�����
            JLabel label2 = GameUtil.getIconSizeJLabel("img\\gaming2.png", 620, 50);

            // ʣ���ӵ�
            JLabel label3 = GameUtil.getIconSizeJLabel("img\\gaming3.png", 50, 860);

            // ʣ��Ѫ��
            JLabel label4 = GameUtil.getIconSizeJLabel("img\\gaming4.png", 620, 860);

            scoreLabel = GameUtil.getNumJLabel(180, 55);
            targetLabel = GameUtil.getNumJLabel(500, 55);
            bulletLabel = GameUtil.getNumJLabel(180, 860);
            healthLabel = GameUtil.getNumJLabel(520, 860);

            targetLabel.setForeground(Color.blue);
            scoreLabel.setForeground(Color.green);
            healthLabel.setForeground(Color.red);

            // ��ʼ����ǩ��ֵ
            scoreLabel.setText(String.valueOf(GameParameter.START_SCORE));
            // Ŀ��������Źؿ���߱��
            targetLabel.setText(String.valueOf(GameParameter.TARGET_SCORE[GameParameter.currentLevel]));
            // ��ʼʣ���ӵ������Źؿ���߱��
            bulletLabel.setText(String.valueOf(GameParameter.START_BULLETS[GameParameter.currentLevel]));
            healthLabel.setText(String.valueOf(GameParameter.START_HEALTH));

            add(label1);
            add(label2);
            add(label3);
            add(label4);

            add(scoreLabel);
            add(targetLabel);
            add(bulletLabel);
            add(healthLabel);

        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            paintLabels();// ��ǩ��ֵ�仯

            try {
                service.paint(g);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void paintLabels() {
            bulletLabel.setText(String.valueOf(service.getPlayer().getBulletsNum()));
            healthLabel.setText(String.valueOf(service.getPlayer().getHealth()));
            scoreLabel.setText(String.valueOf(GameParameter.toTalScore));
        }
    }

    public void CardChange(String name) {
        cardLayout.show(getContentPane(), name);
    }

    //---------------------------------------------------------------------------
    public JButton TestButton(int W, int H, int X, int Y) {
        JButton button = new JButton();
        button.setSize(W, H);
        button.setLocation(X, Y);
        return button;
    }
}
