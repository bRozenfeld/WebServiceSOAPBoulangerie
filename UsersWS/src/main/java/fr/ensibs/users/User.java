package fr.ensibs.users;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a user
 */
@XmlRootElement
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

    /**
     * Constructor
     */
    public User() {

    }

    /**
     * Constructor
     * @param username
     * @param password
     * @param isAdmin
     */
    public User(String username, String password, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.username = username;
        this.password = password;
    }


    @XmlElement
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @XmlElement
    public boolean isAdmin() {
        return isAdmin;
    }
}
