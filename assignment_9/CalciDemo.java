import java.lang.reflect.Method;
import java.util.Scanner;

class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int mul(int a, int b) {
        return a * b;
    }

    public int div(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }
}

public class CalciDemo {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Class c1 = Class.forName("Calculator");
            Object calcInstance = c1.newInstance();

            System.out.print("Enter operation:");
            String operation = scanner.next();

            System.out.print("Enter parameter 1:");
            int op1 = scanner.nextInt();

            System.out.print("Enter parameter 2:");
            int op2 = scanner.nextInt();

	    Method[] methods=c1.getMethods();
	    int result=0;
	    boolean methodFound = false;
	    for(int i=0;i<4;i++){
		if(methods[i].getName().equals(operation)){
			result = (int) methods[i].invoke(calcInstance, op1, op2);
			methodFound=true;
			break;
		}
	}
           if (!methodFound) {
                throw new MethodException("Invalid operation: " + operation);
            }
	System.out.println("Result:" + result);
        } catch (ArithmeticException e) {
            System.out.println(e);
        } catch (MethodException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class MethodException extends Exception{
public MethodException(){
super();
}
public MethodException(String msg){
super(msg);
}

}

//getDeclaredMethods =>all the methods public,private,protected
//getMethod=>only return public method
