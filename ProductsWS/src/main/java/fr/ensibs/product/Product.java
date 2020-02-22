package fr.ensibs.product;

/**
 * Class representing the product that a user can purshase
 * @author  SBAITY Haitam-ROZENFELD Benjamin
 */
public class Product {
    /**
     * Id of the product
     */
    //private int id;
    /**
     *The name of the product
     */
    private String product_Name;
    /**
     * The price of the product
     */
    private Double price;

    /**
     * Constructors
     */
    public Product(){}

    public Product(String product_Name, Double price) {
        this.product_Name = product_Name;
        this.price = price;
    }

    /**
     * Getters and setters
     */



    public String getProduct_Name() {
        return product_Name;
    }

    public void setProduct_Name(String product_Name) {
        this.product_Name = product_Name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "produit  :[nom produit :"+this.product_Name+"prix du produit:"+this.price+"]";
    }
}
