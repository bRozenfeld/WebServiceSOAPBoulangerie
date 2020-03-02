package fr.ensibs.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.ensibs.database.BakeryDBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Authentication {

    public static final String SECRET_KEY = "secret";

    private Authentication() { }

    /**
     * Check if the user's token is valid
     * @param token
     * @return
     */
    public static boolean isAuthenticated(String token) {
        boolean res = false;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            res = true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        if(res) {
            String sql = "SELECT * FROM blacklist_token WHERE token = ? ";
            try (Connection conn = BakeryDBConnect.getInstance().connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, token);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) res = false;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * Indicate if the user is an admin or not
     * @param token
     * @return
     */
    public static boolean isAdmin(String token) {
        boolean res = false;
        try {
            DecodedJWT jwt = JWT.decode(token);
            res = jwt.getClaim("isAdmin").asBoolean();
        } catch(JWTDecodeException e) { e.printStackTrace(); }
        return res;
    }

    /**
     *
     * @param token
     * @return
     */
    public static int getUserId(String token) {
       int res = -1;
       try {
           DecodedJWT jwt = JWT.decode(token);
           res = jwt.getClaim("id").asInt();
       } catch(JWTDecodeException e) {
           e.printStackTrace();
       }
       return res;
    }
}
