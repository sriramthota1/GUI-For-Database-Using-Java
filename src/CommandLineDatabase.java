import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.DatabaseMetaData;


public class CommandLineDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/sriramnani?user=root&password=balaraju1@T";

	

    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(URL);
            initializeDatabase();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter a database command (create, insert, display, or exit to quit): ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                if (command.startsWith("create")) {
                    createTable();
                } else if (command.startsWith("insert")) {
                    insertData();
                } else if (command.equalsIgnoreCase("display")) {
                    displayDatabase();
                } else {
                    System.out.println("Unknown command: " + command);
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        // Create a new database or connect to an existing one
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS my_table (id INTEGER PRIMARY KEY, name TEXT)");
        statement.close();
    }

    private static void createTable() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating a table...");
        System.out.print("Enter table name: ");
        String tableName = scanner.nextLine();
        System.out.print("Enter column definitions (comma-separated): ");
        String columnDefinitions = scanner.nextLine();
    
        try {
            Statement statement = connection.createStatement();
            // Example of columnDefinitions format: "id INT, name VARCHAR(255), age INT"
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnDefinitions + ")";
            statement.executeUpdate(createTableSQL);
            statement.close();
            System.out.println("Table created: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private static void insertData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserting data...");
        System.out.print("Enter table name: ");
        String tableName = scanner.nextLine();
        System.out.print("Enter values (comma-separated): ");
        String values = scanner.nextLine();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO " + tableName + " VALUES (" + values + ")");
            statement.close();
            System.out.println("Data inserted into table: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayDatabase() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });

            System.out.println("Tables in the database:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            tables.close();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the name of the table you want to display: ");
            String selectedTable = scanner.nextLine();

            // Display data from the selected table
            displayTableData(selectedTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayTableData(String tableName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            // Print column names
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
            }
            System.out.println();

            // Print data in a tabular format
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

