package fr.ensibs.product;

import fr.ensibs.response.SOAPResponse;

import javax.jws.*;
import java.util.HashMap;
import java.util.List;

@WebService(name="ProductService", targetNamespace = "http://productwebservice.ensibs.fr/")
public interface ProductService {
    /**
     * Add a new Command with the given parameters
     * @param products Map of products with their quantity
     */
    @WebMethod(operationName = "addCommand")
    @WebResult(name="addCommandResult")
    SOAPResponse addCommand(@WebParam(name="products")HashMap<Product,Integer> products,@WebParam(name="user_token") String token);

    @WebMethod(operationName = "cancelCommand")
    @WebResult(name = "cancelCommandResult")
    /**
     * Method to cancel a command
     */
    SOAPResponse cancelCommand(@WebParam(name="command_id") int commandid,@WebParam(name="user_token") String token);

    @WebMethod(operationName = "getCommands")
    @WebResult(name = "getCommandsResult")
    /**
     * Method to display the lis of commands
     */
    SOAPResponse getListCommands(@WebParam(name="user_token") String token);

    @WebMethod(operationName = "addCommandProduct")
    @WebResult(name = "addCommandProductResult")
    /**
     * Method to add a product to the command
     */
    SOAPResponse commandProduct(@WebParam(name="command_id") int command_id,@WebParam(name="product_id") int productid,@WebParam(name="quantity") int quantity,@WebParam(name="user_token") String token);

    @WebMethod(operationName = "updatePrice")
    @WebResult(name = "updatePriceResult")
    /**
     * Method to update the price total of the command when add a new product
     */
    SOAPResponse updatePrice(@WebParam(name="command_id")int command_id,@WebParam(name="price") double price,@WebParam(name="user_token") String token);

    @WebMethod(operationName = "getProductsOfCommand")
    @WebResult(name = "getProductsOfCommandResult")
    /**
     * Method to get the products id with their quantity
     * @param command_id the id of the command
     * @return Map of products with their quantity
     */
    SOAPResponse getProductsOfCommand(@WebParam(name="command_id")int command_id,@WebParam(name="user_token") String token);

    /**
     * Method to add a product to the products card
     * @param productname
     * @param price
     */
    @WebMethod(operationName = "addProduct")
    @WebResult(name = "addProductResult")
    SOAPResponse addProduct(@WebParam(name="product_name") String productname,
                       @WebParam(name="price") Double price,@WebParam(name="user_token") String token);

    @WebMethod(operationName = "removeProduct")
    @WebResult(name = "removeProductResult")
    /**
     * Method to remove a product from the products card
     */
    SOAPResponse removeProduct(@WebParam(name="productname")String productname,@WebParam(name="user_token") String token);
    @WebMethod(operationName = "getProductCard")
    @WebResult(name = "getProductCardResult")
    /**
     * Method to display the products card
     */
    SOAPResponse getProductsCard(@WebParam(name="user_token") String token);
    @WebMethod(operationName = "getProduct")
    @WebResult(name = "getProductResult")
    /**
     * Method to display a product from the products card
     * @param product_name
     * @return product from the products card
     */
    SOAPResponse getProduct(@WebParam(name="productname") String productname,@WebParam(name="user_token") String token);



}
