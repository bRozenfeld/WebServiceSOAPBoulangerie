package fr.ensibs.payment;

import fr.ensibs.auth.Authentication;
import fr.ensibs.database.BakeryDBConnect;
import fr.ensibs.product.Command;
import fr.ensibs.product.Product;
import fr.ensibs.response.SOAPResponse;
import fr.ensibs.response.SOAPResponseStatus;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Class to define the payment service
 * @author SBAITY Haitam-ROZENFELD Benjamin
 *
 */
@WebService(serviceName = "PaymentService", portName = "PaymentPort", endpointInterface = "fr.ensibs.payment.PaymentService")
public class PaymentServiceImpl implements PaymentService  {

    private BakeryDBConnect database;

    public PaymentServiceImpl() {
        database = BakeryDBConnect.getInstance();
    }

    /**
     * Method to pay the command
     *
     * @param command_id the id of the command to pay
     * @param token      The token of the user that will pay the command
     * @return Soap response that indicate if the operation had success or failed
     */
    @Override
    public SOAPResponse paymentCommand(int command_id, String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        String sql1 = "UPDATE commands SET isPaid =? WHERE command_id = ?";
        String sql ="SELECT isPaid FROM commands WHERE command_id = ?";
        try(Connection conn = database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1,command_id);
            ResultSet rs = stmt.executeQuery();
            int paid = rs.getInt("isPaid");
            if(paid == 0){
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                stmt1.setInt(1, 1);
                stmt1.setInt(2, command_id);
                stmt1.executeUpdate();
                response=getBill(command_id,token);
            }else{
                response = new SOAPResponse("Command number: "+command_id+" is already paid.", SOAPResponseStatus.FAILED, null);
            }
        }catch(SQLException e){e.printStackTrace();}

        if(response == null) response = new SOAPResponse("Error while paying command.", SOAPResponseStatus.FAILED, null);
        return response;
    }

    /**
     * Method to show the bill to user that he should pay
     *
     * @param command_id the id of the command that we will get the bill
     * @param token      The token of the user that will pay the command
     * @return Soap response that indicate if the operation had success or failed
     */
    @Override
    public SOAPResponse getBill(int command_id, String token) {
        SOAPResponse response = null;
        if(!Authentication.isAuthenticated(token)) {
            response = new SOAPResponse("Not allow", SOAPResponseStatus.UNAUTHORIZED, null);
            return response;
        }
        //HashMap<Integer,Integer> productsOfCommand=new HashMap<>();
        HashMap<Product,Integer> products = new HashMap<>();
        String sql = "SELECT product_id, quantity FROM commandsProduct WHERE command_id = ?";
        String sql1 = "SELECT price,isPaid FROM commands WHERE command_id = ?";
        String sql2 = "SELECT productname, price FROM products WHERE id=?";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, command_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int pr_id = rs.getInt("product_id");
                PreparedStatement pstm2=conn.prepareStatement(sql2);
                pstm2.setInt(1, pr_id);
                ResultSet rs2 = pstm2.executeQuery();
                while(rs2.next()){
                    products.put(new Product(rs2.getString("productname"),rs2.getDouble("price")),rs.getInt("quantity"));
                }
            }

            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, command_id);
            ResultSet rs1 = pstmt1.executeQuery();
            double pricetot = rs1.getDouble("price");
            int isPaid = rs1.getInt("isPaid");
            boolean paid;
            if(isPaid==1) paid=true;
            else paid=false;



            Command command=new Command(command_id,products,pricetot,paid);
            response = new SOAPResponse("Bill generated "+command+ "successfully.", SOAPResponseStatus.SUCCESS, command);


        }catch(SQLException e) { e.printStackTrace(); }

        if(response == null) response = new SOAPResponse("Error while getting the bill.", SOAPResponseStatus.FAILED, null);
        return response;
    }
}
