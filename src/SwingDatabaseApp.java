import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.List;


public class SwingDatabaseApp{
    private static final String URL = "jdbc:mysql://localhost:3306/sriramnani?user=root&password=balaraju1@T";
    private Connection connection;
    private JFrame frame;
    private JTextArea outputTextArea;

    public SwingDatabaseApp() {
        initializeUI();
        connectToDatabase();
    }

    private void initializeUI() {
        frame = new JFrame("Database App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());

        String[] options = {"Select","Create Table", "Insert Data", "Display Data", "Exit"};
        JComboBox<String> optionComboBox = new JComboBox<>(options);
        controlPanel.add(optionComboBox);

        frame.add(controlPanel, BorderLayout.NORTH);

        optionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) optionComboBox.getSelectedItem();
                if (selectedOption.equals("Create Table")) {
                    String tableName = JOptionPane.showInputDialog("Enter table name:");
                    String columnDefinitions = JOptionPane.showInputDialog("Enter column definitions (comma-separated):");
                    if (tableName != null && !tableName.isEmpty() && columnDefinitions != null) {
                        createTable(tableName, columnDefinitions);
                    } else {
                        displayMessage("Invalid input. Table creation canceled.");
                    }
                } else if (selectedOption.equals("Insert Data")) {
                    String tableName = JOptionPane.showInputDialog("Enter table name:");
                    String values = JOptionPane.showInputDialog("Enter values (comma-separated):");
                    if (tableName != null && !tableName.isEmpty() && values != null) {
                        insertData(tableName, values);
                    } else {
                        displayMessage("Invalid input. Data insertion canceled.");
                    }
                } else if (selectedOption.equals("Display Data")) {
                    displayDatabase();
                } else if (selectedOption.equals("Exit")) {
                    closeConnection();
                    System.exit(0);
                }
            }
        });

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error connecting to the database: " + e.getMessage());
        }
    }

    private void createTable(String tableName, String columnDefinitions) {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columnDefinitions + ")";
            statement.executeUpdate(createTableSQL);
            displayMessage("Table created: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error creating table: " + e.getMessage());
        }
    }

    private void insertData(String tableName, String values) {
        try {
            Statement statement = connection.createStatement();
            String insertSQL = "INSERT INTO " + tableName + " VALUES (" + values + ")";
            statement.executeUpdate(insertSQL);
            displayMessage("Data inserted into table: " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error inserting data: " + e.getMessage());
        }
    }

    private void displayDatabase() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet databases = metaData.getCatalogs();

            List<String> databaseNames = new ArrayList<>();
            while (databases.next()) {
                String databaseName = databases.getString("TABLE_CAT");
                databaseNames.add(databaseName);
            }

            databases.close();

            String[] databaseArray = databaseNames.toArray(new String[0]);
            String selectedDatabase = (String) JOptionPane.showInputDialog(frame,
                    "Select a database:", "Database Selection", JOptionPane.QUESTION_MESSAGE, null, databaseArray,
                    databaseArray[0]);

            if (selectedDatabase != null) {
                ResultSet tables = metaData.getTables(selectedDatabase, null, null, new String[]{"TABLE"});

                List<String> tableNames = new ArrayList<>();
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }

                tables.close();

                String[] tableArray = tableNames.toArray(new String[0]);
                String selectedTable = (String) JOptionPane.showInputDialog(frame,
                        "Select a table:", "Table Selection", JOptionPane.QUESTION_MESSAGE, null, tableArray,
                        tableArray[0]);

                if (selectedTable != null) {
                    displayTableData(selectedDatabase, selectedTable);
                } else {
                    displayMessage("Invalid selection. Data display canceled.");
                }
            } else {
                displayMessage("Invalid selection. Data display canceled.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error displaying data: " + e.getMessage());
        }
    }

    private void displayTableData(String dbName, String tableName) {
        try {
            Connection dbConnection = DriverManager.getConnection(URL.replace("/sriramnani", "/" + dbName));
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            displayMessage("Displaying data from table: " + tableName);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder header = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                header.append(metaData.getColumnName(i)).append("\t");
            }
            displayMessage(header.toString());

            while (resultSet.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    row.append(resultSet.getString(i)).append("\t");
                }
                displayMessage(row.toString());
            }

            resultSet.close();
            statement.close();
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error displaying data: " + e.getMessage());
        }
    }

    private void displayMessage(String message) {
        outputTextArea.append(message + "\n");
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("Error closing the database connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingDatabaseApp());
    }
}
