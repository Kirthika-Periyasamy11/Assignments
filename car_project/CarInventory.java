import java.sql.*;
import java.util.*;


abstract class Car {
    protected int carID;
    protected String company;
    protected Model model;
    protected boolean sold;
    protected String type;

    public Car(int carID, String company, Model model, String type) {
        this.carID = carID;
        this.company = company;
        this.model = model;
        this.sold = false;
        this.type = type;
    }

    public int getCarID() { return carID; }
    public String getCompany() { return company; }
    public Model getModel() { return model; }
    public boolean isSold() { return sold; }
    public void markAsSold() { this.sold = true; }
    public String getType() { return type; }
}


class SUV extends Car {
    public SUV(int carID, String company, Model model) { super(carID, company, model,"SUV"); }
}

class Sedan extends Car {
    public Sedan(int carID, String company, Model model) { super(carID, company, model,"Sedan"); }
}


class Model {
    private String modelName;
    private int seater;
    private String fuelType;
    private double price;

    public Model(String modelName, int seater, String fuelType, double price) {
        this.modelName = modelName;
        this.seater = seater;
        this.fuelType = fuelType;
        this.price = price;
    }

    public String getModelName() { return modelName; }
    public int getSeater() { return seater; }
    public String getFuelType() { return fuelType; }
    public double getPrice() { return price; }
}

class Database {
    private static Connection connection;

