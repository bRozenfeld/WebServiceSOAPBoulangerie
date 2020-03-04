package fr.ensibs.users;

import fr.ensibs.response.SOAPResponse;

import javax.jws.*;

@WebService(name="UserService")
public interface UserService {

    /**
     * Create a new user with the given parameters
     * @param username {@link String} used to identify this user
     * @param password {@link String} used to auth this user
     * @param isAdmin {@link Boolean} used to define the role of this user
     * @return {@link SOAPResponse} request's response
     */
    @WebMethod(operationName = "register")
    @WebResult(name="registerResult")
    SOAPResponse register(@WebParam(name="username") String username,
                            @WebParam(name="password") String password,
                            @WebParam(name="isAdmin") boolean isAdmin);


    /**
     * Allow a user to log in the app using the given credentials
     * @param username {@link String} used to identify this user
     * @param password {@link String} used to auth this user
     * @return {@link SOAPResponse} request's response
     */
    @WebMethod(operationName = "logIn")
    @WebResult(name = "logInResult")
    SOAPResponse logIn(@WebParam(name="username") String username, @WebParam(name="password") String password);


    /**
     * Allow a user to delete an other user with the given id
     * @param id int of the user to delete
     * @param token String used to identify the user calling this service
     * @return {@link SOAPResponse} request's response
     */
    @WebMethod(operationName = "deleteUser")
    @WebResult(name = "deleteUserResult")
    SOAPResponse deleteUser(@WebParam(name="id") int id, @WebParam(name="token") String token);

    /**
     * Retrieve the list of all users
     * @param token String used to identify the user calling this service
     * @return {@link SOAPResponse} request's response
     */
    @WebMethod(operationName = "getUsers")
    @WebResult(name = "getUsersResult")
    SOAPResponse getUsers(@WebParam(name="token") String token);

    /**
     * Log out the user by invalidating his token
     * @param token String of the user calling this service
     * @return {@link SOAPResponse} request's response
     */
    SOAPResponse logout(@WebParam(name="token") String token);

}
