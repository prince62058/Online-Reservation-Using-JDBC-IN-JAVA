import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class Online_Reservation_System {
    private static final int min = 1000;
    private static final int max = 9999;

    public static class User {
        private String username;
        private String password;
        Scanner sc = new Scanner(System.in);

        public User() {}

        public String getUsername() {
            System.out.print("Enter Username: ");
            username = sc.nextLine();
            return username;
        }

        public String getPassword() {
            System.out.print("Enter Password: ");
            password = sc.nextLine();
            return password;
        }
    }

    public static class PnrRecord {
        private int pnrNumber;
        private String passengerName;
        private String trainNumber;
        private String classType;
        private String journeyDate;
        private String from;
        private String to;

        Scanner sc = new Scanner(System.in);

        public int getPnrNumber() {
            Random random = new Random();
            pnrNumber = random.nextInt(max - min + 1) + min;
            return pnrNumber;
        }

        public String getPassengerName() {
            System.out.print("Enter the passenger name: ");
            passengerName = sc.nextLine();
            return passengerName;
        }

        public String getTrainNumber() {
            System.out.print("Enter the train number: ");
            trainNumber = sc.nextLine();
            return trainNumber;
        }

        public String getClassType() {
            System.out.print("Enter the class type: ");
            classType = sc.nextLine();
            return classType;
        }

        public String getJourneyDate() {
            System.out.print("Enter the journey date as 'YYYY-MM-DD': ");
            journeyDate = sc.nextLine();
            return journeyDate;
        }

        public String getFrom() {
            System.out.print("Enter the starting place: ");
            from = sc.nextLine();
            return from;
        }

        public String getTo() {
            System.out.print("Enter the destination place: ");
            to = sc.nextLine();
            return to;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User u1 = new User();
        String username = u1.getUsername();
        String password = u1.getPassword();

        String url = "jdbc:mysql://127.0.0.1:3306/Prince"; // Update DB name if needed

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("User Connection Granted.\n");

                while (true) {
                    System.out.println("Enter the choice:");
                    System.out.println("1. Insert Record");
                    System.out.println("2. Delete Record");
                    System.out.println("3. Show All Records");
                    System.out.println("4. Exit");

                    int choice = sc.nextInt();
                    sc.nextLine(); // consume newline

                    String insertQuery = "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?, ?)";
                    String deleteQuery = "DELETE FROM reservation WHERE pnr_number = ?";
                    String showQuery = "SELECT * FROM reservation";

                    if (choice == 1) {
                        PnrRecord p1 = new PnrRecord();
                        int pnr_number = p1.getPnrNumber();
                        String passengerName = p1.getPassengerName();
                        String trainNumber = p1.getTrainNumber();
                        String classType = p1.getClassType();
                        String journeyDate = p1.getJourneyDate();
                        String from = p1.getFrom();
                        String to = p1.getTo();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setInt(1, pnr_number);
                            preparedStatement.setString(2, passengerName);
                            preparedStatement.setString(3, trainNumber);
                            preparedStatement.setString(4, classType);
                            preparedStatement.setString(5, journeyDate);
                            preparedStatement.setString(6, from);
                            preparedStatement.setString(7, to);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record added successfully.");
                            } else {
                                System.out.println("No records were added.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 2) {
                        System.out.print("Enter the PNR number to delete the record: ");
                        int pnrNumber = sc.nextInt();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                            preparedStatement.setInt(1, pnrNumber);
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record deleted successfully.");
                            } else {
                                System.out.println("No records were deleted.");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 3) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(showQuery);
                             ResultSet resultSet = preparedStatement.executeQuery()) {
                            System.out.println("\nAll records printing:\n");
                            while (resultSet.next()) {
                                String pnr = resultSet.getString("pnr_number");
                                String name = resultSet.getString("passenger_name");
                                String train = resultSet.getString("train_number");
                                String cls = resultSet.getString("class_type");
                                String date = resultSet.getString("journey_date");
                                String fromLoc = resultSet.getString("from_location");
                                String toLoc = resultSet.getString("to_location");

                                System.out.println("PNR Number: " + pnr);
                                System.out.println("Passenger Name: " + name);
                                System.out.println("Train Number: " + train);
                                System.out.println("Class Type: " + cls);
                                System.out.println("Journey Date: " + date);
                                System.out.println("From Location: " + fromLoc);
                                System.out.println("To Location: " + toLoc);
                                System.out.println("--------------");
                            }
                        } catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }
                    } else if (choice == 4) {
                        System.out.println("Exiting the program.");
                        break;
                    } else {
                        System.out.println("Invalid Choice Entered.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
        }

        sc.close();
    }
}
