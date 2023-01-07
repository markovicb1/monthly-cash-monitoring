package notesPotrosnje;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) {
        int langID = getPrefferedLangauge();
        ResourceBundle langSet;
        if (langID == 1)
            langSet = ResourceBundle.getBundle("Languages", new Locale("en", "US"));
        else
            langSet = ResourceBundle.getBundle("Languages", new Locale("sr", "RS"));
        KorisnickoOkruzenje.setLookAndFeel();
        KorisnickoOkruzenje.language = langID;
        new KorisnickoOkruzenje(langSet);
    }

    private static int getPrefferedLangauge() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app_db", "postgres", "postgresbogdan");
            String sql = "SELECT type\n" +
                    "FROM \"Language\"\n" +
                    "WHERE id = 1";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int ret = 1;
            while (rs.next())
                ret = rs.getInt(1);
            statement.close();
            connection.close();
            return ret;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return 1;
        }

    }
}
