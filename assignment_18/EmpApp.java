package mongoDemo;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;
import java.util.regex.*;

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
        this.designation = designation;
    }

    public int getEmpId() { return empid; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
    public String getDesignation() { return designation; }
    public String getDepartment() { return department; }
    protected void setSalary(double salary) { this.salary = salary; }
}

class Clerk extends Employee {
    private Clerk() { super("Clerk"); }
    public static Clerk getClerk() { return new Clerk(); }
}

class Manager extends Employee {
    private Manager() { super("Manager"); }
    public static Manager getManager() { return new Manager(); }
}

class Programmer extends Employee {
    private Programmer() { super("Programmer"); }
    public static Programmer getProgrammer() { return new Programmer(); }
}

class Others extends Employee {
    public Others(String designation) { super(designation); }
    public static Others getOthers() {
        System.out.println("Enter designation: ");
        String desig = new Scanner(System.in).nextLine();
        return new Others(desig);
    }
}

class EmployeeFactory {
    public static Employee createEmployee(String desig) {
       switch (desig.toLowerCase()) {
            case "clerk" :{return Clerk.getClerk();}
            case "manager" :{return Manager.getManager();}
            case "programmer" : {return Programmer.getProgrammer();}
            case "others" : {return Others.getOthers(); }
            default:
                {System.out.println("Invalid designation! Defaulting to 'Others'.");
                return new Others(desig); }
        }
    }
}
interface EmpDbApp {
    void createEmployee();
    void displayEmployees();
    void raiseSalary();
    void searchEmployee();
}

class EmpDbImpl implements EmpDbApp {
    @Override
    public void createEmployee() {
        System.out.println("Enter designation of employee: ");
        String desig = new Scanner(System.in).nextLine();
        Employee employee = EmployeeFactory.createEmployee(desig);

        if (employee != null) {
            Document doc = new Document("EID", employee.getEmpId())
                    .append("NAME", employee.getName())
                    .append("AGE", employee.getAge())
                    .append("SALARY", employee.getSalary())
                    .append("DESIGNATION", employee.getDesignation())
                    .append("DEPARTMENT", employee.getDepartment());
            Connection.getCollection().insertOne(doc);
            System.out.println("Employee added successfully.");
        }
    }

    @Override
    public static void displayEmployees() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Choose sorting field 1.EID 2.NAME 3.AGE 4.SALARY 5.DESIGNATION): ");
        int f = scanner.nextInt();
        String field="EID";
        switch(f) {
        	case 1: {field="EID"; break;}
        	case 2: {field="NAME"; break;}
        	case 3: {field="AGE"; break;}
        	case 4: {field="SALARY"; break;}
        	case 5: {field="DESIGNATION"; break;}
        }
       
        System.out.println("Choose sorting order (ASC for Ascending, DESC for Descending): ");
        String orderInput = scanner.nextLine().toUpperCase();
        int order = orderInput.equals("DESC") ? -1 : 1; 

        FindIterable<Document> employees = Connection.getCollection()
                .find()
                .sort(new Document(field, order));

      
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-5s %-10s %-15s%n", "EID", "NAME", "AGE", "SALARY", "DESIGNATION");
        System.out.println("--------------------------------------------------------------");

       
        for (Document doc : employees) {
            System.out.printf("%-5d %-15s %-5d %-10.2f %-15s%n",
                    doc.getInteger("EID"),
                    doc.getString("NAME"),
                    doc.getInteger("AGE"),
                    doc.getDouble("SALARY"),
                    doc.getString("DESIGNATION"));
        }
    }

    @Override
    public void raiseSalary() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Employee ID: ");
        int empId = sc.nextInt();
        System.out.print("Enter Salary Increment Amount: ");
        double increment = sc.nextDouble();

        Document query = new Document("EID", empId);
        Document employee = Connection.getCollection().find(query).first();
        if (employee != null) {
            double newSalary = employee.getDouble("SALARY") + increment;
            Connection.getCollection().updateOne(query, new Document("$set", new Document("SALARY", newSalary)));
            System.out.println("Salary updated successfully.");
        } else {
            System.out.println("Employee not found!");
        }
    }

    @Override
    public static void searchEmployee() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Search Employee by: ");
        System.out.println("1. Employee ID");
        System.out.println("2. Name");
        System.out.println("3. Designation");
        System.out.print("Enter your choice (1/2/3): ");
        int choice = sc.nextInt();
        sc.nextLine(); 

        Document query = null;

        switch (choice) {
            case 1:
                System.out.print("Enter Employee ID: ");
                int empId = sc.nextInt();
                query = new Document("EID", empId);
                break;
            case 2:
                System.out.print("Enter Employee Name: ");
                String name = sc.nextLine();
                query = new Document("NAME", name);
                break;
            case 3:
                System.out.print("Enter Employee Designation: ");
                String designation = sc.nextLine();
                query = new Document("DESIGNATION", designation);
                break;
            default:
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                return;
        }

       
        FindIterable<Document> employees = Connection.getCollection().find(query);

        if (employees.iterator().hasNext()) {
            System.out.println("--------------------------------------------------------------");
            System.out.printf("%-5s %-15s %-5s %-10s %-15s%n", "EID", "NAME", "AGE", "SALARY", "DESIGNATION");
            System.out.println("--------------------------------------------------------------");

            for (Document employee : employees) {
                System.out.printf("%-5d %-15s %-5d %-10.2f %-15s%n",
                        employee.getInteger("EID"),
                        employee.getString("NAME"),
                        employee.getInteger("AGE"),
                        employee.getDouble("SALARY"),
                        employee.getString("DESIGNATION"));
            }
        } else {
            System.out.println("No employee found matching the criteria.");
        }
    }
}

    
public class EmpApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        Connection.createConnection();
        EmpDbApp empDb = new EmpDbImpl(); 
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
                case 1 : {empDb.createEmployee(); break;}
                case 2 : {empDb.displayEmployees(); break;}
                case 3 : {empDb.raiseSalary(); break;}
                case 4 : {empDb.searchEmployee(); break;}
                case 5 : {System.out.println("Exiting..."); break;}
                default : {System.out.println("Invalid choice! Please try again."); break;}
            }
        } while (choice != 5);

        Connection.closeConnection();
    }
}


class Connection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void createConnection() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("employeeDB");
        collection = database.getCollection("employees");
        System.out.println("Connected to MongoDB");
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
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
