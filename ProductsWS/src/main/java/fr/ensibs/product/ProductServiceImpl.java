package fr.ensibs.product;

import fr.ensibs.bakerydb.BakeryDBConnect;

import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebService(serviceName = "ProductService", portName = "ProductPort", endpointInterface = "fr.ensibs.product.ProductService")
public class ProductServiceImpl implements ProductService {

    public ProductServiceImpl() {
        BakeryDBConnect.initDB();
    }


    /**
     * Add a new Command with the given parameters
     * @param products the list of products with their quantity in the command
     */
    @Override
    public void addCommand(HashMap<Product,Integer> products)  {
        String sql = "INSERT INTO commandsList (price, isPaid) VALUES (?,?)";
        List<Integer> products_id=new ArrayList<>();
        HashMap<Integer,Integer> product = new HashMap<>();
        Connection conn = BakeryDBConnect.connect();

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            //Start transaction
            conn.setAutoCommit(false);
            //Insert Command table
            pstmt.setDouble(1,0.0);
            pstmt.setInt(2, 0);
            pstmt.executeUpdate();
            System.out.println("Command added successfully");
            //Get command id
            ResultSet rs    = pstmt.executeQuery(sql);
            Long command_id = null;
            if (rs.next()){
                command_id = rs.getLong(1);
            }
            //Get product id and quantity
            for(Product p:products.keySet()) {
                String sql1 = "SELECT id FROM products WHERE productname = ?";
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setString(1,p.getProduct_Name());

                ResultSet rs1 = pstmt1.executeQuery();
                products_id.add(rs1.getInt("id"));
                product.put(rs1.getInt("id"),products.get(p));
            }
            //Insert command product table
            for(int pr_id:products_id){
                commandProduct(Math.toIntExact(command_id),pr_id,product.get(pr_id));
            }
            System.out.println("Products added to command successfully");

            //Update price in command table
            Double price=0.0;
            for(Product p:products.keySet()){
                    price+=p.getPrice()*products.get(p);
            }
            updatePrice(Math.toIntExact(command_id),price);

            conn.commit();

        }catch (SQLException e){
            try {
                if (conn != null) {
                conn.rollback();
                }
            } catch (SQLException e2) {
            System.out.println(e2.getMessage());
            }
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updatePrice(int command_id,double price){
        String sql = "UPDATE commands SET price =? WHERE id = ?";
        try(Connection conn = BakeryDBConnect.connect();
            PreparedStatement stmt3 = conn.prepareStatement(sql);) {
            stmt3.setDouble(1, price);
            stmt3.setInt(2, command_id);
            stmt3.executeUpdate();
            System.out.println("Command price updated successfully");
        }catch(SQLException e){e.printStackTrace();}
    }

    @Override
    public void commandProduct(int command_id,int product_id, int quantity) {
        String sql = "INSERT INTO commandsProduct (product_id, command_id, quantity) VALUES (?,?,?)";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product_id);
            pstmt.setInt(2, command_id);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            System.out.println("Products added to command successfully");
        }catch(SQLException e){e.printStackTrace();}
    }

    @Override
    public void cancelCommand(int command_id) {
        String sql = "DELETE FROM commands WHERE id = ? ";

        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, command_id);
            pstmt.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }

    @Override
    public HashMap<Integer,Integer> getProductsOfCommand(int command_id){
        HashMap<Integer,Integer> productsOfCommand=new HashMap<>();
        String sql = "SELECT DISTINCT product_id, quantity FROM commandsProduct WHERE command_id = ?";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, command_id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int pr_id = rs.getInt("product_id");
                int q = rs.getInt("quantity");
                productsOfCommand.put(pr_id,q);
            }
        } catch(SQLException e) { e.printStackTrace(); }
        return  productsOfCommand;
    }

    @Override
    public List<Command> getListCommands() {
        String sql = "SELECT id FROM commands";
        List<Command> commands = new ArrayList<>();
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("id");
                double productprice = rs.getDouble("price");
                int isPaid = rs.getInt("isPaid");
                if(isPaid==1)commands.add(new Command(id,null,productprice,true));
                else commands.add(new Command(id,null,productprice,false));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    /**
     * create a new product with the given parameters
     * create a new product with the given parameters

     * @param productname
     * @param price
     */
    @Override
    public void addProduct(String productname, Double price) {
        String sql = "INSERT INTO products (productname, price) VALUES (?,?)";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.setDouble(2,price);
            pstmt.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void removeProduct(String productname) {
        String sql = "DELETE FROM products WHERE productname = ? ";
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Product> getProductsCard() {
        String sql = "SELECT id,productname FROM Products";
        List<Product> products = new ArrayList<Product>();
        try (Connection conn = BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                String productname = rs.getString("productname");
                double productprice = rs.getDouble("price");
                products.add(new Product(productname,productprice));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getProduct(String product_name) {
        String sql = "SELECT id,productname FROM products WHERE productname = ? ";
        Product product = new Product();
        try (Connection conn =BakeryDBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {
            pstmt.setString(1,product_name);
            ResultSet rs = pstmt.executeQuery();

            String productname = rs.getString("productname");
            double productprice = rs.getDouble("price");

             product.setProduct_Name(productname);
             product.setPrice(productprice);



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }


}
