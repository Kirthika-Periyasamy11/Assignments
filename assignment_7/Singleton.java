class A{
private static A a1=null;
A(){
if(this instanceof B){
	System.out.println("B instance created");
}
else if(this instanceof A && a1==null){
	a1=this;
	System.out.println("A instance created");
}
else{
	throw new InstanceAlreadyExistsException("Instance already exist exception");
}
}

public static A getObject(){
	if (a1==null)
		a1=new A();
	return a1;
}
}

class B extends A{
private static final B b1=new B();
private B(){}
public static B getObject(){
	return b1;
}
}

class InstanceAlreadyExistsException extends RuntimeException{
InstanceAlreadyExistsException(){
super();}
InstanceAlreadyExistsException(String msg){
super(msg);
}
}
public class Singleton{
public static void main(String args[]){
	A a1=new A();
	//A a2=new A();
	System.out.println(a1);
	//System.out.println(a2);

	A a3=A.getObject();
	System.out.println(a3);
	B b1=B.getObject();
	System.out.println(b1);

}
}