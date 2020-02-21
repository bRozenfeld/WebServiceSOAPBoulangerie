package fr.ensibs.bakerydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class used to initialise the database and get a connection from the database
 */
public final class BakeryDBConnect {

    private static final String DATABASE_URL = "jdbc:sqlite:boulangerie.db";
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users ( \n"
            + " username text PRIMARY KEY, \n"
            + " password text, \n"
            + " isAdmin integer NOT NULL \n"
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

    private BakeryDBConnect() { }

    /**
     * Initialise the database
     */
    public static void initDB() {
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
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Connect to the DATABASE_URL
     * @return {@Link Connection} object
     */
    public static Connection connect() {
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
