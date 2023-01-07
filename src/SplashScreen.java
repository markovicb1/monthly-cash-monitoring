package notesPotrosnje;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SplashScreen extends JFrame {
    public static final int WINDOW_WIDTH = 740;
    public static final int WINDOW_HEIGHT = 435;

    private JLabel backgroundImage;

    public SplashScreen() {
        super();
        setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WINDOW_WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setUndecorated(true);
//        backgroundImage = new JLabel();
//        backgroundImage.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
//        URL splashImage = getClass().getClassLoader().getResource("splashImage.png");
//        backgroundImage.setIcon(new ImageIcon(splashImage));
    }
}
