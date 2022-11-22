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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    public static final String IZMENI_FLAG = "Izmeni aktivnost card";
    public static final String IZMENI_TRANS_FLAG = "Izmeni transakciju card";
    public static final String GENERISI_FLAG = "Generisi godisnji izvestaj card";
    public static final String HELP_FLAG = "Help card";
    public static final String ABOUT_FLAG = "O aplikaciji card";
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
    private JPanel izmeniAktivnostCard;
    private JPanel izmeniTransakcijuCard;
    private JPanel generisiGodisnjiCard;
    private JPanel helpCard;
    private JPanel oAplikacijiCard;
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

    private JMenuBar menuBar;
    private JMenu podaci;
    private JMenu pomoc;
    private JMenuItem izmeniAktivnost;
    private JMenuItem izmeniTransakciju;
    private JMenuItem generisiPDF;
    private JMenuItem kakoKoristiti;
    private JMenuItem oAplikaciji;
    private JLabel izmeniАktivnostNaslov;
    private JLabel izmeniAktivnostNovoIme;
    private JTextField izmeniAktivnostNovoImeTf;
    private JComboBox izmeniAktivnostCB;
    private JLabel izmeniAktivnostIzaberi;
    private JButton izmeniAktivnostBtn;

    private JLabel izmeniTransakcijuNaslov;
    private JLabel izmeniTransakcijuDatumLb;
    private JTextField izmeniTransakcijuGodinaTf;
    private JTextField izmeniTransakcijuMesecTf;
    private JTextField izmeniTransakcijuDanTf;
    private DefaultListModel listModel;
    private JButton izmeniTransakcijuGenerisiTransakcijeBtn;
    private JList izmeniTransakcijuLista;
    private JLabel izmeniTransakcijuNovacLb;
    private JTextField izmeniTransakcijuNovacTf;
    private JButton izmeniTransakcijuUpisiBtn;
    private JScrollPane izmeniTransakcijuScroll;
    private int[] idTransakcija;

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

    private String[] updatePrihodCB() {
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
            sql = "SELECT name FROM \"Activities\" WHERE type = '+' ORDER BY id;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                data[i++] = resultSet.getString(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return data;

        } catch (SQLException e) {
            System.out.println("FATAL Error: " + e.getMessage());
            return null;
        }
    }

    private String[] updateRashodCB() {
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
            sql = "SELECT name FROM \"Activities\" WHERE type = '-' ORDER BY id;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                data[i++] = resultSet.getString(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return data;

        } catch (SQLException e) {
            System.out.println("FATAL Error: " + e.getMessage());
            return null;
        }
    }

    private String[] updateIzmeniAktivnost() {
        String[] data = {};
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            System.out.println("Connected to database.");
            String sql = "SELECT COUNT(name) FROM \"Activities\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                data = new String[resultSet.getInt(1)];
            }
            resultSet.close();
            statement.close();
            sql = "SELECT name FROM \"Activities\" ORDER BY id;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            int i = 0;
            while (resultSet.next()) {
                data[i++] = resultSet.getString(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return data;

        } catch (SQLException e) {
            System.out.println("FATAL Error: " + e.getMessage());
            return null;
        }
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
            sql = "SELECT name FROM \"Activities\" WHERE type = '+' ORDER BY id;";
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
                    izmeniAktivnostCB.removeAllItems();
                    String[] newIzmeniData = updateIzmeniAktivnost();
                    for (String s : newIzmeniData)
                        izmeniAktivnostCB.addItem(s);
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
                    izmeniAktivnostCB.removeAllItems();
                    String[] newIzmeniData = updateRashodCB();
                    for (String s : newIzmeniData)
                        izmeniAktivnostCB.addItem(s);
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
                String[] newIzmeniData = updateIzmeniAktivnost();

                izmeniAktivnostCB.removeAllItems();
                for (String s : newIzmeniData)
                    izmeniAktivnostCB.addItem(s);
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

    private void generisiTabelu() {
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!istorijaDatumGodina.getText().isBlank() || !istorijaDatumMesec.getText().isBlank())
                        defaultTableModel.setRowCount(0);
                    generisiTabelu();
                }
            }
        });

        istorijaGenerisi.addActionListener(al -> {
            if (!istorijaDatumGodina.getText().isBlank() || !istorijaDatumMesec.getText().isBlank())
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

    public void populateIzmeniAktivnostPanel() {
        izmeniAktivnostCard = new JPanel(null);
        izmeniAktivnostCard.setBackground(new Color(199, 211, 212));

        izmeniАktivnostNaslov = new JLabel("Измени активност");
        izmeniАktivnostNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        izmeniАktivnostNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 60, 10, 150, 20);

        izmeniAktivnostIzaberi = new JLabel("Изабери активност");
        izmeniAktivnostIzaberi.setFont(new Font("Times New Roman", Font.BOLD, 14));
        izmeniAktivnostIzaberi.setBounds(WINDOW_WIDTH / 4 + 15, 110, 150, 20);

        String[] data = {};
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            System.out.println("Connected to database.");
            String sql = "SELECT COUNT(name) FROM \"Activities\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                data = new String[resultSet.getInt(1)];
            }
            resultSet.close();
            statement.close();
            sql = "SELECT name FROM \"Activities\" ORDER BY id;";
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
        izmeniAktivnostCB = new JComboBox(data);
        izmeniAktivnostCB.setEditable(false);
        izmeniAktivnostCB.setFont(new Font("Times New Roman", Font.ITALIC, 13));
        izmeniAktivnostCB.setBounds(WINDOW_WIDTH / 4 + 15 + 160, 110, 110, 20);

        izmeniAktivnostNovoIme = new JLabel("Ново име активности");
        izmeniAktivnostNovoIme.setFont(new Font("Times New Roman", Font.BOLD, 14));
        izmeniAktivnostNovoIme.setBounds(WINDOW_WIDTH / 4 + 15, 150, 150, 20);

        izmeniAktivnostNovoImeTf = new JTextField();
        izmeniAktivnostNovoImeTf.setBounds(WINDOW_WIDTH / 4 + 15 + 160, 150, 110, 20);
        izmeniAktivnostNovoImeTf.setHorizontalAlignment(0);

        izmeniAktivnostBtn = new JButton("Ажурирај");
        izmeniAktivnostBtn.setBounds(WINDOW_WIDTH / 4 + 15, WINDOW_HEIGHT - 80, 120, 20);
        String[] finalData = data;
        izmeniAktivnostBtn.addActionListener(al -> {
            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                System.out.println("Connected to database.");
                String item = izmeniAktivnostCB.getSelectedItem().toString();
                String sql = "SELECT id FROM \"Activities\" WHERE name = '" + item + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                int id = -1;
                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                resultSet.close();
                statement.close();

                sql = "UPDATE \"Activities\" " +
                        "SET name = '" + izmeniAktivnostNovoImeTf.getText() + "' " +
                        "WHERE id = " + id + ";";
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                System.out.println("Updated DB");
                connection.close();

                String[] finalDataNew = updateIzmeniAktivnost(); //azurirane vrednosti za combobox
                //int ind = izmeniAktivnostCB.getSelectedIndex();
                //finalDataNew[ind] = izmeniAktivnostNovoImeTf.getText(); //OVDE PUCA jer finalData nema novounetu aktivnost
                izmeniAktivnostCB.removeAllItems();
                for (String s : finalDataNew)
                    izmeniAktivnostCB.addItem(s);
                izmeniAktivnostNovoImeTf.setText("");
                defaultTableModel.setRowCount(0);

                String[] newPrihodData = updatePrihodCB();
                String[] newRashodData = updateRashodCB();
                prihodCB.removeAllItems();
                for (String s : newPrihodData)
                    prihodCB.addItem(s);
                rashodCB.removeAllItems();
                for (String s : newRashodData)
                    rashodCB.addItem(s);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        izmeniAktivnostCard.add(izmeniАktivnostNaslov);
        izmeniAktivnostCard.add(izmeniAktivnostIzaberi);
        izmeniAktivnostCard.add(izmeniAktivnostCB);
        izmeniAktivnostCard.add(izmeniAktivnostNovoIme);
        izmeniAktivnostCard.add(izmeniAktivnostNovoImeTf);
        izmeniAktivnostCard.add(izmeniAktivnostBtn);
    }

    private void populateIzmeniTransakcijuPanel() {
        izmeniTransakcijuCard = new JPanel(null);
        izmeniTransakcijuCard.setBackground(new Color(199, 211, 212));

        izmeniTransakcijuNaslov = new JLabel("Промените количину новца за трансакцију");
        izmeniTransakcijuNaslov.setFont(new Font("Times New Roman", Font.BOLD, 16));
        izmeniTransakcijuNaslov.setBounds(5 * WINDOW_WIDTH / 8 - 175, 10, 350, 20);

        //uneti mesec i godinu u kojoj se nalazi zeljena aktivnost ili ceo datum ako se zna na koju se tacno misli
        izmeniTransakcijuDatumLb = new JLabel("Унети датум YYYY-MM + DD за тачност");
        izmeniTransakcijuDatumLb.setBounds(WINDOW_WIDTH / 4 + 7, 83, 280, 10);
        izmeniTransakcijuDatumLb.setFont(new Font("Times New Roman", Font.BOLD, 14));
        izmeniTransakcijuGodinaTf = new JTextField();
        izmeniTransakcijuGodinaTf.setBounds(WINDOW_WIDTH / 4 + 5 + 285, 80, 50, 20);
        izmeniTransakcijuGodinaTf.setHorizontalAlignment(0);
        izmeniTransakcijuMesecTf = new JTextField();
        izmeniTransakcijuMesecTf.setBounds(WINDOW_WIDTH / 4 + 5 + 285 + 55, 80, 50, 20);
        izmeniTransakcijuMesecTf.setHorizontalAlignment(0);
        izmeniTransakcijuDanTf = new JTextField();
        izmeniTransakcijuDanTf.setBounds(WINDOW_WIDTH / 4 + 5 + 285 + 55 + 55, 80, 50, 20);
        izmeniTransakcijuDanTf.setHorizontalAlignment(0);

        listModel = new DefaultListModel<String>();

        izmeniTransakcijuGenerisiTransakcijeBtn = new JButton("Прикажи");
        izmeniTransakcijuGenerisiTransakcijeBtn.setBounds(WINDOW_WIDTH - 120, 110, 90, 20);
        izmeniTransakcijuGenerisiTransakcijeBtn.addActionListener(al -> {
            if (listModel.getSize() > 0)
                listModel.clear();
            try {
                if (izmeniTransakcijuGodinaTf.getText().isBlank() || izmeniTransakcijuMesecTf.getText().isBlank())
                    throw new GreskaPraznaPolja();
                String startDate = "/", endDate = "/", fullDate = "/";
                if (!izmeniTransakcijuDanTf.getText().isBlank()) {
                    fullDate = "DATE '" + izmeniTransakcijuGodinaTf.getText() + "-" + izmeniTransakcijuMesecTf.getText() + "-" + izmeniTransakcijuDanTf.getText() + "'";
                } else {
                    startDate = "DATE '" + izmeniTransakcijuGodinaTf.getText() + "-" + izmeniTransakcijuMesecTf.getText() + "-01'";
                    int mesec = Integer.valueOf(izmeniTransakcijuMesecTf.getText());
                    mesec = (mesec == 12 ? mesec = 1 : mesec + 1);
                    String mesecStr = "";
                    if (mesec < 10)
                        mesecStr = "0" + String.valueOf(mesec);
                    else
                        mesecStr = String.valueOf(mesec);

                    endDate = "DATE '" + izmeniTransakcijuGodinaTf.getText() + "-" + mesecStr + "-01'";
                }

                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                String sql = "";
                if (fullDate == "/") {
                    sql = "SELECT \"Transactions\".id, \"Activities\".name, \"Transactions\".amount, \"Transactions\".date\n" +
                            "FROM \"Transactions\"\n" +
                            "JOIN \"Activities\" ON \"Transactions\".id_activity = \"Activities\".id\n" +
                            "WHERE \"Transactions\".date >= " + startDate + " AND \"Transactions\".date < " + endDate + "\n" +
                            "ORDER BY \"Transactions\".date DESC;";
                } else {
                    sql = "SELECT \"Transactions\".id, \"Activities\".name, \"Transactions\".amount, \"Transactions\".date\n" +
                            "FROM \"Transactions\"\n" +
                            "JOIN \"Activities\" ON \"Transactions\".id_activity = \"Activities\".id\n" +
                            "WHERE \"Transactions\".date = " + fullDate + ";";
                }
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                int cnt = 0; //limit to max 6 results displayed
                idTransakcija = new int[7];
                while (resultSet.next()) {
                    cnt++;
                    if (cnt == 7)
                        break;
                    StringBuilder sb = new StringBuilder();
                    idTransakcija[cnt - 1] = resultSet.getInt(1);
                    sb.append(resultSet.getString(2));
                    sb.append(" ");
                    sb.append(resultSet.getString(3));
                    sb.append("дин ");
                    sb.append(resultSet.getString(4));
                    listModel.addElement(sb.toString());
                }
                resultSet.close();
                statement.close();
                if (listModel.getSize() == 0)
                    JOptionPane.showMessageDialog(null, "Нема резултата претраге.");
                izmeniTransakcijuLista.setModel(listModel);
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, "SQL ERROR: " + se.getMessage());
            } catch (GreskaPraznaPolja e) {
                JOptionPane.showMessageDialog(null, "Унети сва поља.");
            }
        });

        izmeniTransakcijuLista = new JList(listModel);
        izmeniTransakcijuLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        izmeniTransakcijuLista.setLayoutOrientation(JList.VERTICAL);

        izmeniTransakcijuScroll = new JScrollPane(izmeniTransakcijuLista);
        izmeniTransakcijuScroll.setBounds(WINDOW_WIDTH / 4 + 25, 130, 225, 110);

        izmeniTransakcijuNovacLb = new JLabel("Нова количина новца");
        izmeniTransakcijuNovacLb.setFont(new Font("Times New Roman", Font.BOLD, 14));
        izmeniTransakcijuNovacLb.setBounds(WINDOW_WIDTH - 170, 130 + 50, 150, 10); //WINDOW_WIDTH / 4 + izmeniTransakcijuDatumLb.getWidth()
        izmeniTransakcijuNovacTf = new JTextField();
        izmeniTransakcijuNovacTf.setBounds(WINDOW_WIDTH - 80, 205, 50, 20);
        izmeniTransakcijuNovacTf.setHorizontalAlignment(0);

        izmeniTransakcijuUpisiBtn = new JButton("Промени");
        izmeniTransakcijuUpisiBtn.setBounds(WINDOW_WIDTH - 120, 255, 90, 20);
        izmeniTransakcijuUpisiBtn.addActionListener(al->{
           int selected = izmeniTransakcijuLista.getSelectedIndex();
           if(selected == -1)
               JOptionPane.showMessageDialog(null,"Изабрати трансакцију која се мења.");
           if(izmeniTransakcijuNovacTf.getText().isBlank())
               JOptionPane.showMessageDialog(null,"Унети нову количину новца.");
           try{
               //ovaj odeljak je prebacen iz prostora iznad try bloka
               String uzorak = (String)izmeniTransakcijuLista.getSelectedValue();
               String[] delovi = uzorak.split(" ");
               String staraTransakcija = delovi[delovi.length - 2]; //na primer -750din
               char tipTransakcije = staraTransakcija.charAt(0);

               Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
               int novaVrednost = Math.abs(Integer.valueOf(izmeniTransakcijuNovacTf.getText()));
               String sql = "UPDATE \"Transactions\"\n" +
                       "SET amount = " + (tipTransakcije == '-' ? -novaVrednost : novaVrednost) + "\n" +
                       "WHERE id = " + idTransakcija[izmeniTransakcijuLista.getSelectedIndex()] + ";";
               Statement statement = connection.createStatement();
               statement.executeUpdate(sql);
               statement.close();
               connection.close();
               idTransakcija = null;
               listModel.removeAllElements();
               izmeniTransakcijuLista.setModel(listModel);
               izmeniTransakcijuNovacTf.setText("");
               izmeniTransakcijuGodinaTf.setText("");
               izmeniTransakcijuMesecTf.setText("");
               izmeniTransakcijuDanTf.setText("");
               defaultTableModel.setRowCount(0);
           }
           catch (SQLException se){
                JOptionPane.showMessageDialog(null,"SQL ERROR: " + se.getMessage());
           }
           catch (Exception e){
                System.out.println("Error: " + e.getMessage());
           }
        });

        izmeniTransakcijuCard.add(izmeniTransakcijuNaslov);
        izmeniTransakcijuCard.add(izmeniTransakcijuDatumLb);
        izmeniTransakcijuCard.add(izmeniTransakcijuDatumLb);
        izmeniTransakcijuCard.add(izmeniTransakcijuGodinaTf);
        izmeniTransakcijuCard.add(izmeniTransakcijuMesecTf);
        izmeniTransakcijuCard.add(izmeniTransakcijuDanTf);
        izmeniTransakcijuCard.add(izmeniTransakcijuGenerisiTransakcijeBtn);
        izmeniTransakcijuCard.add(izmeniTransakcijuScroll);
        izmeniTransakcijuCard.add(izmeniTransakcijuNovacLb);
        izmeniTransakcijuCard.add(izmeniTransakcijuNovacTf);
        izmeniTransakcijuCard.add(izmeniTransakcijuUpisiBtn);
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
        //configuring izmeniAktivnost card
        populateIzmeniAktivnostPanel();
        //configuring izmeniTransakciju card
        populateIzmeniTransakcijuPanel();

        container.add(prihod, PRIHOD_FLAG);
        container.add(rashod, RASHOD_FLAG);
        container.add(aktivnost, AKTIVNOST_FLAG);
        container.add(istorija, ISTORIJA_FLAG);
        container.add(izmeniAktivnostCard, IZMENI_FLAG);
        container.add(izmeniTransakcijuCard, IZMENI_TRANS_FLAG);

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

        menuBar = new JMenuBar();
        podaci = new JMenu("Подаци");
        podaci.setMnemonic(KeyEvent.VK_P);
        izmeniAktivnost = new JMenuItem("Измени постојећу активност");
        izmeniAktivnost.addActionListener(al -> {
            cardLayout.show(container, IZMENI_FLAG);
            showSidePanel();
        });
        izmeniTransakciju = new JMenuItem("Измени извршену трансакцију");
        izmeniTransakciju.addActionListener(al -> {
            cardLayout.show(container, IZMENI_TRANS_FLAG);
            showSidePanel();
        });
        generisiPDF = new JMenuItem("Генериши годишњи ПДФ");
        generisiPDF.addActionListener(al -> {
            System.out.println("APACHE PDFBOX");
        });
        podaci.add(izmeniAktivnost);
        podaci.add(izmeniTransakciju);
        podaci.add(generisiPDF);
        pomoc = new JMenu("Помоћ");
        kakoKoristiti = new JMenuItem("Како користити апликацију");
        kakoKoristiti.addActionListener(al -> {
            //open site on browser
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://github.com/markovicb1/monthly-cash-monitoring#how-to-use-v101"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Can't reach site.");
            } catch (URISyntaxException e) {
                JOptionPane.showMessageDialog(null, "Can't reach site.");
            }
        });
        oAplikaciji = new JMenuItem("О апликацији");
        oAplikaciji.addActionListener(al -> {
            new OAplikaciji();
        });
        pomoc.add(kakoKoristiti);
        pomoc.add(oAplikaciji);
        menuBar.add(podaci);
        menuBar.add(pomoc);

        add(side);
        add(container);
        setJMenuBar(menuBar);
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
