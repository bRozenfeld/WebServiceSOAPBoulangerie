package fr.ensibs.product;

import fr.ensibs.auth.Authentication;
import fr.ensibs.database.BakeryDBConnect;
import fr.ensibs.response.SOAPResponse;
import fr.ensibs.response.SOAPResponseStatus;

import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class to define the product service
 * @author SBAITY Haitam-ROZENFELD Benjamin
 *
 */
@WebService(serviceName = "ProductService", portName = "ProductPort", endpointInterface = "fr.ensibs.product.ProductService")
public class ProductServiceImpl implements ProductService {

    private BakeryDBConnect database;

    public ProductServiceImpl() {
        database = BakeryDBConnect.getInstance();
    }


    /**
     * Add a new Command with the given parameters
     * @param products the list of products with their quantity in the command
     */
    @Override
    public SOAPResponse addCommand(HashMap<Product,Integer> products,String token)  {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "INSERT INTO commands (price, isPaid) VALUES (?,?)";
        List<Integer> products_id=new ArrayList<>();
        HashMap<Integer,Integer> product = new HashMap<>();
        Connection conn = database.connect();
        ArrayList<Integer> commandid = new ArrayList<>();

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //Start transaction
            conn.setAutoCommit(false);
            //Insert Command table
            pstmt.setDouble(1,0.0);
            pstmt.setInt(2, 0);
            pstmt.executeUpdate();
            System.out.println("Command added successfully");
            //Get command id
           /** ResultSet rs    = pstmt.executeQuery(sql);
            int command_id = 0;
            if (rs.next()){
                command_id = rs.getInt("command_id");
            }*/
            String sql2="select MAX(command_id)  from commands";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);

