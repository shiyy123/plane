package aircraftbattle.game;

import javax.swing.*;

/**
 * Entry of the program
 */
public class GameMain {
    public static void main(String[] args) {
        GameFrame newGame = new GameFrame();
        newGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        newGame.setVisible(true);
    }
}
