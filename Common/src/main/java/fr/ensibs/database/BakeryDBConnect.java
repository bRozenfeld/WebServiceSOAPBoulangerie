package fr.ensibs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class used to initialise the database and get a connection from the database
 */
public final class BakeryDBConnect {

    private static final String DATABASE_URL = "jdbc:sqlite:bakery.db";
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users ( \n"
            + " username text PRIMARY KEY, \n"
            + " password text, \n"
            + " isAdmin integer NOT NULL \n"
            + ");";

    private static final String CREATE_BLACKLIST_TOKEN_TABLE = "CREATE TABLE IF NOT EXISTS blacklist_token (\n"
            + " id integer PRIMARY KEY, \n"
            + " token text NOT NULL \n"
            + ");";

    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS products ( \n"
            + " id integer PRIMARY KEY AUTOINCREMENT, \n"
            + " productname text UNIQUE, \n"
            + " price double, \n"
            + ");";

    private static final String CREATE_COMMAND_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS commandsProduct ( \n"
            + " product_id integer REFERENCES products (id), \n"
            + " command_id integer REFERENCES commands (command_id) , \n"
            + " quantity integer, \n"
            + ");";

    private static final String CREATE_COMMAND_TABLE = "CREATE TABLE IF NOT EXISTS commands ( \n"
            + " command_id integer PRIMARY KEY, \n"
            + " price double, \n"
            + " isPaid integer NOT NULL, \n"
            + ");";

    // static variable single_instance of type BakeryDBConnect
    private static BakeryDBConnect database = null;

    /**
     * private constructor restricted to this class itself
     * initialise the database
     */
    private BakeryDBConnect() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_USER_TABLE);
            System.out.println("USER TABLE created successfully.");
            stmt.execute(CREATE_PRODUCT_TABLE);
            System.out.println("PRODUCT TABLE created successfully.");
            stmt.execute(CREATE_COMMAND_TABLE);
            System.out.println("COMMAND TABLE created successfully.");
            stmt.execute(CREATE_COMMAND_PRODUCT_TABLE);
            System.out.println("COMMAND_PRODUCT TABLE created successfully.");
            stmt.execute(CREATE_BLACKLIST_TOKEN_TABLE);


            System.out.println("DATABASE initialised successfully.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * static method to create instance of this class
     * @return
     */
    public static BakeryDBConnect getInstance() {
        if(database == null) database = new BakeryDBConnect();
        return database;
    }

    /**
     * Connect to the DATABASE_URL
     * @return {@Link Connection} object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection((DATABASE_URL));
            //this.connexion = DriverManager.getConnection((DATABASE_URL));
            System.out.println("Connection to database has been established.");
        } catch(Exception e) { e.printStackTrace(); }
        return conn;
    }
}
