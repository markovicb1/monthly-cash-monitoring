package notesPotrosnje;

import com.sun.source.doctree.SystemPropertyTree;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class PDFMaker extends JDialog {
    public static final int WINDOW_WIDTH = 320;
    public static final int WINDOW_HEIGHT = 175;

    private static final int userID = 1;
    private static final String dbURL = "jdbc:postgresql://localhost:5432/app_db";
    private static final String dbUsername = "postgres";
    private static final String dbPassword = "postgresbogdan";

    private JPanel panel;
    private JLabel godina;
    private JTextField godinaTf;
    private JButton generisi;
    private JFileChooser chooser;
    private JButton chooseDir;

    private String partPath;

    private File chart = null;

    private void populate() {
        panel = new JPanel(null);

        godina = new JLabel("Година");
        godina.setBounds(50, 30, 80, 10);

        godinaTf = new JTextField();
        godinaTf.setHorizontalAlignment(0);
        godinaTf.setBounds(50, 50, 40, 20);

        generisi = new JButton("Генериши ПДФ");
        generisi.setBounds(140, 50, 120, 20);
        generisi.addActionListener(al -> {
            String startDate = "DATE'" + godinaTf.getText() + "-01-01'";
            String endDate = "DATE'" + godinaTf.getText() + "-12-31'";

            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

                String sql = "select \"Activities\".name, sum(\"Transactions\".amount) as \"Suma\"\n" +
                        "from \"Activities\" inner join \"Transactions\" on \"Activities\".id = \"Transactions\".id_activity\n" +
                        "where \"Transactions\".date between " + startDate + " and " + endDate + " \n" +
                        "group by \"Activities\".name\n" +
                        "order by \"Suma\";";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);

                HashMap<String, Integer> resultPlus = new HashMap<>();
                HashMap<String, Integer> resultMinus = new HashMap<>();
                while (rs.next()) {
                    String activity = rs.getString(1);
                    int sum = rs.getInt(2);
                    if (sum > 0)
                        resultPlus.put(activity, sum);
                    else
                        resultMinus.put(activity, sum);
                }
                connection.close();
                //sada se pravi dokument
                PDDocument document = new PDDocument();
                PDPage page1 = new PDPage();
                document.addPage(page1);
                PDFont font = PDType1Font.TIMES_BOLD_ITALIC;

                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Choose save destination");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int userSelection = fileChooser.showSaveDialog(this);
                    File saveLocation = null;
                    if(userSelection == JFileChooser.APPROVE_OPTION){
                        saveLocation = fileChooser.getSelectedFile();
                    }
                    else{
                        throw new Exception("Choose save destination!");
                    }
                    partPath = saveLocation.getPath();

                    PDPageContentStream contentStream = new PDPageContentStream(document, page1);
                    //title
                    contentStream.beginText();
                    String title = "Finansijksi izveštaj za " + godinaTf.getText() + ". godinu";
                    contentStream.setFont(font, 20);
                    float titleWidth = font.getStringWidth(title) / 1000 * 20;
                    float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 20;
                    float offset = page1.getMediaBox().getHeight() - 70 - titleHeight;
                    contentStream.newLineAtOffset((page1.getMediaBox().getWidth() - titleWidth) / 2, offset);
                    contentStream.showText(title);
                    contentStream.endText();
                    //prihodi title
                    font = PDType1Font.TIMES_BOLD;
                    contentStream.beginText();
                    offset -= 50;
                    contentStream.newLineAtOffset(50, offset);
                    contentStream.setFont(font, 16);
                    int sum = getSum(resultPlus);
                    contentStream.showText("Ukupni prihodi: " + sum + "din");
                    contentStream.endText();
                    //prihodi list
                    font = PDType1Font.TIMES_ROMAN;
                    offset -= 30;
                    for (String key : resultPlus.keySet()) {
                        int money = resultPlus.get(key);
                        String text = "    - " + key + ": " + money + "din";
                        appendToList(contentStream, font, offset, text);
                        offset -= 15;
                    }
                    //rashodi title
                    font = PDType1Font.TIMES_BOLD;
                    contentStream.beginText();
                    offset -= 15;
                    contentStream.newLineAtOffset(50, offset);
                    contentStream.setFont(font, 16);
                    int sumMinus = getSum(resultMinus);
                    contentStream.showText("Ukupni rashodi: " + sumMinus + "din");
                    contentStream.endText();
                    //rashodi list
                    font = PDType1Font.TIMES_ROMAN;
                    offset -= 30;
                    for (String key : resultMinus.keySet()) {
                        int money = resultMinus.get(key);
                        String text = "    - " + key + ": " + money + "din";
                        appendToList(contentStream, font, offset, text);
                        offset -= 15;
                    }
                    //saldo
                    font = PDType1Font.TIMES_BOLD;
                    contentStream.beginText();
                    offset -= 15;
                    contentStream.newLineAtOffset(50, offset);
                    contentStream.setFont(font, 16);
                    int saldo = sum + sumMinus;
                    contentStream.showText("Godišnji saldo: " + saldo + "din");
                    contentStream.endText();
                    //grafik title
                    contentStream.beginText();
                    String graphTitle = "Grafik promene salda tokom godine";
                    float titleGWidth = font.getStringWidth(graphTitle) / 1000 * 18;
                    font = PDType1Font.TIMES_BOLD_ITALIC;
                    offset -= 50;
                    contentStream.setFont(font, 18);
                    contentStream.newLineAtOffset((page1.getMediaBox().getWidth() - titleGWidth) / 2, offset);
                    contentStream.showText(graphTitle);
                    contentStream.endText();
                    //dobijanje podataka za grafik
                    offset -= 60;
                    int[] mesecniSaldo = getMonthlyFlow(godinaTf.getText());
                    chart = getChart(mesecniSaldo);
                    PDImageXObject pdImageXObject = PDImageXObject.createFromFile(chart.getPath(), document);
                    Float height = page1.getMediaBox().getHeight();
                    contentStream.drawImage(pdImageXObject, (page1.getMediaBox().getWidth() - 426) / 2,(height - offset - 240)/2);
                    //label Circled B,doesn't work from JAR file
//                    URL labelPDF = getClass().getClassLoader().getResource("labelPDF.jpg");
//                    String filePath = labelPDF.getFile().substring(6,labelPDF.getFile().length());
//                    PDImageXObject pdImageXObjectLabel = PDImageXObject.createFromFile(labelPDF.getPath(),document);
//                    contentStream.drawImage(pdImageXObjectLabel, 5,page1.getMediaBox().getHeight() - 5 - 41,158,41);
                    contentStream.close();

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H-m");
                    LocalDateTime now = LocalDateTime.now();
                    String dateToday = dtf.format(now).toString();
                    String documentPath = partPath+"\\ Godisnji izvestaj (" + godinaTf.getText() + ") " + dateToday + ".pdf";
                    document.save(documentPath);
                    System.out.println("Doc saved");
                    document.close();
                    chart.delete();
                    File docToOpen = new File(documentPath);
                    if(Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(docToOpen);
                    }
                    this.dispose();
                } catch (Exception e) {
                    if (chart != null)
                        chart.delete();
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        panel.add(godinaTf);
        panel.add(generisi);
        panel.add(godina);

        add(panel);
    }

    private File getChart(int[] mesecniSaldo) throws IOException {
        DefaultCategoryDataset lineChartSet = new DefaultCategoryDataset();
        for (int i = 0; i < 12; i++)
            lineChartSet.addValue(mesecniSaldo[i], "Saldo", String.valueOf(i + 1));
        JFreeChart chartObject = ChartFactory.createLineChart("", "Mesec", "Saldo", lineChartSet, PlotOrientation.VERTICAL, false, false, false);
        int width = 426;
        int height = 240;
        chartObject.setBackgroundPaint(new Color(242,242,242));
        File outputImage = new File(partPath + "\\chartTemporary.jpeg");
        ChartUtils.saveChartAsJPEG(outputImage, chartObject, width, height);
        return outputImage;
    }

    private int[] getMonthlyFlow(String year) {
        int[] saldo = new int[12];
        for (int i = 0; i < 12; i++)
            saldo[i] = 0;
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            String sql = "select date_part('month',date) as \"Mesec\", sum(amount) as \"Saldo za mesec\"\n" +
                    "from \"Transactions\"\n" +
                    "where date_part('year',date) = " + year + "\n" +
                    "group by date_part('month',date)\n" +
                    "order by date_part('month',date);";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int month = resultSet.getInt(1);
                int flow = resultSet.getInt(2);
                saldo[month - 1] = flow;
            }
            connection.close();
            return saldo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return saldo;
        }
    }

    private void appendToList(PDPageContentStream contentStream, PDFont font, float offset, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 14);
        contentStream.newLineAtOffset(50, offset);
        contentStream.showText(text);
        contentStream.endText();
    }

    private int getSum(HashMap<String, Integer> resultPlus) {
        int sum = 0;
        for (Integer val : resultPlus.values())
            sum += val;
        return sum;
    }

    public PDFMaker(JFrame parent) {
        super(parent, "Годишњи ПДФ", ModalityType.APPLICATION_MODAL);
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
