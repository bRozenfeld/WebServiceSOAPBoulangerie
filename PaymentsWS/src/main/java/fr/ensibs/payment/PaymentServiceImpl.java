package fr.ensibs.payment;

import fr.ensibs.database.BakeryDBConnect;
import fr.ensibs.response.SOAPResponse;

/**
 * Class to define the payment service
 * @author SBAITY Haitam-ROZENFELD Benjamin
 *
 */
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
        return null;
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
        return null;
    }
}
