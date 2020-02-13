package fr.ensibs.users;


import javax.xml.ws.Endpoint;

public class UserServiceSOAPPublish {

    public static void main(String[] args) {
        UserService current = new UserServiceImpl();
        Endpoint.publish("http://localhost:8080/userwebservice/users", current);
        System.out.println("UserWebService available...");
    }
}
