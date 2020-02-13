package fr.ensibs.users;

import javax.jws.*;

@WebService(name="UserService", targetNamespace = "http://userwebservice.ensibs.fr/")
public interface UserService {

    /**
     * Create a new user with the given parameters
     * @param username {@link String} used to identify this user
     * @param password {@link String} used to auth this user
     * @param isAdmin {@link Boolean} used to define the role of this user
     */
    @WebMethod(operationName = "createUser")
    @WebResult(name="createUserResult")
    @Oneway
    void createUser(@WebParam(name="username") String username,
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
    String logIn(@WebParam(name="username") String username,
                 @WebParam(name="password") String password);

    /**
     * Log out a user from the app
     * Delete the token given to him
     */
    @WebMethod(operationName = "logOut")
    @WebResult(name = "logOutResult")
    @Oneway
    void logOut();

}
