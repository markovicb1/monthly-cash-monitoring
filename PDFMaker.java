package notesPotrosnje;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.net.URL;

public class PDFMaker extends JDialog {
    public static final int WINDOW_WIDTH = 320;
    public static final int WINDOW_HEIGHT = 175;

    private JPanel panel;
    private JLabel godina;
    private JTextField godinaTf;
    private JButton generisi;
    private JFileChooser chooser;
    private JButton chooseDir;
    private void populate(){
        panel = new JPanel(null);

        godina = new JLabel("Година");
        godina.setBounds(50,30,80,10);

        godinaTf = new JTextField();
        godinaTf.setHorizontalAlignment(0);
        godinaTf.setBounds(50,50,40,20);

        generisi = new JButton("Генериши ПДФ");
        generisi.setBounds(140,50,120,20);
        generisi.addActionListener(al->{
            System.out.println("Majmune");
        });

        //TREBA DODATI LABEL KOJI IMA PUNU PUTANJU DOTLE, DA BI KORISNIK BIO SIGURAN DA JE TO TO
        //TEK NA DNU JE DUGME GENERISI KOJE POTOM GASI JDIALOG DOK SE U POZADINI PRAVI PDF KOLKO GOD DA MU TREBA

        panel.add(godinaTf);
        panel.add(generisi);
        panel.add(godina);

        add(panel);
    }
    public PDFMaker(JFrame parent){
        super(parent, "Годишњи ПДФ",ModalityType.APPLICATION_MODAL);

        //default settings for gui
        setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WINDOW_WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        populate();
        setResizable(false);
        URL imageURL = getClass().getClassLoader().getResource("b.png");
        super.setIconImage(Toolkit.getDefaultToolkit().getImage(imageURL));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
