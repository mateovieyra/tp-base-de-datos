import java.sql.*;
import java.time.LocalDate;
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
    	try	
        {
         System.err.println("antes rollback: " + sqle);
         Connection connection = Conexion.getInstance();
         connection.rollback();
         System.err.println("Error Se produjo una Excepcion accediendo a la base de datoas: " + sqle);
         sqle.printStackTrace();
        } 
        catch(Exception e)
        {
            System.err.println("Error Ejecutando el rollback de la transaccion: " + e.getMessage());
            e.printStackTrace();
        }
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

    connection.setAutoCommit(false);

    Scanner sc = new Scanner(System.in);

    String query = "INSERT INTO hotel.habitaciones (nro ,cant_camas ,codigo_tipo_hab) values(?,?,?)";

    int nro;
    String cant_camas = "";
    int codigo_tipo_hab;

    // Pide numero de habitacion
    System.out.print("Ingresar número de habitación: ");
    nro = sc.nextInt();

    // Pide cantidad de camas
    do {
      System.out.print("\nIngresar cantidad de camas de acuerdo a las siguientes opciones ('simple', 'doble', 'triple', 'cuadruple'): ");
      cant_camas = sc.next();
    } while (!cant_camas.equals("simple") && !cant_camas.equals("doble") && !cant_camas.equals("triple") && !cant_camas.equals("cuadruple"));

    // Pide tipo de habitacion desplegando un menu de opciones
    String opcion = "";
    do {
      System.out.print("\nIngresar el codigo de tipo habitacion de acuerdo a las siguientes opciones (1- Basica, 2- Intermedia, 3-Premium): ");
      opcion = sc.next();
    } while (!opcion.equals("1") && !opcion.equals("2") && !opcion.equals("3"));

    codigo_tipo_hab = Integer.parseInt(opcion);

    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1,nro);
    statement.setString(2,cant_camas);
    statement.setInt(3,codigo_tipo_hab);
    statement.executeUpdate();

    connection.commit();

    System.out.println("\nHabitacion insertada con exito.");

  }

  public static void registrarCliente() throws SQLException{
    Connection connection = Conexion.getInstance();

    Scanner sc = new Scanner(System.in);
    
    String dni;

    Date date = Date.valueOf(LocalDate.now());
    
    System.out.print("Ingresa el DNI del cliente (Ej: 42.638.323): ");
    dni = sc.nextLine();

    // Buscar la persona
    String query = "SELECT * FROM hotel.personas WHERE dni=?;";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, dni);
    ResultSet resultSet = statement.executeQuery();
    connection.setAutoCommit(false);

    // Si la persona no existe, inserto una persona
    if(!resultSet.next()){
      
      System.out.print("Ingresa el nombre y apellido del cliente : ");
      String nyap = sc.nextLine();
      
      System.out.print("Ingresa el telefono del cliente : ");
      String telefono = sc.nextLine();
      
      System.out.print("Ingresa la direccion del cliente : ");
      String direccion = sc.nextLine();
      
      query = "INSERT INTO hotel.personas (dni, nyap, telefono, direccion) VALUES(?,?,?,?)";
      statement = connection.prepareStatement(query);
      statement.setString(1, dni);
      statement.setString(2, nyap);
      statement.setString(3, telefono);
      statement.setString(4, direccion);
      
      statement.executeUpdate();
    }
    
    // Buscar cliente. 
    query = "SELECT * FROM hotel.clientes WHERE dni=?;";
    statement = connection.prepareStatement(query);
    statement.setString(1, dni);
    resultSet = statement.executeQuery();

    // Si no existe, inserto un cliente
    if(!resultSet.next()){
      
      query = "INSERT INTO hotel.clientes (dni, fecha_primer_hosp) VALUES(?,?)";
      statement = connection.prepareStatement(query);
      statement.setString(1, dni);
      statement.setDate(2, date);

      statement.executeUpdate();
    }
    
    // Insertar en registros

      System.out.print("Ingresar numero de habitacion: ");
      int nroHab = sc.nextInt();

      System.out.print("Ingresar cantidad de dias del hospedaje: ");
      int cantDias = sc.nextInt();

      System.out.print("Ingresar costo total del hospedaje: ");
      float costo = sc.nextFloat();

      query = "INSERT INTO hotel.registros (fecha, nro_hab, dni_cliente, cant_dias, costo) VALUES(?,?,?,?,?)";
      statement = connection.prepareStatement(query);
      statement.setDate(1, date);
      statement.setInt(2,nroHab);
      statement.setString(3, dni);
      statement.setInt(4, cantDias);
      statement.setFloat(5, costo);

      statement.executeUpdate();

      connection.commit();

      System.out.println("\nRegistro exitoso.");
  }

  public static void listarRegistroDeClientes() throws SQLException{
    Connection connection = Conexion.getInstance();

    String query = "SELECT dni, nyap, fecha FROM hotel.registros as r JOIN hotel.personas as p ON(r.dni_cliente = p.dni) ORDER BY nyap;";

    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();
    System.out.println("Listado de clientes: \n");
    while(resultSet.next()){
      System.out.print("* DNI: " + resultSet.getString("dni"));
      System.out.print("; Nombre: "+resultSet.getString("nyap"));
      System.out.print("; Fecha: "+resultSet.getDate("fecha"));
      System.out.print("\n\n");
    }

  }
}