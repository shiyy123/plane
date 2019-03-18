package aircraftbattle.stuff;

import java.io.InputStream;


import javazoom.jl.player.advanced.AdvancedPlayer;

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
            // ��ȡ��Ƶ�ļ���
            InputStream mp3 = Music.class.getClassLoader().getResourceAsStream("music/" + mp3Url);

            try {
                // ����������
                AdvancedPlayer adv = new AdvancedPlayer(mp3);

                // ����
                adv.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isLoop);
    }
}
