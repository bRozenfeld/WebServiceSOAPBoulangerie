package fr.ensibs.product;

import javax.xml.ws.Endpoint;

public class ProductServiceSOAPPublish {
    public static void main(String[] args) {
        ProductService current = new ProductServiceImpl();
        Endpoint.publish("http://localhost:8080/productwebservice/products", current);
        System.out.println("ProductWebService available...");
    }
}
