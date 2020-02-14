package fr.ensibs.users;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a user that can sign in
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

    public User() {

    }

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
