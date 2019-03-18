package aircraftbattle.stuff;

import aircraftbattle.game.GameFrame;
import aircraftbattle.game.GameParameter;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class Detection extends Thread {

	private GameFrame gameFrame;
	
	public Detection(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}
	
	public void run() {
		while (GameParameter.suspendFlag) {
			try {
				gameFrame.getService().collisionDetect();// 所有物体碰撞检测
				gameFrame.getService().deathRemove(); //移除死亡物体(移除和移动必须在一个线程里)
				
				gameFrame.getService().othersMove();//移动
			
				if (gameFrame.getService().gameEndDetecte())
					gameFrame.CardChange("end");
				if(gameFrame.getService().nextLevelDetecte())
					gameFrame.CardChange("go");
					
				//暂停
				sleep(10);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
