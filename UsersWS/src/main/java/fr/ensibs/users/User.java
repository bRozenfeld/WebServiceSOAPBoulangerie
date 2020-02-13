package fr.ensibs.users;

/**
 * Class representing a user that can sign in
 */
public class User {
    /**
     * unique identifier
     */
    private String username;

    /**
     * password to log in
     */
    private String password;

    /**
     * user is either admin or customer
     */
    private boolean isAdmin;


    public User(String username, String password, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
