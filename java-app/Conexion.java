import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
  private static String url = "jdbc:postgresql://localhost/test";
  private static String username = "postgres";
  private static String password = "root";
  private static Connection connection;

  public static Connection getInstance() throws SQLException{
    if (connection == null){
      connection = DriverManager.getConnection(url, username, password);
    }
    return connection;
  }
  
}