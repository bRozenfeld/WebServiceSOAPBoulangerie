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
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }

        int userId = Authentication.getUserId(token);

        String sql = "INSERT INTO commands (price, isPaid, user_id) VALUES (?,?,?)";

        try(Connection conn = database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setDouble(1,0.0);
            pstmt.setBoolean(2, false);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();

            response = new SOAPResponse("Command added successfully.", SOAPResponseStatus.SUCCESS, null);

        }catch (SQLException e){
            e.printStackTrace();
        }
        if(response == null) response = new SOAPResponse("Error while creating command.", SOAPResponseStatus.FAILED, null);
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

        double price = -1;
        String sql = "SELECT price FROM products WHERE id = ?";
        try(Connection conn = database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, product_id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                price = rs.getDouble("price");
            } else {
                response = new SOAPResponse(" Product doesn't exist.", SOAPResponseStatus.FAILED, null);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        sql = "INSERT INTO commandsProduct (product_id, command_id, quantity) VALUES (?,?,?)";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            pstmt.setInt(1, product_id);
            pstmt.setInt(2, command_id);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();


            sql = "UPDATE commands SET price = ? WHERE command_id = ?";
            PreparedStatement stmt3 = conn.prepareStatement(sql);
            stmt3.setDouble(1, price * quantity);
            stmt3.setInt(2, command_id);
            stmt3.executeUpdate();

            conn.commit();
            response = new SOAPResponse("product number "+product_id+" added to command number "+command_id+" successfully.", SOAPResponseStatus.SUCCESS, null);
        }catch(SQLException e) {
            e.printStackTrace();
        }
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

        int userId = Authentication.getUserId(token);

        String sql = "SELECT * FROM commands";
        List<Command> commands = new ArrayList<Command>();
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                int id = rs.getInt("command_id");
                double productprice = rs.getDouble("price");
                int isPaid = rs.getInt("isPaid");
                if(isPaid==1)commands.add(new Command(id,null,productprice,true, userId));
                else commands.add(new Command(id,null,productprice,false, userId));
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

        String sqlOne = "SELECT id FROM products WHERE productname = ? ";
        int productId = -1;
        try(Connection conn = database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlOne)){
            pstmt.setString(1, productname);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                productId = rs.getInt("id");
            } else {
                response = new SOAPResponse(productname + " doesn't exist.", SOAPResponseStatus.FAILED, null);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if(productId != -1) {
            String sql = "DELETE FROM products WHERE id = ? ";
            try (Connection conn = database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, productId);
                pstmt.executeUpdate();
                response = new SOAPResponse("Product " + productname + " deleted successfully.", SOAPResponseStatus.SUCCESS, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
