package employee.assg;
import java.util.*;
abstract class Employee{
private int empid;
private String name;
private int age;
private String designation;
private int salary;
static int count=0;

public Employee(String designation, int salary ){
this.designation=designation;
this.salary=salary;
count++;
}

public void getdetails(Employee[] emp) {
    Scanner scanner = new Scanner(System.in);
    do {
        System.out.println("Enter your id:");
	try{
        empid = scanner.nextInt();
        scanner.nextLine();
	validate(emp, empid);
	break;
       	}
	catch(IdAlreadyExistsException e){
		System.out.println(e.getMessage());
	}
	catch(InputMismatchException e){
	System.out.println("Invalid input");
	scanner.nextLine();
	} 
        
    } while (true);

    System.out.println("Enter your name:");
    name = scanner.nextLine();
    do{
    System.out.println("Enter your age:");
    try {
        age = scanner.nextInt();
        scanner.nextLine(); 
        validateAge(age);
	break;
    } catch (UserException e) {
        System.out.println(e.getMessage());
    } catch (InputMismatchException e) {
        System.out.println("Invalid input");
        scanner.nextLine(); 
    }
    }while(true);
}


public void validate(Employee emp[], int id) throws IdAlreadyExistsException {
    for (int i = 0; i < count; i++) {  
        if (emp[i] != null && emp[i].empid == id) {
             throw new IdAlreadyExistsException("Entered id: "+id+" already exists.");
        }
    }
    
}
public void validateAge(int age) throws UserException {
    if (age < 21 || age > 60) {
        throw new UserException("Invalid age: " + age + ".Age must be between 21 and 60.");
    }
}
public final void display(){
System.out.println("Id of the employee: "+this.empid);
System.out.println("Name of the employee: "+this.name);
System.out.println("Age of the employee: "+this.age);
System.out.println("Designation of the employee: "+this.designation);
System.out.println("Salary of the employee: "+this.salary);
System.out.println("\n");
}
protected void setSalary(int salary) {
        this.salary = salary;
    }

protected int getSalary() {
        return this.salary;
}
public abstract void raiseSalary();

public  static void deleteEmployee(Employee[] emp){
	Scanner scanner=new Scanner(System.in);
	System.out.println("Enter employee id: ");
	int employeeId =scanner.nextInt();
	scanner.nextLine();
	int find=0;
	for (int i=0;i<count;i++){
	if (emp[i]!=null && emp[i].empid==employeeId){
		find=1;
		emp[i].display();
		System.out.println("Do you want to delete the employee");
		String delete=scanner.nextLine();
		if (delete.equalsIgnoreCase("y")){
			emp[i]=null;
			for(int j=i;j<count-1;j++){
			emp[j]=emp[j+1];
			}
			emp[count-1]=null;
			count--;
			
		
		System.out.println("Employee deleted successfully.");
		
		}
		else{
			System.out.println("Employee not deleted.");
			
		}  
		break;    
	}
	}
	if(find==0){
		System.out.println("Given Employee id doesnt exist");
	}
}
}



final class Clerk extends Employee{
public Clerk(Employee emp[]){
super("Clerk",20000);
getdetails(emp);
}
public void raiseSalary() {
setSalary(getSalary() + 2000); 
}
}

final class Manager extends Employee{
public Manager(Employee emp[]){
super("Manager",30000);
getdetails(emp);
}
public void raiseSalary() {
setSalary(getSalary() + 15000); 
}
}

final class Programmer extends Employee{
public Programmer(Employee emp[]){
super("Programmer",10000);
getdetails(emp);
}
public void raiseSalary() {
setSalary(getSalary() + 5000); 
}
}



class EmployeeManagement {
    public static void main(String args[]) {
        boolean in = true;
        Employee emp[] = new Employee[100];
        Scanner scanner = new Scanner(System.in);

        while (in) {
            try {
		
                System.out.println("Enter your choice:");
                System.out.println("1.Create\n2.Display\n3.Raise Salary\n4.Delete\n5.Exit");
                int choice = scanner.nextInt();
		
                scanner.nextLine(); 
		
                switch (choice) {
                    case 1:
                        int designation = 0;
                        do {
                            try {
                                System.out.println("Enter your designation:");
                                System.out.println("1.Clerk\n2.Programmer\n3.Manager\n4.Exit");
                                designation = scanner.nextInt();
                                scanner.nextLine(); 

                                switch (designation) {
                                    case 1:
                                        emp[Employee.count] = new Clerk(emp);
                                        break;
                                    case 2:
                                        emp[Employee.count] = new Programmer(emp);
                                        break;
                                    case 3:
                                        emp[Employee.count] = new Manager(emp);
                                        break;
                                    case 4:
                                        System.out.println("Exiting designation creation.");
                                        break;
                                    default:
                                        throw new InvalidDesignationException("Enter valid input between 1 and 4");
                                }
                                
                            } catch(InvalidDesignationException e){
				System.out.println(e.getMessage());
				}
				catch (InputMismatchException e) {
                                System.out.println("Invalid input.");
                                scanner.nextLine();
                            }
                        } while (designation != 4);
                        break;

                    case 2:
                        for (Employee e : emp) {
                            if (e != null) {
                                e.display();
                            }
                        }
                        break;

                    case 3:
                        for (Employee e : emp) {
                            if (e != null) {
                                e.raiseSalary();
                            }
                        }
                        System.out.println("Salary of the employees is updated.");
                        break;

                    case 4:
                        Employee.deleteEmployee(emp);
                        break;

                    case 5:
                        System.out.println("Exiting the main menu.");
                        System.out.println("Total number of employees created: " + Employee.count);
                        in = false;
                        break;

                    default:
                        throw new InvalidChoiceException("Enter valid input between 1 and 5");
                }
            } catch(InvalidChoiceException e){
		System.out.println(e.getMessage());
		}
		catch (InputMismatchException e) {
                System.out.println("Invalid input. ");
                scanner.nextLine(); 
            }
        }
    }
}

class UserException extends Exception{
public UserException(){
super();
}
public UserException(String msg){
super(msg);
}

}
class IdAlreadyExistsException extends Exception{
public IdAlreadyExistsException(){
super();
}
public IdAlreadyExistsException(String msg){
super(msg);
}
}
class InvalidChoiceException extends Exception{
public InvalidChoiceException(){
super();
}
public InvalidChoiceException(String msg){
super(msg);
}
}
class InvalidDesignationException extends Exception{
public InvalidDesignationException(){
super();
}
public InvalidDesignationException(String msg){
super(msg);
}
}
