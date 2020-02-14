package fr.ensibs.client;

import java.util.Scanner;

public class BoulangerieServiceClient {

    private static boolean isAuth;
    private static String token;
    private static UserService_Service userService;
    private static UserService userPort;

    public static void main(String[] args) {
        BoulangerieServiceClient instance = new BoulangerieServiceClient();
        instance.run();
    }

    public BoulangerieServiceClient() {
        token = null;
        userService = new UserService_Service();
        userPort = userService.getUserPort();
    }

    public void run() {
        System.out.println("Hello");
        System.out.println("LIST OF COMMANDS: ");
        System.out.println("\tSIGNUP <username> <password> <'customer'|'admin'>");
        System.out.println("\tLOGIN <username> <password>");
        System.out.println("\tLOGOUT");
        System.out.println("\tQUIT");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        while(!line.equals("quit") && !line.equals("QUIT")) {
            String[] command = line.split(" ");
            switch(command[0]) {
                case "signup":
                case "SIGNUP":
                    if(command.length == 4 && (command[3].equals("customer") || command[3].equals("admin"))) {
                        signUp(command[1], command[2], command[3]);
                    } else { System.out.println("Unknown operation"); }
                    break;
                case "login":
                case "LOGIN":
                    if(command.length == 3) {
                        logIn(command[1], command[2]);
                    } else { System.out.println("Unknown operation"); }
                    break;
                default:
                    System.out.println("Unknown command: \"" + command[0] + "\"");
            }
            line = scanner.nextLine();
        }
        quit();
    }

    public void quit() {
        System.exit(1);
    }

    /**
     * Add a new user to the database
     * @param username
     * @param password
     * @param type
     */
    public void signUp(String username, String password, String type) {
        if(type.equals("admin")) {
            userPort.createUser(username, password, true);
        }
        else {
            userPort.createUser(username, password, false);
        }
    }

    /**
     * Log a user with the given credentials
     * @param username
     * @param password
     */
    public void logIn(String username, String password) {
        userPort.logIn(username, password);

    }

    public boolean isAuth() {
        if(token == null) return false;
        return true;
    }
}
