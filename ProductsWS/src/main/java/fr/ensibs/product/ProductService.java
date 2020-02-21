package fr.ensibs.product;

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
    @Oneway
    void addCommand(@WebParam(name="products")HashMap<Product,Integer> products);

    @WebMethod(operationName = "cancelCommand")
    @WebResult(name = "cancelCommandResult")
    /**
     * Method to cancel a command
     */
    void cancelCommand(@WebParam(name="command_id") int commandid);

    @WebMethod(operationName = "getCommands")
    @WebResult(name = "getCommandsResult")
    /**
     * Method to display the lis of commands
     */
    List<Command> getListCommands();

    @WebMethod(operationName = "addCommand")
    @WebResult(name = "addCommandResult")
    /**
     * Method to add a product to the command
     */
    void commandProduct(@WebParam(name="command_id") int command_id,@WebParam(name="product_id") int productid,@WebParam(name="quantity") int quantity);

    @WebMethod(operationName = "updatePrice")
    @WebResult(name = "updatePriceResult")
    /**
     * Method to update the price total of the command when add a new product
     */
    void updatePrice(@WebParam(name="command_id")int command_id,@WebParam(name="price") double price);

    /**
     * Method to get the products id with their quantity
     * @param command_id the id of the command
     * @return Map of products with their quantity
     */
    HashMap<Integer,Integer> getProductsOfCommand(@WebParam(name="command_id")int command_id);

    /**
     * Method to add a product to the products card
     * @param productname
     * @param price
     */
    @WebMethod(operationName = "addProduct")
    @WebResult(name = "addProductResult")
    @Oneway
    void addProduct(@WebParam(name="product_name") String productname,
                       @WebParam(name="price") Double price);

    @WebMethod(operationName = "removeProduct")
    @WebResult(name = "removeProductResult")
    /**
     * Method to remove a product from the products card
     */
    void removeProduct(@WebParam(name="productname")String productname);
    @WebMethod(operationName = "getProductCard")
    @WebResult(name = "getProductCardResult")
    /**
     * Method to display the products card
     */
    List<Product> getProductsCard();
    @WebMethod(operationName = "getProduct")
    @WebResult(name = "getProductResult")
    /**
     * Method to display a product from the products card
     * @param product_name
     * @return product from the products card
     */
    Product getProduct(@WebParam(name="productname") String productname);



}
