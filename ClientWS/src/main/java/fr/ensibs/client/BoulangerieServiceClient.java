package fr.ensibs.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.List;
import java.util.Scanner;


public class BoulangerieServiceClient {

    private static final String AUTHENTICATION_FAILED = "Authentication failed : Invalid credentials.";
    private static final String AUTHENTICATION_SUCCESSFULL = "Authentication successful !";
    private static final String COMMANDS_USER_LAMBDA = "Welcome to the app, \n"
            + "LIST OF COMMANDS: \n"
            + "\tSIGNUP <username> <password> <'customer'|'admin'> \n"
            + "\tLOGIN <username> <password> \n"
            + "\tQUIT";
    private static final String COMMANDS_USER_ADMIN = "LIST OF COMMANDS: \n"
            + "\tSHOW USERS\n"
            + "\tDELETE <username>\n"
            + "\tLOGOUT \n"
            + "\tQUIT \n";
    private static final String COMMANDS_USER_CUSTOMER = "LIST OF COMMANDS: \n"
            + "\tLOGOUT\n"
            + "\tQUIT\n";

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
        if(token == null) {
            System.out.println(COMMANDS_USER_LAMBDA);
        } else {
            User user = userPort.getUser(token);
            if(user.isAdmin()) {
                System.out.println(COMMANDS_USER_ADMIN);
            } else {
                System.out.println(COMMANDS_USER_CUSTOMER);
            }
        }

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
                case "logout":
                case "LOGOUT":
                    logOut();
                    break;
                case "SHOW":
                    switch(command[1]) {
                        case "USERS":
                            showUsers(token);
                            break;
                    }
                    break;
                case "DELETE":
                    if(command.length == 2) {
                        deleteUser(command[1], token);
                    }
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

    public void logOut() {
        this.token = null;
        run();
    }

    public void deleteUser(String username, String token) {
        userPort.deleteUser(username, token);
        run();
    }

    /**
     * Display the list of all the users
     * @param token
     */
    public void showUsers(String token) {
        List<User> users = userPort.getUsers(token);
        for(User u : users) {
            String s = "";
            if(u.isAdmin()) s += "admin";
            else s += "customer";
            System.out.println(u.getUsername() + " : " + s);
        }
        run();
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
        token = userPort.logIn(username, password);
        if(token == null) {
            System.out.println(AUTHENTICATION_FAILED);
        } else {
            System.out.println(AUTHENTICATION_SUCCESSFULL);
        }
        run();
    }


    public boolean isAuth() {
        if(token == null) return false;
        return true;
    }
}
