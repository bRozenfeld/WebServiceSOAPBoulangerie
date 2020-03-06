package fr.ensibs.payment;

import fr.ensibs.response.SOAPResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Interface to define the payment service
 * @author SBAITY Haitam-ROZENFELD Benjamin-DESCAMP Etienne
 *
 */
@WebService(name="PaymentService", targetNamespace = "http://paymentwebservice.ensibs.fr/")
public interface PaymentService {
    /**
     * Method to pay the command
     * @param command_id the id of the command to pay
     * @param token The token of the user that will pay the command
     * @return Soap response that indicate if the operation had success or failed
     */
    @WebMethod(operationName = "PaymentCommand")
    @WebResult(name="PaymentCommandResult")
    SOAPResponse paymentCommand(@WebParam(name="command_id") int command_id,@WebParam(name="token") String token);

    /**
     * Methode to show the bill to user that he should pay
     * @param command_id the id of the command that we will get the bill
     * @param token The token of the user that will pay the command
     * @return Soap response that indicate if the operation had success or failed
     */
    @WebMethod(operationName = "getBill")
    @WebResult(name="getBillResult")
    SOAPResponse getBill(@WebParam(name="command_id") int command_id,@WebParam(name="token") String token);
}
