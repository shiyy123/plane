package aircraftbattle.game;

import javax.swing.*;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class GameMain {
    public static void main(String[] args) {
        GameFrame newGame = new GameFrame();
        newGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        newGame.setVisible(true);
    }
}
