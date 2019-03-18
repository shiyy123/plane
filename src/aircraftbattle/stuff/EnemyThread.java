package aircraftbattle.stuff;

import java.io.IOException;

import aircraftbattle.game.GameFrame;
import aircraftbattle.game.GameParameter;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class EnemyThread extends Thread {
	
	private GameFrame gameFrame;
	
	public  EnemyThread(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}
	
	public void run() {
		while (GameParameter.suspendFlag) {
			try {
				//�Զ������ɵ����Լ�������Ϊ
				gameFrame.getService().generateEnemy();
				gameFrame.getService().enemyBulletsGenerate();

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
