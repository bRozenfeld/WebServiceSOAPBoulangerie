package fr.ensibs.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "UserService", portName = "UserPort", endpointInterface = "fr.ensibs.users.UserService")
public class UserServiceImpl implements UserService{

    private static final String SECRET_KEY = "secret";
    private static final String AUTHENTICATION_FAILED = "Authentication failed : Invalid credentials.";
    private static final String AUTHENTICATION_SUCCESSFULL = "Authentication successful !";
    private static final String DATABASE_URL = "jdbc:sqlite:boulangerie.db";

    //private Connection connexion;

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users ( \n"
            + " id integer PRIMARY KEY, \n"
            + " username text UNIQUE, \n"
            + " password text, \n"
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
            pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            if(isAdmin) pstmt.setInt(3, 1);
            else pstmt.setInt(3, 0);
            pstmt.executeUpdate();
            System.out.println("User added successfully");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public String logIn(String username, String password) {
        String sql = "SELECT password, isAdmin FROM users WHERE username = ?";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next() == false) {
                System.out.println(AUTHENTICATION_FAILED);
                return null;
            }

            String hashPassword = rs.getString("password");
            int isAdmin = rs.getInt("isAdmin");
            String typeUser;
            if(isAdmin == 0) typeUser = "customer";
            else typeUser = "admin";

            if(BCrypt.checkpw(password, hashPassword)) {
                System.out.println(AUTHENTICATION_SUCCESSFULL);
                try {
                    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                    String token = JWT.create()
                            .withClaim("username", username)
                            .withClaim("isAdmin", typeUser)
                            .sign(algorithm);
                    return token;
                } catch (JWTCreationException exception) {
                    exception.printStackTrace();
                }
            } else {
                System.out.println(AUTHENTICATION_FAILED);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String token) {
        return decodeToken(token);
    }

    @Override
    public void deleteUser(String username, String token) {
        User user = decodeToken(token);
        if(!user.isAdmin()) {
            System.out.println("Error, not allow to do this.");
            return;
        }

        String sql = "DELETE FROM users WHERE username = ? ";
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }


    @Override
    public List<User> getUsers(String token) {

        User user = decodeToken(token);
        if(!user.isAdmin()) {
            System.out.println("Error, not allow to do this.");
            return null;
        }

        String sql = "SELECT username, isAdmin FROM users";
        List<User> users = new ArrayList<User>();
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                String username = rs.getString("username");
                int adminStatus = rs.getInt("isAdmin");
                if(adminStatus==0) users.add(new User(username, "", false));
                else users.add(new User(username, "", true));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User decodeToken(String token) {
        User user = null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            String status = jwt.getClaim("isAdmin").asString();
            String username = jwt.getClaim("username").asString();
            boolean isAdmin = false;
            if(status.equals("admin")) isAdmin = true;

            user = new User(username, "", isAdmin);

        } catch(JWTDecodeException e) { e.printStackTrace(); }
        return user;
    }

    /**
     * Initialise the database
     */
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
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection((DATABASE_URL));
            //this.connexion = DriverManager.getConnection((DATABASE_URL));
            System.out.println("Connection to database has been established.");
        } catch(Exception e) { e.printStackTrace(); }
        return conn;
    }
}
