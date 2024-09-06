import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeeDetailsFrame extends JFrame {
    private JTextArea displayArea;

    public EmployeeDetailsFrame() {
        super("Employee Details");

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        fetchEmployeeDetails();

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fetchEmployeeDetails() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sriramnani?user=root&password=balaraju1@T");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM employee1";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                displayArea.append("Employee ID: " + resultSet.getInt("empoyee_id") + "\n");
                displayArea.append("Name: " + resultSet.getString("name") + "\n");
                displayArea.append("\n------------------------\n");
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeDetailsFrame());
    }
}