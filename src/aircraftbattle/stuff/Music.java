package aircraftbattle.stuff;

import java.io.InputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * @author cary
 * @date 2019/3/18 16:43
 */
public class Music extends Thread {

    private String mp3Url;

    private boolean isLoop;

    public Music(String mp3Url, boolean isLoop) {
        super();
        this.mp3Url = mp3Url;
        this.isLoop = isLoop;
    }

    public void run() {

        do {
            // 读取音频文件流
            InputStream mp3 = Music.class.getClassLoader().getResourceAsStream("music/" + mp3Url);

            try {
                // 创建播放器
                AdvancedPlayer adv = new AdvancedPlayer(mp3);

                // 播放
                adv.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isLoop);
    }
}