    public static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/car_inventory", "postgres", "password");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}

class CarInventoryService {
    public void insertCar(Car car) {
        try {
            String sql = "INSERT INTO Cars (CarID, Company, Model, Seater, FuelType, Price, Sold, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
            stmt.setInt(1, car.getCarID());
            stmt.setString(2, car.getCompany());
            stmt.setString(3, car.getModel().getModelName());
            stmt.setInt(4, car.getModel().getSeater());
            stmt.setString(5, car.getModel().getFuelType());
            stmt.setDouble(6, car.getModel().getPrice());
            stmt.setBoolean(7, car.isSold());
            stmt.setString(8, car.getType());
            stmt.executeUpdate();
            System.out.println("Car Added Successfully!");
        } catch (SQLException e) {
            System.out.println("Error Inserting Car: " + e.getMessage());
        }
    }
    public void insertModel(Model model) {
        String sql = "INSERT INTO Models (ModelName, Seater, FuelType, Price) VALUES (?, ?, ?, ?) RETURNING ModelID";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, model.getModelName());
            stmt.setInt(2, model.getSeater());
            stmt.setString(3, model.getFuelType());
            stmt.setDouble(4, model.getPrice());
            stmt.executeUpdate();
           
        } catch (SQLException e) {
            System.out.println("Error Inserting Model: " + e.getMessage());
        }
    }

    public void displayCars(String condition) {
        try {
            String sql = "SELECT * FROM Cars WHERE " + condition;
            PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Car ID: " + rs.getInt("CarID"));
                System.out.println("Company: " + rs.getString("Company"));
                System.out.println("Model: " + rs.getString("Model"));
                System.out.println("Price: " + rs.getDouble("Price"));
                System.out.println("Sold: " + rs.getBoolean("Sold"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error Fetching Cars: " + e.getMessage());
        }
    }

    public void updateCarSold(int carID) {
        try {
            String sql = "UPDATE Cars SET Sold = true WHERE CarID = ?";
            PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
            stmt.setInt(1, carID);
            stmt.executeUpdate();
            System.out.println("Car marked as sold successfully!");
        } catch (SQLException e) {
            System.out.println("Error Updating Car: " + e.getMessage());
        }
    }
    public void displayAllSoldCars() {
        try {
            String sql = "SELECT * FROM Cars WHERE Sold = true";
            PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            displayCars(rs);
        } catch (SQLException e) {
            System.out.println("Error Fetching Sold Cars: " + e.getMessage());
        }
    }
    private void displayCars(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("Car ID: " + rs.getInt("CarID"));
            System.out.println("Company: " + rs.getString("Company"));
            System.out.println("Model: " + rs.getString("Model"));
            System.out.println("Price: $" + rs.getDouble("Price"));
            System.out.println("Sold: " + rs.getBoolean("Sold"));
            System.out.println("-------------------------");
        }
    }

    public void markCarAsSold(int carID) {
        try {
            String sql = "UPDATE Cars SET Sold = true WHERE CarID = ?";
            PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
            stmt.setInt(1, carID);
            stmt.executeUpdate();
            System.out.println("Car marked as sold successfully!");
        } catch (SQLException e) {
            System.out.println("Error Updating Car: " + e.getMessage());
        }
    }
}
public class CarInventory {
    public static void main(String[] args) {
    	 Scanner sc = new Scanner(System.in);
         Database.connect(); 
         CarInventoryService service = new CarInventoryService();
         
         CarDetailsInput inputHandler = new CarDetailsInput(sc);

        while (true) {
            System.out.println("1. Add Car\n2. Search Cars\n3. Sold Cars\n4. Exit");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                	Model model = inputHandler.getModelDetails();
                    service.insertModel(model);

                    Car car = inputHandler.getCarDetails(model);
                    service.insertCar(car);
                    
                }
                case 2 -> {
                    System.out.println("1. All Unsold\n2. By Company\n3. By Type\n4. By Price Range\n5. Exit");
                    System.out.print("Enter Choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();

                    switch (searchChoice) {
                        case 1 -> service.displayCars("Sold = false");
                        case 2 -> {
                            System.out.print("Enter Company: ");
                            String company = sc.nextLine();
                            service.displayCars("Company = '" + company + "'");
                        }
                        case 3 -> {
                            System.out.print("Enter Type: ");
                            String type = sc.nextLine();
                            service.displayCars("Type = '" + type + "'");
                        }
                        case 4 -> {
                            System.out.print("Enter Min Price: ");
                            double min = sc.nextDouble();
                            System.out.print("Enter Max Price: ");
                            double max = sc.nextDouble();
                            service.displayCars("Price BETWEEN " + min + " AND " + max);
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter Car ID to Mark as Sold: ");
                    int carID = sc.nextInt();
                    service.updateCarSold(carID);
                }
                case 4 ->{
                	 System.out.println("4. Sold\n------------\n1. ALL (All sold cars)\n2. Update (Mark car as sold)\n3. Exit");
                     System.out.print("Enter Choice: ");
                     int soldChoice = sc.nextInt();
                     sc.nextLine();

                     switch (soldChoice) {
                         case 1 -> service.displayAllSoldCars();
                         case 2 -> {
                             System.out.print("Enter Car ID to Mark as Sold: ");
                             int carID = sc.nextInt();
                             service.markCarAsSold(carID);
                         }
                         case 3 -> {
                             System.out.println("Exiting Sold Cars Menu...");
                             return;
                         }
                         default -> System.out.println("Invalid Choice!");
                     }
                	
                }
                case 5 -> System.exit(0);
            }
        }
    }
}


class CarDetailsInput {
    private Scanner sc;

    public CarDetailsInput(Scanner sc) {
        this.sc = sc;
    }

    public Model getModelDetails() {
        System.out.print("Enter Model Name: ");
        String modelName = sc.nextLine();
        System.out.print("Enter Seater: ");
        int seater = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Fuel Type: ");
        String fuelType = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        return new Model(modelName, seater, fuelType, price);
    }

    public Car getCarDetails(Model model) {
        System.out.print("Enter Car ID: ");
        int carID = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Car Type: ");
        String type = sc.nextLine();
        System.out.print("Enter Company: ");
        String company = sc.nextLine();
        
        return CarFactory.createCar(type, carID, company,model);
    }
}

class CarFactory {
    public static Car createCar(String type, int carID, String company, Model model) {
        return switch (type.toLowerCase()) {
            case "suv" -> new SUV(carID, company, model);
            case "sedan" -> new Sedan(carID, company, model);
            default -> throw new IllegalArgumentException("Invalid Car Type");
        };
    }
}

