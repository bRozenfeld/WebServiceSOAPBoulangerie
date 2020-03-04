package fr.ensibs.product;

import fr.ensibs.response.SOAPResponse;

import javax.jws.*;
import java.util.HashMap;

/**
 * Interface to define the product service
 * @author SBAITY Haitam-ROZENFELD Benjamin
 *
 */
@WebService(name="ProductService", targetNamespace = "http://productwebservice.ensibs.fr/")
public interface ProductService {
    /**
     * Add a new Command with the given parameters
     * @param token user token
     * @return Success if the  command has been created successfully
     */
    @WebMethod(operationName = "addCommand")
    @WebResult(name="addCommandResult")
    SOAPResponse addCommand(@WebParam(name="user_token") String token);

    /**
     * Cancel a Command with the given parameters
     * @param commandid id of the command to cancel
     * @param token user token
     * @return Success if the command has been cancelled successfully
     */
    @WebMethod(operationName = "cancelCommand")
    @WebResult(name = "cancelCommandResult")
    SOAPResponse cancelCommand(@WebParam(name="command_id") int commandid,@WebParam(name="user_token") String token);

    /*
     * Method to display the list of commands
     * @param token user token
     * @return List of command of a user
     */
    @WebMethod(operationName = "getCommands")
    @WebResult(name = "getCommandsResult")
    SOAPResponse getListCommands(@WebParam(name="user_token") String token);

    /**
     * Method to add a product to a user command
     * @param token user token
     * @param command_id id of the command
     * @param productid id of the product
     * @param quantity the quantity of product
     * @return Success if the product has been added successfully to command
     */
    @WebMethod(operationName = "addCommandProduct")
    @WebResult(name = "addCommandProductResult")
    SOAPResponse addProductToCommand(@WebParam(name="command_id") int command_id,@WebParam(name="product_id") int productid,@WebParam(name="quantity") int quantity,@WebParam(name="user_token") String token);

    /**
     * Method to remove a product from the command
     * @param productid the id of the product
     * @param command_id the id of the command
     * @param token user token
     * @return success if the product has been removed successfully from the command
     */
    @WebMethod(operationName = "removeCommandProduct")
    @WebResult(name = "removeCommandProductResult")
    SOAPResponse removeProductFromCommand(@WebParam(name="command_id") int command_id,@WebParam(name="product_id") int productid,@WebParam(name="user_token") String token);


    /**
     * Method to get the products id with their quantity
     * @param command_id the id of the command
     * @param token user token
     * @return Map of products with their quantity
     */
    @WebMethod(operationName = "getProductsOfCommand")
    @WebResult(name = "getProductsOfCommandResult")
    SOAPResponse getProductsOfCommand(@WebParam(name="command_id")int command_id,@WebParam(name="user_token") String token);

    /**
     * Method to add a product to the products card
     * @param productname the product name
     * @param price the price of the product
     * @return success if the product added successfully to the products card
     */
    @WebMethod(operationName = "addProduct")
    @WebResult(name = "addProductResult")
    SOAPResponse addProduct(@WebParam(name="product_name") String productname,
                       @WebParam(name="price") Double price,@WebParam(name="user_token") String token);

    /**
     * Method to remove a product from the products card
     * @param token user token
     * @param productname product name
     * @return success if the product removed successfully from the products card
     */
    @WebMethod(operationName = "removeProduct")
    @WebResult(name = "removeProductResult")
    SOAPResponse removeProduct(@WebParam(name="productname")String productname,@WebParam(name="user_token") String token);

    /**
     * Method to display the products card
     * @param token  user token
     * @return the products cards
     */
    @WebMethod(operationName = "getCard")
    @WebResult(name = "gettCardResult")
    SOAPResponse getCard(@WebParam(name="user_token") String token);

    /**
     * Method to display a product from the products card
     * @param productname product name
     * @param token user token
     * @return product from the products card
     */
    @WebMethod(operationName = "getProduct")
    @WebResult(name = "getProductResult")
    SOAPResponse getProduct(@WebParam(name="productname") String productname,@WebParam(name="user_token") String token);

}
