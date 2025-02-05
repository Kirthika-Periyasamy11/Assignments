interface I {
    Thread createThread(Runnable r);
}

class A {
    public void print1to10() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Range : " + i);
        }
    }
}

class B {
    public void evenTill50() {
        for (int i = 0; i <= 50; i += 2) {
            System.out.println("Even : " + i);
        }
    }
}

class C {
    public void fibonacciFrom1to1000() {
        int a = 1, b = 1;
        System.out.println("Fibonacci : " + a);
        System.out.println("Fibonacci : " + b);
        while (true) {
            int next = a + b;
            if (next > 1000) break;
            System.out.println("Fibonacci : " + next);
            a = b;
            b = next;
        }
    }
}

public class Multithreading {
    public static void main(String args[]) {
		
        I i1 = Thread::new;
        
        //A a = new A();
        B b = new B();
        C c = new C();
        
        Thread t1 = i1.createThread(new A()::print1to10);
        Thread t2 = i1.createThread(b::evenTill50);
        Thread t3 = i1.createThread(() -> c.fibonacciFrom1to1000());
        
        //t1.start();
        t2.start();
        //t3.start();
    }
}
