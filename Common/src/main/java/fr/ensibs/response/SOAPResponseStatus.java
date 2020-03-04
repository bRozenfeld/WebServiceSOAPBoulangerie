package fr.ensibs.response;

/**
 * ENumeration of the soap request response
 * Success if success
 * Failed if request failed,
 * Unauthorized if user not allow to do a request
 */
public enum SOAPResponseStatus {
    SUCCESS,
    FAILED,
    UNAUTHORIZED
}
