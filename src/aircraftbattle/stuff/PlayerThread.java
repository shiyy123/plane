package aircraftbattle.stuff;

import java.io.IOException;

import aircraftbattle.game.GameFrame;
import aircraftbattle.game.GameParameter;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class PlayerThread extends Thread {
	
	private GameFrame gameFrame;
	
	public PlayerThread(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}
	
	public void run() {
		while (GameParameter.suspendFlag) {
			try {
				//�����Ϊ�Լ���һ���߳�
				gameFrame.getService().playerBulletsGenerate();
				gameFrame.getService().playerMagicBulletsGenerate();
				gameFrame.getService().playerMove();
				gameFrame.getService().playerBulletsMove();
				gameFrame.getService().playerMagicBulletsMove();
				gameFrame.getService().playerSkill1Generate();
				gameFrame.getService().playerSkill2Generate();
							
				// �ػ�
				gameFrame.repaint();

				// ��ͣ
				sleep(10);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
 }
}
