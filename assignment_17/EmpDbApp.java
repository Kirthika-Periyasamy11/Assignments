import java.sql.*;
import java.util.*;
import java.util.regex.*;
import javax.sql.rowset.*;
import java.io.*;

class Employee {
    private final int empid;
    private final String name;
    private final int age;
    private double salary;
    private final String designation;
    private final String department;

    
    public Employee(String designation) {
        empid = GetDetails.getId();
    	name = GetDetails.getName();
   	age = GetDetails.getAge(21, 60);
    	department = GetDetails.getDepartment();
    	salary = GetDetails.getSalary();
    	this.designation=designation;
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
    private Clerk() {
        super("Clerk");
    }
    public static Clerk getClerk(){
		return new Clerk();
	}


    }

class Manager extends Employee {
    private Manager() {
        super("Manager");
    }
	 public static Manager getManager(){
		return new Manager();
	}

   }

class Programmer extends Employee {
    private Programmer() {
        super("Programmer");
    }
	 public static Programmer getProgrammer(){
		return new Programmer();
	}
}
class Others extends Employee{
public Others(String designation){
	super( designation);
}
 public static Others getOthers(){
		System.out.println("Enter designation: ");
		String desig=new Scanner(System.in).nextLine();
		return new Others(desig);
	}
}

   

class EmployeeFactory {
    public static Employee createEmployee(String desig) {
        return switch (desig.toLowerCase()) {
            case "clerk" -> Clerk.getClerk();
            case "manager" -> Manager.getManager();
            case "programmer" -> Programmer.getProgrammer();
	    case "others"->Others.getOthers();
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


public class EmpDbApp {
    
private static void createEmployee() {
    System.out.println("Enter designation of employee: ");
    String desig = new Scanner(System.in).nextLine();
    Employee employee = EmployeeFactory.createEmployee(desig);
    try {
       		rowSet.setCommand("SELECT * FROM Employee");
                rowSet.execute();
                rowSet.moveToInsertRow();
                rowSet.updateInt("EID", employee.getEmpId());
                rowSet.updateString("NAME", employee.getName());
                rowSet.updateInt("AGE", employee.getAge());
                rowSet.updateDouble("SALARY", employee.getSalary());
                rowSet.updateString("DESIGNATION", employee.getDesignation());
                rowSet.updateString("DEPARTMENT", employee.getDepartment());
                rowSet.insertRow();
                rowSet.moveToCurrentRow();
        }
            catch (SQLException e) {
        System.out.println("Error inserting employee: " + e.getMessage());
    }
}


    private static void displayEmployeesByCriteria() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter criteria to display: ");
        System.out.println("1. ID\n2. Name\n3. Age\n4. Designation\n5. Salary");
        System.out.print("Enter choice: ");
        int displayChoice = sc.nextInt();
        sc.nextLine(); 

        String orderBy = switch (displayChoice) {
            case 1 -> "EID";
            case 2 -> "NAME";
            case 3 -> "AGE";
            case 4 -> "DESIGNATION";
            case 5 -> "SALARY";
            default -> {
                System.out.println("Invalid choice!");
               yield "EID";
            }
        };

        try {
            rowSet.setCommand("SELECT * FROM Employee ORDER BY " + orderBy);
            rowSet.execute();

            while (rowSet.next()) {
                System.out.println("EID: " + rowSet.getInt("EID"));
                System.out.println("Name: " + rowSet.getString("NAME"));
                System.out.println("Age: " + rowSet.getInt("AGE"));
                System.out.println("Salary: " + rowSet.getDouble("SALARY"));
                System.out.println("Designation: " + rowSet.getString("DESIGNATION"));
                System.out.println("Department: " + rowSet.getString("DEPARTMENT"));
                System.out.println("------");
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

    try {
        rowSet.setCommand("SELECT * FROM Employee WHERE EID = ?");
        rowSet.setInt(1, empId);
        rowSet.execute();

        boolean employeeFound = false;
        while (rowSet.next()) {
            double currentSalary = rowSet.getDouble("SALARY");
            rowSet.updateDouble("SALARY", currentSalary + increment);
            rowSet.updateRow();
            employeeFound = true;
        }

        if (employeeFound) {
            System.out.println("Salary updated successfully for Employee ID: " + empId);
        } else {
            System.out.println("Employee not found! Please enter a valid Employee ID.");
        }
        
    } catch (SQLException e) {
        System.out.println("Error updating salary: " + e.getMessage());
    }
}


    private static void searchEmployeeByCriteria() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter criteria to search: ");
        System.out.println("1. ID\n2. Name\n3. Designation");
        System.out.print("Enter choice: ");
        int searchChoice = sc.nextInt();
        sc.nextLine(); 
        String query;
        
        try {
            switch (searchChoice) {
                case 1 -> {
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    rowSet.setCommand("SELECT * FROM Employee WHERE EID = " + empId);
                }
                case 2 -> {
                    System.out.print("Enter Employee Name: ");
                    String empName = sc.nextLine();
                    rowSet.setCommand("SELECT * FROM Employee WHERE NAME LIKE '%" + empName + "%'");
                }
                case 3 -> {
                    System.out.print("Enter Employee Designation: ");
                    String empDesignation = sc.nextLine();
                    rowSet.setCommand("SELECT * FROM Employee WHERE LOWER(DESIGNATION) LIKE '%" + empDesignation.toLowerCase()+ "%'");
                }
                default -> {
                   { System.out.println("Invalid choice!"); }
                  
                }
            }
            rowSet.execute();
            boolean found = false; 
	    while (rowSet.next()) {
    		System.out.println("EID: " + rowSet.getInt("EID"));
    		System.out.println("Name: " + rowSet.getString("NAME"));
    		System.out.println("Age: " + rowSet.getInt("AGE"));
    		System.out.println("Salary: " + rowSet.getDouble("SALARY"));
    		System.out.println("Designation: " + rowSet.getString("DESIGNATION"));
    		System.out.println("Department: " + rowSet.getString("DEPARTMENT"));
    		System.out.println("--------------------------");
    		found = true;
	   }
	   if (!found) {
    	   System.out.println("Employee not found!");
}
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
	Connection.createConnection();
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
                case 5 -> {System.out.println("Exiting...");
			Connection.closeConnection();
			}
                default -> {System.out.println("Invalid choice! Please try again."); 
			   }
				
            }
        } while (choice != 5);      }
}
class Connection{
	private static JdbcRowSet rowSet;
	public static void createConnection(){
	try {
            RowSetFactory rsf = RowSetProvider.newFactory();
            rowSet = rsf.createJdbcRowSet();
            rowSet.setUrl("jdbc:postgresql://localhost:5432/demodb");
            rowSet.setUsername("postgres");
            rowSet.setPassword("tiger");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

	public static void closeConnection() {
    try {
        if (rowSet != null) {
            rowSet.close();
            System.out.println("Database connection closed.");
        }
    } catch (SQLException e) {
        System.out.println("Error closing database connection: " + e.getMessage());
    }
}

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

class AgeException extends Exception {
    public NameException() {
        super();
    }

    public AgeException(String msg) {
        super(msg);
    }
}