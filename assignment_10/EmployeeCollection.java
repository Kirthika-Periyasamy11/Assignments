import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Ceo extends Employee {
    private static Ceo c1 = null;

    private Ceo(int salary, String designation) {
        super(salary, designation);
    }

    public static Ceo getCeoObject() {
        if (c1 == null) {
            c1 = new Ceo(1000000, "CEO");
        }
        return c1;
    }

    public void raiseSalary() {
        setSalary(getSalary() + 100000);
    }
}

abstract class Employee {
    private int empid;
    private String name;
    private int age;
    private int salary;
    private String designation;

    Employee(int salary, String designation) {
        this.empid = GetDetails.getId();
        this.name = GetDetails.getName();
        this.age = GetDetails.getAge(21, 60);
        this.salary = salary;
        this.designation = designation;
    }
    public int getEmpId(){
		return empid;
	}

    public final void display() {
        System.out.println("Id: " + empid + " | Name: " + name + " | Age: " + age + " | Designation: " + designation + " | Salary: " + salary);
    }

    protected void setSalary(int salary) {
        this.salary = salary;
    }

    protected int getSalary() {
        return this.salary;
    }

    public abstract void raiseSalary();
}

class Clerk extends Employee {
    Clerk(int salary, String designation) {
        super(salary, designation);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 2000);
    }
}

class Manager extends Employee {
    Manager(int salary, String designation) {
        super(salary, designation);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 5000);
    }
}

class Programmer extends Employee {
    Programmer(int salary, String designation) {
        super(salary, designation);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 15000);
    }
}

public class EmployeeCollection {
    private static Map<Integer, Employee> employees = new HashMap<>();
    private static int count = 0;

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        System.out.println("Creating CEO object");
        Ceo c1 = Ceo.getCeoObject();
        c1.display();
        createEmployees();
    }

    public static void createEmployees() {
        boolean in = true;
        while (in) {
            System.out.println("1.Create\n2.Display\n3.Raise Salary\n4.Delete\n5.Exit");
            int choice = Menu.readChoice(5);

            switch (choice) {
                case 1:
                    int designation;
                    do {
                        System.out.println("Enter Designation: 1.Clerk 2.Programmer 3.Manager 4.Exit");
                        designation = Menu.readChoice(4);
                        if (designation != 4) {
                            Employee emp = createEmployee(designation);
                            employees.put(emp.getEmpId(), emp);
                        }
                    } while (designation != 4);
                    break;
                case 2:
                    Iterator<Employee> iterator = employees.values().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().display();
                    }
                    break;
                case 3:
                    iterator = employees.values().iterator();
                    while (iterator.hasNext()) {
                        iterator.next().raiseSalary();
                    }
                    System.out.println("Salaries updated.");
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    System.out.println("Exiting. Total employees: " + employees.size());
                    in = false;
                    break;
            }
        }
    }

    public static Employee createEmployee(int designation) {
        switch (designation) {
            case 1:
                return new Clerk(20000, "Clerk");
            case 2:
                return new Programmer(30000, "Programmer");
            case 3:
                return new Manager(50000, "Manager");
            default:
                return null;
        }
    }

    public static void deleteEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Employee ID to delete: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine();

        if (employees.containsKey(employeeId)) {
            Employee emp = employees.get(employeeId);
            emp.display();
            System.out.print("Confirm delete? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                employees.remove(employeeId);
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not deleted.");
            }
        } else {
            System.out.println("Employee ID does not exist.");
        }
    }
}

class GetDetails{
public static int getAge(int min,int max){
	Scanner scanner = new Scanner(System.in);
	
	while(true){
		try{
		System.out.print("Enter age: ");
  		int age=scanner.nextInt();
		
		if(age>=min && age<=max){
			return age;
		}
		else{
			throw new AgeException("Please enter a valid age between 21 and 60.");
		}
		}
		catch(AgeException e){
			System.out.println(e.getMessage());
		}
		catch(InputMismatchException e){
			System.out.println("Age can only be number.");
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
            }  catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }



public static String getName(){
	while(true){
		try{
		System.out.print("Enter name: ");
  		String name=new Scanner(System.in).nextLine();
		Pattern p1=Pattern.compile("([A-Z][a-z]*)+\s([A-Z])+([a-z]*)*");
		Matcher m1=p1.matcher(name);
		if(m1.matches()){
			return name;
		}
		else{
			throw new NameException("Please enter a valid name.");
		}
		}
		catch(NameException e){
			System.out.println(e.getMessage());
		}
	}
}
}

class InvalidChoiceException extends Exception{
public InvalidChoiceException(){
super();
}
public InvalidChoiceException(String msg){
super(msg);
}

public void displayMessage(int max){
System.out.println("Enter a number between 1 and "+max);
}
}

class Menu{
	private static int maxChoice;
	public static int readChoice(int max){
		maxChoice=max;
		Scanner s=new Scanner(System.in);
		while(true){
			try{
				System.out.print("Enter the choice: ");
				int choice=s.nextInt();
				s.nextLine();
				if (choice<1 || choice>maxChoice)
					throw new InvalidChoiceException();
				return choice;
			}
			catch(InvalidChoiceException e){
				e.displayMessage(maxChoice);
			}
			catch(InputMismatchException e){
				System.out.println("Please enter a number");
				s.nextLine();
			
			}
		}
	}
}


class NameException extends Exception{
public NameException(){
super();
}
public NameException(String msg){
super(msg);
}
}

class AgeException extends Exception{
public AgeException(){
super();
}
public AgeException(String msg){
super(msg);
}
}