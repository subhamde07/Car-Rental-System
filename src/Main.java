import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Car {
    private final String carId;
    private final String brand;
    private final String model;
    private final Double basePriceValue;
    private Boolean isAvailable;

    public Car(String carId, String brand, String model, Double basePriceValue) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePriceValue = basePriceValue;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculateTotalPrice(int days) {
        return ( basePriceValue * days );
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private final String customerId;
    private final String name;
    private final int age;
    private final String license;

    public Customer(String customerId, String name, int age, String license) {
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.license = license;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLicense() {
        return license;
    }
}

class Rental {
    private final Car car;
    private final Customer customer;
    private final int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private final List<Car> cars;
    private final List<Customer> customers;
    private final List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if ( car.isAvailable() ) {
            car.rent();
            rentals.add(new Rental(car, customer, days));

        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for ( Rental rental : rentals ) {
            if ( rental.getCar() == car ) {
                rentalToRemove = rental;
                break;
            }
        }
        if ( rentalToRemove != null ) {
            rentals.remove(rentalToRemove);

        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while ( true ) {
            System.out.println("<====== Car Rental System ======>");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if ( choice == 1 ) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your Name: ");
                String customerName = sc.nextLine();
                System.out.print("Enter your Age: ");
                int customerAge = sc.nextInt();
                if ( customerAge >= 18 ) {
                    System.out.print("Enter your Licence Number: ");
                    String customerLicence = sc.nextLine();
                    sc.nextLine();

                    System.out.println("\nAvailable Cars:");
                    for ( Car car : cars ) {
                        if ( car.isAvailable() ) {
                            System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                        }
                    }

                    System.out.print("\nEnter the car ID you want to rent: ");
                    String carId = sc.nextLine();

                    System.out.print("Enter the number of days for rental: ");
                    int rentalDays = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    Customer newCustomer = new Customer("CUSTOMER" + ( customers.size() + 1 ), customerName, customerAge, customerLicence);
                    addCustomer(newCustomer);

                    Car selectedCar = null;
                    for ( Car car : cars ) {
                        if ( car.getCarId().equals(carId) && car.isAvailable() ) {
                            selectedCar = car;
                            break;
                        }
                    }

                    if ( selectedCar != null ) {
                        double totalPrice = selectedCar.calculateTotalPrice(rentalDays);
                        System.out.println("\n=== Rental Information ===\n");
                        System.out.println("Customer ID: " + newCustomer.getCustomerId());
                        System.out.println("Customer Name: " + newCustomer.getName());
                        System.out.println("Customer Age: " + newCustomer.getAge());
                        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                        System.out.println("Rental Days: " + rentalDays);
                        System.out.printf("Total Price: $%.2f%n", totalPrice);

                        System.out.print("\nConfirm rental (Y/N): ");
                        String confirm = sc.nextLine();

                        if ( confirm.equalsIgnoreCase("Y") ) {
                            rentCar(selectedCar, newCustomer, rentalDays);
                            System.out.println("\nCar rented successfully.");
                        } else {
                            System.out.println("\nRental canceled.");
                        }
                    } else {
                        System.out.println("\nInvalid car selection or car not available for rent.");
                    }
                } else {
                    System.out.println("\nSorry!! you are underage.");
                    continue;
                }
            } else if ( choice == 2 ) {
                System.out.println("\n=== Return a Car ===\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = sc.nextLine();

                Car carToReturn = null;
                for ( Car car : cars ) {
                    if ( car.getCarId().equals(carId) && ! car.isAvailable() ) {
                        carToReturn = car;
                        break;
                    }
                }

                if ( carToReturn != null ) {
                    Customer customer = null;
                    for ( Rental rental : rentals ) {
                        if ( rental.getCar() == carToReturn ) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if ( customer != null ) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if ( choice == 3 ) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("\nThank you for using the Car Rental System!");
    }
}


public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car3 = new Car("C002", "Mahindra", "Thar", 130.7);
        Car car2 = new Car("C003", "BMW", "X2 Coupe SUV", 275.5);
        Car car4 = new Car("C004", "Mercedes", "Mercedes-Maybach GLS SUV", 530.7);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.addCar(car4);

        rentalSystem.menu();
    }
}
