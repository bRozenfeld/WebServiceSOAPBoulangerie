package fr.ensibs.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ensibs.auth.Authentication;
import fr.ensibs.database.BakeryDBConnect;
import fr.ensibs.response.SOAPResponse;
import fr.ensibs.response.SOAPResponseStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "UserService", portName = "UserPort", endpointInterface = "fr.ensibs.users.UserService")
public class UserServiceImpl implements UserService{

    private static final String AUTHENTICATION_FAILED = "Authentication failed : Invalid credentials.";
    private static final String AUTHENTICATION_SUCCESSFUL = "Authentication successful !";


    public UserServiceImpl() {
        BakeryDBConnect.initDB();
    }

    @Override
    public SOAPResponse register(String username, String password, boolean isAdmin) {
        SOAPResponse response = null;

        String sql = "INSERT INTO users (username, password, isAdmin) VALUES (?,?,?)";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            if(isAdmin) pstmt.setInt(3, 1);
            else pstmt.setInt(3, 0);
            pstmt.executeUpdate();
            response = new SOAPResponse("User " + username + " added successfully.", SOAPResponseStatus.SUCCESS, null);
        } catch (SQLException e) { e.printStackTrace(); }

        if(response == null) response = new SOAPResponse("Error while adding " + username + ".", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse logIn(String username, String password) {
        SOAPResponse response = null;

        String sql = "SELECT password, isAdmin FROM users WHERE username = ?";
        try(Connection conn = BakeryDBConnect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next() == false) {
                return new SOAPResponse(AUTHENTICATION_FAILED, SOAPResponseStatus.FAILED, null);
            }

            String hashPassword = rs.getString("password");
            boolean isAdmin = rs.getBoolean("isAdmin");

            if(BCrypt.checkpw(password, hashPassword)) {
                System.out.println(AUTHENTICATION_SUCCESSFUL);
                try {
                    Date now = new Date();
                    Algorithm algorithm = Algorithm.HMAC256(Authentication.SECRET_KEY);
                    String token = JWT.create()
                            .withIssuedAt(now)
                            .withExpiresAt(new Date(now.getTime() + 1000*60*60))
                            .withClaim("username", username)
                            .withClaim("isAdmin", isAdmin)
                            .sign(algorithm);

                    response = new SOAPResponse(AUTHENTICATION_SUCCESSFUL, SOAPResponseStatus.SUCCESS, token);
                } catch (JWTCreationException exception) {
                    exception.printStackTrace();
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse(AUTHENTICATION_FAILED, SOAPResponseStatus.FAILED, null);
        return response;
    }


    @Override
    public SOAPResponse deleteUser(int id, String token) {
        SOAPResponse response = null;

        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            return new SOAPResponse("Unauthorized.", SOAPResponseStatus.UNAUTHORIZED, null);
        }

        String sql = "DELETE FROM users WHERE id = ? ";
        try (Connection conn = BakeryDBConnect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            response = new SOAPResponse("User deleted successfully.", SOAPResponseStatus.SUCCESS, null);
        } catch(SQLException e) { e.printStackTrace(); }

        if(response == null) response = new SOAPResponse("Impossible to delete user", SOAPResponseStatus.FAILED, null);
        return response;
    }


    @Override
    public SOAPResponse getUsers(String token) {

        SOAPResponse response = null;

        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }

        String sql = "SELECT username, isAdmin FROM users";
        List<User> users = new ArrayList<User>();
        try (Connection conn = BakeryDBConnect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                String username = rs.getString("username");
                int adminStatus = rs.getInt("isAdmin");
                if(adminStatus==0) users.add(new User(username, "", false));
                else users.add(new User(username, "", true));
            }

            response = new SOAPResponse("Retrieve users list successfully.", SOAPResponseStatus.SUCCESS, users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse("Error while getting list of users.", SOAPResponseStatus.FAILED, null);
        return response;
    }

}
