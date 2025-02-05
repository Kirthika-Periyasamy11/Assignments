package mongoDemo;

import org.json.JSONObject;
import java.sql.*;

public class EmployeeApp {

    public static void storeEmployee(String name, int age) {
        String insertQuery = "INSERT INTO Employee (EmployeeData) VALUES (?::jsonb)";
        try (PreparedStatement pstmt = Connections.conn.prepareStatement(insertQuery)) {
            JSONObject employeeJson = new JSONObject();
            employeeJson.put("EmployeeName", name);
            employeeJson.put("Age", age);

            pstmt.setString(1, employeeJson.toString());
            pstmt.executeUpdate();
            System.out.println("Employee data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveEmployees() {
        String selectQuery = "SELECT EmployeeID, EmployeeData FROM Employee";
        try (Statement stmt = Connections.conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                int employeeID = rs.getInt("EmployeeID");
                String employeeDataJson = rs.getString("EmployeeData");
                JSONObject employeeJson = new JSONObject(employeeDataJson);
                System.out.println("EmployeeID: " + employeeID);
                System.out.println("Employee Data: " + employeeJson.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEmployee(String conditionField, Object conditionValue, String updateField, Object updateValue) {
        if (conditionField == null || updateField == null || conditionValue == null || updateValue == null) {
            System.out.println("Invalid input parameters for update.");
            return;
        }

        String updateQuery = "UPDATE Employee SET EmployeeData = jsonb_set(EmployeeData, '{" + updateField + "}', ?::jsonb) WHERE EmployeeData ->> ? = ?";
        try (PreparedStatement pstmt = Connections.conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, '"' + updateValue.toString() + '"');
            pstmt.setString(2, conditionField);
            pstmt.setString(3, conditionValue.toString());
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Employee data updated successfully." : "Employee not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmployee(String conditionField, Object conditionValue) {
        if (conditionField == null || conditionValue == null) {
            System.out.println("Invalid input parameters for delete.");
            return;
        }

        String deleteQuery = "DELETE FROM Employee WHERE EmployeeData ->> ? = ?";
        try (PreparedStatement pstmt = Connections.conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, conditionField);
            pstmt.setString(2, conditionValue.toString());
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Employee deleted successfully." : "Employee not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connections.createConnection();
        storeEmployee("Alice", 30);
        storeEmployee("Bob", 25);
        System.out.println("Retrieved Employee Data:");
        retrieveEmployees();
        updateEmployee("Age", "30", "EmployeeName", "Alice Johnson");
        //updateEmployee("Age", 25, "Age", 26);
       // deleteEmployee("EmployeeName", "Bob");
        deleteEmployee("Age", 25);
        System.out.println("Updated Employee Data:");
        retrieveEmployees();
        Connections.closeConnection();
    }
}

class Connections {
    static java.sql.Connection conn;
    public static void createConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
