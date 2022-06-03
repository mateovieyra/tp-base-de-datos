import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

  //region Constantes de configuración
  private static final String postgresDriver = "org.postgresql.Driver";
  private static final String EXIT = "4";
  private static final String START = "";
  private static final List<String> tiposHab = List.of("simple","doble","triple","cuadruple");
  private static Scanner sc = new Scanner(System.in);
  //endregion

  /**
   * Función principal de la aplicación.
   *  Se encarga de inicializar la conexión con la base de datos, y de llamar a la función del menú.
   * @param args Argumentos de la línea de comandos.
   */
  public static void main(String[] args) {

    try {

      //Carga del driver de PostgreSQL si no está cargado.
      Class.forName(postgresDriver);


      String opcion = START;

      //Mostrar menú y leer opción
      while (!opcion.equals(EXIT)) {
        
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

          case EXIT:
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

  /**
   * Función que imprime el menú de la aplicación.
   * Su retorno es vacio.
   */
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

  /**
   * Función que inserta una nueva habitación en la base de datos.
   * Su retorno es vacio.
   * @throws SQLException
   */
  private static void insertarHabitacion() throws SQLException{
    Connection connection = Conexion.getInstance();
    connection.setAutoCommit(false);



    String query = "INSERT INTO hotel.habitaciones (nro ,cant_camas ,codigo_tipo_hab) values(?,?,?)";

    int nro;
    String cant_camas;
    int codigo_tipo_hab;

    // Pide numero de habitacion
    System.out.print("Ingresar número de habitación: ");
    nro = sc.nextInt();

    // Pide cantidad de camas
    do {
      System.out.print("\nIngresar cantidad de camas de acuerdo a las siguientes opciones ('simple', 'doble', 'triple', 'cuadruple'): ");
      cant_camas = sc.next();
    } while (!tiposHab.contains(cant_camas));

    // Pide tipo de habitacion desplegando un menu de opciones
    int opcion = 0;
    do {
      System.out.print("\nIngresar el codigo de tipo habitacion de acuerdo a las siguientes opciones (1- Basica, 2- Intermedia, 3-Premium): ");
      opcion = sc.nextInt();
    } while (opcion < 1 || opcion > 3);

    codigo_tipo_hab = opcion;
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setInt(1,nro);
    statement.setString(2,cant_camas);
    statement.setInt(3,codigo_tipo_hab);
    statement.executeUpdate();

    connection.commit();

    System.out.println("\nHabitacion insertada con éxito.");

  }

  /**
   * Funcion que registra un cliente en una habitación en la fecha de hoy.
   * @throws SQLException
   */
  private static void registrarCliente() throws SQLException{
    Connection connection = Conexion.getInstance();
    connection.setAutoCommit(false);

    String dni;
    Date date = Date.valueOf(LocalDate.now());
    
    System.out.print("Ingresa el DNI del cliente (Ej: 42.638.323): ");
    dni = sc.nextLine();


    // Buscar la persona
    String query = "SELECT * FROM hotel.personas WHERE dni=?;";
    PreparedStatement statement = connection.prepareStatement(query);
    statement.setString(1, dni);
    ResultSet resultSet = statement.executeQuery();


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

  /**
   * Funcion que lista los clientes registrados en el hotel.
   * @throws SQLException
   */
  private static void listarRegistroDeClientes() throws SQLException{
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