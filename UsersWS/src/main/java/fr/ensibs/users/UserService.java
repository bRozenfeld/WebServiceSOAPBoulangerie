package fr.ensibs.users;

import fr.ensibs.response.SOAPResponse;

import javax.jws.*;
import java.util.List;

@WebService(name="UserService")
public interface UserService {

    /**
     * Create a new user with the given parameters
     * @param username {@link String} used to identify this user
     * @param password {@link String} used to auth this user
     * @param isAdmin {@link Boolean} used to define the role of this user
     */
    @WebMethod(operationName = "createUser")
    @WebResult(name="createUserResult")
    SOAPResponse register(@WebParam(name="username") String username,
                            @WebParam(name="password") String password,
                            @WebParam(name="isAdmin") boolean isAdmin);


    /**
     * Allow a user to log in the app using the given credentials
     * @param username {@link String} used to identify this user
     * @param password {@link String} used to auth this user
     * @return {@link String} A token to identify this user through the app
     */
    @WebMethod(operationName = "logIn")
    @WebResult(name = "logInResult")
    SOAPResponse logIn(@WebParam(name="username") String username, @WebParam(name="password") String password);




    @WebMethod(operationName = "deleteUser")
    @WebResult(name = "deleteUserResult")
    SOAPResponse deleteUser(@WebParam(name="id") int id, @WebParam(name="token") String token);

    /**
     * Retrieve the list of all users
     * @param token
     * @return
     */

    @WebMethod(operationName = "getUsers")
    @WebResult(name = "getUsersResult")
    SOAPResponse getUsers(@WebParam(name="token") String token);


}
