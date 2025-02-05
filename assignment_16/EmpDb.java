import java.sql.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;


class Employee {
    private final int empid;
    private final String name;
    private final int age;
    private double salary;
    private final String designation;
    private final String department;

    
    public Employee(int empid, String name, int age, double salary, String designation, String department) {
        this.empid = empid;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
        this.department = department;
    }

    public int getEmpId() {
        return empid;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    protected void setSalary(int salary) {
        this.salary = salary;
    }

    
}

class Clerk extends Employee {
    public Clerk(int empid, String name, int age, String department,double salary) {
        super(empid, name, age, salary, "Clerk", department);
    }

    }

class Manager extends Employee {
    public Manager(int empid, String name, int age, String department, double salary) {
        super(empid, name, age, salary, "Manager", department);
    }

   }

class Programmer extends Employee {
    public Programmer(int empid, String name, int age, String department,double salary) {
        super(empid, name, age,salary , "Programmer", department);
    }

   }

class EmployeeFactory {
    public static Employee createEmployee(String type, int empid, String name, int age, String department,double salary) {
        return switch (type.toLowerCase()) {
            case "clerk" -> new Clerk(empid, name, age, department,salary);
            case "manager" -> new Manager(empid, name, age, department,salary);
            case "programmer" -> new Programmer(empid, name, age, department,salary);
            default -> null;
        };
    }
}

class InvalidEntryException extends Exception {
    public InvalidEntryException(String msg) {
        super(msg);
    }
}

class AgeException extends Exception {
    public AgeException(String msg) {
        super(msg);
    }
}


public class EmpDb {
    private static Connection conn;

