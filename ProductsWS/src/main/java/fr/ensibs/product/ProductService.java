package fr.ensibs.product;

import javax.jws.*;
import java.util.ArrayList;
import java.util.List;

@WebService(name="ProductService", targetNamespace = "http://productwebservice.ensibs.fr/")
public interface ProductService {
    /**
     * create a new product with the given parameters
     * @param id
     * @param product_name
     * @param price
     * @param quantity
     * @param isPurshased
     */
    @WebMethod(operationName = "createProduct")
    @WebResult(name="createProductResult")
    @Oneway
    void createProductCard(@WebParam(name="id") int id ,
                           @WebParam(name="product_name") String product_name,
                           @WebParam(name="price") Double price,
                           @WebParam(name="quantity") int quantity,
                           @WebParam(name="status") boolean isPurshased);


    @WebMethod(operationName = "addCommand")
    @WebResult(name = "addCommandResult")
    /**
     * Method to add a product to the command list
     */
    void CommandProduct(@WebParam(name="product_id") int product_id,@WebParam(name="quantity") int quantity);

    @WebMethod(operationName = "cancellCommand")
    @WebResult(name = "cancelCommandResult")
    /**
     * Method to cancel a command
     */
    void cancelCommand(@WebParam(name="command_id") int command_id);

    @WebMethod(operationName = "getCommands")
    @WebResult(name = "getCommandsResult")
    /**
     * Method to display the lis of commands
     */
    ArrayList<Command> getCommands();

    @WebMethod(operationName = "addProduct")
    @WebResult(name = "addProductResult")
    /**
     * Method to add a product to the products card
     */
    void addProduct(@WebParam(name="product_name")String product_name,@WebParam(name="price")Double price);

    @WebMethod(operationName = "removeProduct")
    @WebResult(name = "removeProductResult")
    /**
     * Method to remove a product from the products card
     */
    void removeProduct(@WebParam(name="product_name")String product_name);
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
    Product getProduct(@WebParam(name="product_name") String product_name);
}
