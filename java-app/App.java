import java.sql.*;
import java.util.Scanner;

public class App {


  public static void main(String[] args) {

    try {
      String driver = "org.postgresql.Driver";

      Scanner sc = new Scanner(System.in);

      // Load database driver if not already loaded.
      Class.forName(driver);

      String opcion = "";
      while (!opcion.equals("4")) {
        
        imprimirMenu();
        opcion = sc.next();
        System.out.println();

        switch (opcion) {
          case "1":
            insertarHabitacion();
            break;

          case "2":
            registrarCliente();
            break;

          case "3":
            listarRegistroDeClientes();
            break;

          case "4":
            System.out.println("Muchas gracias. Hasta pronto!\n");
            break;

          default:
            System.out.println("Opcion no disponible. Intente nuevamente.\n");
            break;
        }
      }
      
      
    } catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
    } catch(SQLException sqle) {
    	sqle.printStackTrace();
      System.err.println("Error connecting: " + sqle);
    }

  }

  public static void imprimirMenu(){
    System.out.println("===============================");
    System.out.println("HOTELERIA");
    System.out.println("===============================");
    System.out.println("1) Insertar nueva habitacion.");
    System.out.println("2) Registrar cliente.");
    System.out.println("3) Listar registro de clientes.");
    System.out.println("4) Salir");
    System.out.println("===============================");
    System.out.print("Seleccionar opcion (1 a 4): ");
  }

  public static void insertarHabitacion() throws SQLException{
    Connection connection = Conexion.getInstance();

  }

  public static void registrarCliente() throws SQLException{
    Connection connection = Conexion.getInstance();

  }

  public static void listarRegistroDeClientes() throws SQLException{
    Connection connection = Conexion.getInstance();

  }
}