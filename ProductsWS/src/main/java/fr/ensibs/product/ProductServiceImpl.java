package fr.ensibs.product;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    /**
     * create a new product with the given parameters
     *
     * @param id
     * @param product_name
     * @param price
     * @param quantity
     * @param isPurshased
     */
    @Override
    public void createProductCard(int id, String product_name, Double price, int quantity, boolean isPurshased) {

    }

    @Override
    public void CommandProduct(int product_id, int quantity) {

    }

    @Override
    public void cancelCommand(int command_id) {

    }

    @Override
    public ArrayList<Command> getCommands() {
        return null;
    }

    @Override
    public void addProduct(String product_name, Double price) {

    }

    @Override
    public void removeProduct(String product_name) {

    }

    @Override
    public List<Product> getProductsCard() {
        return null;
    }

    @Override
    public Product getProduct(String product_name) {
        return null;
    }
}
