import java.util.*;
class Employee{
private String name;
private int age;
private String designation;
private int salary;

public Employee(String name, int age, String designation, int salary ){
this.name=name;
this.age=age;
this.designation=designation;
this.salary=salary;
}

public void display(){
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
public void raiseSalary(){
System.out.println("Raising salary of employees");
}
}

class Clerk extends Employee{
public Clerk(String name, int age, String designation, int salary){
super(name,age,designation,salary);
}
public void raiseSalary() {
setSalary(getSalary() + 2000); 
}
}

class Manager extends Employee{
public Manager(String name, int age, String designation, int salary){
super(name,age,designation,salary);
}
public void raiseSalary() {
setSalary(getSalary() + 15000); 
}
}

class Programmer extends Employee{
public Programmer(String name, int age, String designation, int salary){
super(name,age,designation,salary);
}
public void raiseSalary() {
setSalary(getSalary() + 5000); 
}
}



class EmployeeManagement{
public static void main(String args[]){
	boolean in=true;
	ArrayList<Employee> employees=new ArrayList<>();
	Scanner scanner=new Scanner(System.in);
	while(in){
	System.out.println("Enter your choice:");
	System.out.println("1.Create\n2.Display\n3.Raise Salary\n4.Exit");
	int choice=scanner.nextInt();
	switch(choice){
		case 1:
			boolean keep_creating=true;
			while(keep_creating){
			System.out.println("Enter your designation:");
			System.out.println("1.Clerk\n2.Progammer\n3.Manager\n4.Exit");
			int designation=scanner.nextInt();
			scanner.nextLine();
			if (designation==1||designation==2||designation==3){
				System.out.println("Enter your name:");
				String name=scanner.nextLine();
				System.out.println("Enter your age:");
				int age=scanner.nextInt();
			switch(designation){
			case 1:
				employees.add(new Clerk(name,age,"Clerk",20000));
				break;
			case 2:
				
				employees.add(new Programmer(name,age,"Programmer",30000));
				break;
			case 3:
				employees.add(new Manager(name,age,"Manager",100000));
				break;
			}
			}
			else if(designation==4){
				System.out.println("Returning back to main menu");
				keep_creating=false;
				break;
			}
			else{
				System.out.println("Invalid option. Please enter valid input");
			}

			}
			break;
		case 2:
			for (Employee e:employees){
			e.display();
			}
			break;
		case 3:
			for (Employee e:employees){
			e.raiseSalary();
			}
			System.out.println("Salary of the employees are updated");
			break;
	

		case 4:
			System.out.println("Exiting the main menu");
			System.out.println("Total number of employees created: "+employees.size());
			in=false;
			break;
		
		default:
			System.out.println("Invalid option. Please enter valid input");
		}

	}

}
}