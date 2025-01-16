import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Ceo extends Employee{
private static Ceo c1=null;
private Ceo(int salary,String designation){
	super(salary, designation); 
}
public static Ceo getCeoObject(){
 	if (c1==null){
		c1=new Ceo(1000000,"CEO");
	}
	return c1;
}
public void raiseSalary() {
        setSalary(getSalary() + 100000); 
}
}

abstract class Employee{
private int empid;
private String name;
private int age;
private int salary;
private String designation;
static int count=0;
Employee(int salary, String designation){
empid=GetDetails.getId();
name = GetDetails.getName();
age = GetDetails.getAge(21, 60);
this.salary=salary;
this.designation=designation;

}

public static void createEmployee(int designation,Employee[] emp) {
	switch (designation) {
            case 1:
                emp[count]=Clerk.getClerk();
		count++;
		break;
		
            case 2:
                emp[count]=Programmer.getProgrammer();
		count++;
		break;
            case 3:
                emp[count]=Manager.getManager();
		count++;
		break;
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
public int getEmpId() {
    return this.empid;
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

class Clerk extends Employee{
    private Clerk(int salary, String designation){
       super(salary, designation); 
       System.out.println("Clerk Employee created successfully");
     }

    public static Clerk getClerk(){
      	return new Clerk(20000,"Clerk");
       
    }

    public void raiseSalary() {
        setSalary(getSalary() + 2000); 
    }
}

class Manager extends Employee{
    private Manager(int salary, String designation){
       super(salary, designation); 
       System.out.println("Manager Employee created successfully");
     }

    public static Manager getManager(){
      	return new Manager(30000,"Manager");
       
    }

    public void raiseSalary() {
        setSalary(getSalary() + 5000); 
    }
}

class Programmer extends Employee{
    private Programmer(int salary, String designation){
       super(salary, designation); 
       System.out.println("Programmer Employee created successfully");
     }

    public static Programmer getProgrammer(){
      	return new Programmer(30000,"Manager");
       
    }

    public void raiseSalary() {
        setSalary(getSalary() + 15000); 
    }
}



public class EmpDesignPattern{

public static Employee emp[] = new Employee[100];
public static void main(String args[]){
	start();
}

public static void start(){
	System.out.println("Creating object of Ceo");
	Ceo c1 =Ceo.getCeoObject();
	c1.display();
	createEmployees();
}

public static void createEmployees(){
boolean in = true;
        while (in) {
		System.out.println("1.Create\n2.Display\n3.Raise Salary\n4.Delete\n5.Exit");
                int choice = Menu.readChoice(5);
		
		switch (choice) {
                    case 1:
                        int designation;
                        do {
				
                      
                                System.out.println("Enter Designation");
                                System.out.println("1.Clerk\n2.Programmer\n3.Manager\n4.Exit");
				
                                designation = Menu.readChoice(4);
				 if (designation != 4) {
				Employee.createEmployee(designation,emp);}
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

                }
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