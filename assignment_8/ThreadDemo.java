import java.util.*;

class Biker extends Thread {
    String name;
    long startTime;
    long endTime;
    long timeTaken;
    int distanceCovered = 0;

    public static final Object lock = new Object();

    Biker(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(name + " is ready to start the race...");
        synchronized (Biker.lock) {
            try {
                Biker.lock.wait(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (TimeDaemon.class) {
            startTime = TimeDaemon.getCurrentTime();
        }

        while (distanceCovered < 100) {
            distanceCovered += 5;
            try {
                Thread.sleep((int) (Math.random() * 2000)); 
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted.");
            }
        }

        synchronized (TimeDaemon.class) {
            endTime = TimeDaemon.getCurrentTime();
        }

        timeTaken = endTime - startTime;

        synchronized (ThreadDemo.results) {
            ThreadDemo.results.add(
                "Biker name: " + name +
                ", Start Time: " + startTime +
                ", End Time: " + endTime +
                ", Time Taken: " + timeTaken
            );
        }
    }
}

class TimeDaemon extends Thread {
    private static int currentTime = 0;

    public TimeDaemon() {
        setDaemon(true);
    }

    public static synchronized int getCurrentTime() {
        return currentTime;
    }

    public void run() {
        while (true) {
            synchronized (TimeDaemon.class) {
                currentTime++;
            }
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                System.out.println("Daemon interrupted.");
            }
        }
    }
}

public class ThreadDemo {
    public static List<String> results = new ArrayList<>();

    public static void main(String[] args) {
        Biker[] bikers = new Biker[10];
        Scanner scanner = new Scanner(System.in);

        TimeDaemon timeDaemon = new TimeDaemon();
        timeDaemon.start();

        for (int i = 0; i < 10; i++) {
            System.out.print("Enter biker name: ");
            String name = scanner.nextLine();
            bikers[i] = new Biker(name);
        }

        for (Biker biker : bikers) {
            biker.start();
        }

        countdown();

        synchronized (Biker.lock) {
            Biker.lock.notifyAll();
        }

        for (Biker biker : bikers) {
            try {
                biker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        display();
    }

    public static void countdown() {
        for (int i = 10; i > 0; i--) {
            System.out.println(i);
            try {
                Thread.sleep(1000); // Countdown for 1 second
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Go!");
    }

    public static void display() {
        int rank = 1;
        System.out.println("\nRace results:");
        for (String result : results) {
            System.out.println("Rank " + rank + ": " + result);
            rank++;
        }
    }
}
