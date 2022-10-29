package notesPotrosnje;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class KorisnickoOkruzenje extends JFrame {

    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 335;
    public static final String PRIHOD_FLAG = "Prihod card";
    public static final String RASHOD_FLAG = "Rashod card";
    public static final String AKTIVNOST_FLAG = "Aktivnost card";
    public static final String ISTORIJA_FLAG = "Istorija card";
    private static final int userID = 1;
    private static final String dbURL = "jdbc:postgresql://localhost:5432/app_db";
    private static final String dbUsername = "postgres";
    private static final String dbPassword = "postgresbogdan";

    private JPanel side;
    private JPanel container;
    private JPanel prihod;
    private JPanel rashod;
    private JPanel aktivnost;
    private JPanel istorija;
    private JButton noviPrihod;
    private JButton noviRashod;
    private JButton dodajAktivnost;
    private JButton vidiIstoriju;
    private CardLayout cardLayout;
    private JLabel labelMenu;
    private JLabel prihodNaslov;
    private JLabel prihodTip;
    private JLabel prihodKolicina;
    private JTextField prihodKolicinaNovcaTf;
    private JLabel prihodDatumLb;
    private JComboBox prihodCB;
    private JTextField prihodGodina;
    private JTextField prihodMesec;
    private JTextField prihodDan;
    private JButton prihodDanasBtn;
    private JButton prihodUnesiBtn;

    private JLabel rashodNaslov;
    private JLabel rashodTip;
    private JLabel rashodKolicina;
    private JTextField rashodKolicinaNovcaTf;
    private JLabel rashodDatumLb;
    private JComboBox rashodCB;
    private JTextField rashodGodina;
    private JTextField rashodMesec;
    private JTextField rashodDan;
    private JButton rashodDanasBtn;
    private JButton rashodUnesiBtn;
    private JLabel aktivnostNaslov;
    private JLabel aktivnostIme;
    private JTextField aktivnostImeTf;
    private JLabel aktivnostTip;
    private JRadioButton aktivnostTipPrihod;
    private JRadioButton aktivnostTipRashod;
    private ButtonGroup aktivnostRBGrupa;
    private JButton aktivnostUnesi;
    private JLabel istorijaNaslov;
    private JLabel istorijaDatum;
    private JTextField istorijaDatumGodina;
    private JTextField istorijaDatumMesec;
    private JTable istorijaTabela;
    private JScrollPane istorijaScroll;
    private JLabel istorijaSaldo;
    private JTextField istorijaSaldoTf;
    private String[][] tableData;
    private JButton istorijaGenerisi;
    private JButton istorijaObrisi;
    private DefaultTableModel defaultTableModel;

    private void showSidePanel() {
        side = new JPanel(null);
        side.setBounds(0, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT);
        side.setBackground(new Color(125, 22, 250));
        labelMenu = new JLabel("MENI");
        labelMenu.setBounds(WINDOW_WIDTH / 8 - 20, 25, 50, 10);
        labelMenu.setFont(new Font("Times New Roman", Font.BOLD, 15));
        labelMenu.setForeground(new Color(199, 211, 212));
        add(side);
    }

    private void populatePrihodPanel() {
        prihod = new JPanel(null);
        prihod.setBackground(new Color(199, 211, 212));
        prihodNaslov = new JLabel("Нови приход");
        prihodNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        prihodNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 50, 10, 100, 20);

        prihodTip = new JLabel("Тип прихода");
        prihodTip.setFont(new Font("Times New Roman", Font.BOLD, 14));
        prihodTip.setBounds(WINDOW_WIDTH / 4 + 15, 70, 80, 20);

        //initialy filling ComboBox data
        String[] data = {};
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            System.out.println("Connected to database.");
            String sql = "SELECT COUNT(name) FROM \"Activities\" WHERE type = '+';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                data = new String[resultSet.getInt(1)];
            }
            resultSet.close();
            statement.close();
            sql = "SELECT name FROM \"Activities\" WHERE type = '+';";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                data[i++] = resultSet.getString(1);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("FATAL Error: " + e.getMessage());
            dispose();
        }
        prihodCB = new JComboBox(data);
        prihodCB.setEditable(false);
        //prihodCB.setSelectedIndex(0);
        prihodCB.setFont(new Font("Times New Roman", Font.ITALIC, 13));
        prihodCB.setBounds(5 * WINDOW_WIDTH / 8 - 75, 70, 150, 20);

        prihodKolicina = new JLabel("Количина новца");
        prihodKolicina.setFont(new Font("Times New Roman", Font.BOLD, 14));
        prihodKolicina.setBounds(WINDOW_WIDTH / 4 + 15, 120, 110, 20);

        prihodKolicinaNovcaTf = new JTextField();
        prihodKolicinaNovcaTf.setBounds(5 * WINDOW_WIDTH / 8 - 75, 120, 50, 20); //do 6 cifara
        prihodKolicinaNovcaTf.setHorizontalAlignment(0);

        prihodDatumLb = new JLabel("Датум: YYYY-MM-DD");
        prihodDatumLb.setFont(new Font("Times New Roman", Font.BOLD, 14));
        prihodDatumLb.setBounds(WINDOW_WIDTH / 4 + 15, 170, 145, 20);

        prihodGodina = new JTextField();
        prihodGodina.setBounds(5 * WINDOW_WIDTH / 8 - 75, 170, 50, 20);
        prihodGodina.setHorizontalAlignment(0);
        prihodMesec = new JTextField();
        prihodMesec.setBounds(5 * WINDOW_WIDTH / 8 - 75 + 50, 170, 50, 20);
        prihodMesec.setHorizontalAlignment(0);
        prihodDan = new JTextField();
        prihodDan.setBounds(5 * WINDOW_WIDTH / 8 - 75 + 50 + 50, 170, 50, 20);
        prihodDan.setHorizontalAlignment(0);
        prihodDanasBtn = new JButton("Данашњи датум");
        prihodDanasBtn.setBounds(3 * WINDOW_WIDTH / 4, 170, 130, 20);
        prihodDanasBtn.addActionListener(al -> {
            LocalDate now = LocalDate.now();
            prihodGodina.setText(String.valueOf(now.getYear()));
            prihodMesec.setText(String.valueOf(now.getMonthValue()));
            prihodDan.setText(String.valueOf(now.getDayOfMonth()));
        });

        prihodUnesiBtn = new JButton("Унеси приход");
        prihodUnesiBtn.setBounds(WINDOW_WIDTH / 4 + 15, WINDOW_HEIGHT - 80, 120, 20);
        prihodUnesiBtn.addActionListener(al -> {
            try {
                if (prihodKolicinaNovcaTf.getText().isBlank() || prihodGodina.getText().isBlank() || prihodMesec.getText().isBlank() || prihodDan.getText().isBlank()) {
                    throw new GreskaPraznaPolja();
                }
                String dateString = prihodGodina.getText() + "-" + prihodMesec.getText() + "-" + prihodDan.getText();
                java.sql.Date dateSQL = java.sql.Date.valueOf(dateString);
                //SQL
                try {
                    Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                    System.out.println("Connected to database.");
                    String item = prihodCB.getSelectedItem().toString();
                    String sql = "SELECT id FROM \"Activities\" WHERE name = '" + item + "'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    int id = -1;
                    while (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                    resultSet.close();
                    statement.close();

                    sql = "INSERT INTO \"Transactions\"(id_activity, amount, date, id_user) " +
                            "VALUES (" + String.valueOf(id) + ", " + prihodKolicinaNovcaTf.getText() + ", DATE'" + dateSQL +
                            "', " + String.valueOf(userID) + ");";
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    System.out.println("Updated DB");
                    connection.close();
                    defaultTableModel.setRowCount(0);
                    istorijaSaldoTf.setText("");
                    //JOptionPane.showMessageDialog(null, "Podaci uneseni");
                    prihodGodina.setText("");
                    prihodMesec.setText("");
                    prihodDan.setText("");
                    prihodKolicinaNovcaTf.setText("");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            } catch (GreskaPraznaPolja e) {
                JOptionPane.showMessageDialog(null, "Унети све вредности!");
            }
        });

        prihod.add(prihodNaslov);
        prihod.add(prihodTip);
        prihod.add(prihodCB);
        prihod.add(prihodKolicina);
        prihod.add(prihodKolicinaNovcaTf);
        prihod.add(prihodDatumLb);
        prihod.add(prihodGodina);
        prihod.add(prihodMesec);
        prihod.add(prihodDan);
        prihod.add(prihodDanasBtn);
        prihod.add(prihodUnesiBtn);
    }

    private void populateRashodPanel() {
        rashod = new JPanel(null);
        rashod.setBackground(new Color(199, 211, 212));
        rashodNaslov = new JLabel("Нови расход");
        rashodNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rashodNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 50, 10, 100, 20);

        rashodTip = new JLabel("Тип расхода");
        rashodTip.setFont(new Font("Times New Roman", Font.BOLD, 14));
        rashodTip.setBounds(WINDOW_WIDTH / 4 + 15, 70, 80, 20);

        //initialy filling ComboBox data
        String[] data = {};
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            System.out.println("Connected to database.");
            String sql = "SELECT COUNT(name) FROM \"Activities\" WHERE type = '-';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                data = new String[resultSet.getInt(1)];
            }
            resultSet.close();
            statement.close();
            sql = "SELECT name FROM \"Activities\" WHERE type = '-';";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                data[i++] = resultSet.getString(1);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("FATAL Error: " + e.getMessage());
            dispose();
        }
        rashodCB = new JComboBox(data);
        rashodCB.setEditable(false);
        //prihodCB.setSelectedIndex(0);
        rashodCB.setFont(new Font("Times New Roman", Font.ITALIC, 13));
        rashodCB.setBounds(5 * WINDOW_WIDTH / 8 - 75, 70, 150, 20);

        rashodKolicina = new JLabel("Количина новца");
        rashodKolicina.setFont(new Font("Times New Roman", Font.BOLD, 14));
        rashodKolicina.setBounds(WINDOW_WIDTH / 4 + 15, 120, 110, 20);

        rashodKolicinaNovcaTf = new JTextField();
        rashodKolicinaNovcaTf.setBounds(5 * WINDOW_WIDTH / 8 - 75, 120, 50, 20); //do 6 cifara
        rashodKolicinaNovcaTf.setHorizontalAlignment(0);

        rashodDatumLb = new JLabel("Датум: YYYY-MM-DD");
        rashodDatumLb.setFont(new Font("Times New Roman", Font.BOLD, 14));
        rashodDatumLb.setBounds(WINDOW_WIDTH / 4 + 15, 170, 145, 20);

        rashodGodina = new JTextField();
        rashodGodina.setBounds(5 * WINDOW_WIDTH / 8 - 75, 170, 50, 20);
        rashodGodina.setHorizontalAlignment(0);
        rashodMesec = new JTextField();
        rashodMesec.setBounds(5 * WINDOW_WIDTH / 8 - 75 + 50, 170, 50, 20);
        rashodMesec.setHorizontalAlignment(0);
        rashodDan = new JTextField();
        rashodDan.setBounds(5 * WINDOW_WIDTH / 8 - 75 + 50 + 50, 170, 50, 20);
        rashodDan.setHorizontalAlignment(0);
        rashodDanasBtn = new JButton("Данашњи датум");
        rashodDanasBtn.setBounds(3 * WINDOW_WIDTH / 4, 170, 130, 20);
        rashodDanasBtn.addActionListener(al -> {
            LocalDate now = LocalDate.now();
            rashodGodina.setText(String.valueOf(now.getYear()));
            rashodMesec.setText(String.valueOf(now.getMonthValue()));
            rashodDan.setText(String.valueOf(now.getDayOfMonth()));
        });

        rashodUnesiBtn = new JButton("Унеси расход");
        rashodUnesiBtn.setBounds(WINDOW_WIDTH / 4 + 15, WINDOW_HEIGHT - 80, 120, 20);
        rashodUnesiBtn.addActionListener(al -> {
            try {
                if (rashodKolicinaNovcaTf.getText().isBlank() || rashodGodina.getText().isBlank() || rashodMesec.getText().isBlank() || rashodDan.getText().isBlank()) {
                    throw new GreskaPraznaPolja();
                }
                String dateString = rashodGodina.getText() + "-" + rashodMesec.getText() + "-" + rashodDan.getText();
                java.sql.Date dateSQL = java.sql.Date.valueOf(dateString);
                //SQL
                try {
                    Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                    System.out.println("Connected to database.");
                    String item = rashodCB.getSelectedItem().toString();
                    String sql = "SELECT id FROM \"Activities\" WHERE name = '" + item + "'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    int id = -1;
                    while (resultSet.next()) {
                        id = resultSet.getInt(1);
                    }
                    resultSet.close();
                    statement.close();

                    sql = "INSERT INTO \"Transactions\"(id_activity, amount, date, id_user) " +
                            "VALUES (" + String.valueOf(id) + ", " + "-" + rashodKolicinaNovcaTf.getText() + ", DATE'" + dateSQL +
                            "', " + String.valueOf(userID) + ");";
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    System.out.println("Updated DB");
                    connection.close();
                    defaultTableModel.setRowCount(0);
                    istorijaSaldoTf.setText("");
                    //JOptionPane.showMessageDialog(null, "Podaci uneseni");
                    rashodGodina.setText("");
                    rashodMesec.setText("");
                    rashodDan.setText("");
                    rashodKolicinaNovcaTf.setText("");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            } catch (GreskaPraznaPolja e) {
                JOptionPane.showMessageDialog(null, "Унети сва поља!");
            }
        });

        rashod.add(rashodNaslov);
        rashod.add(rashodTip);
        rashod.add(rashodCB);
        rashod.add(rashodKolicina);
        rashod.add(rashodKolicinaNovcaTf);
        rashod.add(rashodDatumLb);
        rashod.add(rashodGodina);
        rashod.add(rashodMesec);
        rashod.add(rashodDan);
        rashod.add(rashodDanasBtn);
        rashod.add(rashodUnesiBtn);
    }

    private void populateAktivnostPanel() {
        aktivnost = new JPanel(null);
        aktivnost.setBackground(new Color(199, 211, 212));

        aktivnostNaslov = new JLabel("Нова активност");
        aktivnostNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        aktivnostNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 60, 10, 120, 20);

        aktivnostIme = new JLabel("Име активности");
        aktivnostIme.setFont(new Font("Times New Roman", Font.BOLD, 14));
        aktivnostIme.setBounds(WINDOW_WIDTH / 4 + 15, 110, 110, 20);

        aktivnostImeTf = new JTextField();
        aktivnostImeTf.setBounds(5 * WINDOW_WIDTH / 8 - 85, 110, 150, 20);
        aktivnostImeTf.setHorizontalAlignment(JTextField.CENTER);

        aktivnostTip = new JLabel("Тип активности");
        aktivnostTip.setFont(new Font("Times New Roman", Font.BOLD, 14));
        aktivnostTip.setBounds(WINDOW_WIDTH / 4 + 15, 160, 120, 20);

        aktivnostTipPrihod = new JRadioButton("Приход");
        aktivnostTipPrihod.setBounds(5 * WINDOW_WIDTH / 8 - 85, 160, 70, 20);
        aktivnostTipRashod = new JRadioButton("Расход");
        aktivnostTipRashod.setBounds(5 * WINDOW_WIDTH / 8 - 85 + 85, 160, 70, 20);
        aktivnostRBGrupa = new ButtonGroup();
        aktivnostRBGrupa.add(aktivnostTipPrihod);
        aktivnostRBGrupa.add(aktivnostTipRashod);

        aktivnostUnesi = new JButton("Унеси активност");
        aktivnostUnesi.setBounds(WINDOW_WIDTH / 4 + 15, WINDOW_HEIGHT - 80, 140, 20);
        aktivnostUnesi.addActionListener(al -> {
            try {
                if (aktivnostImeTf.getText().isBlank() || (!aktivnostTipPrihod.isSelected() && !aktivnostTipRashod.isSelected()))
                    throw new GreskaPraznaPolja();
                char sign = '+';
                if (aktivnostTipRashod.isSelected())
                    sign = '-';
                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                Statement statement = connection.createStatement();
                String sql = "INSERT INTO \"Activities\" (name, type)" +
                        " VALUES ('" + aktivnostImeTf.getText() + "', '" + sign + "');";
                statement.executeUpdate(sql);
                System.out.println("Updated DB");
                if (sign == '+') {
                    prihodCB.addItem(aktivnostImeTf.getText());
                } else {
                    rashodCB.addItem(aktivnostImeTf.getText());
                }
                connection.close();
                //JOptionPane.showMessageDialog(null, "Podaci uneseni");
                aktivnostImeTf.setText("");
            } catch (GreskaPraznaPolja e) {
                JOptionPane.showMessageDialog(null, "Унети сва поља!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        aktivnost.add(aktivnostNaslov);
        aktivnost.add(aktivnostIme);
        aktivnost.add(aktivnostImeTf);
        aktivnost.add(aktivnostTip);
        aktivnost.add(aktivnostTipPrihod);
        aktivnost.add(aktivnostTipRashod);
        aktivnost.add(aktivnostUnesi);
    }

    private void generisiTabelu(){
        try {
            if (istorijaDatumMesec.getText().isBlank() || istorijaDatumGodina.getText().isBlank())
                throw new GreskaPraznaPolja();
            String startDate = "DATE '" + istorijaDatumGodina.getText() + "-" + istorijaDatumMesec.getText() + "-01'";
            int mesec = Integer.valueOf(istorijaDatumMesec.getText());
            mesec = (mesec == 12 ? mesec = 1 : mesec + 1);
            String mesecStr = "";
            if (mesec < 10)
                mesecStr = "0" + String.valueOf(mesec);
            else
                mesecStr = String.valueOf(mesec);

            String endDate = "DATE '" + istorijaDatumGodina.getText() + "-" + mesecStr + "-01'";
            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                Statement statement = connection.createStatement();
                String sql = "SELECT \"Activities\".name, \"Transactions\".amount, \"Transactions\".date\n" +
                        "FROM \"Transactions\"\n" +
                        "JOIN \"Activities\" ON \"Transactions\".id_activity = \"Activities\".id\n" +
                        "WHERE \"Transactions\".date >= " + startDate + " AND \"Transactions\".date < " + endDate + "\n" +
                        "ORDER BY \"Transactions\".date ASC;";
                ResultSet resultSet = statement.executeQuery(sql);
                int i = 0, j = 0;
                while (resultSet.next()) {
                    String imeAktivnosti = resultSet.getString(1);
                    String kolicina = resultSet.getString(2);
                    String datum = resultSet.getString(3);
                    Vector<Object> data = new Vector<Object>();
                    data.add(imeAktivnosti);
                    data.add(kolicina);
                    data.add(datum);
                    defaultTableModel.addRow(data);
                }
                resultSet.close();
                statement.close();

                statement = connection.createStatement();
                sql = "SELECT SUM(amount) FROM \"Transactions\"\n" +
                        "WHERE date >= " + startDate + " AND date < " + endDate + ";";
                int amount = 0;
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    amount = resultSet.getInt(1);
                }
                istorijaSaldoTf.setText(String.valueOf(amount) + "дин");
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } catch (GreskaPraznaPolja e) {
            JOptionPane.showMessageDialog(null, "Унети сва поља!");
        }
    }
    private void populateIstorijaPanel() {
        istorija = new JPanel(null);
        istorija.setBackground(new Color(199, 211, 212));

        istorijaNaslov = new JLabel("Историја трансакција");
        istorijaNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        istorijaNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 80, 10, 160, 20);

        istorijaDatum = new JLabel("Датум YYYY-MM");
        istorijaDatum.setFont(new Font("Times New Roman", Font.BOLD, 14));
        istorijaDatum.setBounds(WINDOW_WIDTH / 4 + 15, 40, 120, 20);

        istorijaDatumGodina = new JTextField();
        istorijaDatumGodina.setBounds(WINDOW_WIDTH / 4 + 15 + 155, 40, 50, 20);
        istorijaDatumGodina.setHorizontalAlignment(0);

        istorijaDatumMesec = new JTextField();
        istorijaDatumMesec.setBounds(WINDOW_WIDTH / 4 + 15 + 125 + 108, 40, 50, 20);
        istorijaDatumMesec.setHorizontalAlignment(0);

        istorijaGenerisi = new JButton("Генериши");
        istorijaGenerisi.setBounds(WINDOW_WIDTH - 120, 40, 90, 20);

        istorijaTabela = new JTable();
        istorijaTabela.setBounds(WINDOW_WIDTH / 4 + 15, 75, 3 * WINDOW_WIDTH / 4 - 45, 160);
        defaultTableModel = new DefaultTableModel(0, 0);
        String[] header = {"Активност", "Количина новца", "Датум"};
        defaultTableModel.setColumnIdentifiers(header);
        istorijaTabela.setModel(defaultTableModel);
        istorijaScroll = new JScrollPane(istorijaTabela);
        istorijaScroll.setBounds(WINDOW_WIDTH / 4 + 15, 75, 3 * WINDOW_WIDTH / 4 - 45, 160);

        istorijaSaldo = new JLabel("Салдо за унети месец");
        istorijaSaldo.setBounds(WINDOW_WIDTH / 4 + 15, 255, 150, 20);

        istorijaSaldoTf = new JTextField();
        istorijaSaldoTf.setBounds(WINDOW_WIDTH / 4 + 15 + 150, 255, 80, 20);
        istorijaSaldoTf.setEditable(false);
        istorijaSaldoTf.setHorizontalAlignment(0);

        istorijaObrisi = new JButton("Обриши");
        istorijaObrisi.setBounds(WINDOW_WIDTH - 120, 255, 90, 20);
        istorijaObrisi.addActionListener(al -> {
            istorijaDatumMesec.setText("");
            istorijaDatumGodina.setText("");
            defaultTableModel.setRowCount(0);
            istorijaSaldoTf.setText("");
        });

        istorijaDatumMesec.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(!istorijaDatumGodina.getText().isBlank() || !istorijaDatumMesec.getText().isBlank())
                        defaultTableModel.setRowCount(0);
                    generisiTabelu();
                }
            }
        });

        istorijaGenerisi.addActionListener(al -> {
            if(!istorijaDatumGodina.getText().isBlank() || !istorijaDatumMesec.getText().isBlank())
                defaultTableModel.setRowCount(0);
            generisiTabelu();
        });

        istorija.add(istorijaNaslov);
        istorija.add(istorijaDatum);
        istorija.add(istorijaDatumGodina);
        istorija.add(istorijaDatumMesec);
        istorija.add(istorijaGenerisi);
        istorija.add(istorijaScroll);
        istorija.add(istorijaSaldo);
        istorija.add(istorijaSaldoTf);
        istorija.add(istorijaObrisi);
    }

    public KorisnickoOkruzenje() {
        super("Нотес потрошње");
        //default settings for gui
        setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WINDOW_WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        URL imageURL = getClass().getClassLoader().getResource("b.png");
        super.setIconImage(Toolkit.getDefaultToolkit().getImage(imageURL));

        //configuring cards
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        container.setBounds(WINDOW_WIDTH / 4, 0, 3 * WINDOW_WIDTH / 4, WINDOW_HEIGHT);

        //configuring prihod card
        populatePrihodPanel();
        //configuring rashod card
        populateRashodPanel();
        //configuring aktivnost card
        populateAktivnostPanel();
        //configuring istorija card
        populateIstorijaPanel();

        container.add(prihod, PRIHOD_FLAG);
        container.add(rashod, RASHOD_FLAG);
        container.add(aktivnost, AKTIVNOST_FLAG);
        container.add(istorija, ISTORIJA_FLAG);

        //configuring side panel
        side = new JPanel(null);
        side.setBounds(0, 0, WINDOW_WIDTH / 4, WINDOW_HEIGHT);
        side.setBackground(new Color(96, 63, 131));
        labelMenu = new JLabel("MENI");
        labelMenu.setBounds(WINDOW_WIDTH / 8 - 20, 25, 50, 10);
        labelMenu.setFont(new Font("Times New Roman", Font.BOLD, 15));
        labelMenu.setForeground(Color.WHITE);

        noviPrihod = new JButton("Нови приход");
        noviPrihod.setBounds(WINDOW_WIDTH / 8 - 65, 60, 130, 30);
        noviPrihod.addActionListener(al -> {
            cardLayout.show(container, PRIHOD_FLAG);
            showSidePanel();
        });

        noviRashod = new JButton("Нови расход");
        noviRashod.setBounds(WINDOW_WIDTH / 8 - 65, 110, 130, 30);
        noviRashod.addActionListener(al -> {
            cardLayout.show(container, RASHOD_FLAG);
            showSidePanel();
        });

        dodajAktivnost = new JButton("Додај активност");
        dodajAktivnost.setBounds(WINDOW_WIDTH / 8 - 65, 160, 130, 30);
        dodajAktivnost.addActionListener(al -> {
            cardLayout.show(container, AKTIVNOST_FLAG);
            showSidePanel();
        });

        vidiIstoriju = new JButton("Историја");
        vidiIstoriju.setBounds(WINDOW_WIDTH / 8 - 65, 210, 130, 30);
        vidiIstoriju.addActionListener(al -> {
            cardLayout.show(container, ISTORIJA_FLAG);
            showSidePanel();
        });

        //side.add(labelMenu);
        side.add(noviPrihod);
        side.add(noviRashod);
        side.add(dodajAktivnost);
        side.add(vidiIstoriju);

        add(side);
        add(container);
        setVisible(true);
        revalidate();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
