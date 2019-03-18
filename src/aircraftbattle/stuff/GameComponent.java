package aircraftbattle.stuff;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;


public abstract class GameComponent {

	protected int x;// ������
	protected int y;// ������
	protected int speedX;// �����ٶ�
	protected int speedY;// �����ٶ�
	protected Image image;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(String path) throws IOException {
		this.image = ImageIO.read(GameComponent.class.getClassLoader().getResourceAsStream(path));	}

	public GameComponent(int x, int y, int speedX, int speedY, String path) throws IOException {
		super();
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.image = ImageIO.read(GameComponent.class.getClassLoader().getResourceAsStream(path));
		//ֻ�����������У���ɾȥAircraftBattleĿ¼�µ�img�ļ���
		
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, null);
	}

	public Rectangle getRectangle() {
		return new Rectangle(x,y,image.getWidth(null),image.getHeight(null));
	}
	
	public abstract void move();
	
	
}
