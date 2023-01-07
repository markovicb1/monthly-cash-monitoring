package notesPotrosnje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class OAplikaciji extends JDialog {
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 300;
    private JPanel left;
    private JPanel right;

    private ResourceBundle langSet;

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

        JLabel naslov = new JLabel(langSet.getString("AboutLabelName"));
        naslov.setFont(new Font("Times New Roman",Font.BOLD,13));
        naslov.setBounds(5 * WINDOW_WIDTH / 8 - 112, 10, 270, 20);

        JLabel verzija = new JLabel(langSet.getString("Version"));
        verzija.setFont(new Font("Times New Roman",Font.ITALIC,13));
        verzija.setBounds(WINDOW_WIDTH / 3 + 5,70,150,20);

        JLabel datum = new JLabel(langSet.getString("Production"));
        datum.setFont(new Font("Times New Roman",Font.ITALIC,13));
        datum.setBounds(WINDOW_WIDTH / 3 + 5,110,180,20);

        JLabel autor = new JLabel(langSet.getString("Author"));
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

    public OAplikaciji(KorisnickoOkruzenje parent){
        //super(parent, "О Апликацији",ModalityType.APPLICATION_MODAL);
        this.langSet = parent.langSet;
        setTitle(langSet.getString("About"));
        setModalityType(ModalityType.APPLICATION_MODAL);
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
