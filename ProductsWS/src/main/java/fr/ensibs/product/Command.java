package fr.ensibs.product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing the order of a user
 * @author SBAITY Haitam-ROZENFELD Benjamin
 */
public class Command {
    /**
     * Id of the command
     */
    private int command_id;
    /**
     * List of the products with their quantity
     */
    private HashMap<Product,Integer> productsList;
    /**
     * The total price of the command
     */
    private Double price;
    /**
     * Indicate if a command is paid or not yet
     */
    private Boolean isPaid;

    /**
     * id of the user owning this command
     */
    private int user_id;

    /**
     * Constructors
     */
    public Command(){}

    public Command(int id,HashMap<Product,Integer> productsList,Double price, Boolean isPaid, int user_id) {
        this.productsList=productsList;
        this.command_id = id;
        this.price = price;
        this.isPaid = isPaid;
        this.user_id = user_id;
    }

    /**
     * Getters and setters
     *
     */
    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public HashMap<Product, Integer> getProductsList() {
        return productsList;
    }

    public void setProductsList(HashMap<Product, Integer> productsList) {
        this.productsList = productsList;
    }
    public List<Product> getProducts(){
         return (List<Product>) productsList.keySet();
    }

    public List<Integer> getQuantity(){
        List<Integer> products_quantity=new ArrayList<>();
        for(Product p:productsList.keySet()) {
            products_quantity.add(productsList.get(p));
        }
        return products_quantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBill(){
        String bill="";
        for(Product p:productsList.keySet()){
            bill+=" name : "+p.getProduct_Name()+"     price:"+p.getPrice()+"€      quantity"+productsList.get(p)+"\n";

        }
        return bill;
   }

    @Override
    public String toString(){

        return "Commande numero  : "+this.command_id+"\n"+getBill()+"\n prix totale : "+this.price+"€, \n payé : "+this.isPaid+"\n";

    }
}
