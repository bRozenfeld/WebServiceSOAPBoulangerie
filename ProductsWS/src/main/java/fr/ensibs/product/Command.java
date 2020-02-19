package fr.ensibs.product;



/**
 * Class representing the order of a user
 */
public class Command {
    /**
     * Id of the command
     */
    private int id;
    /**
     *The name of the product
     */
    private String product_Name;
    /**
     * The total price of the command
     */
    private Double price;
    /**
     * The quatity of commanded product
     */
    private int quantity;
    /**
     * Indicate if a command is paid or not yet
     */
    private Boolean isPurchased;

    /**
     * Constructors
     */
    public Command(){}

    public Command(int id, String product_Name, Double price, int quantity, Boolean isPurchased) {
        this.id = id;
        this.product_Name = product_Name;
        this.price = price;
        this.quantity = quantity;
        this.isPurchased = isPurchased;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getPurshased() {
        return isPurchased;
    }

    public void setPurshased(Boolean purshased) {
        isPurchased = purshased;
    }

    @Override
    public String toString(){
        return "Commande numero "+this.id+" : [nom produit : "+this.product_Name+", quantité : "+this.quantity+
                ", prix totale : "+this.price+"€, payé : "+this.isPurchased+"]";

    }
}