            ResultSet rs = pstmt2.executeQuery();
            int command_id = rs.getInt(1);
            /**String sql2="select command_id  from commands";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            ResultSet rs = pstmt2.executeQuery();
            while(rs.next()){
                commandid.add(rs.getInt(1));
            }
            int command_id = commandid.get(commandid.size()-1);*/
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
                commandProduct(command_id,pr_id,product.get(pr_id),token);
            }
            System.out.println("Products added to command successfully");

            //Update price in command table
            Double price=0.0;
            for(Product p:products.keySet()){
                    price+=p.getPrice()*products.get(p);
            }
            updatePrice(command_id,price,token);
            response = new SOAPResponse("Command number "+command_id+" added successfully.", SOAPResponseStatus.SUCCESS, null);
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
        if(response == null) response = new SOAPResponse("Error while adding products.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse getLastCommandId(String token){
        SOAPResponse response=null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }

        Connection conn = database.connect();
       // ArrayList<Integer> commandid = new ArrayList<>();

        try {

            String sql2="select MAX(command_id)  from commands";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            ResultSet rs = pstmt2.executeQuery();
            //while(rs.next()){
                int command_id=rs.getInt(1);
            //}
            //int command_id = commandid.get(commandid.size()-1);
            response=new SOAPResponse("Command id number "+command_id+" retrieved successfuly.", SOAPResponseStatus.SUCCESS, command_id);
        }catch(SQLException e){e.printStackTrace();}

        if(response == null) response = new SOAPResponse("Error while searching command id.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse updatePrice(int command_id,double price,String token){
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "UPDATE commands SET price =? WHERE command_id = ?";
        try(Connection conn = database.connect();
            PreparedStatement stmt3 = conn.prepareStatement(sql);) {
            stmt3.setDouble(1, price);
            stmt3.setInt(2, command_id);
            stmt3.executeUpdate();
            response = new SOAPResponse("Command price updated successfully.", SOAPResponseStatus.SUCCESS, null);
        }catch(SQLException e){e.printStackTrace();}
        if(response == null) response = new SOAPResponse("Error while updating price.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse commandProduct(int command_id,int product_id, int quantity,String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "INSERT INTO commandsProduct (product_id, command_id, quantity) VALUES (?,?,?)";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product_id);
            pstmt.setInt(2, command_id);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            response = new SOAPResponse("product number "+product_id+" added to command number "+command_id+" successfully.", SOAPResponseStatus.SUCCESS, null);
        }catch(SQLException e){e.printStackTrace();}
        if(response == null) response = new SOAPResponse("Error while commanding product.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse cancelCommand(int command_id,String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "DELETE FROM commands WHERE command_id = ? ";

        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, command_id);
            pstmt.executeUpdate();
            response = new SOAPResponse("command number "+command_id+" cancelled successfully.", SOAPResponseStatus.SUCCESS, null);
        } catch(SQLException e) { e.printStackTrace(); }
        if(response == null) response = new SOAPResponse("Error while cancelling command.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse getProductsOfCommand(int command_id,String token){
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) ) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        HashMap<Integer,Integer> productsOfCommand=new HashMap<>();
        String sql = "SELECT product_id, quantity FROM commandsProduct WHERE command_id = ?";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, command_id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int pr_id = rs.getInt("product_id");
                int q = rs.getInt("quantity");
                productsOfCommand.put(pr_id,q);
            }
            response = new SOAPResponse("Retrieve products"+productsOfCommand.toString()+"of command number "+command_id+" successfully.", SOAPResponseStatus.SUCCESS, productsOfCommand);
        } catch(SQLException e) { e.printStackTrace(); }
        if(response == null) response = new SOAPResponse("Error while getting list of products of the command.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse getListCommands(String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "SELECT * FROM commands";
        List<Command> commands = new ArrayList<Command>();
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("command_id");
                double productprice = rs.getDouble("price");
                int isPaid = rs.getInt("isPaid");
                if(isPaid==1)commands.add(new Command(id,null,productprice,true));
                else commands.add(new Command(id,null,productprice,false));
            }
            response = new SOAPResponse("Retrieve commands "+commands.toString()+ "successfully.", SOAPResponseStatus.SUCCESS, commands);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse("Error while getting list of commands.", SOAPResponseStatus.FAILED, null);
        return response;

    }

    /**
     * create a new product with the given parameters
     * create a new product with the given parameters

     * @param productname
     * @param price
     */
    @Override
    public SOAPResponse addProduct(String productname, Double price,String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "INSERT INTO products (productname, price) VALUES (?,?)";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.setDouble(2,price);
            pstmt.executeUpdate();
            response = new SOAPResponse("Product "+productname+" added successfully.", SOAPResponseStatus.SUCCESS, null);

        } catch (SQLException e) { e.printStackTrace(); }
        if(response == null) response = new SOAPResponse("Error while adding product.", SOAPResponseStatus.FAILED, null);
        return response;
    }


    @Override
    public SOAPResponse removeProduct(String productname,String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token) || !Authentication.isAdmin(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "DELETE FROM products WHERE productname = ? ";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productname);
            pstmt.executeUpdate();
            response = new SOAPResponse("Product "+productname+" deleted successfully.", SOAPResponseStatus.SUCCESS, null);
        } catch(SQLException e) { e.printStackTrace(); }
        if(response == null) response = new SOAPResponse("Error while deleting product.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse getProductsCard(String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<Product>();
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                String productname = rs.getString("productname");
                double productprice = rs.getDouble("price");
                products.add(new Product(productname,productprice));
            }
            response = new SOAPResponse("Products card "+products.toString()+"retrievied successfully.", SOAPResponseStatus.SUCCESS, products);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse("Error while getting products card.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    @Override
    public SOAPResponse getProduct(String product_name,String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql = "SELECT * FROM products WHERE productname = ? ";
        Product product = new Product();
        try (Connection conn =database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {
            pstmt.setString(1,product_name);
            ResultSet rs = pstmt.executeQuery();

            String productname = rs.getString("productname");
            double productprice = rs.getDouble("price");

             product.setProduct_Name(productname);
             product.setPrice(productprice);

            response = new SOAPResponse(product+" retrievied successfully.", SOAPResponseStatus.SUCCESS, product);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse("Error while getting product.", SOAPResponseStatus.FAILED, null);
        return response;
    }


}
