package notesPotrosnje;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OAplikaciji extends JFrame {
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 300;
    private JPanel left;
    private JPanel right;

    private JPanel populateLeftPanel() {
        left = new JPanel(null);
        left.setBounds(0, 0, WINDOW_WIDTH / 3, WINDOW_HEIGHT);
        left.setBackground(Color.WHITE);

        URL imageURL = getClass().getClassLoader().getResource("smallB.png");
        JLabel picture = new JLabel();
        picture.setIcon(new ImageIcon(imageURL));
        picture.setBounds(6,WINDOW_HEIGHT/2 - 90,120,120);
        JLabel label = new JLabel("Circled B-all the way");
        label.setFont(new Font("Consolas", Font.BOLD, 11));
        label.setBounds(4,WINDOW_HEIGHT/2 - 90 + 123,150,20);
        label.setForeground(new Color(162, 65, 204));
        left.add(picture);
        left.add(label);
        return left;
    }

    private JPanel populateRightPanel(){
        right = new JPanel(null);
        right.setBounds(WINDOW_WIDTH / 3, 0, 2*WINDOW_WIDTH / 3, WINDOW_HEIGHT);
        right.setBackground(new Color(199, 211, 212));

        JLabel naslov = new JLabel("Нотес потрошње - пратите своје финансије");
        naslov.setFont(new Font("Times New Roman",Font.BOLD,13));
        naslov.setBounds(5 * WINDOW_WIDTH / 8 - 112, 10, 270, 20);

        JLabel verzija = new JLabel("Верзија            v1.2");
        verzija.setFont(new Font("Times New Roman",Font.ITALIC,13));
        verzija.setBounds(WINDOW_WIDTH / 3 + 5,70,150,20);

        JLabel datum = new JLabel("Производња           11.2022.");
        datum.setFont(new Font("Times New Roman",Font.ITALIC,13));
        datum.setBounds(WINDOW_WIDTH / 3 + 5,110,180,20);

        JLabel autor = new JLabel("Направио и развио \u00a9 Богдан Circled B");
        autor.setFont(new Font("Times New Roman",Font.BOLD,13));
        autor.setForeground(Color.BLUE);
        autor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI("https://github.com/markovicb1/"));
                } catch (IOException e2) {
                    JOptionPane.showMessageDialog(null,"Can't reach site.");
                } catch (URISyntaxException e3) {
                    JOptionPane.showMessageDialog(null,"Can't reach site.");
                }
            }
        });
        autor.setBounds(WINDOW_WIDTH / 3 + 5,150,250,20);
        autor.setToolTipText("Click for contact");

        right.add(naslov);
        right.add(verzija);
        right.add(datum);
        right.add(autor);
        return right;
    }

    public OAplikaciji(){
        super("О Апликацији");
        //default settings for gui
        setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WINDOW_WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        URL imageURL = getClass().getClassLoader().getResource("b.png");
        super.setIconImage(Toolkit.getDefaultToolkit().getImage(imageURL));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(populateLeftPanel());
        add(populateRightPanel());
        setVisible(true);
    }

}
