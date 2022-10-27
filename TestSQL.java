package notesPotrosnje;

import java.sql.*;

public class TestSQL {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/app_db";
        String username = "postgres";
        String password = "postgresbogdan";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database.");
//          String sql = "SELECT * FROM \"Transactions\"";
//          Statement statement = connection.createStatement();
//          ResultSet rs = statement.executeQuery(sql);
//          while(rs.next()){
//              System.out.println(rs.getString(4));
//          }
//          rs.close();
            String sql = "INSERT INTO \"Activities\"(name, type)"
                    + "VALUES('Skolski pribor', '-')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Updated db!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