    static {
        try {
		
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
	finally{
	
	}
	
    }

    private static void createEmployee() {
	Scanner sc = new Scanner(System.in);
	int empid=GetDetails.getId();
	String name = GetDetails.getName();
	int age = GetDetails.getAge(21, 60);
	String department=GetDetails.getDepartment();
	double salary=GetDetails.getSalary();
	String type=GetDetails.getType();
    
	switch(type){
        case "clerk":
	case "manager":
	case "programmer":
	{
	 Employee employee = EmployeeFactory.createEmployee(type, empid, name, age, department,salary);
        try {
            String query = "INSERT INTO Employee (EID,NAME, AGE, SALARY, DESIGNATION, DEPARTMENT) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, employee.getEmpId());
                stmt.setString(2, employee.getName());
                stmt.setInt(3, employee.getAge());
                stmt.setDouble(4, employee.getSalary());
                stmt.setString(5, employee.getDesignation());
                stmt.setString(6, employee.getDepartment());
                stmt.executeUpdate();
                System.out.println("Employee added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting employee: " + e.getMessage());
        }
    break;}
	case "others":{
		
		System.out.print("Enter Employee Type : ");
       		 String desig= sc.nextLine().toLowerCase();
		Employee employee=new Employee(empid, name, age,salary,desig,department);
		try {
            String query = "INSERT INTO Employee (EID,NAME, AGE, SALARY, DESIGNATION, DEPARTMENT) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, employee.getEmpId());
                stmt.setString(2, employee.getName());
                stmt.setInt(3, employee.getAge());
                stmt.setDouble(4, employee.getSalary());
                stmt.setString(5, desig);
                stmt.setString(6, employee.getDepartment());
                stmt.executeUpdate();
                System.out.println("Employee added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting employee: " + e.getMessage());
        }
		break;
}
}
}

		

    private static void displayEmployeesByCriteria() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter criteria to display: ");
            System.out.println("1. ID\n2. Name\n3. Age\n4. Designation\n5. Salary");
            System.out.print("Enter choice: ");
            int displayChoice = Integer.parseInt(br.readLine());

            String query = "";
            switch (displayChoice) {
                case 1 -> query = "SELECT * FROM Employee ORDER BY EID";
                case 2 -> query = "SELECT * FROM Employee ORDER BY NAME";
                case 3 -> query = "SELECT * FROM Employee ORDER BY AGE";
                case 4 -> query = "SELECT * FROM Employee ORDER BY DESIGNATION";
                case 5 -> query = "SELECT * FROM Employee ORDER BY SALARY";
                default -> {
                    System.out.println("Invalid choice!");
                    return;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("EID: " + rs.getInt("EID"));
                    System.out.println("Name: " + rs.getString("NAME"));
                    System.out.println("Age: " + rs.getInt("AGE"));
                    System.out.println("Salary: " + rs.getDouble("SALARY"));
                    System.out.println("Designation: " + rs.getString("DESIGNATION"));
                    System.out.println("Department: " + rs.getString("DEPARTMENT"));
                    System.out.println("------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchEmployeeByCriteria() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter criteria to search: ");
        System.out.println("1. ID\n2. Name\n3. Designation");
        System.out.print("Enter choice: ");
        int searchChoice = sc.nextInt();
        sc.nextLine(); 
        String query = "";
        int empId = 0;
        String empName = "", empDesignation = "";

        switch (searchChoice) {
            case 1 -> {
                System.out.print("Enter Employee ID: ");
                empId = sc.nextInt();
                query = "SELECT * FROM Employee WHERE EID = ?";
            }
            case 2 -> {
                System.out.print("Enter Employee Name: ");
                empName = sc.nextLine();
                query = "SELECT * FROM Employee WHERE NAME LIKE ?";
            }
            case 3 -> {
                System.out.print("Enter Employee Designation: ");
                empDesignation = sc.nextLine();
                query = "SELECT * FROM Employee WHERE DESIGNATION LIKE ?";
            }
            default -> {
                System.out.println("Invalid choice!");
                return;
            }
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            if (searchChoice == 1) stmt.setInt(1, empId);
            else stmt.setString(1, "%" + (searchChoice == 2 ? empName : empDesignation) + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("EID: " + rs.getInt("EID"));
                    System.out.println("Name: " + rs.getString("NAME"));
                    System.out.println("Age: " + rs.getInt("AGE"));
                    System.out.println("Salary: " + rs.getDouble("SALARY"));
                    System.out.println("Designation: " + rs.getString("DESIGNATION"));
                    System.out.println("Department: " + rs.getString("DEPARTMENT"));
                } else {
                    System.out.println("Employee not found!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void raiseSalary() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Employee ID: ");
        int empId = sc.nextInt();

        System.out.print("Enter Salary Increment Amount: ");
        double increment = sc.nextDouble();

        String query = "UPDATE Employee SET SALARY = SALARY + ? WHERE EID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, increment);
            stmt.setInt(2, empId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Salary updated successfully for Employee ID: " + empId);
            } else {
                System.out.println("Employee not found! Please enter a valid Employee ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating salary: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("------ Menu ------");
            System.out.println("1. Create Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Raise Salary");
            System.out.println("4. Search Employee");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();             
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> displayEmployeesByCriteria();
                case 3 -> raiseSalary();
                case 4 -> searchEmployeeByCriteria();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);      }
}

class GetDetails {

    public static int getAge(int min, int max) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Enter age: ");
                int age = scanner.nextInt();

                if (age >= min && age <= max) {
                    return age;
                } else {
                    throw new AgeException("Please enter a valid age between 21 and 60.");
                }
            } catch (AgeException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Age can only be a number.");
                scanner.nextLine();
            }
        }
    }

    public static int getId() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter employee ID: ");
                int id = scanner.nextInt();
                return id;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }

    public static double getSalary() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter employee salary: ");
                double salary = scanner.nextDouble();
                return salary;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }

    public static String getDepartment() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter employee department: ");
                String dept = scanner.nextLine();
                return dept;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }

    public static String getType() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter employee type: ");
                String type = scanner.nextLine(); // Fixed this line
                return type;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }

    public static String getName() {
        while (true) {
            try {
                System.out.print("Enter name: ");
                String name = new Scanner(System.in).nextLine();
                Pattern p1 = Pattern.compile("([A-Z][a-z]*)+\\s([A-Z])+([a-z]*)*");
                Matcher m1 = p1.matcher(name);
                if (m1.matches()) {
                    return name;
                } else {
                    throw new NameException("Please enter a valid name.");
                }
            } catch (NameException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

class NameException extends Exception {
    public NameException() {
        super();
    }

    public NameException(String msg) {
        super(msg);
    }
}  