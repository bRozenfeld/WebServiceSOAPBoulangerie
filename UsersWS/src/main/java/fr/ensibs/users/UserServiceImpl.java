package fr.ensibs.users;

import javax.jws.WebService;
import java.sql.*;

@WebService(serviceName = "UserService", portName = "UserPort", endpointInterface = "fr.ensibs.users.UserService")
public class UserServiceImpl implements UserService{

    private static final String DATABASE_URL = "jdbc:sqlite:boulangerie.db";
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id integer PRIMARY KEY, \n"
            + " username text UNIQUE, \n"
            + " isAdmin integer NOT NULL \n"
            + ");";

    public UserServiceImpl() {
        this.initDB();
    }

    @Override
    public void createUser(String username, String password, boolean isAdmin) {

        String sql = "INSERT INTO users (username, password, isAdmin) VALUES (?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            if(isAdmin) pstmt.setInt(3, 1);
            else pstmt.setInt(3, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public String logIn(String username, String password) {
        return null;
    }

    @Override
    public void logOut() {

    }

    private void initDB() {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_USER_TABLE);
            System.out.println("USER TABLE created successfully.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Connect to the DATABASE_URL
     * @return {@Link Connection} object
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection((DATABASE_URL));
            System.out.println("Connection to database has been established.");
        } catch(SQLException e) { e.printStackTrace(); }
        return conn;
    }
}
