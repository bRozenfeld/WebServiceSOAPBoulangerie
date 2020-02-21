package fr.ensibs.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

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
}
