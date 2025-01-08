class Employee{
private String name;
private int age;
private int salary;
private String designation;

public Employee(String name, int age, int salary,String designation){
validate_designation(designation);
this.name=name;
this.age=age;
this.salary=salary;
}


public String getname(){
	return name;
}
public void setname(String name){
	this.name=name;
}
public int getsalary(){
	return salary;
}
public void setsalary(int salary){
	this.salary=salary;
}

public void display(){
System.out.println("Name of the employee is"+this.name);
System.out.println("Age of the employee is"+this.age);
System.out.println("Salary of the employee is"+this.salary);
System.out.println("Designation of the employee is"+this.designation);
}

public void validate_designation(String designation){
if (!designation.equalsIgnoreCase("clerk")&&!designation.equalsIgnoreCase("manager")&&!designation.equals("programmer")){
throw new IllegalArgumentException("Invalid Designation: "+designation);
}
}
}

class Clerk extends Employee{

public Clerk(String name, int age, int salary,String designation){
super(name,age,salary,designation);
}
public void raise_salary(int increment){
	System.out.println("Name of the Clerk: " + super.getname());
	int salary=super.getsalary();
	int updated_salary=increment+salary;
	super.setsalary(updated_salary);
	System.out.println("Updated salary of the Clerk: "+super.getsalary());
}
}

class Programmer extends Employee{

public Programmer(String name, int age, int salary,String designation){
super(name,age,salary,designation);
}
public void raise_salary(int increment){
	System.out.println("Name of the Programmer: " + super.getname());
	int salary=super.getsalary();
	int updated_salary=increment+salary;
	super.setsalary(updated_salary);
	System.out.println("Updated salary of the Programmer: "+super.getsalary());
}
}

class Manager extends Employee{
public Manager(String name, int age, int salary,String designation){
super(name,age,salary,designation);

}
public void raise_salary(int increment){
	System.out.println("Name of the Designer: " + super.getname());
	int salary=super.getsalary();
	int updated_salary=increment+salary;
	super.setsalary(updated_salary);
	System.out.println("Updated salary of the Designer: "+super.getsalary());

}
} 

public class Main{
public static void main(String[] args){
Employee e1=new Employee("aaa",20,1000,"clerk");
System.out.println(e1.getname());
Clerk c=new Clerk("Kalai",23,1000,"clerk");
c.raise_salary(100);


Programmer p=new Programmer("Kirthika",21,2000,"programmer");
p.raise_salary(100);

Manager d=new Manager("bbb",21,2000,"Manager");
d.setname("John");
d.setsalary(3000);
d.raise_salary(100);

}
} 