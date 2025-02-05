import java.util.*;
import java.util.regex.*;

abstract class Employee {
    private int empid;
    private String name;
    private int age;
    private String designation;
    private int salary;

    public Employee(String designation, int salary) {
        this.designation = designation;
        this.salary = salary;
    }

    public void getDetails(Map<Integer, Employee> empMap) {
        empid = GetDetails.getId(empMap);
        name = GetDetails.getName();
        age = GetDetails.getAge(21, 60);
    }

    public final void display() {
        System.out.println("Id of the employee: " + this.empid);
        System.out.println("Name of the employee: " + this.name);
        System.out.println("Age of the employee: " + this.age);
        System.out.println("Designation of the employee: " + this.designation);
        System.out.println("Salary of the employee: " + this.salary);
        System.out.println();
    }

    protected void setSalary(int salary) {
        this.salary = salary;
    }

    public int getEmpId() {
        return this.empid;
    }

     public int getAge() { 
        return this.age;
    }
    public String getName() {
        return this.name;
    }

    public String getDesignation() {
        return this.designation;
    }

    protected int getSalary() {
        return this.salary;
    }

    public abstract void raiseSalary();

    public static void deleteEmployee(Map<Integer, Employee> empMap) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee id: ");
        int employeeId = scanner.nextInt();

        if (empMap.containsKey(employeeId)) {
            Employee emp = empMap.get(employeeId);
            emp.display();
            System.out.print("Do you want to delete the employee? (y/n): ");
            scanner.nextLine();
            String delete = scanner.nextLine();
            if (delete.equalsIgnoreCase("y")) {
                empMap.remove(employeeId);
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not deleted.");
            }
        } else {
            System.out.println("Given Employee id doesn't exist.");
        }
    }
}

final class Clerk extends Employee {
    public Clerk(Map<Integer, Employee> empMap) {
        super("Clerk", 20000);
        getDetails(empMap);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 2000);
    }
}

final class Manager extends Employee {
    public Manager(Map<Integer, Employee> empMap) {
        super("Manager", 30000);
        getDetails(empMap);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 15000);
    }
}

final class Programmer extends Employee {
    public Programmer(Map<Integer, Employee> empMap) {
        super("Programmer", 10000);
        getDetails(empMap);
    }

    public void raiseSalary() {
        setSalary(getSalary() + 5000);
    }
}

public class EmpSorter {
    public static void main(String[] args) {
        boolean in = true;
        Map<Integer, Employee> empMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (in) {
            System.out.println("1. Create\n2. Display\n3. Raise Salary\n4. Delete\n5. Search\n6. Exit");
            int choice = Menu.readChoice(6);

            switch (choice) {
                case 1:
                    int designation;
                    do {
                        System.out.println("Enter Designation");
                        System.out.println("1. Clerk\n2. Programmer\n3. Manager\n4. Exit");
                        designation = Menu.readChoice(4);
                        switch (designation) {
                            case 1:
                                Employee clerk = new Clerk(empMap);
                                empMap.put(clerk.getEmpId(), clerk);
                                break;
                            case 2:
                                Employee programmer = new Programmer(empMap);
                                empMap.put(programmer.getEmpId(), programmer);
                                break;
                            case 3:
                                Employee manager = new Manager(empMap);
                                empMap.put(manager.getEmpId(), manager);
                                break;
                            case 4:
                                System.out.println("Exiting designation creation.");
                                break;
                        }
                    } while (designation != 4);
                    break;

                case 2:
                    System.out.println("Enter the criteria for sorting: ");
                    System.out.println("1. Id\n2. Name\n3. Age\n4. Salary\n5. Designation");
                    int sortChoice = Menu.readChoice(5);

                    List<Employee> empList = new ArrayList<>(empMap.values());
                    switch (sortChoice) {
                        case 1:
                            empList.sort(new IdSorter());
                            break;
                        case 2:
                            empList.sort(new NameSorter());
                            break;
                        case 3:
                            empList.sort(new AgeSorter());
                            break;
                        case 4:
                            empList.sort(new SalarySorter());
                            break;
                        case 5:
                            empList.sort(new DesignationSorter());
                            break;
                    }

                    for (Employee emp : empList) {
                        emp.display();
                    }
                    break;

                case 3:
                    for (Employee emp : empMap.values()) {
                        emp.raiseSalary();
                    }
                    System.out.println("Salaries updated successfully.");
                    break;

                case 4:
                    Employee.deleteEmployee(empMap);
                    break;

                case 5:
                    System.out.println("Enter the criteria for searching: ");
                    System.out.println("1. Id\n2. Name\n3. Designation");
                    int searchChoice = Menu.readChoice(3);
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter employee id to search: ");
                            int searchId = scanner.nextInt();
                            if (empMap.containsKey(searchId)) {
                                empMap.get(searchId).display();
                            } else {
                                System.out.println("Employee not found.");
                            }
                            break;
                        case 2:
                            System.out.print("Enter name to search: ");
                            scanner.nextLine();
                            String sname = scanner.nextLine();
                            for (Employee emp : empMap.values()) {
                                if (emp.getName().equalsIgnoreCase(sname)) {
                                    emp.display();
                                }
                            }
                            break;
                        case 3:
                            System.out.print("Enter designation to search: ");
                            scanner.nextLine();
                            String sdesig = scanner.nextLine();
                            for (Employee emp : empMap.values()) {
                                if (emp.getDesignation().equalsIgnoreCase(sdesig)) {
                                    emp.display();
                                }
                            }
                            break;
                        default:
                            System.out.println("Enter valid option.");
                            break;
                    }
                    break;

                case 6:
                    System.out.println("Exiting the main menu.");
                    in = false;
                    break;
            }
        }
        scanner.close();
    }
}

class Menu {
    private static int maxChoice;

    public static int readChoice(int max) {
        maxChoice = max;
        Scanner s = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter the choice: ");
                int choice = s.nextInt();
                if (choice < 1 || choice > maxChoice) {
                    throw new InvalidChoiceException();
                }
                return choice;
            } catch (InvalidChoiceException e) {
                e.displayMessage(maxChoice);
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                s.nextLine();
            }
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

    public static int getId(Map<Integer, Employee> empMap) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter employee ID: ");
                int id = scanner.nextInt();
                if (empMap.containsKey(id)) {
                    throw new IdAlreadyExistsException("Entered id: " + id + " already exists.");
                }
                return id;
            } catch (IdAlreadyExistsException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
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

class UserException extends Exception {
    public UserException() {
        super();
    }

    public UserException(String msg) {
        super(msg);
    }
}

class IdAlreadyExistsException extends Exception {
    public IdAlreadyExistsException() {
        super();
    }

    public IdAlreadyExistsException(String msg) {
        super(msg);
    }
}

class InvalidChoiceException extends Exception {
    public InvalidChoiceException() {
        super();
    }

    public InvalidChoiceException(String msg) {
        super(msg);
    }

    public void displayMessage(int max) {
        System.out.println("Enter a number between 1 and " + max);
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
    public AgeException() {
        super();
    }

    public AgeException(String msg) {
        super(msg);
    }
}

class IdSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.getEmpId(), e2.getEmpId());
    }
}

class NameSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return e1.getName().compareToIgnoreCase(e2.getName());
    }
}

class AgeSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.getAge(), e2.getAge());
    }
}

class SalarySorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.getSalary(), e2.getSalary());
    }
}

class DesignationSorter implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return e1.getDesignation().compareToIgnoreCase(e2.getDesignation());
    }
}
